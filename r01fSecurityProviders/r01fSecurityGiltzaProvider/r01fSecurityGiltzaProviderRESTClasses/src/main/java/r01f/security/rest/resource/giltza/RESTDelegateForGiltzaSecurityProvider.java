package r01f.security.rest.resource.giltza;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.giltza.GiltzaCallBackOnLoginHandler;
import r01f.security.handler.giltza.GiltzaLoginEntryPointHandler;
import r01f.security.handler.giltza.GiltzaUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;


/**
 * Google base login: https://developers.google.com/actions/identity/oauth2-code-flow
 */
  class RESTDelegateForGiltzaSecurityProvider<U extends User,L extends GiltzaLogin,
  											  E extends UserLoginEntry>
extends RESTDelegateForLoginBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final GiltzaCallBackOnLoginHandler _callbackOnLoginHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForGiltzaSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final SecurityProviderSecretConfig securityProviderSecretConfig,
												 // handlers
												 final GiltzaLoginEntryPointHandler loginEntryPointHandler,
									  			 final GiltzaCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GiltzaUserDataFetchHandler<U,L> fetchUserDataHandler,
									  			 // marshaller
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final Factory<E> loginEntryFactory,
									  			 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(SecurityProviderID.GILTZA,
			  sysSecurityContextProvider,
			  securityProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchUserDataHandler,
			  marshaller,
			  loginEntryFactory,loginEntryCrud);
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
