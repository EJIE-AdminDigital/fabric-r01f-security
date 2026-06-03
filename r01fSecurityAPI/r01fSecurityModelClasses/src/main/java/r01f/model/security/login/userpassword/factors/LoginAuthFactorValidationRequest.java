package r01f.model.security.login.userpassword.factors;

import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;


public interface LoginAuthFactorValidationRequest {

	public LoginID getLoginId();
	
	public Password getSecret();	
	
	public <F extends LoginAuthFactor> F getFactor(); 

	public <V extends LoginAuthFactorValidationRequest> V as(final Class<V> type);
	
	public boolean hasMoreFactors();
	
	public LoginAuthFactor getNextAuthFactor();	 
}