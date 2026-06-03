package r01f.services.delegates.sub.security.otp;


import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.persistence.CRUDServicesForOTPTask;
import r01f.eventbus.EventBus;
import r01f.model.security.otp.operations.OTPErrorTypes;
import r01f.model.security.otp.operations.OTPOperation;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.operations.OTPOperationExecResultBuilder;
import r01f.model.security.otp.task.OTPTask;
import r01f.model.security.otp.validation.OTPValidationRequest;
import r01f.model.security.otp.validation.OTPValidationResponse;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.util.types.Strings;

@Slf4j
public class OTPServicesSubDelegateForValidation
   extends OTPServicesSubDelegateBase {
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPServicesSubDelegateForValidation(final ServicesCoreModuleConfig coreConfig,
											   final Marshaller modelObjectsMarshaller, EventBus eventBus,
											   final CRUDServicesForOTPTask taskPersistenceService) {
			super(coreConfig, modelObjectsMarshaller, eventBus, taskPersistenceService);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public <R extends OTPValidationRequest<?>> OTPOperationExecResult<OTPValidationResponse> validate(final SecurityContext securityContext,
			                                                                                          final R validationReq) {
		// [1] - Get the OTP task either by taskOid, phone or email
    	log.info("[1][OTPValidation]: validate otp task {}={}",
    			 validationReq.getKeyType().getCode(),validationReq.getKey());

		OTPTask otpTask = loadTaskBy( securityContext,
				                      validationReq.getAppCode(),validationReq.getAppComponent(),
									  validationReq.getKey());

	    // [2] - If the task was not found, it was either expired or deleted
		if (otpTask == null) {
			String msg = Strings.customized("Searched an otp task by {} with key={} " +
											"BUT it was NOT found: the OTP task might be expired or it does NOT exists!",
								 			validationReq.getKeyType().getCode(),validationReq.getKey());
			log.info("[2][OTPValidation]: {}",msg);
			return OTPOperationExecResultBuilder.notExecuted(OTPOperation.VALIDATION)
											 	.because(OTPErrorTypes.OTP_VALIDATION_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED,
											 			 msg);
	    }
		log.info("[2][OTPValidation]: found otp task {}={}: oid={}",
					 validationReq.getKeyType().getCode(),validationReq.getKey(),
					 otpTask.getOid());


		// [3] - Check the number of retries
		if (otpTask.getRemainingRetryNum() == 0) {
		    log.warn("[3][OTPValidation]: otp task oid={} has exhausted the number of retries!",
		    		 otpTask.getOid());
		   	return OTPOperationExecResultBuilder.notExecuted(OTPOperation.VALIDATION)
									 	        .because(OTPErrorTypes.OTP_VALIDATION_ERROR_OTP_NUMBER_OF_ATTEMPTS_EXCEEDED,
									 	        		 "Number of attempts exceeded for key "+ otpTask.getSecret() + " !");

		}
		log.info("[3][OTPValidation]: otp task oid={} has {} retries left",
		    		 otpTask.getOid(),otpTask.getRemainingRetryNum());

		// [4] - Check the secret
		if (otpTask.getSecret().is(validationReq.getSecret())) {
			log.info("[3][OTPValidation]: otp task oid={} was SUCCESFULLY validated!",
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
		log.info("[3][OTPValidation]: otp task oid={} was NOT validated: the provided secret does NOT match the previously sent one!",
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


