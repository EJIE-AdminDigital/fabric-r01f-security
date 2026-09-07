package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;

@Accessors(prefix="_")
public abstract class LoginAuthFactorValidationRequestUsingOTPBase<F extends LoginAuthFactor>
			  extends LoginAuthFactorValidationRequestBase<F> {

	private static final long serialVersionUID = 1106292544725125704L;
	
///////////////////////////////////////////////////////////////////////////////////////
//FIELDS
///////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="taskOid")
	@Getter protected final OTPTaskOID _taskOid;

/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationRequestUsingOTPBase(final OTPTaskOID taskOid,
														final LoginID  loginId,
														final Password secret,
														final F factor) {
		super(loginId,
			  secret,
			  factor);
		_taskOid = taskOid;
	}

}