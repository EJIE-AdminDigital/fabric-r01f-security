package r01f.security.handler.pairedphone;

import r01f.security.authn.handler.LoginEntryPointHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.url.Url;

public class PairedPhoneLoginEntryPointHandler
	 extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginEntryPointHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl,
									 final String impl) {
		return new Url(_securityProviderSecretConfig.getLoginUrl());
	}
}