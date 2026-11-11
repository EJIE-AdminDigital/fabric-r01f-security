package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginConsentResponseFromPairedPhone;
import r01f.model.security.login.pairedphone.messages.login.PairedPhoneLoginResponse;
import r01f.objectstreamer.Marshaller;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;
import r01f.types.contact.Phone;

public class ClientAPIDelegateForPairedPhoneLoginServices
     extends ClientAPIServiceDelegateBase<PairedPhoneLoginServices> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPairedPhoneLoginServices(final Provider<SecurityContext>  securityContextProvider,
												 		final Marshaller marshaller,
												 		final PairedPhoneLoginServices pairedPhoneServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  pairedPhoneServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginConsentRequest loadConsentRequest(final Phone phone) {
		return this.getServiceProxyAs(PairedPhoneLoginServices.class)
				   .loadConsentRequest(this.getSecurityContext(),
									   phone)
				   .getOrThrow();
	}
	public PairedPhoneLoginConsentRequest createConsentRequestFor(final Phone phone) {
		return this.getServiceProxyAs(PairedPhoneLoginServices.class)
				   .createConsentRequestFor(this.getSecurityContext(),
									  	    phone)
				   .getOrThrow();
	}
	public PairedPhoneLoginResponse receiveConsentResponseFromPairedPhone(final PairedPhoneLoginConsentResponseFromPairedPhone response) {
		return this.getServiceProxyAs(PairedPhoneLoginServices.class)
				   .receiveConsentResponseFromPairedPhone(this.getSecurityContext(),
									  	    			  response)
				   .getOrThrow();
	}
}
