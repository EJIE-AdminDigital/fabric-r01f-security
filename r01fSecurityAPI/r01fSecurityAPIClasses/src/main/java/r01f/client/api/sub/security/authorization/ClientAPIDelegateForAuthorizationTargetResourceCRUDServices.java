package r01f.client.api.sub.security.authorization;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.authorization.CRUDServicesForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForAuthorizationTargetResourceCRUDServices<R extends AuthorizationTargetResource>
	 extends ClientAPIDelegateForModelObjectCRUDServices<AuthorizationTargetResourceOID,R> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthorizationTargetResourceCRUDServices(final Provider<SecurityContext>  securityContextProvider,
																	   final Marshaller marshaller,
																	   final CRUDServicesForAuthorizationTargetResource<R> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}

}
