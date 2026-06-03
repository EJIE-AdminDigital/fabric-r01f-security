package r01f.security.base;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;
import r01f.security.provider.SecurityProviderIdCookieStore;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.url.Host;

/**
 * Encapsulates the logic executed when a {@link LoginSession} exists
 * @param <U>
 */
@Slf4j
@RequiredArgsConstructor
public class SecuritySessionHandler<U extends User> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final StoresSecurityDataAtThreadLocalStorage<U> _securityDataAtThreadLocalStorage;
	private final FactoryFrom<LoginSession<U>,SecurityContext> _securityContextFromLoginSessionFactory;
	private final String _securityProviderIdCookieName;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Cleans the session
	 * @param request
	 * @param response
	 */
	public void cleanupSession(final HttpServletRequest request,final HttpServletResponse response) {
		// remove the [login session] from the [web session]
		LoginSessionStore.removeFromHttpSession(request);

		// remove the [security context] from the [web session]
		SecurityContextStore.removeFromHttpSession(request);

		// remove the [cookie] that stores the [security provider id] used to login
		SecurityProviderIdCookieStore securityProviderIdCookieStore = SecurityProviderIdCookieStore.createWithCookieName(_securityProviderIdCookieName);
		securityProviderIdCookieStore.removeProviderId(request,response);

		// clear she [web session]
		// request.getSession().invalidate();	// BEWARE! do NOT invalidate since if there exists another filters or session data it's lost
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a {@link SecurityContext} from a {@link LoginSession}
	 * ... if the {@link SecurityContext} was already present at the [web session] it ensures
	 * 	   that the stored one is for the SAME login (same user) and if not it's invalidated
	 * @param loginSession
	 * @param request
	 * @param response
	 * @return
	 */
	public SecurityContext securityContextFromLoginSession(final LoginSession<U> loginSession,
									   					   final HttpServletRequest request,final HttpServletResponse response) {
		boolean logEnabled = _isLogEnabled();

		// [1] - put the [login session] at the ThreadLocal & [web session] storage
		LoginSessionStore.storeAtHttpSession(request,
											 loginSession);
		_securityDataAtThreadLocalStorage.storeLoginSessionAtThreadLocalStorage(loginSession);

		// [2] - get the [security context] from the [session store]
		SecurityContext securityContext = SecurityContextStore.getFromSession(request);

		// BEWARE!!! the user might have change the [login] (he/she might have logged in with another login)
		//			 ... so the session-stored [security context] might NOT be the same
		if (securityContext != null
		 && securityContext.getLoginId().isNOT(loginSession.getLoginId())) {
			SecurityContextStore.removeFromHttpSession(request);	// remove from session
			securityContext = null;
		}

		// [3] - if the security context is NOT available create it
		if (securityContext == null) {
			if (logEnabled) log.trace("[There DOES NOT exsist a [security context] object at the [web session]; attach it...");
			securityContext = _securityContextFromLoginSessionFactory.from(loginSession);
		}
		// [4] - put the created [security context] at the [thread local] store
		if (securityContext != null) {
			if (logEnabled) log.trace("[security context] for user oid={} with loginId={} > " +
									  "the new [security context] object DOES NOT have any [auth] info since that info is the [auth filter] responsibility",
									  loginSession.getUserOid(),loginSession.getLoginId());
			// put the created [security context] at the [thread local] & web session
			SecurityContextStore.storeAtHttpSession(request,
													securityContext);
			_securityDataAtThreadLocalStorage.storeSecurityContextAtThreadLocalStorage(securityContext);
		} else {
			log.error("[login filter] > NO [security context] created: this is usually a developer error!!!!");
		}
		// [99] - Return the created security context
		return securityContext;
	}
	/**
	 * Sets a [cookie] that stores the [security provider ID] the [user] used to log-in
	 * @param providerId
	 * @param request
	 * @param response
	 */
	public void setSecurityProviderIdCookie(final SecurityProviderID providerId,
											final HttpServletRequest request,final HttpServletResponse response) {
		// set a DOMAIN cookie used when a user navigates from one [web app] to another that shares the SAME login
		//		 The flow is like:
		//			a) the [user] access [webApp A] and creates a [login session]
		//			b) the [user] access [webApp B] > since the [web session] is NOT THE SAME, the user is NOT authenticated
		//											  and the user should be redirected to the [login page]
		//		 If [webApp A] and [webApp B] shares the SAME [login], a "trick" is used:
		//			a) when the [user] logs in at [webApp A], a [DOMAIN cookie] is set with the [securityProviderId] (ie: goolge, xlnets, saml...)
		//			b) when the [user] access [webApp B], there's NO [login session] so the [DOMAIN cookie] is checked
		//			   and the [user] ir REDIRECTED to the [security REST service] /login end-point where the [user] is in turn REDIRECTED again
		//			   to the [security provider]'s [login page]
		//			c) ... since the [user] is already loged-in, he/she is REDIRECTED again to the [target url] (the [webApp B] url) with the
		//			   properly generated [jwt] token
		//			d) ... with the [after login jwt] token the [login session] is created as usual
		SecurityProviderIdCookieStore providerIdcookieStore = SecurityProviderIdCookieStore.createWithCookieName(_securityProviderIdCookieName);
		if (!providerIdcookieStore.existsProviderIdAt(request)) {
			Host clientRequestedHost = HttpServletRequestUtils.clientRequestedHost(request); 	// BEWARE!! this method takes proxies into account in order to get the REAL client url
			providerIdcookieStore.storeProviderId(clientRequestedHost,
												  providerId,
												  response);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	private boolean _isLogEnabled() {
		return log.isTraceEnabled();
	}
}
