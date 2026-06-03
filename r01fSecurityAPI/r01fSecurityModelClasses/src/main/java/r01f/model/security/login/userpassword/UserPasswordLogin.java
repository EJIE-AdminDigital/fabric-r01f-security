package r01f.model.security.login.userpassword;

import java.time.Instant;

import r01f.model.security.login.Login;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.PasswordHash;

/**
 * User login
 */
public interface UserPasswordLogin
		 extends Login {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public Password getPassword();
	public void setPassword(final Password password);

	public PasswordHash getPasswordHash();
	public void setPasswordHash(final PasswordHash passwordHash);

	public Instant getPasswordExpirationDate();
	public void setPasswordExpirationDate(final Instant date);
	
	public boolean isConfirmed();
	public void setConfirmed(boolean isConfirmed);
}
