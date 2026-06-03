package r01f.security.base;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.servlet.http.Cookie;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import r01f.types.url.Host;
import r01f.types.url.Url;
import r01f.util.types.Strings;

/**
 * Base type for [security]-related [cookies]
 */
@Slf4j
public class SecurityCookie {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final String _name;
	@Getter protected final Host _domain;
	@Getter protected final String _content;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
//	protected SecurityCookie(final String name,
//							 final String domain,
//					   	   	 final String content) {
//		this(Host.from(domain),
//			 name,content);
//	}
	protected SecurityCookie(final String name,
							 final Host domain,
					   	   	 final String content) {
		_domain = domain;
		_name = name;
		_content = content;
	}
	protected SecurityCookie(final String name,
							 final Url url,final Pattern hostNameMatchingPattern,
					   	   	 final String content) {
		// [1] - Guess the cookie domain (should be cms.euskadi.xxx)
		Host cookieDomain = null;

		Host host = url.getHost();
		if (host.is(Host.LOCALHOST)) {
			cookieDomain = Host.LOCALHOST;
		}
		
		else if (hostNameMatchingPattern != null) {
			// use a pattern to EXTRACT the host-name & domain from the URL
			// Pattern p = Pattern.compile("([^.]+)\\.(cms\\..+)");	// ie: (XXXX).(cms.euskadi.eus)
			Matcher m = hostNameMatchingPattern.matcher(host.asString());
			if (m.find()) {
				if (m.groupCount() == 1) {
					cookieDomain = Host.from(m.group(1));
				} else if (m.groupCount() == 2) {
					cookieDomain = Host.from(m.group(2));
				} else {
					throw new IllegalArgumentException("The host name matching pattern " + hostNameMatchingPattern + " MUST have 1 or 2 capture groups");
				}
				log.debug("Creating legitimate security cookie for {} > domain: {} using {}",
						  host.asString(),
						  cookieDomain,
						  hostNameMatchingPattern);
			} else {
				throw new IllegalStateException(host.asString() + " is not a supported host");
			}
		}
		else {
			cookieDomain = host;
		}
		_domain = cookieDomain;

		// [2] - Set the [cookie] name & value
		_name = name;
		_content = content;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public Cookie createPersistentCookie() {
		Cookie outCookie = _createBaseCookie();
		return outCookie;
	}
	public Cookie createNonPersistentCookie() {
		Cookie outCookie = _createBaseCookie();
		outCookie.setMaxAge(-1);	// maxAge > 0 ===> the cookie will expire after that many seconds have passed
									// maxAge < 0 ===> the cookie is not stored persistently and will be deleted when the Web browser exits.
									// maxAge = 0 ===> the cookie is deleted.
		return outCookie;
	}
	private Cookie _createBaseCookie() {
		if (_domain == null
		 || Strings.isNullOrEmpty(_name)
		 || Strings.isNullOrEmpty(_content)) return null;

		Cookie outCookie = new Cookie(_name,
									  _content);
		if (_domain != null) outCookie.setDomain(_domain.asString());
		outCookie.setPath("/");
		outCookie.setHttpOnly(true);
		outCookie.setSecure(true);
		return outCookie;
	}
}
