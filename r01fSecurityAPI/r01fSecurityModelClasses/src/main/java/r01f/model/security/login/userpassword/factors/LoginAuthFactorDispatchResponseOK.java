package r01f.model.security.login.userpassword.factors;

import java.time.Instant;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorDispatchResponseOK")
@Accessors(prefix="_")
public class LoginAuthFactorDispatchResponseOK
  	 extends LoginAuthFactorDispatchResponseBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorDispatchResponseOK(final boolean dispatched, 
			                                 final Instant tst) {
		super(dispatched, tst);		
	}

	
}
