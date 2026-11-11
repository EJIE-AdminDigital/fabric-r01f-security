package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationOnResource;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.SecurityPrincipalAuthorizationOnResource;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.summaries.AuthorizationsWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.SecurityRole;
import r01f.oids.SecurityOIDs.AuthorizationOnResourceOID;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCountServices;

public class ClientAPIDelegateForAuthorizationOnResourceCountServices
	 extends ClientAPIDelegateForModelObjectCountServices<AuthorizationOnResourceOID,SecurityPrincipalAuthorizationOnResource> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForAuthorizationOnResourceCountServices(final Provider<SecurityContext>  securityContextProvider,
													  				final Marshaller marshaller,
													  				final CountServicesForAuthorizationOnResource CountServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  CountServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Counts the number of users with authorization on a target resource
	 * @param resourceOid
	 * @return
	 */
	public long countSecurityPrincipalsWithAuthsOnResource(final AuthorizationTargetResourceOID resourceOid) {
		CountResult<SecurityPrincipalAuthorizationOnResource> countResult = this.getServiceProxyAs(CountServicesForAuthorizationOnResource.class)
																				   .countSecurityPrincipalsWithAuthsOnResource(this.getSecurityContext(),
																						   					 				   resourceOid);
		return countResult.getOrThrow();
	}

	/**
	 * Counts the number of users with authorization on a target resource with a given user role
	 * @param resourceOid
	 * @param userRole
	 * @return
	 */
	public long countSecurityPrincipalsWithAuthsOnResource(final AuthorizationTargetResourceOID resourceOid,final SecurityRole userRole) {
		CountResult<SecurityPrincipalAuthorizationOnResource> countResult = this.getServiceProxyAs(CountServicesForAuthorizationOnResource.class)
																				   .countSecurityPrincipalsWithAuthsOnResource(this.getSecurityContext(),
																						   					 				   resourceOid,userRole);
		return countResult.getOrThrow();
	}
	/**
	 * Counts the number of users with authorization on a target resource grouping by user role
	 * @param resourceOid
	 * @return
	 */
	public Collection<AuthorizationsWithRoleCount> countSecurityPrincipalsWithAuthsOnResourceByRole(final AuthorizationTargetResourceOID resourceOid) {
		COREServiceMethodExecResult<Collection<AuthorizationsWithRoleCount>> countResult = this.getServiceProxyAs(CountServicesForAuthorizationOnResource.class)
																							   .countSecurityPrincipalsWithAuthsOnResourceByRole(this.getSecurityContext(),
																									   					 	   					 resourceOid);
		return countResult.getOrThrow();
	}
}
