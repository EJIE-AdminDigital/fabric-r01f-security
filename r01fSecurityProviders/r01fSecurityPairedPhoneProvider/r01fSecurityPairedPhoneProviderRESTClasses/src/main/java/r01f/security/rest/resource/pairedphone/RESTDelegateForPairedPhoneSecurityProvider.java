package r01f.security.rest.resource.pairedphone;

import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.core.Response;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.pairedphone.PairedPhoneCallBackOnLoginHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginConsentHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginEntryPointHandler;
import r01f.security.handler.pairedphone.PairedPhoneUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;
import r01f.types.url.Url;




  class RESTDelegateForPairedPhoneSecurityProvider<U extends User,L extends PairedPhoneLogin,
  												   E extends UserLoginEntry,
  												   C extends PairedPhoneLoginConsent>
extends RESTDelegateForLoginBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final PairedPhoneCallBackOnLoginHandler<C> _callbackOnLoginHandler;
	private final PairedPhoneLoginConsentHandler<C> _userLoginConsentHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  // handlers
													  final SecurityProviderSecretConfig securityProviderSecretConfig,
													  final PairedPhoneLoginEntryPointHandler loginEntryPointHandler,
									  				  final PairedPhoneCallBackOnLoginHandler<C> callbackOnLoginHandler,
									  				  final PairedPhoneUserDataFetchHandler<U,L> fetchUserDataHandler,
									  				  final PairedPhoneLoginConsentHandler<C> userPasswordLoginHandler,
									  				  // marshaller
									  				  final Marshaller marshaller,
										  			  // used to create [login entries] when the user logs-in
										  			  final Factory<E> loginEntryFactory,
									  				  final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(SecurityProviderID.PAIRED_PHONE,
			  sysSecurityContextProvider,
			  securityProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchUserDataHandler,
			  marshaller,
			  loginEntryFactory,loginEntryCrud);
		_callbackOnLoginHandler = callbackOnLoginHandler;
		_userLoginConsentHandler = userPasswordLoginHandler;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Response loginCallBack(final HttpServletRequest req,
								  final PairedPhoneLoginConsentOID oid,
			                      final Url urlTo) {
		// [1] -  Get the login consent
		C consent = _userLoginConsentHandler.load(oid);

		// [2] - Callback
		return _callbackOnLoginHandler.loginCallBack(consent,
											         urlTo);
	}
	public Response load(final PairedPhoneLoginConsentOID oid) {
		C consent = _userLoginConsentHandler.load(oid);
		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
			.build();

	}
	public Response request(final PairedPhoneLoginConsentRequest request) {
		// [1] - Request consent to phone.
		C consent = _userLoginConsentHandler.request(request);
		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
					   .build();

	}
	public Response request(final Phone phone) {
		// [1] - Request consent to phone.
		C consent = _userLoginConsentHandler.request(phone);

		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
			.build();

	}
	public Response response(final PairedPhoneLoginConsentResponse response) {
		// [1] - Receive Response
		C consent = _userLoginConsentHandler.response(response);

		// [2] - Set response
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(consent))
				       .build();
	}
}
