package r01f.bootstrap.spring.core.security.otp.persistence;

import org.springframework.stereotype.Component;


import r01f.client.api.otp.internal.OTPModuleCodes;
import r01f.db.entities.security.otp.persistence.DBEntityForOTPTask;
import r01f.internal.R01FAppCodes;
import r01f.services.bootstrap.core.db.spring.CoreSpringBootstrapPersistenceModulePostProcessor;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.security.otp.persistence.CoreOTPDBPersistenceService;

@Component
public class OTPCORESpringCoreBootstrapPersistenceModulePostProcessor
		extends CoreSpringBootstrapPersistenceModulePostProcessor {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////  
    public OTPCORESpringCoreBootstrapPersistenceModulePostProcessor() {
		super(CoreAppCode.of(R01FAppCodes.APP_CODE), OTPModuleCodes.SECURITY_SERVICES_PERSISTENCE_OTP_MOD, 
				DBEntityForOTPTask.class, 
				CoreOTPDBPersistenceService.class);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////

}