package r01f.model.security.login.userpassword.factors;

import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;

@Accessors(prefix="_")
public class LoginAuthFactorValidationRequestUsingOTPByEMail
     extends LoginAuthFactorValidationRequestUsingOTPBase<LoginAuthFactorUsingOTPByEMail> {

	private static final long serialVersionUID = 764373603014902357L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationRequestUsingOTPByEMail(final OTPTaskOID taskOid,
														   final LoginID  loginId,
														   final Password secret, final EMail email) {
		super(taskOid,
			  loginId,
			  secret,
			  new LoginAuthFactorUsingOTPByEMail(email));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getEMail() {
		return super.getFactor().getMail();
	}	
}