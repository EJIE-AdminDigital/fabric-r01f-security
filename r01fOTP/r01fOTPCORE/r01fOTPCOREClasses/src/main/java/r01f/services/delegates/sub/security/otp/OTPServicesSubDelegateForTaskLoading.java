package r01f.services.delegates.sub.security.otp;



import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;


public class OTPServicesSubDelegateForTaskLoading 
   extends OTPServicesSubDelegateBase {	
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPServicesSubDelegateForTaskLoading(final ServicesCoreModuleConfig coreConfig,
											    final Marshaller modelObjectsMarshaller, EventBus eventBus,
											    final CRUDServicesForOTPTask taskPersistenceService) {
		super(coreConfig, modelObjectsMarshaller, eventBus, taskPersistenceService);	
	}	
}


