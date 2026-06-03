package r01f.model.security.login.pairedphone;

import r01f.model.security.login.Login;
import r01f.types.contact.Phone;

/**
 * Paired Phone Login
 */
public interface PairedPhoneLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public Phone getPairedPhone();
	public void setPairedPhone(final Phone phone);
}
