package r01f.security.authorization.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

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
import r01f.api.interfaces.security.authorization.FindServicesForUserAuthorizationOnResource;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.user.User;
import r01f.model.security.user.UserRoleInModule;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Memoized;
import r01f.security.base.SecurityCookie;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.url.Host;
import r01f.types.url.Url;
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
 * 									- UserAuthorizationSession > contains info about the user's [auth]s on [resources]
 * </pre>
 * This is the AUTH filter; once the user has a valid login it JUST
 * queries the [auth repo] to get the user's [auths] and put the into
 * the [security context] (remember that the [security context] MUST
 * have been created at the [login filter] BUT without any user [auth]
 */
@Slf4j
@Accessors(prefix="_")
public abstract class AuthorizationServletFilterBase<U extends User,
													 A extends UserAuthorizationOnResource>
  		   implements Filter {

/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	protected final FindServicesForUserAuthorizationOnResource<A> _userAuthOnResourceFind;

	protected final AppCode _appCode;
	protected final AppComponent _appComponent;

	protected FilterConfig _servletFilterConfig = null;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthorizationServletFilterBase(final Provider<SecurityContext> sysSecurityContextProvider,	// system [security context] provider used to access the [authorizations] on [resources]
										  final Marshaller marshaller,
										  final FindServicesForUserAuthorizationOnResource<A> userAuthOnResourceFind) {
		this(sysSecurityContextProvider,
			 marshaller,
			 userAuthOnResourceFind,
			 null,null);	// every appCode/appModule
	}
	public AuthorizationServletFilterBase(final Provider<SecurityContext> sysSecurityContextProvider,	// system [security context] provider used to access the [authorizations] on [resources]
										  final Marshaller marshaller,
										  final FindServicesForUserAuthorizationOnResource<A> userAuthOnResourceFind,
										  final AppCode appCode,final AppComponent appComponent) {
		super();
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_userAuthOnResourceFind = userAuthOnResourceFind;

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
				sendErrorMessageToUser (res, "Saio baliogabea. Itxi web arakatzaileko leiho guztiak eta aplikazioa berriro ireki. / Sesion Invalida. Cierre todas las ventanas del navegador y vuelva a abrir la aplicacion.", null);
			} else {
				sendErrorMessageToUser (res, "Errorea / Error:" + illegalExc.getMessage(), illegalExc);
			}
		} catch (Throwable t) {
			if (t.getMessage().contains("Connection reset by peer")){
				log.warn("INFO: CONNECTION RESET BY PEER. UNKNOWN REASON. REQUEST URL:{}", ((HttpServletRequest)req).getRequestURL());
			} else {
				sendErrorMessageToUser (res, "Errore larria / Error grave:" + t.getMessage(), t);
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
		LoginSession<U> loginSession = LoginSessionStore.getFromThreadLocalStorage();
		if (loginSession == null) {
			log.warn("NO [loginSession] stored at the [thread local] storage: the user has NOT logged-in!!");
			throw new IllegalStateException("The user MUST have been logged-in... the flow CANNOT reach this point if the user has not logged-in!!");
		}
		// [1] - Get the user from the [loginSession]
		// 		 (usually THIS filter MUST be AFTER the LoginSessionFilter)
		//		 (see LoginServletFilter)
		UserOID userOid = loginSession.getUserOid();
		if (userOid == null) throw new IllegalStateException("Could NOT get the userOid from the [loginSession] " +
														     "(the [loginSession] is present at the web session, " +
															 "BUT the userOid could NOT be get)");
		U user = loginSession.getUser();
		Collection<UserRoleInModule> systemWideUserRoles = user.getUserSystemWideRoles() != null ? user.getUserSystemWideRoles() : Lists.newArrayList();

		// [2] - Get
		//			A) the [security context] > it might be stored by the [login filter] at the [thread local] & [web session] storage
		//			B) The [auth session]     > it's present at the [web session] if the [auth session] has previously been retrieved
		SecurityContext securityContext = SecurityContextStore.getFromThreadLocalStorage();
		UserAuthorizationSession<A> authSession = UserAuthorizationContextStore.getFromSession(request);

		// BEWARE!!! the user might have change the [login] (he/she might have logged in with another login)
		//			 ... so the session-stored [auths] might NOT be the same
		if (authSession != null
		 && authSession.getUserOid().isNOT(userOid)) {
			UserAuthorizationContextStore.removeFromHttpSession(request);
			authSession = null;
		}

		if (authSession == null) {
			// The FIRST time the user access the app: use the userOid to query the authorized resources API
			// 										   ...and create a authSession with it
			log.info("[auth filter] > There is NO [authSession] stored at the [web session], but it seems the user has logged in because there exists a [login session]: " +
					 "Create the [auth session]...");

			// a) find the [user auths]
			Collection<A> userAuths = null;
			FindResult<A> userAuthsFind = _userAuthOnResourceFind.findBy(_sysSecurityContextProvider.get(),
																	  	 userOid,
																	  	 _appCode,_appComponent);
			userAuths = userAuthsFind.hasSucceeded() ? userAuthsFind.getOrThrow()
													 : Lists.newArrayList();
			if (CollectionUtils.isNullOrEmpty(userAuths)
			 && CollectionUtils.isNullOrEmpty(systemWideUserRoles)) {
				//	no auth & no sys-wide roles > unauthorized
				_unauthorized(request,response,
							  userOid,requestedUrlPath);
				return;
			}
			// b) Create the [user auth session] object
			authSession = new UserAuthorizationSession<>(userOid,
														 systemWideUserRoles,
											 			 userAuths);
			// c) If there exists a [security context] at the [thread local] store, give the application
			//	  the opportunity to set the [user]'s [auths] at the [security context]
			if (securityContext != null) {
				log.info("[auth filter] > let the app to put the [user auths] at the [security context]");
				this.setUserAuthsAtSecurityContext(authSession.getUserAuths(),
												   securityContext);
			}
			// d) and store at the [thread local] & [web session] stores
			UserAuthorizationContextStore.storeAtHttpSession(request,
															 authSession);
		}
		// [3] - store the [auth session] at the [thread local] & [web session] stores
		UserAuthorizationContextStore.storeAtThreadLocalStorage(authSession);

		// [4] - At this point an authSession object is present
		// 		 ... let the application check if the user is authorized
		boolean authorized = this.checkUserAuthorization(requestedUrlPath,
														 securityContext,
														 loginSession,
													 	 authSession.getUserAuths());
		// ... if not authorized return a FORBIDDEN result
		if (!authorized) {
			_unauthorized(request,response,
						  userOid,requestedUrlPath);
			return;	// nothing else to do
		}
		// [5] - Authorized!!!
		if (_isLogEnabled()) log.debug("[auth filter] > User oid={} is AUTHORIZED at {}... go on with auth session\n" +
				 					  "{}",
				 					  authSession.getUserOid(),requestedUrlPath,
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
							   		  final UserOID userOid,final UrlPath urlPathBeingAuthorized) throws IOException {
		log.info("[auth filter] > User oid={} is NOT AUTHORIZED at {}... FORBIDDEN",
				 userOid,urlPathBeingAuthorized);

		// remove stored data
		UserAuthorizationContextStore.removeFromHttpSession(request);
		UserAuthorizationContextStore.removeFromThreadLocalStorage();

		response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
						   Strings.customized("User with oid={} is NOT authorized to access {}",
											  userOid,urlPathBeingAuthorized));
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
	protected void storeUserAuthSessionAtThreadLocalStorage(final UserAuthorizationSession<A> userAuthSession) {
		UserAuthorizationContextStore.storeAtThreadLocalStorage(userAuthSession);
	}
	@Deprecated
	protected boolean checkUserAuthorization(final Url url,
											 final SecurityContext securityContext,
											 final LoginSession<U> loginSession,
									  	 	 final Collection<A> userAuths) {
		return this.checkUserAuthorization(url != null ? url.getUrlPath() : null,
										   securityContext,loginSession,userAuths);
	}
	/**
	 * Gives the application an opportunity to check the [user auths]
	 * @param urlPath the resource being accessed
	 * @param securityContext
	 * @param loginSession
	 * @param userAuths
	 * @return
	 */
	protected boolean checkUserAuthorization(final UrlPath urlPath,
											 final SecurityContext securityContext,final LoginSession<U> loginSession,final Collection<A> userAuths) {
		log.warn("OVERRIDE {}#checkUserAuthorization() method!!! (now it just return AUTHORIZED!)",AuthorizationServletFilterBase.class.getSimpleName());
		if (CollectionUtils.hasData(userAuths)) {
			log.info("User is accessing {}",urlPath);
			for (A auth : userAuths) {
				log.info("\t-Auth for user={} to resource={} with role={}",
						 auth.getUserOid(),auth.getAuthorizationTargetResourceOid(),auth.getRole());
			}
		}
		return true;
	}
	/**
	 * Gives the application an opportunity to set the [users auths] at the [security context]
	 * @param userAuths
	 * @param securityContext
	 */
	protected void setUserAuthsAtSecurityContext(final Collection<A> userAuths,
												 final SecurityContext securityContext) {
		//
	}
	/**
	 * Creates a {@link Map} of {@link AuthorizationTargetResource} indexed by oid
	 * @param userAuths
	 * @return
	 */
	protected AuthorizationServletFilterMapAuths _authsResourcesByOid(final Collection<A> userAuths) {
		return new AuthorizationServletFilterMapAuths(userAuths);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	protected class AuthorizationServletFilterMapAuths {
		private final Collection<A> _userAuths;

		public <R extends AuthorizationTargetResource> Map<AuthorizationTargetResourceOID,R> using(final SecurityContext securityContext,
																								   final CRUDServicesForAuthorizationTargetResource<R> authResCrud) {
			Map<AuthorizationTargetResourceOID,R> authResByOid = CollectionUtils.hasData(_userAuths)
																	? _userAuths.stream()
																			// get unique [resource] oids (a user might have multiple [auths] on the SAME [resource]
																			// (although it's not the usual case)
																			.map(A::getAuthorizationTargetResourceOid)
																			.distinct()
																			// load the [resource]
																			.map(resourceOid -> authResCrud.load(securityContext,
																												 resourceOid)
																										   .getOrThrow())
																			// filter the ones for the [portal manger]
																			.filter(authRes -> (authRes.getAppCode().is(_appCode)
																				&& authRes.getAppComponent().is(_appComponent)))
																			.collect(Collectors.toMap(R::getOid,				// key
																									  Function.identity()))		// value
																	: null;	// no auth
			return authResByOid;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  SECURITY COOKIES
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "unused","static-method" })
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
	@SuppressWarnings("static-method")
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