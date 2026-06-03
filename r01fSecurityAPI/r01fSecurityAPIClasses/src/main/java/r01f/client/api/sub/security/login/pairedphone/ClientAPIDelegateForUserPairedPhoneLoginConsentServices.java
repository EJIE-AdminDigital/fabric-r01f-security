package r01f.client.api.sub.security.login.pairedphone;

import jakarta.inject.Provider;

import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;

public class ClientAPIDelegateForUserPairedPhoneLoginConsentServices<C extends PairedPhoneLoginConsent>
	 extends ClientAPIServiceDelegateBase<PairedPhoneLoginConsentServices<C>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPairedPhoneLoginConsentServices(final Provider<SecurityContext>  securityContextProvider,
														 	       final Marshaller marshaller,
														 	       final PairedPhoneLoginConsentServices<C> crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Requests a consent to a paired phone
	 * @param phone
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public C request(final PairedPhoneLoginConsentRequest req) {
		SecurityOperationResult<C> opResult = this.getServiceProxyAs(PairedPhoneLoginConsentServices.class)
													.request(this.getSecurityContext() ,req);

		C outEntity = opResult.getOrThrow();
		return outEntity;
	}

	/**
	 * Response to a consent from paired phone
	 * @param phone
	 * @param password
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public C response(final PairedPhoneLoginConsentResponse req) {
		SecurityOperationResult<C> opResult = this.getServiceProxyAs(PairedPhoneLoginConsentServices.class)
													.response(this.getSecurityContext() ,req);

		C outEntity = opResult.getOrThrow();
		return outEntity;
	}



	/**
	 * Loads the related paired phone login consent.
	 * @param consentOid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public C loadConsent(final PairedPhoneLoginConsentOID consentOid) {

		SecurityOperationResult<C> opResult = this.getServiceProxyAs(PairedPhoneLoginConsentServices.class)
													.loadConsent(this.getSecurityContext() ,consentOid);

		C outEntity = opResult.getOrThrow();
		return outEntity;
	}
}
