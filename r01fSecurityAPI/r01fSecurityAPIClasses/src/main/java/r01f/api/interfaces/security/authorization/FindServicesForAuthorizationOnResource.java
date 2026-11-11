package r01f.api.interfaces.security.authorization;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.FindResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForAuthorizationOnResource
		 extends FindServicesForModelObject<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource>,
				 IsSecurityServiceInterface {

/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENDED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns all authorizations for a given [security principal]
	 * @param securityContext 
	 * @param identityProvider
	 * @param securityPrincipalOid
	 * @return  
	 */
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid);
	/**
	 * Returns all authorizations for a given [security principal]
	 * @param securityContext 
	 * @param identityProvider
	 * @param securityPrincipalOid
	 * @param appCode
	 * @param appComp
	 * @return  
	 */
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AppCode appCode,final AppComponent appComp);
	/**
	 * Returns all authorizations for a given target
	 * @param securityContext 
	 * @param authResOid
	 * @return  
	 */
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final AuthorizationTargetResourceOID authResOid);
	/**
	 * Returns all authorizations for a given [security principal] on a given target
	 * @param securityContext 
	 * @param identityProvider
	 * @param securityPrincipalOid
	 * @param resOid
	 * @return  
	 */
	public FindResult<SecurityPrincipalAuthorizationOnResource> findBy(final SecurityContext securityContext,
																	   final SecurityPrincipalOID securityPrincipalOid,
																	   final AuthorizationTargetResourceOID resOid);
	
}
