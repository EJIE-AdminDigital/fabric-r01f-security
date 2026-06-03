package r01f.bootstrap.guice.core.security.otp;

import java.util.List;

import com.google.inject.Binder;
import com.google.inject.Module;

import lombok.EqualsAndHashCode;
import r01f.core.services.notifier.bootstrap.guice.NotifierGuiceModule;
import r01f.core.services.notifier.config.NotifiersConfigs;
import r01f.inject.HasMoreBindings;
import r01f.internal.R01FAppCodes;
import r01f.services.bootstrap.core.guice.CoreServicesBootstrapGuiceModuleBindsEventListeners;
import r01f.services.bootstrap.core.guice.ServicesBootstrapCoreGuiceModuleBase;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;
import r01f.services.ids.ServiceIDs.CoreSubModule;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;


@EqualsAndHashCode(callSuper=true)
public class OTPCoreServicesBootstrapGuiceModule
	 extends ServicesBootstrapCoreGuiceModuleBase 
 implements CoreServicesBootstrapGuiceModuleBindsEventListeners,
	        HasMoreBindings {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPCoreServicesBootstrapGuiceModule(final ServicesCoreModuleConfig coreBootstrapCfg) {
		super(CoreAppCode.of(R01FAppCodes.APP_CODE),CoreModule.OTP,
			  true, //Isolated
			  // ..this will check is anny additional module is needed
			  _getAdditionalGuiceModulesOrNullFor(coreBootstrapCfg)
			 );
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configureMoreBindings(final Binder binder) {
		// No more bindings
	}
	@Override
	public void bindEventListeners(final Binder binder) {
		// // No more bindings
	}
/////////////////////////////////////////////////////////////////////////////////////////
	protected static List<Module> _getAdditionalGuiceModulesOrNullFor(final ServicesCoreModuleConfig coreBootstrapCfg) {		
		List<Module> modules = Lists.newArrayList();
		// ..notifier module. Check if was specified at: OTPBeanCoreBootstrapConfigBuilder
		NotifiersConfigs notifierConfigs = null;
		try {
			notifierConfigs = coreBootstrapCfg.getSubModuleConfigFor(CoreSubModule.NOTIFIER,NotifiersConfigs.class);
		} catch (final Throwable th) {
			// This point throws an exception is that config does not exist [ that depends on see @ OTPBeanCoreBootstrapConfigBuilder
		}
		if (notifierConfigs != null) {
			modules.add(NotifierGuiceModule.createUsing(notifierConfigs));
		}
		// ..add more modules if needed.
		// ...
		if (CollectionUtils.isNullOrEmpty(modules))
			return null;		
		//.. return as array
		return modules;
	}
}
