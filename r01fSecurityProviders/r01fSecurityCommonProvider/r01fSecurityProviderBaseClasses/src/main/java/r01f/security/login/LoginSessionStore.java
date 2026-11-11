package r01f.security.login;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.IsSecurityPrincipal;
import r01f.servlet.HttpServletSessionUtils;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class LoginSessionStore {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANT
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String LOGIN_CTX_SESSIONATTR = "r01LoginSession";
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN GETTERS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static <U extends IsSecurityPrincipal> LoginSession<U> getFromSession(final HttpServletRequest request) {
		if (log.isTraceEnabled()) log.trace("Retrieve [login session] from http session ");
		LoginSession<U> outSession = null;
		HttpSession ses = request.getSession(false);	// do not create a new session
		if (ses != null) {
			outSession = (LoginSession<U>)ses.getAttribute(LOGIN_CTX_SESSIONATTR);
			if (log.isTraceEnabled()) log.info("[login session] stored at http session with id={}!",
					 						   ses.getId());
		} else {
			if (log.isTraceEnabled()) log.info("NO [login session] stored at http session!");
		}
		if (log.isTraceEnabled() && outSession == null && ses != null) {
			log.info("The [login session] was NOT found at the [web session]\n{}",
					 HttpServletSessionUtils.composeSessionDebugInfo(ses));
		}
		return outSession;
	}
	public static <U extends IsSecurityPrincipal> LoginSession<U> getFromThreadLocalStorage() {
		if (log.isTraceEnabled()) log.trace("Retrieve [login session] from ThreadLocal storage ");
		return LoginAuthContextThreadLocalManager.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN SETTERS
/////////////////////////////////////////////////////////////////////////////////////////	
	public static <U extends IsSecurityPrincipal> void storeAtHttpSession(final HttpServletRequest request,
														   				  final LoginSession<U> loginSession) {
		 if (log.isTraceEnabled()) log.info("Storing [login session] on session.. {}",
				  							loginSession.toString());
		 HttpSession ses = request.getSession(true);
		 request.setAttribute(LOGIN_CTX_SESSIONATTR,loginSession);
		 ses.setAttribute(LOGIN_CTX_SESSIONATTR, loginSession);
	}
	/**
	 * When [principal] is updated, the [session-stored] data should also be updated
	 * be updated
	 * @param <P>
	 * @param request
	 * @param updatedPrincipal
	 */
	public static <P extends IsSecurityPrincipal> void updateAtHttpSessionFrom(final HttpServletRequest request,
														 					   final P updatedPrincipal) {
		// Update the [principal] data stored at the [LoginSession]
		// (this object is used at the [LoginSessionServletFilter])
		LoginSession<P> loginSession = LoginSessionStore.getFromSession(request);
		LoginSession<P> updatedLoginSession = loginSession.cloneForUpdatedSecurityPrincipal(updatedPrincipal);
		LoginSessionStore.storeAtHttpSession(request,updatedLoginSession);
	}
	public static <P extends IsSecurityPrincipal> void  storeAtThreadLocalStorage(final LoginSession<P> loginSession) {
		 if (log.isTraceEnabled()) log.info("Storing [login session] on ThreadLocal.. {}",
				 							loginSession.toString());
		 LoginAuthContextThreadLocalManager.set(loginSession);
	}
	public static <P extends IsSecurityPrincipal> void removeFromHttpSession(final HttpServletRequest request) {
		 if (log.isTraceEnabled()) log.info("Removing [login session] from session.");
		 request.removeAttribute(LOGIN_CTX_SESSIONATTR);
		 HttpSession ses = request.getSession(false);
		 if (ses != null) {
			 ses.removeAttribute(LOGIN_CTX_SESSIONATTR);
		 }
	}
	public static <U extends IsSecurityPrincipal> void  removeFromThreadLocalStorage() {
		 if (log.isTraceEnabled()) log.info("Removing [login session] from ThreadLocal.");
		 LoginAuthContextThreadLocalManager.remove();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Thread Manager with  attached to Local Thread
	 */
	public static class LoginAuthContextThreadLocalManager {
		// Local Thread
		private static ThreadLocal<LoginSession<?>> LOGIN_SESSION_STORAGE = new ThreadLocal<>();

		// Remove
		public static void remove() {
			LOGIN_SESSION_STORAGE.remove();
		}
		//set
		public static <P extends IsSecurityPrincipal> void set(final LoginSession<P> ses) {
			LOGIN_SESSION_STORAGE.set(ses);
		}
		@SuppressWarnings("unchecked")
		public static <P extends IsSecurityPrincipal> LoginSession<P> get() {
			return (LoginSession<P>)LOGIN_SESSION_STORAGE.get();
		}
	}
}
