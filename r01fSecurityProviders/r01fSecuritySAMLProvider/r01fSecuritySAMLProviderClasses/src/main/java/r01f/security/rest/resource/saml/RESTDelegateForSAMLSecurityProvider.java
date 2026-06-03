package r01f.security.rest.resource.saml;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.saml.SAMLCallBackOnLoginHandler;
import r01f.security.handler.saml.SAMLLoginEntryPointHandler;
import r01f.security.handler.saml.SAMLUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLIDs.SAMLIdPID;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.url.Url;


/**
 * SAML base login
 * see {@link SAMLAPI} for details
 */
@Slf4j
  class RESTDelegateForSAMLSecurityProvider<U extends User,L extends SAMLLogin,
  											E extends UserLoginEntry>
extends RESTDelegateForLoginBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLCallBackOnLoginHandler _callbackOnLoginHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR 
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForSAMLSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
											   final SecurityProviderSecretConfig securityProviderSecretConfig,
											   // handlers
											   final SAMLLoginEntryPointHandler loginEntryPointHandler,
									  		   final SAMLCallBackOnLoginHandler callbackOnLoginHandler,
									  		   final SAMLUserDataFetchHandler<U,L> fetchUserDataHandler,
									  		   // marshaller
									  		   final Marshaller marshaller,
									  		   // used to create [login entries] when the user logs-in
									  		   final Factory<E> loginEntryFactory,
									  		   final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(SecurityProviderID.SAML,
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
	public Response loginCallBack(final SAMLIdPID idp,final SAMLIdPBinding idpBinding,
								  final String samlResponse,
								  final Url afterLoginRedirToUrl) {
		log.debug("[SAML ACS (assertion consumer service)] > Receiving [IdP]={} response ({}) =======================================",idp,idpBinding);
		return _callbackOnLoginHandler.loginCallBack(idp,idpBinding,
											  		 samlResponse,
											  		 afterLoginRedirToUrl);
	}
}
