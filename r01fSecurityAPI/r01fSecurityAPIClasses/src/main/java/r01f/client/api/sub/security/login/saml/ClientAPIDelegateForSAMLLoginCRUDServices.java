package r01f.client.api.sub.security.login.saml;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.saml.CRUDServicesForSAMLLogin;
import r01f.client.api.sub.security.login.ClientAPIDelegateForLoginCRUDServicesBase;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;

public class ClientAPIDelegateForSAMLLoginCRUDServices<L extends SAMLLogin>
	 extends ClientAPIDelegateForLoginCRUDServicesBase<L> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForSAMLLoginCRUDServices(final Provider<SecurityContext>  securityContextProvider,
													 final Marshaller marshaller,
													 final CRUDServicesForSAMLLogin<L> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
}
