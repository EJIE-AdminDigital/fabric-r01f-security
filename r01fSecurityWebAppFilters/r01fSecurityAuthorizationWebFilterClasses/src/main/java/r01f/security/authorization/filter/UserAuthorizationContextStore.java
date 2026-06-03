package r01f.security.authorization.filter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.authorization.UserAuthorizationOnResource;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class UserAuthorizationContextStore {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANT
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String AUTH_CTX_SESSIONATTR = "r01AuthCtx";
/////////////////////////////////////////////////////////////////////////////////////////
//	USER AUTHS GETTERS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static <A extends UserAuthorizationOnResource> UserAuthorizationSession<A> getFromSession(final HttpServletRequest request) {
		log.info("Retrieve [user auths] from http session ");
		UserAuthorizationSession<A> outAuthCtx = null;
		HttpSession ses = request.getSession(false);	// do not create a new session
		if (ses != null) {
			outAuthCtx = (UserAuthorizationSession<A>)ses.getAttribute(AUTH_CTX_SESSIONATTR);
			log.debug("[auth context] stored at http session with id={}!",
					 ses.getId());
		} else {
			log.debug("NO [auth context] stored at http session!");
		}
		return outAuthCtx;
	}
	public static <A extends UserAuthorizationOnResource> UserAuthorizationSession<A> getFromThreadLocalStorage() {
		log.trace("Retrieve [user auths] from ThreadLocal storage ");
		return UserAuthsThreadLocalManager.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER AUTHS SETTERS
/////////////////////////////////////////////////////////////////////////////////////////	
	public static <A extends UserAuthorizationOnResource> void storeAtHttpSession(final HttpServletRequest request,
									  	  								   		  final UserAuthorizationSession<A> authCtx) {
		log.info("Storing [auth context] at http session... {}",
				 authCtx.toString());
		HttpSession ses = request.getSession(true);
		request.setAttribute(AUTH_CTX_SESSIONATTR,authCtx);
		ses.setAttribute(AUTH_CTX_SESSIONATTR, authCtx);
	}
	public static <A extends UserAuthorizationOnResource> void  storeAtThreadLocalStorage(final UserAuthorizationSession<A> userAuths) {
		log.trace("Storing [user auths] on ThreadLocal.. {}",
				  userAuths.toString());
		UserAuthsThreadLocalManager.set(userAuths);
	}
	public static <A extends UserAuthorizationOnResource> void removeFromHttpSession(final HttpServletRequest request) {
		log.trace("Removing [user auths] from session.");
		request.removeAttribute(AUTH_CTX_SESSIONATTR);
		HttpSession ses = request.getSession(false);
		if (ses != null) {
			ses.removeAttribute(AUTH_CTX_SESSIONATTR);
		}
	}
	public static <A extends UserAuthorizationOnResource> void  removeFromThreadLocalStorage() {
		log.trace("Removing [user auths] from ThreadLocal.");
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
		private static ThreadLocal<UserAuthorizationSession<?>> USER_AUTHS_STORAGE = new ThreadLocal<>();

		// Remove
		public static void remove() {
			USER_AUTHS_STORAGE.remove();
		}
		//set
		public static <A extends UserAuthorizationOnResource> void set(final UserAuthorizationSession<A> ses) {
			USER_AUTHS_STORAGE.set(ses);
		}
		@SuppressWarnings("unchecked")
		public static <A extends UserAuthorizationOnResource> UserAuthorizationSession<A> get() {
			return (UserAuthorizationSession<A>)USER_AUTHS_STORAGE.get();
		}
	}
}
