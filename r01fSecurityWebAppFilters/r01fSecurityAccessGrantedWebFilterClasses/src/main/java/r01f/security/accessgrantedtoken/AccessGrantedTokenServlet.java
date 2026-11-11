package r01f.security.accessgrantedtoken;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Collections;

import jakarta.inject.Provider;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.authorization.FindServicesForAuthorizationOnResource;
import r01f.crypto.CryptoKeyPair;
import r01f.crypto.CryptoKeyPairBuilder;
import r01f.jwt.JWT;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.SecurityRoleInModule;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.provider.cookie.AuthenticationProviderCookieStore;
import r01f.security.base.SecuritySessionHandler;
import r01f.security.base.StoresSecurityDataAtThreadLocalStorage;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.AfterLoginRedirUrlExtraInfoStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.security.login.filter.AfterLoginJWTStore;
import r01f.security.login.filter.LoginPageUtil;
import r01f.security.login.filter.LoginPageUtil.LoginPageUtilUrlToStep;
import r01f.security.login.filter.SecurityLoginFilterConfig;
import r01f.security.login.filter.SecurityPrincipalDataFromAfterLoginJWT;
import r01f.security.login.filter.SecurityProviderSessionFromRequestProvider;
import r01f.servlet.HttpRequestParamsWrapper;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.StringBase64;
import r01f.types.url.Host;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlQueryString;
import r01f.util.types.Strings;

/**
 * See docs folder
 *
 * [1] >            redir access token servlet: /r01PLATEAWebCMSLoginUIWar/cms-preview-access-granted?urlToBeAuthorized=pagA	<--- store pagA in session
 * [2] >    redir security rest services login: /r01PLATEAWebCORESecurityRESTServicesWar/api/xlnets/login?to=/r01PLATEAWebCMSLoginUIWar/cms-preview-access-granted
 * [3] >               redir security provider: /N38LoginInicioServlet/N38API=/r01PLATEAWebCORESecurityRESTServicesWar/api/xlnets/users/login/xlnetscallback?to=/r01PLATEAWebCMSLoginUIWar/cms-preview-access-granted
 * [4] > redir security rest services callback: /r01PLATEAWebCORESecurityRESTServicesWar/api/xlnets/users/login/xlnetscallback?to=/r01PLATEAWebCMSLoginUIWar/cms-preview-access-granted?jwt=<>
 * [5] >            redir access token servlet: /r01PLATEAWebCMSLoginUIWar/cms-preview-access-granted?jwt=<>   					<--- restore pagA from session
 * [6] >                                 redir: pagA
 *
 * @param <P>
 * @param <A>
 */
@Slf4j
@Accessors(prefix="_")
public abstract class AccessGrantedTokenServlet<P extends IsSecurityPrincipal>
  		      extends HttpServlet
  		   implements StoresSecurityDataAtThreadLocalStorage<P> {

	private static final long serialVersionUID = -4196196155556215637L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String AFTER_LOGIN_JWT_TOKEN_WEB_REQUEST_PARAM_NAME = "token";
	public static final String URL_TO_BE_AUTHORIZED_WEB_REQUEST_PARAM_NAME = "urlToBeAuthorized";
	public static final String URL_TO_BE_AUTHORIZED_WEB_SESSION_PARAM_NAME = "urlToBeAuthorized";
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final Class<P> _securityPrincipalType;

	// used to find [user auths]
	@Getter protected final Provider<SecurityContext> _sysSecurityContextProvider;

	@Getter protected final FindServicesForAuthorizationOnResource _authzOnResourceFind;
	@Getter protected final AppCode _appCode;
	@Getter protected final AppComponent _appComponent;

	// Login filter config > used to get the login page url
	@Getter protected final SecurityLoginFilterConfig _loginFilterConfig;

	// Queries a LOCAL endpoint that returns the LOCAL user data
	// BEWARE!!
	// 		The LOCAL [user data] IS NOT the same as the one returned by a [security provider] (ie google, xlnets, etc)
	// 		Usually the LOCAL [user data] is created FROM the one returned by google, xlnets, etc,
	// 		BUT might have more (or less) properties
	@Getter protected final SecurityPrincipalDataFromAfterLoginJWT<P> _userDataFromAfterLoginJWT;

	// After a user is logged-in, it's redirected to the [Security REST Services WAR] where a [JWT Token] is created and then
	// the [user] is redirected to the [UI]
	// At the [UI] the request passes through the {@link LoginSessionServletFilter} that tries to create the {@link LoginSession}
	// ... to do so, the [login session servlet filter] calls back the [Security REST Services WAR] to get the user data
	// 		U user = _userDataFromJWT.getUserDataFromJWT(jwt);  <<<< issues an HTTP request to the [Security REST Services WAR] to get the [user data]
	// 																 ... the HTTP request contains the [JWT Token] so the [security REST services WAR]
	// 																	 can be sure the request is a legitimate one
	// In order to get the [user data], the [security REST services WAR] uses some data encoded at the [JWT token]
	// ... BUT sometimes additional data is needed, and that data it's NOT encoded at the [JWT token]!!
	//
	// This provider gives the filter an opportunity to get this additional data
	@Getter protected final SecurityProviderSessionFromRequestProvider _securityProviderSessionFromRequestProvider;

	// service used to validate the [access granted tokens]
	@Getter protected final AccessGrantedTokenService _accessGrantedJWTService;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedTokenServlet(final Class<P> principalType,
									 // system [security context] provider used to access the [authorizations] on [resources]
									 final Provider<SecurityContext> sysSecurityContextProvider,
									 final Marshaller marshaller,
									 // login filter config (used to get the login page url)
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to "serialize" the XLNets session from the [UI War] to the [security REST services WAR]
									 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
									 // used to generate the [access granted token]
									 final AccessGrantedTokenSpec accessGrantedTokenSpec) {
		this(principalType,
			 sysSecurityContextProvider,
			 marshaller,
			 null,		// finder
			 null,null,	// app code / app component
			 loginFilterConfig,
			 securityProviderSessionFromRequestProvider,
			 accessGrantedTokenSpec);
	}
	public AccessGrantedTokenServlet(final Class<P> principalType,
									 // system [security context] provider used to access the [authorizations] on [resources]
									 final Provider<SecurityContext> sysSecurityContextProvider,
									 final Marshaller marshaller,
									 // login filter config (used to get the login page url)
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to generate the [access granted token]
									 final AccessGrantedTokenSpec accessGrantedTokenSpec) {
		this(principalType,
			 sysSecurityContextProvider,
			 marshaller,
			 null,		// finder
			 null,null,	// app code / app component
			 loginFilterConfig,
			 (SecurityProviderSessionFromRequestProvider)null,
			 accessGrantedTokenSpec);
	}
	public AccessGrantedTokenServlet(final Class<P> principalType,
									 // system [security context] provider used to access the [authorizations] on [resources]
									 final Provider<SecurityContext> sysSecurityContextProvider,
									 final Marshaller marshaller,
									 // used to find [authorizations] on [resource]
									 final FindServicesForAuthorizationOnResource AuthzOnResourceFind,
									 final AppCode appCode,final AppComponent appComponent,
									 // login filter config (used to get the login page url)
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to generate the [access granted token]
									 final AccessGrantedTokenSpec accessGrantedTokenSpec) {
		this(principalType,
			 sysSecurityContextProvider,
			 marshaller,
			 AuthzOnResourceFind,appCode,appComponent,
			 loginFilterConfig,
			 (SecurityProviderSessionFromRequestProvider)null,
			 accessGrantedTokenSpec);
	}
	public AccessGrantedTokenServlet(final Class<P> userType,
									 // system [security context] provider used to access the [authorizations] on [resources]
									 final Provider<SecurityContext> sysSecurityContextProvider,
									 final Marshaller marshaller,
									 // used to find [authorizations] on [resource]
									 final FindServicesForAuthorizationOnResource authzOnResourceFind,
									 final AppCode appCode,final AppComponent appComponent,
									 // login filter config (used to get the login page url)
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to "serialize" the XLNets session from the [UI War] to the [security REST services WAR]
									 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
									 // used to generate the [access granted token]
									 final AccessGrantedTokenSpec accessGrantedTokenSpec) {
		_securityPrincipalType = userType;

		// used to get the [authorizations]
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_authzOnResourceFind = authzOnResourceFind;
		_appCode = appCode;
		_appComponent = appComponent;

		// user to get the [login page] url
		_loginFilterConfig = loginFilterConfig;

		// used to query the [security rest services] and create the [user]
		_userDataFromAfterLoginJWT = new SecurityPrincipalDataFromAfterLoginJWT<>(_securityPrincipalType,
												 					 _loginFilterConfig,
												 					 marshaller);
		_securityProviderSessionFromRequestProvider = securityProviderSessionFromRequestProvider;

		if (log.isInfoEnabled()) log.info("[access granted token servlet] > CONSTRUCTOR: user:{}, provider:{}", _userDataFromAfterLoginJWT, _securityProviderSessionFromRequestProvider);
		
		// Access granted token service > creates and validates [access granted] tokens
		CryptoKeyPair rsaKeyPair = CryptoKeyPairBuilder.createRSAKeyPair();	// BEWARE! if the server is restarted ALL existing [access tokens] will NOT be valid
		
		if (log.isInfoEnabled()) log.info("[access granted token servlet] > CRYPTO:{}", rsaKeyPair.toString());
		
		_accessGrantedJWTService = new AccessGrantedTokenService(accessGrantedTokenSpec,
															   	 rsaKeyPair);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HTTP METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	protected void doGet(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																				 			 IOException {
		this.service(req,resp);
	}
	@Override
	protected void doPost(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																				   			  IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void doHead(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																				   			  IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void doPut(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																							 IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void doDelete(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																								IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void doOptions(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																								 IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void doTrace(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																							   IOException {
		throw new UnsupportedOperationException("Just GET method is supported!");
	}
	@Override
	protected void service(final HttpServletRequest req,final HttpServletResponse resp) throws ServletException,
																							   IOException {
		this.service((ServletRequest)req,(ServletResponse)resp);
	}
	@Override
	public void service(final ServletRequest req,final ServletResponse res) throws ServletException,
																				   IOException {
		_doService(req,res);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	MAIN IMPL
/////////////////////////////////////////////////////////////////////////////////////////
	private void _doService(final ServletRequest req,final ServletResponse res) throws IOException {

		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		// [-1] - Get some data from the request
		Url requestedUrl = Url.from(request.getRequestURL());	// http://localhost:8080/{webAp}/{urlPath} (does NOT include query string) BEWARE! this url might NOT be the client-requested one if there's a proxy upstream
		Url clientRequestedUrl = HttpServletRequestUtils.clientRequestedHost(request) 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
												  		.asUrl()
												  		.joinWith(UrlPath.from(request.getRequestURI()));	// {webApp}/{urlPath}	(does NOT include query string)
		UrlPath webAppContext = UrlPath.from(request.getContextPath());		// {webApp}
		UrlPath requestedUrlPath = UrlPath.from(request.getRequestURI());	// {webApp}/{urlPath}	(does NOT include query string)
		UrlQueryString requestedQryString = UrlQueryString.fromParamsString(request.getQueryString());


		// manages how the [access granted token] is stored inside a [cookie]
		AccessGrantedTokenCookieStore accessGrantedTokenCookieStore = AccessGrantedTokenCookieStore.createWithCookieName(this.accessGrantedTokenCookieName());
		HttpRequestParamsWrapper reqParams = new HttpRequestParamsWrapper(request);

		// [0] - Get data from [web request]
		JWT accessGrantedTokenJWTInCookie = accessGrantedTokenCookieStore.getAccessGrantedTokenFrom(request);
		JWT accessGrantedTokenJWTInRequestParam = reqParams.getParameter(AFTER_LOGIN_JWT_TOKEN_WEB_REQUEST_PARAM_NAME)
														   .asType(JWT.class)
														   .usingFactory(str -> Strings.isNOTNullOrEmpty(str) ? JWT.from(str.toString()) : null)
														   .orNull();
		Url urlToBeAuthorized = reqParams.getParameter(URL_TO_BE_AUTHORIZED_WEB_REQUEST_PARAM_NAME)
								    	 .asUrl()
								    	 .orNull();
		if (log.isInfoEnabled()) log.info("[access granted token servlet][START]: {} >>>>>>>>>>\n" +
								 "\t- client requested url={}\n" +
								 "\t- webAppContext={} url path={} query string={}\n" +
								 "\t- access granted token jwt in cookie={}\n" +
								 "\t- access granted token jwt in request param={}\n" +
								 "\t- url to be authorized={}",
								 requestedUrl,
								 clientRequestedUrl,
								 webAppContext,requestedUrlPath,requestedQryString,
								 accessGrantedTokenJWTInCookie,accessGrantedTokenJWTInRequestParam,
								 urlToBeAuthorized);
		// [1] - Is Logout?
		// 		 the logout flow is like:
		//
		//			   [UI]			[SecurityRestServices]
		//				|					   |
		//				+----redir----> /api/{providerId}/users/logout
		//				|					   |
		//				|					  [ ] ----> register logout
		//				|					   |
		//			/logout <------redir--------+
		//				|
		//			 [filter] (remove everything)
		if (LoginPageUtil.isLogout(request)) {
			if (log.isInfoEnabled()) log.info("[access granted token servlet] > logout!!");

			// remove everything from the session
			_cleanupSession(request,response);
		}

		// [2] - Get the [after login jwt] from the request
		//		 (the [after login jwt] is only present if the user enters here AFTER being authenticated)
		JWT afterLoginJWT = AfterLoginJWTStore.getFromRequest(request);

		////////// After being logged-in > create the [login session]
		if (afterLoginJWT != null) {
			// The user comes here after being logged-in
			if (log.isInfoEnabled()) log.info("[access granted token servlet] > request reaches here just AFTER being loged-in at the [security provider] > create a [access granted token] from JWT token");

			// a) remove everything
			_cleanupSession(request,response);

			// b) The [user] has logged-in using a [security provider]'s login page (ie google, xlnets...)
			// 	  After being logged-in, the [user] has been redirected from the [security provider]'s login ui to the [security REST services]
			// 	  where an [after login JWT] token is created and then redirected again to the [UI] where this filter is configured
			// 	  ... so this is the first time he enters the target...
			// 	  here just an [after login JWT] ia available but in order to create the [Login Session], the [user data] is needed
			// 	  ... and to get the [user data], the [filter] calls back the [security REST services] with the [after login JWT] token
			//     	  that "tells" the [security REST services] it's a legitimate request and just returns the [user data]
			LoginSession<P> loginSession = _userDataFromAfterLoginJWT.getLoginSessionFormJWT(request,
																			 			  	 afterLoginJWT,
																			 			  	 _securityProviderSessionFromRequestProvider);
			// c) now the http session contains the [loginSession]
			SecurityContext securityContext = _onLoginSessionExists(loginSession,
								  									request,response);

			// d) store loging & security context
			LoginSessionStore.storeAtThreadLocalStorage(loginSession);
			SecurityContextStore.storeAtThreadLocalStorage(securityContext);

			LoginSessionStore.storeAtHttpSession(request,loginSession);
			SecurityContextStore.storeAtHttpSession(request,securityContext);

			// e) Redir to the requested url WITHOUT the [after login jwt]
			//	  (the url was stored at the [web session] and might be also received as toUrl extra info base64)
			StringBase64 toeb64 = AfterLoginRedirUrlExtraInfoStore.getFromRequest(request);	// extra info
			Url urlToExt = toeb64 != null ? Url.from(toeb64.decode()) : null;	// it should be the same as the session-stored one...
			urlToBeAuthorized = (Url)request.getSession(false)
									   		.getAttribute(URL_TO_BE_AUTHORIZED_WEB_SESSION_PARAM_NAME);
			if (urlToBeAuthorized == null) throw new IllegalArgumentException("NO url to be authorized received as query string param!!");
			if (log.isDebugEnabled()) log.debug("[access granted token filter][REDIR] {} , url ext:{} <<<<<<<<<<",
									  urlToBeAuthorized,urlToExt);

			response.sendRedirect(urlToBeAuthorized.asString());
		}
		////////// Authorize
		else if (urlToBeAuthorized != null) {
			if (log.isInfoEnabled()) log.info("[access granted token servlet] > authorize url={}",urlToBeAuthorized);

			// a) login???
			boolean loginExists = this.chekLogin(request);

			if (log.isDebugEnabled()) log.debug("\tlogin exists={}",loginExists);
			if (!loginExists) {
				// store the [url to be authorized] at the [web session]
				// ... later when the [user] has logged-in and the flow returns to this servlet to create the [login session] and [security context],
				//	   the [url to be authorized] is restored from the [web session] and the [user] is redirected
				request.getSession(true)
					   .setAttribute(URL_TO_BE_AUTHORIZED_WEB_SESSION_PARAM_NAME,urlToBeAuthorized);
				// get the url to redir to
				Url loginPageUrl = this.getLoginPageUrl(urlToBeAuthorized,
														request);
				if (log.isDebugEnabled()) log.debug("\tRedir to login page: {}",loginPageUrl);
				response.sendRedirect(loginPageUrl.asString());
				return;	// nothing else to do
			}

			// b) authorization???
			boolean authorized = this.checkAuthorization(urlToBeAuthorized,
															 request);
			if (log.isDebugEnabled()) log.debug("\tauthorized={}",authorized);
			if (!authorized) {
				response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
								   Strings.customized("NOT AUTHORIZED (FORBIDDEN) url={}",
													  urlToBeAuthorized));
				return;	// nothing else to do
			}
			// c) create the [access granted] token and store it inside a [cookie]
			AccessGrantedToken accessGrantedToken = _accessGrantedJWTService.generate();
			accessGrantedTokenCookieStore.storeAccessGrantedToken(HttpServletRequestUtils.clientRequestedHost(request), 	// BEWARE the "real" client requested host (not the proxied one)
																  accessGrantedToken.getJwt(),
																  response);
			// d) redir to the url to be authorized
			response.sendRedirect(urlToBeAuthorized.asString());
		}
		////////// Check [access granted token]
		else if (accessGrantedTokenJWTInRequestParam != null) {
			if (log.isInfoEnabled()) log.info("[access granted token servlet] > validate [access granted token]={}",accessGrantedTokenJWTInRequestParam);

			AccessGrantedToken verifiedToken = _accessGrantedJWTService.verifyJWT(accessGrantedTokenJWTInRequestParam);
			try (PrintWriter writer = response.getWriter()) {
				if (verifiedToken != null) {
					if (log.isInfoEnabled()) log.info("[access granted token servlet] > validate result IS {}", verifiedToken.isJwtValid());
					writer.write(Boolean.toString(verifiedToken.isJwtValid()));		// true / false
				} else {
					log.warn("[access granted token servlet] > validate result FALSE (NULL OBJECT)");
					writer.write(Boolean.toString(false));
				}
				writer.flush();
			}
		}
		////////// fail
		else {
			throw new IllegalArgumentException("[access granted token servlet]: NO param received > either a JWT token to be verified or an URL to be accessed is needed");
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SESSION HANDLING
/////////////////////////////////////////////////////////////////////////////////////////
	private void _cleanupSession(final HttpServletRequest request,final HttpServletResponse response) {
		// create an utility that encapsulates the [security session] handling
		SecuritySessionHandler<P> sessionHandler = new SecuritySessionHandler<>(// StoresSecurityDataAtThreadLocalStorage
																				this,
																				// SecurityContext from LoginSession)
																				theLoginSession -> this.createSecurityContextFromLoginSession(theLoginSession),
																				// providerId cookie name
																				this.authenticationProviderIdCookieName());
		sessionHandler.cleanupSession(request,response);
	}
	private SecurityContext _onLoginSessionExists(final LoginSession<P> loginSession,
									   			  final HttpServletRequest request,final HttpServletResponse response) {
		// create an utility that encapsulates the [security session] handling
		SecuritySessionHandler<P> sessionHandler = new SecuritySessionHandler<>(// StoresSecurityDataAtThreadLocalStorage
																				this,
																				// SecurityContext from LoginSession)
																				theLoginSession -> this.createSecurityContextFromLoginSession(theLoginSession),
																				// providerId cookie name
																				this.authenticationProviderIdCookieName());
		// create a [security context] from the [login session]
		SecurityContext outSecurityContext = sessionHandler.securityContextFromLoginSession(loginSession,
												   											request,response);
		// store a [cookie] that sets the [security provider id] that the [user] used to log-in
		// (this cookie is used to re-create the [security session] when the [user] goes from one [webapp]
		// where he/she has been authenticated to another were he/she has not
		sessionHandler.setAuthnProviderCookie(loginSession.getIdentityProvider(),loginSession.getAuthenticationMethod(),
											   request,response);
		// return
		return outSecurityContext;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	OVERRIDABLE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the [login page] url
	 * @param urlToBeAuthorized
	 * @param request
	 * @return
	 */
	protected Url getLoginPageUrl(final Url urlToBeAuthorized,
								  final HttpServletRequest request) {

		Cookie providerIdCookie = null;
		AuthenticationProviderCookieStore authnProviderCookieStore = AuthenticationProviderCookieStore.createWithCookieName(this.authenticationProviderIdCookieName());
		LoginPageUtilUrlToStep forAuthnProviderStoredAt = LoginPageUtil.forIdentityProviderStoredAt(authnProviderCookieStore);

		if (authnProviderCookieStore == null) {
			// Do nothing
		} else {
			providerIdCookie = authnProviderCookieStore.getAuthnProviderCookieFrom(request);
			if (providerIdCookie == null) {
				Cookie xlnetsCookie = null;
				if (request.getCookies() != null) {
					for (Cookie cookie : request.getCookies()) {
						if( cookie.getName().equals("n38UidSesion") ) {
							xlnetsCookie = cookie;
							break;
						}
					}
				}
				if (xlnetsCookie != null) {
					forAuthnProviderStoredAt = LoginPageUtil.forAuthenticationProvider(IdentityProvider.XLNETS,AuthenticationMethod.UNKNOWN);
					if (log.isDebugEnabled()) log.debug("[access granted token servlet] > No exists provider, but XLNETS session exists.");
				}
			}
		}

		Url loginPageUrl = forAuthnProviderStoredAt
								// after being logged-in the request MUST be redirected to THIS servlet with the jwt that
								// is used to create the login session
								.usingUrlToProvider(theReq -> {
															Host host = HttpServletRequestUtils.clientRequestedHost(request); 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
															UrlPath requestedUrlPath = UrlPath.from(request.getRequestURI());	// {webApp}/{urlPath}	(does NOT include query string)

															// once authorized, the flow will return to THIS servlet but the [url to be authorized]
															// is needed so it's stored as [extended url to info]
															Url toUrl = Url.from(host).joinWith(requestedUrlPath);
															Url outUrl = AfterLoginRedirUrlExtraInfoStore.addAsUrlQueryStringParam(StringBase64.encode(urlToBeAuthorized.asString()))
																										 .to(toUrl);
															if (log.isInfoEnabled()) log.info("... after being logged-in the request will be redirected to: {}",outUrl);
															return outUrl;
													})
							    .loginPageUrl(_loginFilterConfig,
											  request);
		return loginPageUrl;
	}
	/**
	 * Checks if there exists a login session
	 * @param request
	 * @return
	 */
	protected boolean chekLogin(final HttpServletRequest request) {
		// check if there's a [web session] for the user
		LoginSession<P> loginSession = LoginSessionStore.getFromSession(request);
		SecurityContext securityContext = SecurityContextStore.getFromSession(request);
		return loginSession != null && securityContext != null;
	}
	protected boolean checkAuthorization(final Url targetUrl,
									     final HttpServletRequest request) {
		LoginSession<P> loginSession = LoginSessionStore.getFromSession(request);
		SecurityContext securityContext = SecurityContextStore.getFromSession(request);
		if (loginSession == null || securityContext == null) throw new IllegalStateException("NO login: cannot access " + targetUrl);
		return this.checkAuthorization(securityContext,
									   loginSession);
	}
	/**
	 * Gives the application an opportunity to check the [authzs]
	 * @param securityContext
	 * @param loginSession
	 * @return
	 */
	protected boolean checkAuthorization(final SecurityContext securityContext,final LoginSession<P> loginSession) {
		// [1] - Get the user from the [loginSession]
		// 		 (usually THIS filter MUST be AFTER the LoginSessionFilter)
		//		 (see LoginServletFilter)
		SecurityPrincipalOID principalOid = loginSession.getSecurityPrincipalOid();
		if (principalOid == null) throw new IllegalStateException("Could NOT get the [security principal] oid from the [loginSession] " +
														     	  "(the [loginSession] is present at the web session, " +
															 	  "BUT the [security principal] oid could NOT be get)");
		P principal = loginSession.getSecurityPrincipal();
		Collection<SecurityRoleInModule> systemWideRoles = principal.getSystemWideRoles() != null ? principal.getSystemWideRoles() 
																								  : Collections.emptyList();

		// [2] - find the [user auths]
		FindResult<SecurityPrincipalAuthorizationOnResource> authzFind = _authzOnResourceFind.findBy(_sysSecurityContextProvider.get(),
																  									 principalOid,
																  									 _appCode,_appComponent);
		Collection<SecurityPrincipalAuthorizationOnResource> principalAuthzs = authzFind.hasSucceeded() ? authzFind.getOrThrow()
																										: null;
		if (log.isInfoEnabled()) log.info("[access granted token servlet] > [principal] oid={} has {} sys-wide roles and {} auths",
										  principalOid,
										  systemWideRoles != null ? systemWideRoles.size() : 0,
										  principalAuthzs != null ? principalAuthzs.size() : 0);

		// [99] - Delegate
		return this.checkAuthorization(securityContext,
									   principalOid,
									   systemWideRoles,principalAuthzs);
	}
	/**
	 * Gives the application an opportunity to check the [user auths]
	 * @param securityContext
	 * @param principalOid
	 * @param systemWideRoles
	 * @param authzs
	 * @return
	 */
	protected boolean checkAuthorization(final SecurityContext securityContext,
										 final SecurityPrincipalOID principalOid,
										 final Collection<SecurityRoleInModule> systemWideRoles,final Collection<SecurityPrincipalAuthorizationOnResource> authzs) {
		if (log.isDebugEnabled()) log.debug("[access granted token servlet] > [security principal] authzs NOT checked!!! > override this method to check the auth; for now just return AUTHORIZED");
		return true;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	COOKIES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the name of the [cookie] used to store the [security provider id]
	 * (the [security provider] used to log-in)
	 * @return
	 */
	protected String authenticationProviderIdCookieName() {
		return AuthenticationProviderCookieStore.AUTH_PROVIDER_COOKIE_DEFAULT_NAME;
	}
	/**
	 * Returns the name of the [cookie] used to store the [security provider id]
	 * (the [security provider] used to log-in)
	 * @return
	 */
	protected String accessGrantedTokenCookieName() {
		return AccessGrantedTokenCookieStore.ACCESS_GRANTED_COOKIE_DEFAULT_NAME;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO BE IMPLEMENTED
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called AT THE END of the filter if the {@link LoginSession} object
	 * could be created (and stored into the web session) or retrieved from the web session
	 * Overriding this method, a {@link SecurityContext} object can be created FROM the {@link LoginSession}
	 * If the method returns a NOT null value, the created {@link SecurityContext} is attached at
	 * the {@link ThreadLocal} and put into the [web session] store
	 * @param loginSession
	 * @return
	 */
	protected abstract SecurityContext createSecurityContextFromLoginSession(final LoginSession<P> loginSession);
}