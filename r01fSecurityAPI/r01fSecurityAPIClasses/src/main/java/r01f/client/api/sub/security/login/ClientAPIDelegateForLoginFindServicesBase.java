package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.FindServicesForLogin;
import r01f.model.security.login.Login;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;

public abstract class ClientAPIDelegateForLoginFindServicesBase<L extends Login>
	 		  extends ClientAPIDelegateForModelObjectFindServices<LoginOID,L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForLoginFindServicesBase(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final FindServicesForLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
