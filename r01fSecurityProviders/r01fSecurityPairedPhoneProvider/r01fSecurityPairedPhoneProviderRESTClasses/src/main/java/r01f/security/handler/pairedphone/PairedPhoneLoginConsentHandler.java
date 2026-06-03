package r01f.security.handler.pairedphone;

import java.time.Instant;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.security.provider.SecurityProviderException;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;


public class PairedPhoneLoginConsentHandler<C extends  PairedPhoneLoginConsent> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Provider<SecurityContext>                  _securityContextProvider;
	private final PairedPhoneLoginConsentServices<C>         _pairePhoneLoginContentServices;
	private final CRUDServicesForPairedPhoneLoginConsent<C>  _pairePhoneLoginContentCRUDServices;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginConsentHandler(final Provider<SecurityContext> securityContextProvider,
										  final CRUDServicesForPairedPhoneLoginConsent<C> pairePhoneLoginContentCRUDServices,
									      final PairedPhoneLoginConsentServices<C> pairePhoneLoginContentServices) {
		_securityContextProvider =  securityContextProvider;
		_pairePhoneLoginContentServices = pairePhoneLoginContentServices;
		_pairePhoneLoginContentCRUDServices = pairePhoneLoginContentCRUDServices;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public C login(final LoginID login,final Password password) {
		//1. Build call consent
		Phone phone = Phone.of(login.getId());
		if (! phone.isValid() ) {
			throw new SecurityProviderException(" the login id parameter {} must be a phone number" + login);
		}
		PairedPhoneLoginConsentRequest request = new PairedPhoneLoginConsentRequest(phone,
                                                                                    PairedPhoneLoginConsentOID.supply(),Instant.now());
		// 2. Call Consent
		SecurityOperationResult<C>  loginConsentCallResult = _pairePhoneLoginContentServices.request(_securityContextProvider.get(),
																									 request);
		if (loginConsentCallResult.hasFailed()) {
			throw new SecurityProviderException("Error while sending consent rquesr: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		};
		return loginConsentCallResult.getOrThrow();

	}
	public C load(final PairedPhoneLoginConsentOID oid) {
		// Load Consent
		CRUDResult<C>  loginConsentCallResult = _pairePhoneLoginContentCRUDServices.load(_securityContextProvider.get(),
																						 oid);
		if (loginConsentCallResult.hasFailed()) {
			throw new SecurityProviderException("Error while sending consent request: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		};
		return loginConsentCallResult.getOrThrow();
	}

	public C request(final PairedPhoneLoginConsentRequest request ) {
		if (request.getPhone() == null
		 || !request.getPhone().isValid() ) {
			throw new SecurityProviderException(" request must provide a valid phone, and requested {} is null or not valid " + request.getPhone() );
		}
		if (request.getRequestedAt() == null) {
			request.setRequestedAt(Instant.now());
		}
		if (request.getLoginConsentOid() == null) {
			request.setLoginConsentOid(PairedPhoneLoginConsentOID.supply());
		}
		// 2. Call Consent
		SecurityOperationResult<C>  loginConsentCallResult = _pairePhoneLoginContentServices.request(_securityContextProvider.get(),
																									 request);
		if (loginConsentCallResult.hasFailed()) throw new SecurityProviderException("Error while sending consent request: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		return loginConsentCallResult.getOrThrow();
	}
	public C request(final Phone phone ) {
		return request(PairedPhoneLoginConsentRequest.from(phone));
	}
	public C response(final PairedPhoneLoginConsentResponse  response ) {
		SecurityOperationResult<C>  loginConsentCallResult = _pairePhoneLoginContentServices.response(_securityContextProvider.get(), 
																									   response);
		if (loginConsentCallResult.hasFailed()) throw new SecurityProviderException("Error while setting consent response: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		return loginConsentCallResult.getOrThrow();
	}
}
