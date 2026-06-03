package r01f.bootstrap.core.security.otp.persistence;

import com.google.inject.Binder;

import lombok.EqualsAndHashCode;
import r01f.inject.HasMoreBindings;
import r01f.services.bootstrap.core.db.guice.ServicesBootstrapCoreDBPersistenceGuiceModuleBase;
import r01f.services.bootstrap.core.guice.CoreServicesBootstrapGuiceModuleBindsEventListeners;

import r01f.services.core.persistence.db.config.DBModuleConfig;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;


@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
public class OTPDBPersistenceServicesBootstrapGuiceModule
     extends ServicesBootstrapCoreDBPersistenceGuiceModuleBase
  implements CoreServicesBootstrapGuiceModuleBindsEventListeners,
  			 HasMoreBindings {
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    public OTPDBPersistenceServicesBootstrapGuiceModule(final  CoreAppCode appCode,final CoreModule coreModule,
    		                                            final  DBModuleConfig dbModuleCfg) {
		super(appCode,coreModule,
			  new OTPDBGuiceModule(dbModuleCfg));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//MORE BINDINGS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configureMoreBindings(final Binder binder) {
		//noop
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  EVENTS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void bindEventListeners(final Binder binder) {
		// noop
	}
}
