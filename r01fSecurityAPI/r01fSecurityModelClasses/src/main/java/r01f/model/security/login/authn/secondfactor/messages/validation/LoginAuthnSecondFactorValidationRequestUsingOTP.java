package r01f.model.security.login.authn.secondfactor.messages.validation;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.model.security.login.authn.secondfactor.LoginAuthnSecondFactorUsingOTP;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.types.jobs.JobOID;

@MarshallType(as="loginAuthSecondFactorValidationRequestUsingOTP")
@Accessors(prefix="_")
public class LoginAuthnSecondFactorValidationRequestUsingOTP
	 extends LoginAuthnSecondFactorValidationRequestBase<LoginAuthnSecondFactorUsingOTP,
	 													 LoginAuthnSecondFactorValidationRequestUsingOTP> {

	private static final long serialVersionUID = 1106292544725125704L;
	
///////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
///////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="otpJobOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter protected final JobOID _otpJobOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorValidationRequestUsingOTP(final JobOID otpJobOid,
														   final LoginID  loginId,
														   final Secret secret,
														   final LoginAuthnSecondFactorUsingOTP factor) {
		super(loginId,
			  secret,
			  factor);
		_otpJobOid = otpJobOid;
	}
	public LoginAuthnSecondFactorValidationRequestUsingOTP(final JobOID otpJobOid,
														   final LoginID  loginId,
														   final Secret secret,
														   final EMail email) {
		this(otpJobOid,
			 loginId,
			 secret,
			 LoginAuthnSecondFactorUsingOTP.forEMail(email));
	}
	public LoginAuthnSecondFactorValidationRequestUsingOTP(final JobOID otpJobOid,
														   final LoginID  loginId,
														   final Secret secret,
														   final Phone phone) {
		this(otpJobOid,
			 loginId,
			 secret,
			 LoginAuthnSecondFactorUsingOTP.forPhone(phone));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <F extends IsLoginAuthnSecondFactor, 
			R extends IsLoginAuthnSecondFactorValidationRequest> R cloneWith(final F newFactor) {
		return (R)new LoginAuthnSecondFactorValidationRequestUsingOTP(this.getOtpJobOid(),
																      this.getLoginId(),
																      this.getSecret(),
																      (LoginAuthnSecondFactorUsingOTP)newFactor);
	}
}