package r01f.bootstrap.guice.core.security.otp;


import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.patterns.IsBuilder;
import r01f.xmlproperties.XMLPropertiesForApp;

/**
 * OTPBeanCoreBootstrapConfigBuilder
 * This builder provides different ways to start the services for OTP. 
 * This is important depending on where NOTIFIER services are bound and bootstrapped in different ways and at different points, fe:
 * 
 * 	1. One Notifier into each core module layer (f.e, Pannic Buttom )      
 * 	2. One common notifier into the REST layer   (f.e  TCKSs) 
 * 
 * ...depending on this architectures use this builder, one way or another:
 *    
 *    [CASE 1 ] One Notifier into each core module layer ( f.e, Pannic Buttom ) 
 *    -------------------------------------------------------------------------
 *    	OTPBeanCoreBootstrapConfigBuilder.buildCoreBootstrapConfig(xmlprops) - the cannonical way -
 *    	
 *    	.... this implies (also) :
 *    			1. Ensure that the Notifier is in each module
 *    		    2. Ensure that there is NO common Notifier is in the entry point, f.e into the REST ( multiple bindings)
 *
 *    [CASE 2 ] One common Notifier into the REST layer (f.e, TCKSs) 
 *    -------------------------------------------------------------------------
 *    	  OTPBeanCoreBootstrapConfigBuilderXMLPropsStep.create() 
 *												   .from(xmlProps)
 *											       .withCommonNotifier()
 *										       	   .build();
 *	..... this implies (also) :
 *	
 *	.... this implies (also) :
 *    			1. Ensure that theres IS NO Notifier is in each module ( multiple bindings)
 *    		    2. Ensure that there is a common Notifier is in the entry point, f.e into the REST ( multiple bindings)
 *  
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPBeanCoreBootstrapConfigBuilder
           implements IsBuilder {	
/////////////////////////////////////////////////////////////////////////////////////////
// CANNONICAL WAY [ ..in order to simplify and maintain compatibility with clients	
/////////////////////////////////////////////////////////////////////////////////////////
	/*public static ServicesCoreModuleBootstrapConfig buildCoreBootstrapConfig(final XMLPropertiesForApp xmlProps) {
		return create() //..don't looks cannonical, but it's the cannonical way
				.from(xmlProps)
			    .withoutCommonNotifier()
		       .build();
	}*/
/////////////////////////////////////////////////////////////////////////////////////////
// BUILDER STEPPER WAY [ ..in order to provide somes added features for bootraping ..	
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPBeanCoreBootstrapConfigBuilderXMLPropsStep create() {
		OTPBootstrapBuilderWrapperData initData = new OTPBootstrapBuilderWrapperData();		
		return new OTPBeanCoreBootstrapConfigBuilder() { /* nothing */ }
						.new OTPBeanCoreBootstrapConfigBuilderXMLPropsStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class OTPBeanCoreBootstrapConfigBuilderXMLPropsStep {
		private final OTPBootstrapBuilderWrapperData _initData;

		public OTPBeanCoreBootstrapConfigBuilderNotifierStep from(final XMLPropertiesForApp xmlProps) {
			_initData.setXmlProps(xmlProps);
			return new OTPBeanCoreBootstrapConfigBuilderNotifierStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class OTPBeanCoreBootstrapConfigBuilderNotifierStep {
		private final OTPBootstrapBuilderWrapperData _initData;

		public OTPBeanCoreBootstrapConfigBuilderBuildStep usingCommonNotifier() {
			_initData.setUsesCommonNotifer(true);
			return new OTPBeanCoreBootstrapConfigBuilderBuildStep(_initData);
		}
		public OTPBeanCoreBootstrapConfigBuilderBuildStep withoutCommonNotifier() {
			_initData.setUsesCommonNotifer(false);
			return new OTPBeanCoreBootstrapConfigBuilderBuildStep(_initData);
		}	
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class OTPBeanCoreBootstrapConfigBuilderBuildStep {
		@SuppressWarnings("unused")
		private final OTPBootstrapBuilderWrapperData _initData;

		/*public ServicesCoreModuleBootstrapConfig build() {		
			return _build(_initData);
		}*/
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER BOOTSTRAP WRAPPER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * OTOBootstrapBuilderWrapperData
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor
	public static class OTPBootstrapBuilderWrapperData
	         implements Serializable {

		private static final long serialVersionUID = 8415672935909526803L;
	
		@Getter @Setter  private  XMLPropertiesForApp _xmlProps;  
		@Getter @Setter  private  boolean  _usesCommonNotifer;  

	}
/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHOD
/////////////////////////////////////////////////////////////////////////////////////////
	/*protected static  ServicesCoreModuleBootstrapConfig _build (final OTPBootstrapBuilderWrapperData initData) {
		// [ Case 1 ] The Notifier is provided out of this module, f.e. into the REST Layer Bootstraping
		if ( initData._usesCommonNotifer) {			
			return ServicesCoreModuleBootstrapConfigBuilder.forCoreAppAndModule(CoreAppCode.of(R01FAppCodes.APP_CODE),CoreModule.OTP)
														 .inProcessImplemented()
														 .bootstrappedBy(OTPCoreServicesBootstrapGuiceModule.class)
														 .findCoreServicesExtending(OTPSecurityCoreServiceImpl.class)					 
														.build();
		 };
	   // [ Case 2 ] NO notifier is provided out of this module. A notifier will be provided inside this module
		XMLPropertiesForAppComponent notifierXmlProps =	initData.getXmlProps().forComponent(AppComponent.compose(CoreModule.OTP,
				                                                                                   CoreSubModule.NOTIFIER));
		

		return ServicesCoreModuleBootstrapConfigBuilder.forCoreAppAndModule(CoreAppCode.of(R01FAppCodes.APP_CODE),CoreModule.OTP)
												 .inProcessImplemented()
												 .bootstrappedBy(OTPCoreServicesBootstrapGuiceModule.class)
												 .findCoreServicesExtending(OTPSecurityCoreServiceImpl.class)
												 .withSubModulesConfigs(
														// notifier config
							   							ServicesCoreSubModuleConfig.createForNotifierSubModule(NotifiersConfigBuilder.loadNotifiersConfigFrom(notifierXmlProps)
																							                                                         .forEMail(OTPNotifierConfigForEMail::new)
																							                                                         .forSMS(OTPNotifierConfigForSMS::new)
																							                                                         .noVoice()	
																							                                                         .forLog()
																							                                                         .noPush()
																								                                              .build())
														
														 )
												 .build();
	}*/
}
