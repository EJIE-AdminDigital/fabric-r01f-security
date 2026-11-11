package r01f.security.rest.resource.pairedphone;


import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.pairedphone.PairedPhoneCallBackOnLoginHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginConsentHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginEntryPointHandler;
import r01f.security.handler.pairedphone.PairedPhoneSecurityPrincipalPrincipalFetchHandler;
import r01f.security.rest.resource.RESTDelegateForLoginBase;




  class RESTDelegateForPairedPhoneSecurityProvider<P extends IsSecurityPrincipal>
extends RESTDelegateForLoginBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final PairedPhoneCallBackOnLoginHandler _callbackOnLoginHandler;
	private final PairedPhoneLoginConsentHandler _pairedPhoneConsentHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  // handlers
													  final AuthenticationProviderSecretConfig authnProviderSecretConfig,
									  				  // marshaller
									  				  final Marshaller marshaller,
									  				  // handlers
													  final PairedPhoneLoginEntryPointHandler loginEntryPointHandler,
									  				  final PairedPhoneCallBackOnLoginHandler callbackOnLoginHandler,
									  				  final PairedPhoneSecurityPrincipalPrincipalFetchHandler<P> fetchPrincipalDataHandler,
									  				  final PairedPhoneLoginConsentHandler pairedPhoneLoginHandler,
										  			  // used to create [login entries] when the user logs-in
									  				  final CRUDServicesForLoginEntry loginEntryCrud) {
		super(IdentityProvider.FABRIC,AuthenticationMethod.PAIRED_PHONE,
			  sysSecurityContextProvider,
			  authnProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchPrincipalDataHandler,
			  marshaller,
			  loginEntryCrud);
		_callbackOnLoginHandler = callbackOnLoginHandler;
		_pairedPhoneConsentHandler = pairedPhoneLoginHandler;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
/*
	public Response loginCallBack(final HttpServletRequest req,
								  final PairedPhoneLoginConsentOID oid,
			                      final Url urlTo) {
		// [1] -  Get the login consent
		PairedPhoneLoginConsentRequest req = _pairedPhoneConsentHandler.load(oid);

		// [2] - Callback
		return _callbackOnLoginHandler.loginCallBack(req,
											         urlTo);
	}
	public Response load(final PairedPhoneLoginConsentOID oid) {
		C consent = _pairedPhoneConsentHandler.load(oid);
		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
					   	.build();

	}
	public Response request(final PairedPhoneLoginConsentRequest request) {
		// [1] - Request consent to phone.
		C consent = _pairedPhoneConsentHandler.request(request);
		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
					   .build();

	}
	public Response request(final Phone phone) {
		// [1] - Request consent to phone.
		C consent = _pairedPhoneConsentHandler.request(phone);

		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
			.build();

	}
	public Response response(final PairedPhoneLoginConsentResponseFromPairedPhone response) {
		// [1] - Receive Response
		C consent = _pairedPhoneConsentHandler.response(response);

		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
				       .build();
	}
*/
}
