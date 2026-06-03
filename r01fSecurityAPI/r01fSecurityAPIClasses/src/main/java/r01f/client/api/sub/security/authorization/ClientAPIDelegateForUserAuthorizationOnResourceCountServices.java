package r01f.client.api.sub.security.authorization;

import java.util.Collection;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.authorization.CountServicesForUserAuthorizationOnResource;
import r01f.model.persistence.CountResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.model.security.summaries.UserAuthWithRoleCount;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCountServices;

public class ClientAPIDelegateForUserAuthorizationOnResourceCountServices<A extends UserAuthorizationOnResource>
	 extends ClientAPIDelegateForModelObjectCountServices<UserAuthorizationOnResourceOID,A> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForUserAuthorizationOnResourceCountServices(final Provider<SecurityContext>  securityContextProvider,
													  				    final Marshaller marshaller,
													  				    final CountServicesForUserAuthorizationOnResource<A> CountServicesProxy) {
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
	@SuppressWarnings("unchecked")
	public long countUserAuthsOnResource(final AuthorizationTargetResourceOID resourceOid) {
		CountResult<A> countResult = this.getServiceProxyAs(CountServicesForUserAuthorizationOnResource.class)
										   .countUserAuthsOnResource(this.getSecurityContext(),
												   					 resourceOid);
		return countResult.getOrThrow();
	}

	/**
	 * Counts the number of users with authorization on a target resource with a given user role
	 * @param resourceOid
	 * @param userRole
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public long countUserAuthsOnResource(final AuthorizationTargetResourceOID resourceOid,
												   final UserRole userRole) {
		CountResult<A> countResult = this.getServiceProxyAs(CountServicesForUserAuthorizationOnResource.class)
										   .countUserAuthsOnResource(this.getSecurityContext(),
												   					 resourceOid,
												   					 userRole);
		return countResult.getOrThrow();
	}
	/**
	 * Counts the number of users with authorization on a target resource grouping by user role
	 * @param resourceOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Collection<UserAuthWithRoleCount> countUserAuthsOnResourceByRole(final AuthorizationTargetResourceOID resourceOid) {
		COREServiceMethodExecResult<Collection<UserAuthWithRoleCount>> countResult = this.getServiceProxyAs(CountServicesForUserAuthorizationOnResource.class)
																						   .countUserAuthsOnResourceByRole(this.getSecurityContext(),
																								   					 	   resourceOid);
		return countResult.getOrThrow();
	}
}
