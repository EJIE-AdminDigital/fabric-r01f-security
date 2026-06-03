package r01f.bootstrap.spring.core.security.otp;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import r01f.client.api.otp.internal.OTPModuleCodes;
import r01f.internal.R01FAppCodes;
import r01f.services.bootstrap.core.spring.CoreSpringComponentScan;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.core.annotations.ServicesCoreConfigFor;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.security.otp.OTPSecurityCoreServiceImpl;
import r01f.spring.IsSpringConfiguration;


@Configuration
//@EnableCoreEvents
@CoreSpringComponentScan(basePackageClasses  = { OTPSecurityCoreServiceImpl.class     
						})
public class OTPCORESpringBootstrapConfiguration 
  implements IsSpringConfiguration {	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Bean  @ServicesCoreConfigFor(OTPModuleCodes.SECURITY_SERVICES_OTP_FQN)
	public ServicesCoreModuleConfig coreModuleConfigForOTP() {
		return new ServicesCoreModuleConfig(CoreAppCode.of(R01FAppCodes.APP_CODE),
				                            OTPModuleCodes.SECURITY_SERVICES_OTP_MOD);
	}  
////////////////////////////////////////////////////////////////////////
///  Searcher Provider
////////////////////////////////////////////////////////////////////////
	


}
