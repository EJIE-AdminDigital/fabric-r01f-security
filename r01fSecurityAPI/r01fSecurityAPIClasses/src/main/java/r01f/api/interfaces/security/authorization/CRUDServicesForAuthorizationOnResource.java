package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForAuthorizationOnResource
		 extends CRUDServicesForModelObject<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>,
				 IsSecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns an authorization for the principal and targetResourceOid
	 * @param securityContext 
	 * @param securityPrincipalOid
	 * @param resourceOid
	 * @return 
	 */
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resourceOid);
	
	/**
	 * Returns an authorization for the principal and targetResourceOid
	 * @param securityContext
	 * @param securityPrincipalOid
	 * @param resourceOid
	 * @param role
	 * @return 
	 */
	public CRUDResult<SecurityPrincipalAuthorizationOnResource> loadBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resourceOid,final SecurityRole role);
	
}
