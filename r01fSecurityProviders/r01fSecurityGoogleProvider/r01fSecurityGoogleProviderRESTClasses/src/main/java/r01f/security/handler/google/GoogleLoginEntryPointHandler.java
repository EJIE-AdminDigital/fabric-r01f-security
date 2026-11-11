package r01f.security.handler.google;

import r01f.security.authn.handler.LoginEntryPointHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.url.Url;

public class GoogleLoginEntryPointHandler
     extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	final GoogleApi _googleOAuth;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public GoogleLoginEntryPointHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
		_googleOAuth = new GoogleApi(_securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl,
									 final String impl) {
		return _googleOAuth.getAuthorizationCodeUri(afterLoginRedirToUrl);
	}
}
