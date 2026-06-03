package r01f.bootstrap.core.security.otp.persistence;

import jakarta.inject.Provider;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.api.interfaces.security.otp.persistence.OTPPersistenceServiceInterface;
import r01f.securitycontext.SecurityContext;
import r01f.services.bootstrap.config.ServicesBootstrapConfigBuilder;
import r01f.services.bootstrap.config.ServicesCoreModuleBootstrapConfig;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;
import r01f.services.security.otp.persistence.CoreOTPDBPersistenceService;
import r01f.xmlproperties.XMLProperties;

/**
 * Builds the services bootstrap config
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPDBPersistenceBeanCoreBootstrapConfigBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public static ServicesCoreModuleBootstrapConfig coreDBModuleBootstrapConfigBuilderFor(final CoreAppCode coreAppCode,final CoreModule coreModule,
							                                                             final XMLProperties xmlProps,
																					     final Provider<SecurityContext> securityContextProvider) {
		// [1] - Build the services bootstrap config
		// a) core modules bootstrap config
		ServicesCoreModuleBootstrapConfig persistModCfg = null;
		persistModCfg =	ServicesBootstrapConfigBuilder.coreModuleBootstrapConfigBuilderFor(coreAppCode,coreModule)
													   // Servie interfaces extending DN00IsServiceInterfaceForInteropConfig	
													   .exposingServiceInterfacesExtending(OTPPersistenceServiceInterface.class)
													   .withServicesCoreImplOrProxyToCoreModuleConfigs(// core services impl extending DN01COREPersistenceServiceForPersistableInteropConfigObject
													    		 									  ServicesBootstrapConfigBuilder.createServiceCoreModuleInProcess()
															  																		.withCoreServiceOrProxyToCoreServiceExtending(CoreOTPDBPersistenceService.class)
															  																		.build())
													   .build();
	   return persistModCfg;
	}
}
