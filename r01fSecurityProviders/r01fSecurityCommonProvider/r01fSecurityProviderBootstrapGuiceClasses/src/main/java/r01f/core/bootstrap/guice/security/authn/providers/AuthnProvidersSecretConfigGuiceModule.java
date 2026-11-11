package r01f.core.bootstrap.guice.security.authn.providers;

import com.google.inject.Module;

import r01f.services.ids.ServiceIDs.CoreAppCode;


public class AuthnProvidersSecretConfigGuiceModule
    extends AuthnProvidersSecretConfigGuiceModuleBase
		implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    protected AuthnProvidersSecretConfigGuiceModule(final CoreAppCode coreAppCode) {
		super(coreAppCode);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CREATE
/////////////////////////////////////////////////////////////////////////////////////////
    public static AuthnProvidersSecretConfigGuiceModule createFor(final  CoreAppCode coreAppCode ) {
    	return new  AuthnProvidersSecretConfigGuiceModule(coreAppCode);
    }
}
