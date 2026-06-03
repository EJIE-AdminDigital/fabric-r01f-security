package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
		 extends CRUDServicesForModelObject<UserAuthorizationOnResourceOID,A>,
				 SecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns an authorization from the userOid and targetResourceOid
	 * @param securityContext the user auth data & context info
	 * @param userOid
	 * @param resourceOid
	 * @return 
	 */
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid);
	
	/**
	 * Returns an authorization from the userOid and targetResourceOid
	 * @param securityContext the user auth data & context info
	 * @param userOid
	 * @param resourceOid
	 * @param role
	 * @return 
	 */
	public CRUDResult<A> loadBy(final SecurityContext securityContext,
								final UserOID userOid,
								final AuthorizationTargetResourceOID resourceOid,
								final UserRole role);
	
}
