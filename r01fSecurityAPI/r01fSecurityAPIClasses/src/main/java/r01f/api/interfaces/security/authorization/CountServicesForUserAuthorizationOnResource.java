package r01f.api.interfaces.security.authorization;

import java.util.Collection;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForUserAuthorizationOnResource<A extends UserAuthorizationOnResource>
		 extends CountServicesForModelObject<UserAuthorizationOnResourceOID,A>,
				 SecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Counts the number of users with authorization on a target resource
	 * @param securityContext the user auth data & context info
	 * @param resourceOid
	 * @return 
	 */
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid);
	
	/**
	 * Counts the number of users with authorization on a target resource with a given user role
	 * @param securityContext the user auth data & context info
	 * @param resourceOid
	 * @param userRole
	 * @return 
	 */
	public CountResult<A> countUserAuthsOnResource(final SecurityContext securityContext,
												   final AuthorizationTargetResourceOID resourceOid,
												   final UserRole userRole);
	/**
	 * Counts the number of users with authorization on a target resource grouping by user role
	 * @param securityContext
	 * @param resourceOid
	 * @return
	 */
	public COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> countUserAuthsOnResourceByRole(final SecurityContext securityContext,
																										 final AuthorizationTargetResourceOID resourceOid);
}
