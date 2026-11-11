package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;

public class ClientAPIDelegateForLoginFindServices
     extends ClientAPIDelegateForModelObjectFindServices<LoginOID,Login> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
												 final Marshaller marshaller,
												 final FindServicesForLogin findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
