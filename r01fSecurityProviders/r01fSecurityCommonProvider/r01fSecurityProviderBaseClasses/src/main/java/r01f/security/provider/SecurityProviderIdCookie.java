package r01f.security.provider;

import java.util.regex.Pattern;

import lombok.experimental.Accessors;
import r01f.security.base.SecurityCookie;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.url.Host;
import r01f.types.url.Url;

/**
 * A [cookie] that stores the [provider id]
 */
@Accessors(prefix="_")
public class SecurityProviderIdCookie
	 extends SecurityCookie {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityProviderIdCookie(final String cookieName,
									final Host domain,
					   			  	final SecurityProviderID providerId) {
		super(cookieName,
			  domain,
			  providerId.asString());
	}
	public SecurityProviderIdCookie(final String cookieName,
									final Url url,final Pattern hostNameMatchingPattern,
					   			  	final SecurityProviderID providerId) {
		super(cookieName,
			  url,hostNameMatchingPattern,
			  providerId.asString());
	}
}