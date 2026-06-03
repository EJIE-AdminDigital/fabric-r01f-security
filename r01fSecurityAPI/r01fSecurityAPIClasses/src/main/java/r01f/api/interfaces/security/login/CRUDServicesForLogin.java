package r01f.api.interfaces.security.login;

import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.Login;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForLogin<L extends Login>
		 extends CRUDServicesForModelObject<LoginOID,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a login for the given user
	 * @param securityContext the user auth data & context info
	 * @param userOid the user
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final UserOID userOid);
	/**
	 * Returns a login for the given login id
	 * @param securityContext the user auth data & context info
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<L> loadBy(final SecurityContext securityContext,
								final LoginID login);
}

