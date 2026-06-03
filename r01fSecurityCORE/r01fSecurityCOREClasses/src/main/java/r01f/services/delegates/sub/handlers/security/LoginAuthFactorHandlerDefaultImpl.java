package r01f.services.delegates.sub.handlers.security;

import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.model.security.user.User;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

public class LoginAuthFactorHandlerDefaultImpl <U extends User & HasMailAddress & HasPhoneNumber> 
		extends LoginAuthFactorHandlerUsingOTPImplBase<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorHandlerDefaultImpl(final ServicesCoreModuleConfig coreCfg,
											 final CRUDServicesForUser<U> userCrud, 
											 final OTPService otpService) {
		super(coreCfg, userCrud, otpService);		
	}
}
