package r01f.security.handler.google;

import r01f.security.handler.LoginEntryPointHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
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
	public GoogleLoginEntryPointHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
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
