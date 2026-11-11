package r01f.security.authn.provider.cookie;

import java.util.regex.Pattern;

import lombok.experimental.Accessors;
import r01f.model.security.IdentityProviderAndAuthnMethod;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.base.SecurityCookie;
import r01f.types.url.Host;
import r01f.types.url.Url;

/**
 * A [cookie] that stores the [provider id]
 */
@Accessors(prefix="_")
public class AuthenticationProviderCookie
	 extends SecurityCookie {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticationProviderCookie(final String cookieName,
										final Host domain,
										final IdentityProvider identityProvider,final AuthenticationMethod authMethod) {
		super(cookieName,
			  domain,
			  AuthenticationProviderCookie.encodeCookieValue(identityProvider,authMethod));	// cookie value is "providerId/authMethodId"
	}
	public AuthenticationProviderCookie(final String cookieName,
										final Url url,final Pattern hostNameMatchingPattern,
										final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		super(cookieName,
			  url,hostNameMatchingPattern,
			  new IdentityProviderAndAuthnMethod(identityProvider,authnMethod).asString());	// cookie value is "providerId/authMethodId"
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static String encodeCookieValue(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		return IdentityProviderAndAuthnMethod.asString(identityProvider,authnMethod);
	}
	public static IdentityProviderAndAuthnMethod decodeCookieValue(final String cookieValue) {
		return IdentityProviderAndAuthnMethod.fromString(cookieValue);
	}
}