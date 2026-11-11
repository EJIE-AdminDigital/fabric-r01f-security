package r01f.security.authn.provider.cookie;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import r01f.model.security.IdentityProviderAndAuthnMethod;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.base.SecurityCookieStoreBase;
import r01f.types.url.Host;
import r01f.util.types.Strings;

public class AuthenticationProviderCookieStore
	 extends SecurityCookieStoreBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String AUTH_PROVIDER_COOKIE_DEFAULT_NAME = "r01AuthProvider";
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	private AuthenticationProviderCookieStore(final String cookieName) {
		super(cookieName);
	}
	public static AuthenticationProviderCookieStore createDefault() {
		return new AuthenticationProviderCookieStore(AUTH_PROVIDER_COOKIE_DEFAULT_NAME);
	}
	public static AuthenticationProviderCookieStore createWithCookieName(final String cookieName) {
		return new AuthenticationProviderCookieStore(cookieName);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks whether a [cookie] that stores the [identity provider] + [auth method] exists
	 * @param request
	 * @return
	 */
	public boolean existsAuthnProviderAt(final HttpServletRequest request) {
	   Cookie theCookie = this.getAuthnProviderCookieFrom(request);
	   return theCookie != null;
	}
	/**
	 * Returns the [cookie] that contains the [securityProviderId]
	 * @param request
	 * @return
	 */
	public Cookie getAuthnProviderCookieFrom(final HttpServletRequest request) {
	   return this.getSecurityCookieFrom(request);
	}
	/**
	 * Sets a [securityProviderId] [cookie]
	 * @param domain
	 * @param identityProvider
	 * @param response
	 * @return
	 */
	public AuthenticationProviderCookie storeAuthnProvider(final Host domain,
														   final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
									  					   final HttpServletResponse response) {
		AuthenticationProviderCookie spCookie = new AuthenticationProviderCookie(_cookieName,
																		 			 domain,
																		 			 identityProvider,authnMethod);
		response.addCookie(spCookie.createNonPersistentCookie());
		return spCookie;
	}
	/**
	 * Removes the [securityProviderId] [cookie]
	 * @param request
	 * @param response
	 */
	public void removeAuthnProvider(final HttpServletRequest request,final HttpServletResponse response) {
		this.removeSecurityCookie(request,response);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public IdentityProviderAndAuthnMethod getIdentityProviderAndAuthnMethodFrom(final HttpServletRequest request) {
		Cookie theCookie = this.getAuthnProviderCookieFrom(request);
		if (theCookie == null || Strings.isNullOrEmpty(theCookie.getValue())) return null;
		return IdentityProviderAndAuthnMethod.fromString(theCookie.getValue());
	}
	/**
	 * Returns the [identity provider] from the [cookie]
	 * @param request
	 * @return
	 */
	public IdentityProvider getIdentityProviderFrom(final HttpServletRequest request) {
		Cookie theCookie = this.getAuthnProviderCookieFrom(request);
		if (theCookie == null || Strings.isNullOrEmpty(theCookie.getValue())) return null;
		IdentityProviderAndAuthnMethod providerAndAuthMethod = IdentityProviderAndAuthnMethod.fromString(theCookie.getValue());
		if (providerAndAuthMethod == null) return null;
		
		return providerAndAuthMethod.identityProvider();
	}
	/**
	 * Returns the [identity provider] from the [cookie]
	 * @param request
	 * @return
	 */
	public AuthenticationMethod getAuthenticationMethodFrom(final HttpServletRequest request) {
		Cookie theCookie = this.getAuthnProviderCookieFrom(request);
		if (theCookie == null || Strings.isNullOrEmpty(theCookie.getValue())) return null;
		IdentityProviderAndAuthnMethod providerAndAuthMethod = IdentityProviderAndAuthnMethod.fromString(theCookie.getValue());
		if (providerAndAuthMethod == null) return null;
		
		return providerAndAuthMethod.authnMethod();
	}
}
