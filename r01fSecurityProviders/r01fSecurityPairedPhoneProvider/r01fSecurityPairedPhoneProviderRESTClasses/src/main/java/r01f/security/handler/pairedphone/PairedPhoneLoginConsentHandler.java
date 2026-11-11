package r01f.security.handler.pairedphone;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.model.security.context.SecurityContext;


public class PairedPhoneLoginConsentHandler {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Provider<SecurityContext> _securityContextProvider;
	private final PairedPhoneLoginServices _pairePhoneLoginServices;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginConsentHandler(final Provider<SecurityContext> securityContextProvider,
									      final PairedPhoneLoginServices pairePhoneLoginServices) {
		_securityContextProvider = securityContextProvider;
		_pairePhoneLoginServices = pairePhoneLoginServices;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
/*
	public PairedPhoneLoginConsentRequest login(final LoginID login,final Secret secret) {
		return this.login(Phone.of(login.getId()),secret);
	}
	public PairedPhoneLoginConsentRequest login(final Phone phone,final Secret secret) {
		// [1]  Build call consent
		if (!phone.isValid()) throw new AuthenticationProviderException(" the login id parameter {} must be a phone number" + phone);
		PairedPhoneLoginConsentRequest request = new PairedPhoneLoginConsentRequest(PairedPhoneLoginConsentOID.supply(),phone);
		// 2. Call Consent
		IsSecurityOperationResult<PairedPhoneLoginConsentRequest> loginConsentCallResult = _pairePhoneLoginServices.request(_securityContextProvider.get(),
																									 						request);
		if (loginConsentCallResult.hasFailed()) throw new AuthenticationProviderException("Error while sending consent rquesr: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		return loginConsentCallResult.getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOAD
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentRequest load(final PairedPhoneLoginConsentOID oid) {
		// Load Consent
		CRUDResult<C> loginConsentCallResult = _pairePhoneLoginContentCRUDServices.load(_securityContextProvider.get(),
																						oid);
		if (loginConsentCallResult.hasFailed()) {
			throw new AuthenticationProviderException("Error while sending consent request: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		};
		return loginConsentCallResult.getOrThrow();
	}

	public C request(final PairedPhoneLoginConsentRequest request ) {
		if (request.getPhone() == null
		 || !request.getPhone().isValid() ) {
			throw new AuthenticationProviderException(" request must provide a valid phone, and requested {} is null or not valid " + request.getPhone() );
		}
		if (request.getRequestedAt() == null) {
			request.setRequestedAt(Instant.now());
		}
		if (request.getLoginConsentOid() == null) {
			request.setLoginConsentOid(PairedPhoneLoginConsentOID.supply());
		}
		// 2. Call Consent
		IsSecurityOperationResult<C>  loginConsentCallResult = _pairePhoneLoginServices.request(_securityContextProvider.get(),
																								request);
		if (loginConsentCallResult.hasFailed()) throw new AuthenticationProviderException("Error while sending consent request: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		return loginConsentCallResult.getOrThrow();
	}
	public C request(final Phone phone ) {
		return request(PairedPhoneLoginConsentRequest.from(phone));
	}
	public C response(final PairedPhoneLoginConsentResponseFromPairedPhone  response ) {
		IsSecurityOperationResult<C>  loginConsentCallResult = _pairePhoneLoginServices.response(_securityContextProvider.get(), 
																									   response);
		if (loginConsentCallResult.hasFailed()) throw new AuthenticationProviderException("Error while setting consent response: " + loginConsentCallResult.asCOREServiceMethodExecError().getDetailedMessage());
		return loginConsentCallResult.getOrThrow();
	}
*/
}
