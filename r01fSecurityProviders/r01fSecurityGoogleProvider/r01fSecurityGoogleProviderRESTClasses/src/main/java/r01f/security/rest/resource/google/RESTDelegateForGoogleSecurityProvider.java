package r01f.security.rest.resource.google;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.google.GoogleCallBackOnLoginHandler;
import r01f.security.handler.google.GoogleLoginEntryPointHandler;
import r01f.security.handler.google.GoogleSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTDelegateForLoginBase;


/**
 * Google base login: https://developers.google.com/actions/identity/oauth2-code-flow
 */
  class RESTDelegateForGoogleSecurityProvider<P extends IsSecurityPrincipal>
extends RESTDelegateForLoginBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final GoogleCallBackOnLoginHandler _callbackOnLoginHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForGoogleSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final AuthenticationProviderSecretConfig authProviderSecretConfig,
												 // handlers
												 final GoogleLoginEntryPointHandler loginEntryPointHandler,
									  			 final GoogleCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GoogleSecurityPrincipalDataFetchHandler<P> fetchPrincipalDataHandler,
									  			 // marshaller
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final CRUDServicesForLoginEntry loginEntryCrud) {
		super(IdentityProvider.GOOGLE,AuthenticationMethod.OAUTH_AUTH_CODE_FLOW,
			  sysSecurityContextProvider,
			  authProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchPrincipalDataHandler,
			  marshaller,
			  loginEntryCrud);
		_callbackOnLoginHandler = callbackOnLoginHandler;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Response loginCallBack(final String code,
								  final String state) {
		return _callbackOnLoginHandler.loginCallBack(code,
				 									 state);
	}
}
