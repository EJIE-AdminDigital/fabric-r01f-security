package r01f.model.security.login.userpassword.confirmation;

import java.time.Instant;

import r01f.model.security.SecurityObject;
import r01f.model.security.user.UserStatus;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

public interface UserPasswordLoginConfirmationResponse 
		 extends SecurityObject {

/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid();
	public void setUserOid(final UserOID userOid);	
	
	public LoginOID getLoginOid();
	public void setLoginOid(final LoginOID loginOid);
	
	public LoginID getLoginId();
	public void setLoginId(final LoginID login);
	
	public UserStatus getUserStatusAfterLoginConfirmation();
	public void setUserStatusAfterLoginConfirmation(final UserStatus userStatus);
	
	public Instant getResponsedAt();
	public void setResponsedAt(final Instant date);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasFailed();
	public boolean hasSucceeded();
	
	public <R extends UserPasswordLoginConfirmationResponse> R as(final Class<R> type);
	public UserPasswordLoginConfirmationResponseOK asResponseOK();
	public UserPasswordLoginConfirmationResponseNOK asResponseNOK();
}
