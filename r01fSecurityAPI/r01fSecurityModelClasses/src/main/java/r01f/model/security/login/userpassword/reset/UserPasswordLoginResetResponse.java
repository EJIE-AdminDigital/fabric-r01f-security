package r01f.model.security.login.userpassword.reset;

import java.time.Instant;

import r01f.model.security.SecurityObject;
import r01f.securitycontext.SecurityIDS.LoginID;

public interface UserPasswordLoginResetResponse 
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
	
	public <R extends UserPasswordLoginResetResponse> R as(final Class<R> type);
	public UserPasswordLoginResetResponseOK asResponseOK();
	public UserPasswordLoginResetResponseNOK asResponseNOK();
}
