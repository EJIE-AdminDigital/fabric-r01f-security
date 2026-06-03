package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.EMail;

@Accessors(prefix="_")
@RequiredArgsConstructor
@MarshallType(as="loginAuthFactorUsingOTPByEMail") 
public class LoginAuthFactorUsingOTPByEMail 
	extends LoginAuthFactorUsingOTPBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="mail",
	               whenXml=@MarshallFieldAsXml(attr=true))
	@Getter  private final EMail _mail;
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public static LoginAuthFactor from(final EMail email) {		
		return new LoginAuthFactorUsingOTPByEMail(email);
	}
}
