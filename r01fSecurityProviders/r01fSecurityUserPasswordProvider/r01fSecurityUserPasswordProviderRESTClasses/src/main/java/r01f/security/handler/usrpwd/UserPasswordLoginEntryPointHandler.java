package r01f.security.handler.usrpwd;

import r01f.security.handler.LoginEntryPointHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.types.url.Url;

public class UserPasswordLoginEntryPointHandler
     extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginEntryPointHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
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
