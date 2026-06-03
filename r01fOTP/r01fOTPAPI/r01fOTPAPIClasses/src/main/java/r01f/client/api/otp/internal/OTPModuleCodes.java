package r01f.client.api.otp.internal;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.internal.R01FAppCodes;
import r01f.services.ids.ServiceIDs.CoreModule;

@Accessors(prefix="_")
@RequiredArgsConstructor
public class OTPModuleCodes {

	
/////////////////////////////////////////////////////////////////////////////////////////
//  SECURITY SERVICES FOR OTP
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String SECURITY_SERVICES_OTP_STR = "security.services.otp";
	
	public static final String SECURITY_SERVICES_OTP_FQN =  R01FAppCodes.R01_APP_CODE_STR + "." + SECURITY_SERVICES_OTP_STR;
	
	
	public static final CoreModule SECURITY_SERVICES_OTP_MOD = CoreModule.forId(SECURITY_SERVICES_OTP_STR);
	
	
/////////////////////////////////////////////////////////////////////////////////////////
// SECURITY SERVICES FOR OTP PERSISTENCE
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String SECURITY_SERVICES_OTP_PERSISTENCE_STR = "security.services.otp.persistence";
	
	public static final String SECURITY_SERVICES_OTP_PERSISTENCE_FQN =  R01FAppCodes.R01_APP_CODE_STR + "." + SECURITY_SERVICES_OTP_PERSISTENCE_STR;	
	
	public static final CoreModule SECURITY_SERVICES_PERSISTENCE_OTP_MOD = CoreModule.forId(SECURITY_SERVICES_OTP_PERSISTENCE_STR);
	


}
