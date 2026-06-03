package r01f.model.security.login.giltza;

import r01f.model.security.login.Login;
import r01f.types.contact.EMail;

/**
 * Giltza login
 */
public interface GiltzaLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getEmail();
	public void setEmail(EMail email);
}
