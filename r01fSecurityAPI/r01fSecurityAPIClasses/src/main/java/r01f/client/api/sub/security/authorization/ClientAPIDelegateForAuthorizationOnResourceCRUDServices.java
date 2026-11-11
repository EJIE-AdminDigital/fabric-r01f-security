package r01f.client.api.sub.security.authorization;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationOnResource;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForAuthorizationOnResourceCRUDServices
	 extends ClientAPIDelegateForModelObjectCRUDServices<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForAuthorizationOnResourceCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													  			   final Marshaller marshaller,
													  			   final CRUDServicesForAuthorizationOnResource crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns an authorization from the userOid and targetResourceOid
	 * @param securityPrincipalOid
	 * @param resourceOid
	 * @return
	 */
	public SecurityPrincipalAuthorizationOnResource loadBy(final SecurityPrincipalOID securityPrincipalOid,
														   final AuthorizationTargetResourceOID resourceOid,final SecurityRole role) {
		CRUDResult<SecurityPrincipalAuthorizationOnResource> loadResult = this.getServiceProxyAs(CRUDServicesForAuthorizationOnResource.class)
																			   .loadBy(this.getSecurityContext(),
																					   securityPrincipalOid,
																					   resourceOid,role);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(loadResult);
	}
	/**
	 * Returns an authorization from the userOid and targetResourceOid
	 * @param identityProvider
	 * @param securityPrincipalOid
	 * @param resourceOid
	 * @return 
	 */
	public SecurityPrincipalAuthorizationOnResource loadBy(final SecurityPrincipalOID securityPrincipalOid,
														   final AuthorizationTargetResourceOID resourceOid) {
		CRUDResult<SecurityPrincipalAuthorizationOnResource> loadResult = this.getServiceProxyAs(CRUDServicesForAuthorizationOnResource.class)
																			   .loadBy(this.getSecurityContext(),
																					   securityPrincipalOid,
																					   resourceOid);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(loadResult);
	}
}
