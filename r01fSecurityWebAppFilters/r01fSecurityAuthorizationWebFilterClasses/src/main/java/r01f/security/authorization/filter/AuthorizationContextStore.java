package r01f.security.authorization.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class AuthorizationContextStore {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANT
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String AUTH_CTX_SESSIONATTR = "r01AuthCtx";
/////////////////////////////////////////////////////////////////////////////////////////
//	USER AUTHS GETTERS
/////////////////////////////////////////////////////////////////////////////////////////
	public static AuthorizationSession getFromSession(final HttpServletRequest request) {
		log.info("Retrieve [authzs] from http session ");
		AuthorizationSession outAuthCtx = null;
		HttpSession ses = request.getSession(false);	// do not create a new session
		if (ses != null) {
			outAuthCtx = (AuthorizationSession)ses.getAttribute(AUTH_CTX_SESSIONATTR);
			log.debug("[auth context] stored at http session with id={}!",
					 ses.getId());
		} else {
			log.debug("NO [auth context] stored at http session!");
		}
		return outAuthCtx;
	}
	public static AuthorizationSession getFromThreadLocalStorage() {
		log.trace("Retrieve [user auths] from ThreadLocal storage ");
		return UserAuthsThreadLocalManager.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER AUTHS SETTERS
/////////////////////////////////////////////////////////////////////////////////////////	
	public static void storeAtHttpSession(final HttpServletRequest request,
									  	  final AuthorizationSession authCtx) {
		log.info("Storing [auth context] at http session... {}",
				 authCtx.toString());
		HttpSession ses = request.getSession(true);
		request.setAttribute(AUTH_CTX_SESSIONATTR,authCtx);
		ses.setAttribute(AUTH_CTX_SESSIONATTR, authCtx);
	}
	public static void  storeAtThreadLocalStorage(final AuthorizationSession authzs) {
		log.trace("Storing [authzs] on ThreadLocal.. {}",
				  authzs.toString());
		UserAuthsThreadLocalManager.set(authzs);
	}
	public static void removeFromHttpSession(final HttpServletRequest request) {
		log.trace("Removing [authzs] from session.");
		request.removeAttribute(AUTH_CTX_SESSIONATTR);
		HttpSession ses = request.getSession(false);
		if (ses != null) {
			ses.removeAttribute(AUTH_CTX_SESSIONATTR);
		}
	}
	public static void  removeFromThreadLocalStorage() {
		log.trace("Removing [authzs] from ThreadLocal.");
		UserAuthsThreadLocalManager.remove();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Thread Manager with  attached to Local Thread
	 */
	public static class UserAuthsThreadLocalManager {
		// Local Thread
		private static ThreadLocal<AuthorizationSession> AUTHZS_STORAGE = new ThreadLocal<>();

		// Remove
		public static void remove() {
			AUTHZS_STORAGE.remove();
		}
		//set
		public static void set(final AuthorizationSession ses) {
			AUTHZS_STORAGE.set(ses);
		}
		public static AuthorizationSession get() {
			return AUTHZS_STORAGE.get();
		}
	}
}
