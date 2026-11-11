package r01f.services.delegates.security.userpassword.secondfactor;

import r01f.api.interfaces.security.otp.OTPService;
import r01f.model.security.IsSecurityPrincipal;
import r01f.services.core.ServicesCoreModuleConfig;

public class LoginAuthnSecondFactorHandlerDefaultImpl<P extends IsSecurityPrincipal> 
	 extends LoginAuthnSecondFactorHandlerUsingOTPImplBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorHandlerDefaultImpl(final ServicesCoreModuleConfig coreCfg,
											 		final OTPService otpService) {
		super(coreCfg,
			  otpService);		
	}
}
