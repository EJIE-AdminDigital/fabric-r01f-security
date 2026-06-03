package r01f.bootstrap.core.security;

import com.google.inject.Binder;
import com.google.inject.multibindings.OptionalBinder;

import lombok.EqualsAndHashCode;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForPasswordRecoverMessageLog;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForPasswordRecoverMessageNotifyByEMail;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForPasswordRecoverMessageNotifyBySMS;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForUserRegistrationMessageLog;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForUserRegistrationMessageNotifyByEMail;
import r01f.events.security.EventListenersForSecurityEvents.EventListenerForUserRegistrationMessageNotifyBySMS;
import r01f.services.bootstrap.core.db.guice.ServicesBootstrapCoreDBPersistenceGuiceModuleBase;
import r01f.services.bootstrap.core.guice.CoreServicesBootstrapGuiceModuleBindsEventListeners;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;

@EqualsAndHashCode(callSuper=true)				// This is important for guice modules
public class CoreServicesBootstrapGuiceModuleForSecurityBase
	 extends ServicesBootstrapCoreDBPersistenceGuiceModuleBase
  implements CoreServicesBootstrapGuiceModuleBindsEventListeners {
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CoreServicesBootstrapGuiceModuleForSecurityBase(final CoreAppCode coreAppCode, final CoreModule coreModule,
			                                               final DBGuiceModuleForSecurity dbGuiceModuleForSecurity) {
		super(coreAppCode, coreModule,
			  dbGuiceModuleForSecurity);
	
	}
	
	public CoreServicesBootstrapGuiceModuleForSecurityBase(final CoreAppCode coreAppCode, final CoreModule coreModule,
            												final DBGuiceModuleForSecurity dbGuiceModuleForSecurity,
            												final NotifierGuiceModuleForSecurity notifierGuiceModuleForSecurity) {
		super(coreAppCode, coreModule,
		   	 dbGuiceModuleForSecurity,notifierGuiceModuleForSecurity);
	
	}

	
/////////////////////////////////////////////////////////////////////////////////////////
// 	(OLD) CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/*public CoreServicesBootstrapGuiceModuleForSecurityBase(final ServicesCoreModuleBootstrapConfig coreBootstrapCfg) {
		super(coreBootstrapCfg,
			  // db module
			  new DBGuiceModuleForSecurity(DBModuleConfigBuilderForEclipseLink.dbModuleConfigFrom(coreBootstrapCfg)),
			  // other modules: notifier
			  Lists.newArrayList(new NotifierGuiceModuleForSecurity(coreBootstrapCfg.getSubModuleConfigFor(CoreSubModule.NOTIFIER,NotifiersConfigs.class))));
	}
	public CoreServicesBootstrapGuiceModuleForSecurityBase(final ServicesCoreModuleBootstrapConfig coreBootstrapCfg,
														   final DBGuiceModuleForSecurity dbGuiceModuleForSecurity,
														   final NotifierGuiceModuleForSecurity notifierGuiceModuleForSecurity ) {
		super(coreBootstrapCfg,
			  // db module
			  dbGuiceModuleForSecurity,
			 // other modules: notifier
			Lists.newArrayList(notifierGuiceModuleForSecurity));
	}
	public CoreServicesBootstrapGuiceModuleForSecurityBase(final ServicesCoreModuleBootstrapConfig coreBootstrapCfg,
														   final DBGuiceModuleForSecurity dbGuiceModuleForSecurity,
														   final NotifierGuiceModule notifiersGuiceModule,
														   final NotifierGuiceModuleForSecurity notifierGuiceModuleForSecurity ) {
		super(coreBootstrapCfg,
			  // db module
			  dbGuiceModuleForSecurity,
			  // other modules: notifier
			  Lists.newArrayList(notifiersGuiceModule,notifierGuiceModuleForSecurity));
	}*/
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		super.configure(binder);

		this.bindEventListeners(binder);
		this.bindOptionalBinderForOTPService(binder);
	}
	@Override
	public void bindEventListeners(final Binder binder) {
		// ... sms
		binder.bind(EventListenerForPasswordRecoverMessageNotifyBySMS.class)
			  .asEagerSingleton();

		binder.bind(EventListenerForUserRegistrationMessageNotifyBySMS.class)
			  .asEagerSingleton();
		// ... mail
		binder.bind(EventListenerForPasswordRecoverMessageNotifyByEMail.class)
			  .asEagerSingleton();

		binder.bind(EventListenerForUserRegistrationMessageNotifyByEMail.class)
			  .asEagerSingleton();
		// ... log
		binder.bind(EventListenerForPasswordRecoverMessageLog.class)
			  .asEagerSingleton();
		binder.bind(EventListenerForUserRegistrationMessageLog.class)
			  .asEagerSingleton();
	}
	
    //  OTP Services :The OTP services should only be necessary in the application where the Login services are invoked, fundamentally ( eg Z99SecurityLoginRESTWAR ).
	//  	This excludes to bootstrap otp services at UIWar applications, which however use other services from the same CORE.
	//  	----> To lighten the bootstraping in this cases an OptionalBinding will be used.	
	public void bindOptionalBinderForOTPService(final Binder binder) {
		OptionalBinder.newOptionalBinder(binder, OTPService.class);
	}	
}
