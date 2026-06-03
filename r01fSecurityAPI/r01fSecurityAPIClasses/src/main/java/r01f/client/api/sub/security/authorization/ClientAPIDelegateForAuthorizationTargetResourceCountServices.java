package r01f.client.api.sub.security.authorization;



import jakarta.inject.Provider;
import r01f.api.interfaces.security.authorization.CountServicesForAuthorizationTargetResource;
import r01f.model.security.authorization.AuthorizationTargetResource;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.AuthorizationTargetResourceOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCountServices;

public class ClientAPIDelegateForAuthorizationTargetResourceCountServices<R extends AuthorizationTargetResource>
	 extends ClientAPIDelegateForModelObjectCountServices<AuthorizationTargetResourceOID,R> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthorizationTargetResourceCountServices(final Provider<SecurityContext>  securityContextProvider,
																	    final Marshaller marshaller,
																	    final CountServicesForAuthorizationTargetResource<R> countServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  countServicesProxy);
	}

}
