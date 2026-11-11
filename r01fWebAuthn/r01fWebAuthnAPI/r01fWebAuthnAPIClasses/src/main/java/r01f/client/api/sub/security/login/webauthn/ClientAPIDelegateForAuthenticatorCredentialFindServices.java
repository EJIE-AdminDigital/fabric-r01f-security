package r01f.client.api.sub.security.login.webauthn;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.webauthn.FindServicesForAuthenticatorCredential;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;

public class ClientAPIDelegateForAuthenticatorCredentialFindServices< C extends AuthenticatorCredential>
	 extends ClientAPIDelegateForModelObjectFindServices<CredentialOID,C> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForAuthenticatorCredentialFindServices(final Provider<SecurityContext>  securityContextProvider,
														 	 final Marshaller marshaller,
														 	 final FindServicesForAuthenticatorCredential<C> servicecrudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  servicecrudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////

}
