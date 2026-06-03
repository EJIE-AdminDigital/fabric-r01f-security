package r01f.security.accessgrantedtoken;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import r01f.jwt.JWT;
import r01f.security.base.SecurityCookieStoreBase;
import r01f.types.url.Host;
import r01f.util.types.Strings;

public class AccessGrantedTokenCookieStore
	 extends SecurityCookieStoreBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String ACCESS_GRANTED_COOKIE_DEFAULT_NAME = "r01AccessGranted";
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	private AccessGrantedTokenCookieStore(final String cookieName) {
		super(cookieName);
	}
	public static AccessGrantedTokenCookieStore createDefault() {
		return new AccessGrantedTokenCookieStore(ACCESS_GRANTED_COOKIE_DEFAULT_NAME);
	}
	public static AccessGrantedTokenCookieStore createWithCookieName(final String cookieName) {
		return new AccessGrantedTokenCookieStore(cookieName);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Checks whether a [cookie] that stores the [access granted token] exists
	 * @param request
	 * @return
	 */
	public boolean existsAccessGrantedTokenAt(final HttpServletRequest request) {
	   Cookie theCookie = this.getAccessGrantedTokenCookieFrom(request);
	   return theCookie != null;
	}
	/**
	 * Returns the [access granted token] from the [cookie]
	 * @param request
	 * @return
	 */
	public JWT getAccessGrantedTokenFrom(final HttpServletRequest request) {
		Cookie theCookie = this.getAccessGrantedTokenCookieFrom(request);
		if (theCookie == null || Strings.isNullOrEmpty(theCookie.getValue())) return null;
		return JWT.from(theCookie.getValue());
	}
	/**
	 * Returns the [cookie] that contains the [access granted token]
	 * @param request
	 * @return
	 */
	public Cookie getAccessGrantedTokenCookieFrom(final HttpServletRequest request) {
	   return this.getSecurityCookieFrom(request);
	}
	/**
	 * Sets a [access granted token] [cookie]
	 * @param domain
	 * @param providerId
	 * @param response
	 * @return
	 */
	public AccessGrantedTokenCookie storeAccessGrantedToken(final Host domain,final JWT token,
									  					  	final HttpServletResponse response) {
		AccessGrantedTokenCookie agCookie = new AccessGrantedTokenCookie(_cookieName,
																		 domain,
																	 	 token);
		response.addCookie(agCookie.createNonPersistentCookie());
		return agCookie;
	}
	/**
	 * Removes the [access granted token] [cookie]
	 * @param request
	 * @param response
	 */
	public void removeAccessGrantedToken(final HttpServletRequest request,final HttpServletResponse response) {
		this.removeSecurityCookie(request,response);
	}
}
