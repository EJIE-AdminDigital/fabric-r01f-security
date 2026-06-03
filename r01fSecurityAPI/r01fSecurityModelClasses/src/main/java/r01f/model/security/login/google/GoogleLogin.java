package r01f.model.security.login.google;

import r01f.model.security.login.Login;
import r01f.types.contact.EMail;

/**
 * Google login
 */
public interface GoogleLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getGoogleMail();
	public void setGoogleMail(EMail mail);
}
