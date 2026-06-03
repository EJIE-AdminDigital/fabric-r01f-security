package r01f.security.accessgrantedtoken;

import lombok.experimental.Accessors;
import r01f.jwt.JWT;
import r01f.security.base.SecurityCookie;
import r01f.types.url.Host;

/**
 * A [cookie] that stores the [access granted token]
 */
@Accessors(prefix="_")
public class AccessGrantedTokenCookie
	 extends SecurityCookie {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public AccessGrantedTokenCookie(final String cookieName,
									final Host domain,
					   			  	final JWT token) {
		super(cookieName,
			  domain,
			  token.asString());
	}
}