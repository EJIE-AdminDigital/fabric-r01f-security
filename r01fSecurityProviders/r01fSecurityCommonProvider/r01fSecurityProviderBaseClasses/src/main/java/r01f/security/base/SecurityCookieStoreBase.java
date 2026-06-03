package r01f.security.base;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import r01f.servlet.HttpServletRequestUtils;

@Slf4j
public abstract class SecurityCookieStoreBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final String _cookieName;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected SecurityCookieStoreBase(final String cookieName) {
		_cookieName = cookieName;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns the [cookie]
	 * @param request
	 * @return
	 */
	protected Cookie getSecurityCookieFrom(final HttpServletRequest request) {
	   Cookie theCookie = HttpServletRequestUtils.cookieWithName(request,_cookieName);

	   log.debug("> cookie named {} {}",
				 _cookieName,theCookie != null ? "EXISTS" : "DOES NOT EXISTS");
	   if (theCookie == null && log.isTraceEnabled()) { // debug cookies
		  log.trace("Cookie info:\n" +
				  	"{}",
				  	HttpServletRequestUtils.cookiesDebugInfo(request));
	   }
	   return theCookie;
	}
	/**
	 * Removes the [cookie]
	 * @param request
	 * @param response
	 */
	public void removeSecurityCookie(final HttpServletRequest request,final HttpServletResponse response) {
		HttpServletRequestUtils.removeCookie(request,response,
											 _cookieName,null);	// no path
	}
}
