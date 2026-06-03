package r01f.model.security.login.userpassword.factors;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorValidationResponseNOK")
@Accessors(prefix="_")
public class LoginAuthFactorValidationResponseNOK
  	 extends LoginAuthFactorValidationResponseBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationResponseNOK(long remainingRetryNum) {
		super(false, remainingRetryNum);		
	}	
}
