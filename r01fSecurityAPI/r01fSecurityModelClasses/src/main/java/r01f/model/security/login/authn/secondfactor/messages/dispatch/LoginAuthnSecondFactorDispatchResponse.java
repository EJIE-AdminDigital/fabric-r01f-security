package r01f.model.security.login.authn.secondfactor.messages.dispatch;

import java.time.Instant;

import r01f.model.security.SecurityResponseBase;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="loginAuthFactorDispatchResponse")
public class LoginAuthnSecondFactorDispatchResponse 
	 extends SecurityResponseBase<LoginAuthnSecondFactorDispatchResult,
								  LoginAuthnSecondFactorDispatchResponse> {

	private static final long serialVersionUID = -7761347947056551083L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorDispatchResponse(final LoginAuthnSecondFactorDispatchResult result) {		
		this(Instant.now(),result);
	}
	public LoginAuthnSecondFactorDispatchResponse(final Instant at,final LoginAuthnSecondFactorDispatchResult result) {		
		super(at,result);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean wasDispatched() {	
		return _result != null && _result == LoginAuthnSecondFactorDispatchResult.DISPATCHED;
	}
}
