package r01f.services.security.otp;



import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.client.api.otp.internal.OTPModuleCodes;
import r01f.core.services.notifier.NotifierServiceForEMail;
import r01f.core.services.notifier.NotifierServiceForSMS;
import r01f.core.services.notifier.annotations.UseEMailNotifier;
import r01f.core.services.notifier.annotations.UseMessagingNotifier;
import r01f.eventbus.EventBus;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.task.dispatch.IsOTPDispatchRequest;
import r01f.model.security.otp.task.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.task.generation.OTPGenerationRequest;
import r01f.model.security.otp.task.generation.OTPGenerationResponse;
import r01f.model.security.otp.task.validation.IsOTPValidationRequest;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.objectstreamer.Marshaller;
import r01f.services.bootstrap.annotations.core.CoreService;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.annotations.ServicesCoreConfigFor;
import r01f.services.delegates.security.otp.OTPServicesDelegate;

@CoreService @Singleton
public class CoreOTPServiceImpl
	  extends CoreOTPServiceImplBase<OTPServicesDelegate>
   implements OTPService,
		      OTPSecurityCoreServiceImpl {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	@Inject
	public CoreOTPServiceImpl(@ServicesCoreConfigFor(OTPModuleCodes.SECURITY_SERVICES_OTP_FQN)     final ServicesCoreModuleConfig coreConfig,
		                      @ModelObjectsMarshaller                                              final Marshaller modelObjectsMarshaller,
																			                       final EventBus eventBus,
																			                         // ....persistence service
																			                        final CRUDServicesForOTPTask taskPersistenceService,
																			                        //  .... notifier
			                   @UseEMailNotifier(OTPModuleCodes.SECURITY_SERVICES_OTP_FQN)          final NotifierServiceForEMail mailNotifier, 
			                   @UseMessagingNotifier( OTPModuleCodes.SECURITY_SERVICES_OTP_FQN)     final NotifierServiceForSMS smsNotifier
		                       ) {

		super(coreConfig,
			  modelObjectsMarshaller, eventBus,
			  // .. the delegate provider
			  scx -> new  OTPServicesDelegate(coreConfig,
					                          modelObjectsMarshaller,eventBus,
					                          taskPersistenceService,
					                          mailNotifier, smsNotifier)
			 );

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Transactional
	@Override
	public OTPOperationExecResult<OTPGenerationResponse> generate(final SecurityContext securityContext,
			                                                      final OTPGenerationRequest otpRequest) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(OTPService.class)
						.generate(securityContext,
								  otpRequest);
	}
	@Transactional
	@Override
	public <R extends IsOTPDispatchRequest<?, ?, ?>> OTPOperationExecResult<OTPDispatchResponse> dispatch(final SecurityContext securityContext,
			                                                                                            final R otpRequest) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(OTPService.class)
						.dispatch(securityContext,
								  otpRequest);
	}
	
	@Transactional
	@Override
	public <R extends IsOTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate(final SecurityContext securityContext,
			                                                                                          final R validationReq) {
		return this.forSecurityContext(securityContext)
					.createDelegateAs(OTPService.class)
						.validate(securityContext,
								validationReq);
	}
}
