package r01f.client.api.sub.security.login.webauthn;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForAuthenticatorCredentialCRUDServices< C extends AuthenticatorCredential>
	 extends ClientAPIDelegateForModelObjectCRUDServices<CredentialOID,C> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthenticatorCredentialCRUDServices(final Provider<SecurityContext>  securityContextProvider,
														 	 final Marshaller marshaller,
														 	 final CRUDServicesForAuthenticatorCredential<C> servicecrudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  servicecrudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
