package r01f.security.provider;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import r01f.security.base.SecurityCookieStoreBase;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.url.Host;
import r01f.util.types.Strings;

public class SecurityProviderIdCookieStore
	 extends SecurityCookieStoreBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String SECURITY_PROVIDER_COOKIE_DEFAULT_NAME = "r01SecurityProvider";
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	private SecurityProviderIdCookieStore(final String cookieName) {
		super(cookieName);
	}
	public static SecurityProviderIdCookieStore createDefault() {
		return new SecurityProviderIdCookieStore(SECURITY_PROVIDER_COOKIE_DEFAULT_NAME);
	}
	public static SecurityProviderIdCookieStore createWithCookieName(final String cookieName) {
		return new SecurityProviderIdCookieStore(cookieName);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks whether a [cookie] that stores the [securityProviderId] exists
	 * @param request
	 * @return
	 */
	public boolean existsProviderIdAt(final HttpServletRequest request) {
	   Cookie theCookie = this.getProviderIdCookieFrom(request);
	   return theCookie != null;
	}
	/**
	 * Returns the [securityProviderId] from the [cookie]
	 * @param request
	 * @return
	 */
	public SecurityProviderID getProviderIdFrom(final HttpServletRequest request) {
		Cookie theCookie = this.getProviderIdCookieFrom(request);
		if (theCookie == null || Strings.isNullOrEmpty(theCookie.getValue())) return null;
		return SecurityProviderID.forId(theCookie.getValue());
	}
	/**
	 * Returns the [cookie] that contains the [securityProviderId]
	 * @param request
	 * @return
	 */
	public Cookie getProviderIdCookieFrom(final HttpServletRequest request) {
	   return this.getSecurityCookieFrom(request);
	}
	/**
	 * Sets a [securityProviderId] [cookie]
	 * @param domain
	 * @param providerId
	 * @param response
	 * @return
	 */
	public SecurityProviderIdCookie storeProviderId(final Host domain,final SecurityProviderID providerId,
									  				final HttpServletResponse response) {
		SecurityProviderIdCookie spCookie = new SecurityProviderIdCookie(_cookieName,
																		 domain,
																		 providerId);
		response.addCookie(spCookie.createNonPersistentCookie());
		return spCookie;
	}
	/**
	 * Removes the [securityProviderId] [cookie]
	 * @param request
	 * @param response
	 */
	public void removeProviderId(final HttpServletRequest request,final HttpServletResponse response) {
		this.removeSecurityCookie(request,response);
	}
}
