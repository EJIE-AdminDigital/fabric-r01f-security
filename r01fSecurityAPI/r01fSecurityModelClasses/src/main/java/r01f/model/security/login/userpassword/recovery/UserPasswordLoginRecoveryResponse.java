package r01f.model.security.login.userpassword.recovery;

import java.time.Instant;

import r01f.model.security.SecurityObject;
import r01f.securitycontext.SecurityIDS.LoginID;

public interface UserPasswordLoginRecoveryResponse
		 extends SecurityObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginID getLoginId();
	public void setLoginId(final LoginID login);
	
	public Instant getResponsedAt();
	public void setResponsedAt(final Instant date);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasFailed();
	public boolean hasSucceeded();
	
	public <R extends UserPasswordLoginRecoveryResponse> R as(final Class<R> type);
	public UserPasswordLoginRecoveryResponseOK asResponseOK();
	public UserPasswordLoginRecoveryResponseNOK asResponseNOK();
}
