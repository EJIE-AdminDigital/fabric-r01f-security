package r01f.security.authorization.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import jakarta.inject.Provider;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.SecurityRoleInModule;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.Memoized;
import r01f.security.base.SecurityCookie;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.url.Host;
import r01f.types.url.UrlPath;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * There are TWO filters in place: [login] filter and [auth] fileter
 * <pre>
 * 		+=================+
 * 		|   login filter  | <-- check if the [user] has a login in the [security system]
 * 		+=================+		This filter puts TWO objects at the [thread local] and at the [web session] storages
 * 				 |					- the LoginSession > contains info about the user who has loged-in
 * 				 \/ 				- the SecurityContext
 * 		+=================+
 * 		|   auth filter   |	<-- uses the authorizations repo to get the auths for the user
 * 		+=================+	    This filter puts an object at the [thread loca] and at the [web session] storages
 * 									- AuthorizationSession > contains info about the [principal]'s [auth]s on [resources]
 * </pre>
 * This is the AUTH filter; once the [principal] has a valid login it JUST
 * queries the [auth repo] to get the [principal]'s [auths] and put the into
 * the [security context] (remember that the [security context] MUST
 * have been created at the [login filter] BUT without any [auth]
 */
@Slf4j
@Accessors(prefix="_")
public abstract class AuthorizationServletFilterBase<P extends IsSecurityPrincipal>
  		   implements Filter {

/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	protected final FindServicesForAuthorizationOnResource _authzOnResourceFind;

	protected final AppCode _appCode;
	protected final AppComponent _appComponent;

	protected FilterConfig _servletFilterConfig = null;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthorizationServletFilterBase(final Provider<SecurityContext> sysSecurityContextProvider,	// system [security context] provider used to access the [authorizations] on [resources]
										  final Marshaller marshaller,
										  final FindServicesForAuthorizationOnResource authzOnResourceFind) {
		this(sysSecurityContextProvider,
			 marshaller,
			 authzOnResourceFind,
			 null,null);	// every appCode/appModule
	}
	public AuthorizationServletFilterBase(final Provider<SecurityContext> sysSecurityContextProvider,	// system [security context] provider used to access the [authorizations] on [resources]
										  final Marshaller marshaller,
										  final FindServicesForAuthorizationOnResource authzOnResourceFind,
										  final AppCode appCode,final AppComponent appComponent) {
		super();
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_authzOnResourceFind = authzOnResourceFind;

		_appCode = appCode;
		_appComponent = appComponent;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public void init(final FilterConfig config) throws ServletException {
		_servletFilterConfig = config;
	}
	@Override
	public void destroy() {
		_servletFilterConfig = null;
	}
	@Override @SuppressWarnings("unused")
	public void doFilter(final ServletRequest req,final ServletResponse res,
						 final FilterChain chain) throws IOException,
														 ServletException {
		try {
			_executeFilter(req,res,chain);
		} catch (IllegalStateException illegalExc) {
			if (illegalExc.getMessage().contains("Error. Invalid jwt EXPIRED")) {
				this.sendErrorMessageToUser (res,"Saio baliogabea. Itxi web arakatzaileko leiho guztiak eta aplikazioa berriro ireki. / Sesion Invalida. Cierre todas las ventanas del navegador y vuelva a abrir la aplicacion.", null);
			} else {
				this.sendErrorMessageToUser (res,"Errorea / Error:" + illegalExc.getMessage(), illegalExc);
			}
		} catch (Throwable t) {
			if (t.getMessage().contains("Connection reset by peer")){
				log.warn("INFO: CONNECTION RESET BY PEER. UNKNOWN REASON. REQUEST URL:{}", ((HttpServletRequest)req).getRequestURL());
			} else {
				this.sendErrorMessageToUser (res, "Errore larria / Error grave:" + t.getMessage(), t);
			}
		}
	}
		
	private void _executeFilter(final ServletRequest req,
								final ServletResponse res,
						 		final FilterChain chain) throws IOException,
														 		ServletException {
		
		log.info("[auth filter] >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		// [-1] - Get some data from the request
		UrlPath requestedUrlPath = UrlPath.from(request.getRequestURI());	// {webApp}/{urlPath} (does NOT include query string)

		// [0] - Get the [loginSession] from the [thread local] storage
		LoginSession<P> loginSession = LoginSessionStore.getFromThreadLocalStorage();
		if (loginSession == null) {
			log.warn("NO [loginSession] stored at the [thread local] storage: the user has NOT logged-in!!");
			throw new IllegalStateException("The user MUST have been logged-in... the flow CANNOT reach this point if the user has not logged-in!!");
		}
		// [1] - Get the [principal] from the [loginSession]
		// 		 (usually THIS filter MUST be AFTER the LoginSessionFilter)
		//		 (see LoginServletFilter)
		SecurityPrincipalOID securityPrincipalOid = loginSession.getSecurityPrincipalOid();
		if (securityPrincipalOid == null) throw new IllegalStateException("Could NOT get the [security principal] from the [loginSession] " +
														     			  "(the [loginSession] is present at the web session, " +
															 			  "BUT the [security principal] could NOT be get)");
		P principal = loginSession.getSecurityPrincipal();
		Collection<SecurityRoleInModule> systemWideRoles = principal.getSystemWideRoles() != null ? principal.getSystemWideRoles()
																								  : Collections.emptyList();

		// [2] - Get
		//			A) the [security context] > it might be stored by the [login filter] at the [thread local] & [web session] storage
		//			B) The [auth session]     > it's present at the [web session] if the [auth session] has previously been retrieved
		SecurityContext securityContext = SecurityContextStore.getFromThreadLocalStorage();
		AuthorizationSession authSession = AuthorizationContextStore.getFromSession(request);

		// BEWARE!!! the user might have change the [login] (he/she might have logged in with another login)
		//			 ... so the session-stored [auths] might NOT be the same
		if (authSession != null
		 && authSession.getSecurityPrincipalOid().isNOT(securityPrincipalOid)) {
			AuthorizationContextStore.removeFromHttpSession(request);
			authSession = null;
		}

		if (authSession == null) {
			// The FIRST time the user access the app: use the userOid to query the authorized resources API
			// 										   ...and create a authSession with it
			log.info("[auth filter] > There is NO [authSession] stored at the [web session], but it seems the user has logged in because there exists a [login session]: " +
					 "Create the [auth session]...");

			// a) find the [authzs]
			Collection<SecurityPrincipalAuthorizationOnResource> authzs = null;
			FindResult<SecurityPrincipalAuthorizationOnResource> authzsFind = _authzOnResourceFind.findBy(_sysSecurityContextProvider.get(),
																							  			  securityPrincipalOid,
																							  			  _appCode,_appComponent);
			authzs = authzsFind.hasSucceeded() ? authzsFind.getOrThrow()
											   : Collections.emptyList();
			if (CollectionUtils.isNullOrEmpty(authzs)
			 && CollectionUtils.isNullOrEmpty(systemWideRoles)) {
				//	no auth & no sys-wide roles > unauthorized
				_unauthorized(request,response,
							  securityPrincipalOid,requestedUrlPath);
				return;
			}
			// b) Create the [auth session] object
			authSession = new AuthorizationSession(securityPrincipalOid,
												   systemWideRoles,
											 	   authzs);
			// c) If there exists a [security context] at the [thread local] store, give the application
			//	  the opportunity to set the [auths] at the [security context]
			if (securityContext != null) {
				log.info("[auth filter] > let the app to put the [authzs] at the [security context]");
				this.setAuthzsAtSecurityContext(authSession.getAuthorizations(),
												   securityContext);
			}
			// d) and store at the [thread local] & [web session] stores
			AuthorizationContextStore.storeAtHttpSession(request,
															 authSession);
		}
		// [3] - store the [auth session] at the [thread local] & [web session] stores
		AuthorizationContextStore.storeAtThreadLocalStorage(authSession);

		// [4] - At this point an authSession object is present
		// 		 ... let the application check if the user is authorized
		boolean authorized = this.checkAuthorization(requestedUrlPath,
													 securityContext,
													 loginSession,
													 authSession.getAuthorizations());
		// ... if not authorized return a FORBIDDEN result
		if (!authorized) {
			_unauthorized(request,response,
						  securityPrincipalOid,requestedUrlPath);
			return;	// nothing else to do
		}
		// [5] - Authorized!!!
		if (_isLogEnabled()) log.debug("[auth filter] > [principal] oid={} is AUTHORIZED at {}... go on with auth session\n" +
				 					  "{}",
				 					  authSession.getSecurityPrincipalOid(),requestedUrlPath,
				 					  authSession.debugInfo());

		// [6] - Create a cookie with a secret value that anyone can use to check if the
		//		 user was authenticated by a legitimate party
		Collection<String> authGrantedCookiesNames = _authorizationGrantedCookiesNamesMemo.get();
		if (CollectionUtils.hasData(authGrantedCookiesNames)) {
			for (String authGrantedCookieName : authGrantedCookiesNames) {
				Cookie existing = HttpServletRequestUtils.cookieWithName(request,authGrantedCookieName);
				if (existing == null) {
					// create a cookie that contains a signed token
					// ... this signed token can only be generated by a legitimate part owning a secret PRIVATE key
					//	   any other part can rest assured that the user has been logged in just by ensuring
					//	   that the cookie exists and that it's value was generated by a legitimate part using
					// 	   a PUBLIC key (or even the same PRIVATE key as the cookie generator)
					SecurityCookie authGrantedCookie = _createAuthorizationGrantedCookie(securityContext,
																						 HttpServletRequestUtils.clientRequestedHost(request), 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
																						 authGrantedCookieName);
					if (authGrantedCookie != null) {
						Cookie cookie = authGrantedCookie.createNonPersistentCookie();
						response.addCookie(cookie);
					}
				}
			}
		}
		// [99] - Chain
		chain.doFilter(request,response);
	}
	private static void _unauthorized(final HttpServletRequest request,final HttpServletResponse response,
							   		  final SecurityPrincipalOID principalOid,final UrlPath urlPathBeingAuthorized) throws IOException {
		log.info("[auth filter] > User oid={} is NOT AUTHORIZED at {}... FORBIDDEN",
				 principalOid,urlPathBeingAuthorized);

		// remove stored data
		AuthorizationContextStore.removeFromHttpSession(request);
		AuthorizationContextStore.removeFromThreadLocalStorage();

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						   Strings.customized("User with oid={} is NOT authorized to access {}",
											  principalOid,urlPathBeingAuthorized));
	}
	@SuppressWarnings("static-method")
	protected boolean _isLogEnabled() {
		return log.isDebugEnabled();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS THAT CAN BE OVERRIDEN
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Puts the {@link SecurityContext} at the {@link ThreadLocal} storage
	 * It can be overriden to use any custom method to put the {@link SecurityContext at the {@link ThreadLocal} storage
	 * for example when using Spring MVC
	 * @param securityContext
	 */
	protected void storeAuthzSessionAtThreadLocalStorage(final AuthorizationSession authzSession) {
		AuthorizationContextStore.storeAtThreadLocalStorage(authzSession);
	}
	/**
	 * Gives the application an opportunity to check the [user auths]
	 * @param urlPath the resource being accessed
	 * @param securityContext
	 * @param loginSession
	 * @param authzs
	 * @return
	 */
	protected boolean checkAuthorization(final UrlPath urlPath,
										 final SecurityContext securityContext,final LoginSession<P> loginSession,
										 final Collection<SecurityPrincipalAuthorizationOnResource> authzs) {
		log.warn("OVERRIDE {}#checkUserAuthorization() method!!! (now it just return AUTHORIZED!)",AuthorizationServletFilterBase.class.getSimpleName());
		if (CollectionUtils.hasData(authzs)) {
			log.info("User is accessing {}",urlPath);
			for (SecurityPrincipalAuthorizationOnResource auth : authzs) {
				log.info("\t-Auth for user={} to resource={} with role={}",
						 auth.getSecurityPrincipalOid(),auth.getAuthorizationTargetResourceOid(),auth.getRole());
			}
		}
		return true;
	}
	/**
	 * Gives the application an opportunity to set the [auths] at the [security context]
	 * @param authzs
	 * @param securityContext
	 */
	protected void setAuthzsAtSecurityContext(final Collection<SecurityPrincipalAuthorizationOnResource> authzs,
											  final SecurityContext securityContext) {
		// override this method to set the [auths] at the [security context]
	}
	/**
	 * Creates a {@link Map} of {@link IsAuthorizationTargetResource} indexed by oid
	 * @param userAuthzs
	 * @return
	 */
	protected AuthorizationServletFilterMapAuths _authsResourcesByOid(final Collection<SecurityPrincipalAuthorizationOnResource> userAuthzs) {
		return new AuthorizationServletFilterMapAuths(userAuthzs);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	protected class AuthorizationServletFilterMapAuths {
		private final Collection<SecurityPrincipalAuthorizationOnResource> _userAuths;

		public Map<AuthorizationTargetResourceOID,AuthorizationTargetResource> using(final SecurityContext securityContext,
																				     final CRUDServicesForAuthorizationTargetResource authResCrud) {
			return CollectionUtils.hasData(_userAuths)
						? _userAuths.stream()
								// get unique [resource] oids (a user might have multiple [auths] on the SAME [resource]
								// (although it's not the usual case)
								.map(SecurityPrincipalAuthorizationOnResource::getAuthorizationTargetResourceOid)
								.distinct()
								// load the [resource]
								.map(resourceOid -> authResCrud.load(securityContext,
																	 resourceOid)
															   .getOrThrow())
								// filter the ones for the [portal manger]
								.filter(authRes -> (authRes.getAppCode().is(_appCode)
									&& authRes.getAppComponent().is(_appComponent)))
								.collect(Collectors.toMap(AuthorizationTargetResource::getOid,	// key
														  Function.identity()))					// value
						: null;	// no auth
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  SECURITY COOKIES
/////////////////////////////////////////////////////////////////////////////////////////
	protected SecurityCookie _createAuthorizationGrantedCookie(final SecurityContext securityContext,
															   final Host domain,
															   final String cookieName) {
		return null;	// do nothing by default
	}
	private final Memoized<Collection<String>> _authorizationGrantedCookiesNamesMemo = new Memoized<>() {
																								@Override
																								public Collection<String> supply() {
																									return _authorizationGrantedCookiesNames();
																								}
																					   };
	protected Collection<String> _authorizationGrantedCookiesNames() {
		return null;
	}
	
	private void sendErrorMessageToUser(final ServletResponse res, final String stMessage, final Throwable t) {
		String traza_error = "ID_FILTER:[" + Instant.now().toEpochMilli() + "]. " + stMessage;

		if (t == null) {
			log.warn(traza_error);
		} else {
			log.error(traza_error, t);
		}

		HttpServletResponse response = (HttpServletResponse)res;
		
		response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		response.setContentType("text/html");

		PrintWriter out = null;
		try {
			out = response.getWriter();
			out.println("<html><body>");
			out.println("<h1>INFO:</h1>");
			out.println(traza_error);
			out.println("</body></html>");
			out.flush();
		} catch (IOException e) {
			log.error("Error al escribir la respuesta:" + e.getMessage(), e);
		}
	}
}