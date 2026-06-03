package r01f.core.security.rest.bootstrap;

import com.google.inject.Module;

import r01f.services.ids.ServiceIDs.CoreAppCode;


public class RESTSecurityProvidersSecretConfigModule
    extends RESTSecurityProvidersSecretConfigModuleBase
		implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
    protected RESTSecurityProvidersSecretConfigModule(final CoreAppCode coreAppCode) {
		super(coreAppCode);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CREATE
/////////////////////////////////////////////////////////////////////////////////////////
    public static RESTSecurityProvidersSecretConfigModule createFor(final  CoreAppCode coreAppCode ) {
    	return new  RESTSecurityProvidersSecretConfigModule(coreAppCode);
    }
}
