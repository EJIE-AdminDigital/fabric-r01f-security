package r01f.services.delegates.sub.security.otp;


import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.OTPTask;
import r01f.model.security.otp.operations.OTPErrorTypes;
import r01f.model.security.otp.operations.OTPOperation;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.operations.OTPOperationExecResultBuilder;
import r01f.model.security.otp.task.validation.IsOTPValidationRequest;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.objectstreamer.Marshaller;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.util.types.Strings;

@Slf4j
public class OTPServicesSubDelegateForValidation
   extends OTPServicesSubDelegateBase {
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPServicesSubDelegateForValidation(final ServicesCoreModuleConfig coreConfig,
											   final Marshaller modelObjectsMarshaller, final EventBus eventBus,
											   final CRUDServicesForOTPTask taskPersistenceService) {
			super(coreConfig, modelObjectsMarshaller, eventBus, taskPersistenceService);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <R extends IsOTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate(final SecurityContext securityContext,
			                                                                                          final R validationReq) {
		// [1] - Get the OTP task either by taskOid, phone or email
		log.info("[1][OTPValidation]: validate otp task ({}) of type {}={}",
				 validationReq.getTaskOid(),validationReq.getKeyType(),validationReq.getKey());

//		OTPTask otpTask = loadTaskBy( securityContext,
//				                      validationReq.getAppCode(),validationReq.getAppComponent(),
//									  validationReq.getKey());
		OTPTask otpTask = loadTask(securityContext,
								   validationReq.getTaskOid());
		
		// [2] - If the task was not found, it was either expired or deleted
		if (otpTask == null) {
			String msg = Strings.customized("Searched an otp task with oid={} " +
											"BUT it was NOT found: the OTP task might be expired or it does NOT exists!",
								 			validationReq.getTaskOid());
			log.info("[2][OTPValidation]: {}",msg);
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.VALIDATION)
											 	.because(OTPErrorTypes.OTP_VALIDATION_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED,
											 			 msg);
		}
		log.info("[2][OTPValidation]: found otp task {}={}: oid={}",
					 otpTask.getOid());
		
		// [3] - If the task was found, check if request is valid for the task
		boolean taskValidationOk = validateRequest(validationReq.getKey(), otpTask);
		if (!taskValidationOk) {
			String msg = Strings.customized("Otp task with oid= {} not valid for key {}={}",
											validationReq.getTaskOid(),validationReq.getKeyType(),validationReq.getKey());
			log.info("[3][OTPValidation]: {}",msg);
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.VALIDATION)
												.because(OTPErrorTypes.OTP_VALIDATION_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED,
														 msg);

		}
		log.info("[3][OTPValidation]: valid otp request task with oid {} and key {}={",
				 otpTask.getOid(),validationReq.getKeyType(),validationReq.getKey()
				 );

		// [4] - Check the number of retries
		if (otpTask.getRemainingRetryNum() == 0) {
		    log.warn("[4][OTPValidation]: otp task oid={} has exhausted the number of retries!",
		    		 otpTask.getOid());
		   	return OTPOperationExecResultBuilder.notExecuted(OTPOperation.VALIDATION)
									 	        .because(OTPErrorTypes.OTP_VALIDATION_ERROR_OTP_NUMBER_OF_ATTEMPTS_EXCEEDED,
									 	        		 "Number of attempts exceeded for key "+ otpTask.getSecret() + " !");

		}
		log.info("[4][OTPValidation]: otp task oid={} has {} retries left",
		    		 otpTask.getOid(),otpTask.getRemainingRetryNum());

		// [5] - Check the secret
		if (otpTask.getSecret().is(validationReq.getSecret())) {
			log.info("[5][OTPValidation]: otp task oid={} was SUCCESFULLY validated!",
					 otpTask.getOid());

			// Remove from cache after a successful otp validation,
			 _taskPersistenceService.delete(securityContext,
					                        otpTask.getOid()).getOrThrow();

			 // Send the response
			OTPValidationResponse response = new OTPValidationResponse(otpTask.getOid(),
																	   true,		// secret valid
																	   otpTask.getRemainingRetryNum());
			log.info("...return validation ok!");
			return OTPOperationExecResultBuilder.executed(OTPOperation.VALIDATION)
													.returning(response);
		}
		log.info("[5][OTPValidation]: otp task oid={} was NOT validated: the provided secret does NOT match the previously sent one!",
					 otpTask.getOid());
			// decrement the number of remaining retries
		 long remainingRetryNum = otpTask.decrementRemainingRetryNumber();

		 _taskPersistenceService.update(securityContext,
				                        otpTask).getOrThrow();

			// send the response
		 OTPValidationResponse response = new OTPValidationResponse(otpTask.getOid(),
																	false,		// secret not valid
																    remainingRetryNum);
		return OTPOperationExecResultBuilder.executed(OTPOperation.VALIDATION)
												.returning(response);
	}
}


