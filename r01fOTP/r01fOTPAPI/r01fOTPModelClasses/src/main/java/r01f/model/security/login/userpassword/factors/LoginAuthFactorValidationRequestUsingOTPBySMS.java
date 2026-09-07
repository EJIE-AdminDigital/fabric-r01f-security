package r01f.model.security.login.userpassword.factors;

import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
public class LoginAuthFactorValidationRequestUsingOTPBySMS
     extends LoginAuthFactorValidationRequestUsingOTPBase<LoginAuthFactorUsingOTPBySMS> {

	private static final long serialVersionUID = 2742689756931252664L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationRequestUsingOTPBySMS(final OTPTaskOID taskOid,
														 final LoginID  loginId, 
														 final Password secret,final Phone phone) {
		super(taskOid,
			  loginId,
			  secret, 
			  new LoginAuthFactorUsingOTPBySMS(phone));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public Phone getPhone() {
		return super.getFactor().getPhone();
	}
	
}