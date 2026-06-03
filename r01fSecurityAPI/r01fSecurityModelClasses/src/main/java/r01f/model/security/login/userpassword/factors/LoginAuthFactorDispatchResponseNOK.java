package r01f.model.security.login.userpassword.factors;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorDispatchResponseNOK")
@Accessors(prefix="_")
public class LoginAuthFactorDispatchResponseNOK
  	 extends LoginAuthFactorDispatchResponseBase {	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="errorType",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserPasswordLoginErrorType _errorType;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorDispatchResponseNOK(final Instant tst , final UserPasswordLoginErrorType errorType) {
		super(false, tst);		
		_errorType = errorType;
	}	
}
