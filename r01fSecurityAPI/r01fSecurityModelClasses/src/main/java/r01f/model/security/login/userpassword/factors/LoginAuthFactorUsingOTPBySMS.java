package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
@RequiredArgsConstructor
@MarshallType(as="loginAuthFactorUsingOTPBySMS") 
public class LoginAuthFactorUsingOTPBySMS 
 	 extends LoginAuthFactorUsingOTPBase {	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="phone",
	               whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final Phone _phone;
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public static LoginAuthFactor from(final Phone email) {		
		return new LoginAuthFactorUsingOTPBySMS(email);
	}
}
