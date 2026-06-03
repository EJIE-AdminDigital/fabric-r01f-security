package r01f.model.security.login.userpassword;

import java.time.Instant;


import r01f.model.security.SecurityObject;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.eventbus.Event;

public interface UserPasswordLoginResponse
		 extends SecurityObject ,
		         Event {
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
	public boolean isPendingOnAuthFactor();
	
	public <R extends UserPasswordLoginResponse> R as(final Class<R> type);
	public <U extends User> UserPasswordLoginResponseOK<U> asResponseOKFor(final Class<U> userType);
	public UserPasswordLoginResponseNOK asResponseNOK();
}
