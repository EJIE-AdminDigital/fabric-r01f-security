package r01f.security.user;

import java.io.Serializable;

import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;

public interface UserDataFromThirdPartySecurityProvider
		 extends Serializable {
	
	public LoginID getLoginId();
	public EMail getMail();
}
