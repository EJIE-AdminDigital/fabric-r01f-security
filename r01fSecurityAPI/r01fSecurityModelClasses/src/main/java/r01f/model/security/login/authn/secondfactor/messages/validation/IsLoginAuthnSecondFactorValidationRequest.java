package r01f.model.security.login.authn.secondfactor.messages.validation;

import r01f.model.security.IsSecurityObject;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Secret;

public interface IsLoginAuthnSecondFactorValidationRequest 
		 extends IsSecurityObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginID getLoginId();
	
	public Secret getSecret();	
	
	public <F extends IsLoginAuthnSecondFactor> F getFactor(); 

	public boolean hasMoreFactors();
	
	public IsLoginAuthnSecondFactor getNextAuthFactor();	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public <F extends IsLoginAuthnSecondFactor,
			R extends IsLoginAuthnSecondFactorValidationRequest> R cloneWith(F newFactor);
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public default <V extends IsLoginAuthnSecondFactorValidationRequest> V as(final Class<V> type) {
		return (V)this;
	}
	public default LoginAuthnSecondFactorValidationRequestUsingOTP asLoginAuthnSecondFactorValidationRequestUsingOTP() {
		return this.as(LoginAuthnSecondFactorValidationRequestUsingOTP.class);
	}
}
