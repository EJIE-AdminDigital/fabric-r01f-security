package r01f.security.context;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.context.SecurityContext;
import r01f.security.login.LoginSessionStore.LoginAuthContextThreadLocalManager;
import r01f.servlet.HttpServletSessionUtils;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SecurityContextStore {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANT
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String SECURITY_CONTEXT_SESSIONATTR = "r01SecurityContext";
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY CONTEXT GETTERS
/////////////////////////////////////////////////////////////////////////////////////////
	public static <S extends SecurityContext> S getFromSession(final HttpServletRequest request) {
		if (log.isTraceEnabled()) log.trace("Retrieve [security context] from http session ");

		HttpSession ses = request.getSession(false);	// do not create a new session
		return SecurityContextStore.getFromSession(ses);
	}
	@SuppressWarnings("unchecked")
	public static <S extends SecurityContext> S getFromSession(final HttpSession ses) {
		SecurityContext outSecurityContext = null;
		if (ses != null) {
			outSecurityContext = (S)ses.getAttribute(SECURITY_CONTEXT_SESSIONATTR);
			if (log.isTraceEnabled()) log.info("[security context] stored at http session with id={}!",
					 						   ses.getId());
		} else {
			if (log.isTraceEnabled()) log.info("NO [security context] stored at http session!");
		}
		if (log.isTraceEnabled() && outSecurityContext == null && ses != null) {
			log.info("The [security context] was NOT found at the [web session]\n{}",
					 HttpServletSessionUtils.composeSessionDebugInfo(ses));
		}
		return (S)outSecurityContext;
	}
	public static <S extends SecurityContext> S getFromThreadLocalStorage() {
		if (log.isTraceEnabled()) log.info("Retrieve [security context] from ThreadLocal storage ");
		return SecurityContextStoreAtThreadLocalStorage.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY CONTEXT SETTERS
/////////////////////////////////////////////////////////////////////////////////////////	
	public static <S extends SecurityContext> void storeAtHttpSession(final HttpServletRequest request,
																	  final S securityContext) {
		 if (log.isTraceEnabled()) log.info("Storing [security context] on session for [principal]={}",
				  							securityContext.getSecurityPrincipalId());
		 HttpSession ses = request.getSession(true);
		 request.setAttribute(SECURITY_CONTEXT_SESSIONATTR,securityContext);
		 ses.setAttribute(SECURITY_CONTEXT_SESSIONATTR,securityContext);
	}
	public static <S extends SecurityContext> void  storeAtThreadLocalStorage(final S securityContext) {
		 if (log.isTraceEnabled()) log.info("Storing [security context] on ThreadLocal.. {}",
				 							securityContext.toString());
		 SecurityContextStoreAtThreadLocalStorage.set(securityContext);
	}
	public static void removeFromHttpSession(final HttpServletRequest request) {
		 if (log.isTraceEnabled()) log.info("Removing [security context] from session.");
		 request.removeAttribute(SECURITY_CONTEXT_SESSIONATTR);
		 HttpSession ses = request.getSession(false);
		 if (ses != null) {
			 ses.removeAttribute(SECURITY_CONTEXT_SESSIONATTR);
		 }
	}
	public static void  removeFromThreadLocalStorage() {
		 if (log.isTraceEnabled()) log.info("Removing [security context] from ThreadLocal.");
		 LoginAuthContextThreadLocalManager.remove();
	}
}
