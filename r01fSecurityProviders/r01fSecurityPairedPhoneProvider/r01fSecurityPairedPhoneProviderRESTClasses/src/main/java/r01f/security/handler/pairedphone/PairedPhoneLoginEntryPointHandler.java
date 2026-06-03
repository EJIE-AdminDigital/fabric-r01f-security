package r01f.security.handler.pairedphone;

import r01f.security.handler.LoginEntryPointHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.types.url.Url;

public class PairedPhoneLoginEntryPointHandler
	 extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginEntryPointHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
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