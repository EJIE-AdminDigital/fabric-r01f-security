package r01f.client.api.sub.security.login.saml;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.saml.FindServicesForSAMLLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginFindServicesBase;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForSAMLLoginFindServices<L extends SAMLLogin>
	 extends ClientAPIDelegateForLoginFindServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForSAMLLoginFindServices(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final FindServicesForSAMLLogin<L> findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
