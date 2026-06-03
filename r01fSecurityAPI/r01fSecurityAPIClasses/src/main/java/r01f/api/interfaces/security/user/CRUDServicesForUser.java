package r01f.api.interfaces.security.user;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

public interface CRUDServicesForUser<U extends User>
		 extends CRUDServicesForModelObject<UserOID,U>,
				 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final SecurityProviderID securityProviderId,final LoginID loginId);
	/**
	 * Loads the owner with the given email (should exist a single owner with the given mail)
	 * @param email
	 * @return
	 */
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final EMail email);
	/**
	 * Loads the owner with the given phone (should exist a single owner with the given phone)
	 * @param phone
	 * @return
	 */
	public CRUDResult<U> loadBy(final SecurityContext securityContext,
								final Phone phone);
}

