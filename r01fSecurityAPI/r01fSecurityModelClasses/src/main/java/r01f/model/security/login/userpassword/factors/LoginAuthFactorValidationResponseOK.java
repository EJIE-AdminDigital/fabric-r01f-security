package r01f.model.security.login.userpassword.factors;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorValidationResponseOK")
@Accessors(prefix="_")
public class LoginAuthFactorValidationResponseOK
  	 extends LoginAuthFactorValidationResponseBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationResponseOK(boolean secretValidated, 
			                                   long remainingRetryNum) {
		super(secretValidated, remainingRetryNum);		
	}	
}
