package r01f.client.api.sub.security.authorization;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForAuthorizationTargetResourceCRUDServices
	 extends ClientAPIDelegateForModelObjectCRUDServices<AuthorizationTargetResourceOID,AuthorizationTargetResource> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthorizationTargetResourceCRUDServices(final Provider<SecurityContext>  securityContextProvider,
																	   final Marshaller marshaller,
																	   final CRUDServicesForAuthorizationTargetResource crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}

}
