package r01f.core.security.providers.bootstrap.guice;

import com.google.inject.Module;

import r01f.services.ids.ServiceIDs.CoreAppCode;


public class SecurityProvidersSecretConfigGuiceModule
    extends SecurityProvidersSecretConfigGuiceModuleBase
		implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    protected SecurityProvidersSecretConfigGuiceModule(final CoreAppCode coreAppCode) {
		super(coreAppCode);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CREATE
/////////////////////////////////////////////////////////////////////////////////////////
    public static SecurityProvidersSecretConfigGuiceModule createFor(final  CoreAppCode coreAppCode ) {
    	return new  SecurityProvidersSecretConfigGuiceModule(coreAppCode);
    }
}
