package r01f.model.security.otp.validation;

import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.Phone;



@Accessors(prefix="_")
public class OTPValidationRequestByPhone
     extends OTPValidationRequestBase<Phone> {

	private static final long serialVersionUID = 2742689756931252664L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPValidationRequestByPhone(final AppCode appCode,final AppComponent appComp,
									   final Phone key,final Password secret) {
		super(appCode,appComp,
			  key,secret);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public Phone getPhone() {
		return _key;
	}
	@Override
	public OTPValidationRequestKey getKeyType() {
		return OTPValidationRequestKey.SMS;
	}
}