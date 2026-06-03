package r01f.model.security.login.saml;

import r01f.model.security.login.Login;
import r01f.types.contact.EMail;

/**
 * Google login
 */
public interface SAMLLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getMail();
	public void setMail(EMail mail);
}
