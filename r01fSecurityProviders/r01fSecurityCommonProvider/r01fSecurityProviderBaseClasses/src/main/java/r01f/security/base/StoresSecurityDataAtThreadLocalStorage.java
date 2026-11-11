package r01f.security.base;

import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.security.context.SecurityContextStore;
import r01f.security.login.LoginSession;
import r01f.security.login.LoginSessionStore;

/**
 * Interface that can be used by [web filters] to store [security data] at the {@link ThreadLocal} storage
 * useful for example when using Spring MVC
 */
public interface StoresSecurityDataAtThreadLocalStorage<P extends IsSecurityPrincipal> {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Puts the {@link SecurityContext} at the {@link ThreadLocal} storage
	 * It can be overriden to use any custom method to put the {@link SecurityContext at the {@link ThreadLocal} storage
	 * for example when using Spring MVC
	 * @param securityContext
	 */
	public default void storeSecurityContextAtThreadLocalStorage(final SecurityContext securityContext) {
		SecurityContextStore.storeAtThreadLocalStorage(securityContext);
	}
	/**
	 * Puts the {@link LoginSession} at the {@link ThreadLocal} storage
	 * It can be overriden to use any custom method to put the {@link SecurityContext at the {@link ThreadLocal} storage
	 * for example when using Spring MVC
	 * @param securityContext
	 */
	public default void storeLoginSessionAtThreadLocalStorage(final LoginSession<P> loginSession) {
		LoginSessionStore.storeAtThreadLocalStorage(loginSession);
	}
}
