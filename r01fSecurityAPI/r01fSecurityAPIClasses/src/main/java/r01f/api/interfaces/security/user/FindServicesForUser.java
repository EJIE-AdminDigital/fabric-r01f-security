package r01f.api.interfaces.security.user;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.FindResult;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForUser<U extends User>
		 extends FindServicesForModelObject<UserOID,U>,
				 SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Fina all users
	 * @param securityContext
	 * @return
	 */
	public FindResult<U> findAllUsers(final SecurityContext securityContext);
	/**
	 * Finds all uses that have any system-wide role
	 * @param securityContext
	 * @return
	 */
	public FindResult<U> findUsersWithSystemWideRole(final SecurityContext securityContext);
	/**
	 * Find uses by text
	 * @param ontext
	 * @return
	 */
	public FindResult<U> findByText(final SecurityContext securityContext,
									final String text);
}