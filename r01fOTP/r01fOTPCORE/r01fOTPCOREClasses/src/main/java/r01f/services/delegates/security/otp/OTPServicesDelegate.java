package r01f.services.delegates.security.otp;


import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.task.dispatch.IsOTPDispatchRequest;
import r01f.model.security.otp.task.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.task.generation.OTPGenerationRequest;
import r01f.model.security.otp.task.generation.OTPGenerationResponse;
import r01f.model.security.otp.task.validation.IsOTPValidationRequest;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.sub.security.otp.OTPServicesSubDelegateForDispatch;
import r01f.services.delegates.sub.security.otp.OTPServicesSubDelegateForGeneration;
import r01f.services.delegates.sub.security.otp.OTPServicesSubDelegateForValidation;

public class OTPServicesDelegate
	 extends OTPServicesDelegateBase
  implements OTPService {	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final OTPServicesSubDelegateForGeneration     _servicesSubDelegateForGeneration;
	protected final OTPServicesSubDelegateForDispatch       _servicesSubDelegateForDispatch;
	protected final OTPServicesSubDelegateForValidation     _servicesSubDelegateForValidation;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPServicesDelegate(final ServicesCoreModuleConfig coreConfig,
			                   final Marshaller modelObjectsMarshaller, final EventBus eventBus,
			                   final CRUDServicesForOTPTask taskPersistenceService,
							   // Notifiers
							   final NotifierServiceForEMail mailNotifier,final NotifierServiceForSMS smsNotifier) {
		super(coreConfig,modelObjectsMarshaller,eventBus,taskPersistenceService);
		
		_servicesSubDelegateForGeneration = new OTPServicesSubDelegateForGeneration(coreConfig,modelObjectsMarshaller,eventBus,taskPersistenceService);
		_servicesSubDelegateForDispatch  =  new OTPServicesSubDelegateForDispatch(coreConfig,modelObjectsMarshaller,eventBus,taskPersistenceService,mailNotifier,smsNotifier);
		_servicesSubDelegateForValidation = new OTPServicesSubDelegateForValidation(coreConfig,modelObjectsMarshaller,eventBus,taskPersistenceService);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public OTPOperationExecResult<OTPGenerationResponse> generate(final SecurityContext securityContext,
			                                                      final OTPGenerationRequest otpRequest) {		
		return _servicesSubDelegateForGeneration.generate(securityContext,
														  otpRequest);
	}
	@Override
	public <R extends IsOTPDispatchRequest<?, ?, ?>> OTPOperationExecResult<OTPDispatchResponse> dispatch(final SecurityContext securityContext,
			                                                                                            final R otpRequest) {	
		return _servicesSubDelegateForDispatch.dispatch(securityContext,
				                                        otpRequest);
	}
	@Override
	public <R extends IsOTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate( final SecurityContext securityContext,
			                                                                                           final R validationReq) {
		return _servicesSubDelegateForValidation.validate(securityContext,
				                                          validationReq);
	}
}
