package r01f.client.api.sub.security.authorization;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.authorization.CRUDServicesForUserAuthorizationOnResource;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.authorization.UserAuthorizationOnResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.UserRole;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.securitycontext.SecurityOIDs.UserAuthorizationOnResourceOID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForUserAuthorizationOnResourceCRUDServices<A extends UserAuthorizationOnResource>
	 extends ClientAPIDelegateForModelObjectCRUDServices<UserAuthorizationOnResourceOID,A> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public ClientAPIDelegateForUserAuthorizationOnResourceCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													  				   final Marshaller marshaller,
													  				   final CRUDServicesForUserAuthorizationOnResource<A> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns an authorization from the userOid and targetResourceOid
	 * @param userOid
	 * @param resourceOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public A loadBy(final UserOID userOid,
					final AuthorizationTargetResourceOID resourceOid,
					final UserRole role) {
		CRUDResult<A> loadResult = this.getServiceProxyAs(CRUDServicesForUserAuthorizationOnResource.class)
									   .loadBy(this.getSecurityContext(),
											   userOid,resourceOid,role);
		return loadResult.getOrThrow();
	}
}
