package r01f.bootstrap.core.security.webauthn;

import com.google.inject.Binder;

import lombok.EqualsAndHashCode;
import r01f.services.bootstrap.core.db.guice.ServicesBootstrapCoreDBPersistenceGuiceModuleBase;
import r01f.services.bootstrap.core.guice.CoreServicesBootstrapGuiceModuleBindsEventListeners;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;

@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
public class CoreServicesBootstrapGuiceModuleForWeabuthnSecurityBase
	 extends ServicesBootstrapCoreDBPersistenceGuiceModuleBase
  implements CoreServicesBootstrapGuiceModuleBindsEventListeners {
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/*public CoreServicesBootstrapGuiceModuleForWeabuthnSecurityBase(final ServicesCoreModuleConfig coreBootstrapCfg) {
		super(coreBootstrapCfg,
			  // db module
			  new DBGuiceModuleForWebAuthnSecurity(DBModuleConfigBuilderForEclipseLink.dbModuleConfigFrom(coreBootstrapCfg)));
			  // other modules: notifier)));
	}*/
	public CoreServicesBootstrapGuiceModuleForWeabuthnSecurityBase(final CoreAppCode coreAppCode,final CoreModule coreModule,
														           final DBGuiceModuleForWebAuthnSecurity dbGuiceModuleForSecurity ) {
		super(coreAppCode,coreModule,
			  // db module
			  dbGuiceModuleForSecurity
			 // other modules: notifier
			);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		super.configure(binder);

		this.bindEventListeners(binder);

	}
	@Override
	public void bindEventListeners(final Binder binder) {
		//
	}
}
