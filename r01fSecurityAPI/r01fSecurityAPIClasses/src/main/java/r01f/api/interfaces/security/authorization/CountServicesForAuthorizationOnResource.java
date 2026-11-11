package r01f.api.interfaces.security.authorization;

import java.util.Collection;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.AuthorizationsWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.interfaces.CountServicesForModelObject;

public interface CountServicesForAuthorizationOnResource
		 extends CountServicesForModelObject<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>,
				 IsSecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Counts the number of [security principals] with authorization on a target resource
	 * @param securityContext
	 * @param resourceOid
	 * @return 
	 */
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
																										    final AuthorizationTargetResourceOID resourceOid);
	
	/**
	 * Counts the number of [security principals] with authorization on a target resource with a given role
	 * @param securityContext
	 * @param resourceOid
	 * @param role
	 * @return 
	 */
	public CountResult<SecurityPrincipalAuthorizationOnResource> countSecurityPrincipalsWithAuthsOnResource(final SecurityContext securityContext,
												   					 										final AuthorizationTargetResourceOID resourceOid,final SecurityRole role);
	/**
	 * Counts the number of [security principals] with authorization on a target resource grouping by role
	 * @param securityContext
	 * @param resourceOid
	 * @return
	 */
	public COREServiceMethodExecResult<Collection<AuthorizationsWithRoleCount>> countSecurityPrincipalsWithAuthsOnResourceByRole(final SecurityContext securityContext,
																										 	   					 final AuthorizationTargetResourceOID resourceOid);
}
