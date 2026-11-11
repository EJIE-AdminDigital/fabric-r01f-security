package r01f.security.handler.giltza;

import r01f.security.authn.handler.LoginEntryPointHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.url.Url;

public class GiltzaLoginEntryPointHandler
     extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	final GiltzaApi _giltzaOAuth;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public GiltzaLoginEntryPointHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
		_giltzaOAuth = new GiltzaApi(_securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl,
									 final String impl) {		
		return _giltzaOAuth.getAuthorizationCodeUri(afterLoginRedirToUrl);
	}
}
