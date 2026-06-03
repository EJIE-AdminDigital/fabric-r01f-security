package r01f.bootstrap.spring.core.security.otp;

import org.springframework.stereotype.Component;

import r01f.client.api.otp.internal.OTPModuleCodes;
import r01f.internal.R01FAppCodes;
import r01f.services.bootstrap.core.db.spring.CoreSpringBootstrapPersistenceModulePostProcessor;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.security.otp.CoreOTPServiceImpl;

@Component
public class OTPCORESpringCoreBootstrapServicesModulePostProcessor
		extends CoreSpringBootstrapPersistenceModulePostProcessor {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////  
    public OTPCORESpringCoreBootstrapServicesModulePostProcessor() {
		super(CoreAppCode.of(R01FAppCodes.APP_CODE), OTPModuleCodes.SECURITY_SERVICES_OTP_MOD, 			
				CoreOTPServiceImpl.class);	// Just for enable @Transaction ( Transactionality for invoked methods )	
	}
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
 
}