package r01f.api.interfaces.security.login.userpassword;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;

public interface CRUDServicesForUserPasswordLogin<L extends UserPasswordLogin>
		 extends CRUDServicesForLogin<L>,
		 		 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Updates a login password
	 * @param securityContext the user auth data & context info
	 * @param login
	 * @param password
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID login,
										final Password password);
	/**
	 * Updates a login password BUT first the old password is checked
	 * @param securityContext the user auth data & context info
	 * @param login
	 * @param oldPassword
	 * @param newPassword
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> updatePassword(final SecurityContext securityContext,
										final LoginID login,
										final Password oldPassword,final Password newPassword);
}

