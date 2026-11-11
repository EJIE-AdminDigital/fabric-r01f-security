package r01f.security.login.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Instant;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import com.google.common.base.Stopwatch;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.model.security.IdentityProviderAndAuthnMethod;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.security.authn.provider.cookie.AuthenticationProviderCookie;
import r01f.security.authn.provider.cookie.AuthenticationProviderCookieStore;
import r01f.security.base.SecuritySessionHandler;
import r01f.security.base.StoresSecurityDataAtThreadLocalStorage;
import r01f.security.context.SecurityContextStore;
import r01f.security.context.SecurityContextStoreAtThreadLocalStorage;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.url.Host;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * SEE THE DOCs FOLDER FOR THE SEQUENCE DIAGRAMS
 *
 * There are TWO filters in place: [login] filter and [auth] filter
 * <pre>
 * 		+=================+
 * 		|   login filter  | <-- check if the [user] has a login in the [security system]
 * 		+=================+		This filter puts TWO objects at the [thread local] and at the [web session] storages
 * 				 |					- the LoginSession > contains info about the user who has loged-in
 * 				 \/ 				- the SecurityContext
 * 		+=================+
 * 		|   auth filter   |	<-- uses the authorizations repo to get the auths for the user
 * 		+=================+		This filter puts an object at the [thread loca] and at the [web session] storages
 * 									- UserAuthorizationSession > contains info about the user's [auth]s on [resources]
 * </pre>
 * This is the LOGIN filter; it JUST check the [security system] to see if the user
 * has a valid login; if not, the user is redirected to the login page
 * once logged-in, Usually, the [login info] is used to create a [security context] object
 * used in all the stack. This [security context] object DOES NOT jet have the users's [auths]
 * in this place; it's the [auth filter]'s responsibility to query the [auth repo] and put
 * the user's [auths] into the [security context]
 */
@Slf4j
@Accessors(prefix="_")
public abstract class LoginSessionServletFilter<P extends IsSecurityPrincipal>
  		   implements Filter,
  		   			  StoresSecurityDataAtThreadLocalStorage<P> {
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final Class<P> _userType;
	@Getter protected final SecurityLoginFilterConfig _loginFilterConfig;

	@Getter protected final Predicate<ServletRequest> _notFilteredResourcePredicate;

	// Queries a LOCAL endpoint that returns the LOCAL user data
	// BEWARE!!
	// 		The LOCAL [user data] IS NOT the same as the one returned by a [security provider] (ie google, xlnets, etc)
	// 		Usually the LOCAL [user data] is created FROM the one returned by google, xlnets, etc,
	// 		BUT might have more (or less) properties
	@Getter protected final SecurityPrincipalDataFromAfterLoginJWT<P> _userDataFromAfterLoginJWT;

	// After a user is logged-in, it's redirected to the [Security REST Services WAR] where an [after login jwt] token is created and then
	// the [user] is redirected to the [UI] with this [after login jwt] token as param
	// At the [UI] the request passes through the {@link LoginSessionServletFilter} that tries to create the {@link LoginSession}
	// ... to do so, the [login session servlet filter] calls back the [Security REST Services WAR] to get the [user data]
	// 		U user = _userDataFromAfterLoginJWT.getUserDataFromJWT(jwt);  <<<< issues an HTTP request to the [Security REST Services WAR] to get the [user data]
	// 																 		   ... the HTTP request contains the [after login JWT] token so the [security REST services WAR]
	// 																	 	       can be sure the request is a legitimate one
	// In order to get the [user data], the [security REST services WAR] uses some data encoded at the [after login JWT] token
	// ... BUT sometimes additional data is needed, and that data it's NOT encoded at the [after login JWT] token!!
	//
	// This provider gives the filter an opportunity to get this additional data
	@Getter protected final SecurityProviderSessionFromRequestProvider _securityProviderSessionFromRequestProvider;

	@Getter protected FilterConfig _servletFilterConfig = null;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig,
						 			 // used to "serialize" the XLNets session from the [UI War] to the [security REST services WAR]
									 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 securityProviderSessionFromRequestProvider,
			 (Predicate<ServletRequest>)null);		// notFilteredResourcePredicate
	}
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to "serialize" the XLNets session from the [UI War] to the [security REST services WAR]
									 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
									 // not filtered resources
									 final Collection<Pattern> notFilteredResourcesPatterns) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 securityProviderSessionFromRequestProvider,
			 _createServletRequestPredicateFrom(notFilteredResourcesPatterns));
	}
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig,
									 // used to "serialize" the XLNets session from the [UI War] to the [security REST services WAR]
									 final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider,
									 // not filtered resources
									 final Predicate<ServletRequest> notFilteredResourcePredicate) {
		_loginFilterConfig = loginFilterConfig;
		_userType = userType;
		_userDataFromAfterLoginJWT = new SecurityPrincipalDataFromAfterLoginJWT<>(_userType,
												 					 			  _loginFilterConfig,
												 					 			  marshaller);
		_securityProviderSessionFromRequestProvider = securityProviderSessionFromRequestProvider;
		_notFilteredResourcePredicate = notFilteredResourcePredicate;
	}
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 (SecurityProviderSessionFromRequestProvider)null);
	}
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig,
									 final Collection<Pattern> notFilteredResourcesPatterns) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 null,		// SecurityProviderSessionFromRequestProvider
			 _createServletRequestPredicateFrom(notFilteredResourcesPatterns));
	}
	public LoginSessionServletFilter(final Class<P> userType,
									 final Marshaller marshaller,
									 final SecurityLoginFilterConfig loginFilterConfig,
									 final Predicate<ServletRequest> notFilteredResourcePredicate) {
		this(userType,
			 marshaller,
			 loginFilterConfig,
			 (SecurityProviderSessionFromRequestProvider)null,
			 notFilteredResourcePredicate);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected static Predicate<ServletRequest> _createServletRequestPredicateFrom(final Collection<Pattern> notFilteredResourcesPatterns) {
		// create a predicate using the filtered resources patterns
		return req -> {
					boolean outNotFiltered = false;	// not filtered by default

					HttpServletRequest request = (HttpServletRequest)req;
					String reqUri = request.getRequestURI();
					if (CollectionUtils.hasData(notFilteredResourcesPatterns)) {
						for (Pattern notFilteredPattern : notFilteredResourcesPatterns) {
							if (notFilteredPattern.matcher(reqUri).matches()) {
								outNotFiltered = true;	// not filter
								break;
							}
						}
					}
					return outNotFiltered;
				};
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
	@Override
	public void doFilter(final ServletRequest req,final ServletResponse res,
						 final FilterChain chain) throws IOException,
														 ServletException {
		try {
			if (log.isTraceEnabled()) {
				_tracePrincipalOfSecurityContext("INIT");
			}
			_executeFilter(req, res, chain);
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
		} finally {
			if (log.isTraceEnabled()) {
				_tracePrincipalOfSecurityContext("END");
			}
			SecurityContextStoreAtThreadLocalStorage.remove();
			log.trace("[login filter][remove Thread]. REMOVED");
		}
	}

	private void _executeFilter(final ServletRequest req,
								final ServletResponse res,
						 		final FilterChain chain) throws IOException,
														 		ServletException {
		boolean logEnabled = _isLogEnabled();
		if (logEnabled) log.trace("[login filter][START]: {}", ((HttpServletRequest)req).getRequestURL());

		// [0] - not filtered resources
		HttpServletRequest request = (HttpServletRequest)req;
		HttpServletResponse response = (HttpServletResponse)res;

		//		 beware!! do NOT move... always filter AFTER setting the security context at thread local
		if (_notFilteredResourcePredicate != null
		 && _notFilteredResourcePredicate.test(req)) {
			if (logEnabled) log.trace("[login filter][NOT FILTERED]: {} >>>>>>>>>>",
									  request.getRequestURI());
			_filterChain(chain,
						 null,request,response);
			return;
		}

		// [0.5] - Get some data from the request
		Url requestedUrl = Url.from(request.getRequestURL());	// http://localhost:8080/{webAp}/{urlPath} (does NOT include query string) BEWARE! this url might NOT be the client-requested one if there's a proxy upstream
		Url clientRequestedUrl = HttpServletRequestUtils.clientRequestedHost(request) 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
													  		.asUrl()
													  		.joinWith(UrlPath.from(request.getRequestURI()));	// {webApp}/{urlPath}	(does NOT include query string)
		UrlPath webAppContext = UrlPath.from(request.getContextPath());		// {webApp}
		UrlPath requestedUrlPath = UrlPath.from(request.getRequestURI());	// {webApp}/{urlPath}	(does NOT include query string)
		UrlQueryString requestedQryString = UrlQueryString.fromParamsString(request.getQueryString());

		if (logEnabled) log.trace("[login filter][START]: {} >>>>>>>>>>\n" +
								  "\t- client requested url={}\n" +
								  "\t- web app context={} url path={} query string={}\n",
								  requestedUrl,
								  clientRequestedUrl,
								  webAppContext,requestedUrlPath,requestedQryString);

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
		boolean isLogout = false;
		if (LoginPageUtil.isLogout(request)) {
			if (logEnabled) log.trace("[login filter] > logout!!");
			isLogout = true;

			// remove everything from the session
			_cleanupSession(request,response);
		}

		// [2] - Check the http session to see if there exists an [loginSession]
		LoginSession<P> loginSession = LoginSessionStore.getFromSession(request);
		if (loginSession != null) _onLoginSessionExists(loginSession,
								  						request,response);

		// [3] - Get the [after login jwt] from the request
		//		 (the [after login jwt] is only present if the user enters here AFTER being authenticated)
		JWT afterLoginJWT = AfterLoginJWTStore.getFromRequest(request);

		// Scenarios
		// 	a.- There exists a [login session] but an [after login jwt] is received from the request (the user comes here after being authenticated) > remove the PREVIOUS [login session]
		//	b.- The user has been authenticated and this is the first time he enters the target with an [after login jwt] at the request > The user has just an [after login jwt] (NO [login session])
		// 	c.- The user has NOT been authenticated > neither [login session] exists nor an [after login jwt] is received at the request > redir to the login page
		//	d.- There exists a [login session] and an [after login jwt] is NOT received at the request > The user has been previously authenticated and is returning
		if (loginSession != null
		 && afterLoginJWT != null) {
			if (logEnabled) log.trace("[login filter] > [login session]=EXISTS / [after login jwt token]=EXISTS > NEW Login without a Logout of the previous session > remove previous [login session] and apply new login");

			loginSession = null;
		}

		if (loginSession == null
		 && afterLoginJWT != null) {
			if (logEnabled) log.trace("[login filter] > [login session]=ABSENT / [after login jwt token]=EXISTS > request reaches here just AFTER being loged-in at the [security provider] > create a [login session] from JWT token");

			// a) remove everything
			_cleanupSession(request,response,
							false); // Do not remove cookieProviderId

			// b) The [user] has logged-in using a [security provider]'s login page (ie google, xlnets...)
			// 	  After being logged-in, the [user] has been redirected from the [security provider]'s login ui to the [security REST services]
			// 	  where an [after login JWT] token is created and then redirected again to the [UI] where this filter is configured
			// 	  ... so this is the first time he enters the target...
			// 	  here just an [after login JWT] ia available but in order to create the [Login Session], the [user data] is needed
			// 	  ... and to get the [user data], the [filter] calls back the [security REST services] with the [after login JWT] token
			//     	  that "tells" the [security REST services] it's a legitimate request and just returns the [user data]
			try {
				loginSession = _userDataFromAfterLoginJWT.getLoginSessionFormJWT(request,
																				 afterLoginJWT,
																				 _securityProviderSessionFromRequestProvider);
			} catch (IllegalStateException illegalExc) {
				if (illegalExc.getMessage().contains("Error. Invalid jwt EXPIRED")) {
					if (logEnabled) log.trace("[login filter] > [login session]=ABSENT / jwt EXPIRED > is authenticated OR redir to login page");

					// Two options: either the user is authenticated, or they need to log in through the login page.
					_goToPossibleLog(request, response, loginSession, clientRequestedUrl, isLogout);
					return;
				} else {
					throw illegalExc;
				}
			}
			// c) now the http session contains the [loginSession]
			_onLoginSessionExists(loginSession,
								  request,response);

			// d) Give the application an opportunity to verify the user status
			// 	  The session created at this point is correct BUT there could be some actions to complete depending on
			// 	  for example the user status [see @UserStatus]
			// 	  By default UserStatus is create as ACTIVE, but the are some other cases such as:
			//  	- PENDING_ADMIN_APROVAL			: A user has been created but requires an admin aproval
			//							 					> redir to an informative page
			//		- PENDING_USER_DATA_COMPLETION	: the user has been created, but additional user data not provided by the google / xlnets / etc must be completed.
			//												> redir to a user-data completion page
			//		- INACTIVE						: the user has been put into an inactive state for any reason
			//												> redir to an informative page
			Url redirUrl = this.getRedirUrlAfterLoginIfNecessary(loginSession,
																 request,response);
			if (redirUrl == null
			 || redirUrl.getUrlPath().is(requestedUrlPath)) {	// BEWARE!! avoid entering into an infinite redirection loop
				// Redir to the requested url WITHOUT the [after login jwt]
				UrlQueryString qryStringWithoutJWTParam = requestedQryString.withoutParam(AfterLoginJWTStore.JWT_PARAM);	// remove the [after login jwt] query string param & redir
				redirUrl = Url.from(requestedUrlPath)
							  .joinWith(qryStringWithoutJWTParam);	// request url WITHOUT the [after login JWT] param
			} else {
				// the [principal] has a [login session] BUT maybe the [principal] is NOT active or [principal] data must be completed
				if (logEnabled) log.trace("[login filter] > [principal]={} has to complete some data: redir to {}",
						 				  loginSession.getSecurityPrincipalId(),
						 				  redirUrl);
			}
			if (logEnabled) log.trace("[login filter][REDIR] {} <<<<<<<<<<",
									  redirUrl);
			this.beforeRedirectWithLogin(loginSession,
										 request,response);
			response.sendRedirect(redirUrl.asString());
		}
		else if (loginSession == null
			  && afterLoginJWT == null) {
			if (logEnabled) log.trace("[login filter] > [login session]=ABSENT / [after login jwt token]=ABSENT > NO [login session] > POSSIBLE redir to login page");

			// Two options: either the [principal] is authenticated, or they need to log in through the login page.
			_goToPossibleLog(request, response, loginSession, clientRequestedUrl, isLogout);
		}
		else if (loginSession != null
			  && afterLoginJWT != null) {
			throw new IllegalStateException("This is IMPOSIBLE: once the [principal] is authenticated, the [after login jwt] token MUST be removed from url; this is a developer fault!!");
		}
		else if (loginSession != null) {
			// the [principal] has previously been authenticated > the [login session] and [security context] MUST be present (see _onLoginSessionExists)
			if (LoginSessionStore.getFromSession(request) == null || SecurityContextStore.getFromSession(request) == null) throw new IllegalStateException("[login session] or [security context] is NOT present!!");

			if (logEnabled) log.trace("[login filter] > [login session]=PRESENT / [after login jwt]=ABSENT > There exists a [loginSession] for [principal]={} go on to {}",
					 				  loginSession.getSecurityPrincipalId(),
					 				  request.getRequestURI());
			 // Warn : _filterChain exposes internally beforeFilterChain if any condition must be validated.
			_filterChain(chain,
						 loginSession,request,response);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHAIN
/////////////////////////////////////////////////////////////////////////////////////////
	protected void _filterChain(final FilterChain chain,
								final LoginSession<P> loginSession,
								final HttpServletRequest request,final HttpServletResponse response) throws IOException,
																										  	ServletException {
		boolean logEnabled = _isLogEnabled();
		if (logEnabled) log.trace("[login filter][CHAIN] start --------------------------{}",
								  request.getRequestURI());
		Stopwatch sw = logEnabled ? Stopwatch.createStarted() : null;

		Url urlToRedirectBeforeFilterChain = this.beforeFilterChain(loginSession,
									   								request,response);
		if (urlToRedirectBeforeFilterChain == null) {
			chain.doFilter(request,response);
		} else {
			if (logEnabled) log.trace("[login filter] > must REDIRECT TO : {}",
									  urlToRedirectBeforeFilterChain);
			response.sendRedirect(urlToRedirectBeforeFilterChain.asString());
		}
		long elapsedMilis = sw != null ? sw.elapsed(TimeUnit.MILLISECONDS) : 0;
		if (logEnabled) log.trace("[login filter][CHAIN] end ({} milis) -----------------{}",
								  elapsedMilis,request.getRequestURI());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SESSION HANDLING
/////////////////////////////////////////////////////////////////////////////////////////
	private void _cleanupSession(final HttpServletRequest request,final HttpServletResponse response) {
		_cleanupSession(request, response, true);
	}
	private void _cleanupSession(final HttpServletRequest request,final HttpServletResponse response, final boolean isWithCookieClean) {
		if (isWithCookieClean) {
			// create an utility that encapsulates the [security session] handling
			SecuritySessionHandler<P> sessionHandler = new SecuritySessionHandler<>(// StoresSecurityDataAtThreadLocalStorage
																					this,
																					// SecurityContext from LoginSession)
																					this::createSecurityContextFromLoginSession,
																					// providerId cookie name
																					this.securityProviderIdCookieName());
			sessionHandler.cleanupSession(request,response);
		} else {
			// remove the [login session] from the [web session]
			LoginSessionStore.removeFromHttpSession(request);

			// remove the [security context] from the [web session]
			SecurityContextStore.removeFromHttpSession(request);
		}
	}
	private SecurityContext _onLoginSessionExists(final LoginSession<P> loginSession,
									   			  final HttpServletRequest request,final HttpServletResponse response) {
		// create an utility that encapsulates the [security session] handling
		SecuritySessionHandler<P> sessionHandler = new SecuritySessionHandler<>(// StoresSecurityDataAtThreadLocalStorage
																				this,
																				// SecurityContext from LoginSession)
																				this::createSecurityContextFromLoginSession,
																				// providerId cookie name
																				this.securityProviderIdCookieName());
		// create a [security context] from the [login session]
		SecurityContext outSecurityContext = sessionHandler.securityContextFromLoginSession(loginSession,
												   											request,response);
		// store a [cookie] that sets the [security provider id] that the [principal] used to log-in
		// (this cookie is used to re-create the [security session] when the [principal] goes from one [webapp]
		// where he/she has been authenticated to another were he/she has not
		if (loginSession.getIdentityProvider() != null) {
			sessionHandler.setAuthnProviderCookie(loginSession.getIdentityProvider(),loginSession.getAuthenticationMethod(),
												  request,response);
		}
		// return
		return outSecurityContext;
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN CONTROL
/////////////////////////////////////////////////////////////////////////////////////////
	private void _goToPossibleLog(final HttpServletRequest request, final HttpServletResponse response,
			final LoginSession<P> loginSession, final Url clientRequestedUrl, final boolean isLogout) throws IOException {

		boolean logEnabled = _isLogEnabled();

		// remove everything
		_cleanupSession(request,response,
				        false); // Do not remove cookieProviderId

// ----------------------------------------------------
		boolean hasSessionInProvider = false;
		String urlSendRedirect = null;

		IdentityProvider identityProvider = null;
		AuthenticationMethod authnMethod = null;
		
		Cookie authnProviderCookie = null;
		AuthenticationProviderCookieStore authnProviderCookieStore = AuthenticationProviderCookieStore.createWithCookieName(this.securityProviderIdCookieName());

		if (authnProviderCookieStore == null) {
			// Do nothing
		} else {
			authnProviderCookie = authnProviderCookieStore.getAuthnProviderCookieFrom(request);
			if (authnProviderCookie == null) {
				Cookie xlnetsCookie = null;
				if (request.getCookies() != null) {
					for (Cookie cookie : request.getCookies()) {
						if (cookie.getName().equals("n38UidSesion") ) {
							xlnetsCookie = cookie;
							break;
						}
					}
				}
				if (xlnetsCookie != null) {
					Host clientRequestedHost = HttpServletRequestUtils.clientRequestedHost(request); 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
					AuthenticationProviderCookie spic = authnProviderCookieStore.storeAuthnProvider(clientRequestedHost,
																									 IdentityProvider.XLNETS,AuthenticationMethod.UNKNOWN, 
																									 response);
					authnProviderCookie = spic.createNonPersistentCookie();
					if (logEnabled) log.trace("[login filter] > [login session]=ABSENT / [after login jwt token]=ABSENT > NO [login session] > No provider, but exists XLNETS session");
				}
			}
		}

		if (authnProviderCookie == null) {
			// Do nothing
			if (logEnabled) log.trace("[login filter] [Go To Possible Log] NO SECURITY PROVIDER. GO TO LOGIN.");
		} else {
			if (!isLogout
			 && Strings.isNOTNullOrEmpty(authnProviderCookie.getValue())
			 && authnProviderCookie.getMaxAge() != 0) {

				if (logEnabled) log.trace("[login filter] [Go To Possible Log] Exists a SECURITY PROVIDER: {}", authnProviderCookie.getValue());

				IdentityProviderAndAuthnMethod idpAndAuthnMethod = AuthenticationProviderCookie.decodeCookieValue(authnProviderCookie.getValue());
				identityProvider = idpAndAuthnMethod.getIdentityProvider();
				authnMethod = idpAndAuthnMethod.getAuthnMethod();

				Url securityRESTServicesLoginEndPointForProvider = _loginFilterConfig.getSecurityRESTServicesLoginEndPointForProviderWithId(identityProvider,authnMethod);

				String reqUrlStr = clientRequestedUrl.toString();

				reqUrlStr = reqUrlStr.endsWith("/logout/") ? reqUrlStr.replaceAll("/logout/","/") : reqUrlStr;
				reqUrlStr = reqUrlStr.endsWith("/logout") ? reqUrlStr.replaceAll("/logout","/") : reqUrlStr;

				Url urlTo = Url.from(reqUrlStr);

				// TODO maybe POST form params should be "translated" to GET query string params??? (if so use the commented code below)
				// add the query params
				if (Strings.isNOTNullOrEmpty(request.getQueryString())) {
					UrlQueryString qryStrUrlTo = UrlQueryString.fromParamsString(request.getQueryString());
					urlTo = urlTo.joinWith(qryStrUrlTo);
				}

				Url urlToRedir = null;
				// the final url
				urlToRedir = securityRESTServicesLoginEndPointForProvider.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("to",urlTo)));

				// There is a provider, so the [principal] must to redirect to it.
				hasSessionInProvider = true;

				// Store the callback url param in session
				// ... and anything else, because must go to url security REST Services Login End Point For Provider.
				this.beforeFilterChain(loginSession, request,response);

				urlSendRedirect = urlToRedir.asString();
			} else {
				if (logEnabled) log.trace("[login filter] > [login session]=ABSENT / [after login jwt token]=ABSENT > NO [login session or security provider] > Redir to Login.");
			}
		}

// ----------------------------------------------------
		if (!hasSessionInProvider) {
			// get the url to redir to
			Url loginPageUrl = LoginPageUtil.forIdentityProviderStoredAt(authnProviderCookieStore)
											.usingDefaultUrlToProvider()
											.loginPageUrl(_loginFilterConfig,
														  request);
			if (logEnabled) log.trace("[login filter][REDIR TO LOGIN]: {}",loginPageUrl);

			this.beforeRedirectWithoutLogin(request,response);
			urlSendRedirect = loginPageUrl.asString();
		} else {
			if (logEnabled) log.trace("[login filter][REDIR to known provider]: {}", urlSendRedirect);
		}
// ----------------------------------------------------
		response.sendRedirect(urlSendRedirect);
	}

/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called AT THE END of the filter if the [login session] object
	 * could be created or retrieved from the [web session]
	 * Overriding this method, a {@link SecurityContext} object can be created FROM the {@link LoginSession}
	 * If the method returns a NOT null value, the created {@link SecurityContext} is attached at
	 * the {@link ThreadLocal} and put into the [thread local] and [web session] store
	 * @param loginSession
	 * @return
	 */
	protected abstract SecurityContext createSecurityContextFromLoginSession(final LoginSession<P> loginSession);
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PROVIDER COOKIE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the name of the [cookie] used to store the [security provider id]
	 * (the [security provider] used to log-in)
	 * @return
	 */
	@SuppressWarnings("static-method")
	protected String securityProviderIdCookieName() {
		return AuthenticationProviderCookieStore.AUTH_PROVIDER_COOKIE_DEFAULT_NAME;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HOOK POINTS THAT CAN BE OVERRIDEN
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called AT THE END of the filter if the {@link LoginSession} object
	 * Gives the application an opportunity to verify the [principal] status
	 * The session created at this point is correct BUT there could be some actions to complete depending on
	 * for example the [principal] status [see @SecurityPrincipalStatus]
	 * By default [principal] status is create as ACTIVE, but the are some other cases such as:
	 *  	- PENDING_ADMIN_APROVAL			: A [principal] has been created but requires an admin aproval
	 *									 		> redir to an informative page
	 *		- PENDING_USER_DATA_COMPLETION	: the [principal] has been created, but additional user data not provided by the google / xlnets / etc must be completed.
	 *												> redir to a user-data completion page
	 *		- INACTIVE						: the [principal] has been put into an inactive state for any reason
	 *											> redir to an informative page
	 * This method just returns the url where the request should be redirected (if any)
	 * if the method returns null, the filter is chained
	 * @param request
	 * @param loginSession
	 */
	@SuppressWarnings("unused")
	protected Url getRedirUrlAfterLoginIfNecessary(final LoginSession<P> loginSession,
												   final HttpServletRequest request,final HttpServletResponse response) {
		return null;	// by default null > no redirection
	}
	/**
	 * Gives any type extending this type an opportunity to set a cookie, a request / session attribute....
	 * BEFORE chaining
	 * @param loginSession
	 * @param request
	 * @param response
	 */
	protected Url beforeFilterChain(final LoginSession<P> loginSession,
									final HttpServletRequest request,final HttpServletResponse response) {
		return null;	// do nothing by default
	}
	/**
	 * Gives any type extending this type an opportunity to set a cookie, a session attribute....
	 * BEFORE redirecting
	 * @param request
	 * @param response
	 */
	protected void beforeRedirectWithoutLogin(final HttpServletRequest request,final HttpServletResponse response) {
		// do nothing by default
	}
	/**
	 * Gives any type extending this type an opportunity to set a cookie, a session attribute....
	 * BEFORE redirecting
	 * @param loginSession
	 * @param request
	 * @param response
	 */
	protected void beforeRedirectWithLogin(final LoginSession<P> loginSession,
								  		   final HttpServletRequest request,final HttpServletResponse response) {
		// do nothing by default
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOG
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	private boolean _isLogEnabled() {
		return log.isTraceEnabled();
	}

	private void sendErrorMessageToUser(final ServletResponse res, final String stMessage, final Throwable t) {
		String traza_error = "ID_FILTER:[" + Instant.now().toEpochMilli() + "]. " + stMessage + "...END_TRACE.";

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

	private void _tracePrincipalOfSecurityContext(final String msg) {
		String principalIdStr = "[PRINCIPAL NO EXISTS AT SECURITY CONTEXT]";
		SecurityContext securityContext = SecurityContextStoreAtThreadLocalStorage.get();
		if (securityContext != null) {
			SecurityPrincipalID principalId = securityContext.getSecurityPrincipalId();
			if (principalId == null) { 
				log.trace("[login filter][Thread]. {}. [principal] IS NULL",
						  msg);
			} else {
				principalIdStr = principalId.asString();
			}
		} else {
			log.trace("[login filter][Thread]. {}. ThreadLocalStorage IS NULL",
					  msg);
		}
		log.trace("[login filter][Thread]. {}. [principal]={} (SecurityContext:{})",
				  msg,principalIdStr,
				  securityContext);
	}
}