package r01f.model.security.login.userpassword.create;

import java.time.Instant;

import r01f.model.security.SecurityObject;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;

public interface UserPasswordLoginCreateResponse 
  	     extends SecurityObject {

/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid();
	public void setUserOid(final UserOID userOid);
	
	public LoginID getLoginId();
	public void setLoginId(final LoginID login);
	
	public Instant getRegisteredAt();
	public void setRegisteredAt(final Instant date);
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasFailed();
	public boolean hasSucceeded();
	
	public <R extends UserPasswordLoginCreateResponse> R as(final Class<R> type);
	public UserPasswordLoginCreateResponseOK asResponseOK();
	public UserPasswordLoginCreateResponseNOK asResponseNOK();
}
