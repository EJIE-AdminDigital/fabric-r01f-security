package r01f.security.rest.resource.saml;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.saml.SAMLCallBackOnLoginHandler;
import r01f.security.handler.saml.SAMLLoginEntryPointHandler;
import r01f.security.handler.saml.SAMLSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.types.url.Url;


/**
 * SAML base login
 * see {@link SAMLAPI} for details
 */
@Slf4j
  class RESTDelegateForSAMLSecurityProvider<P extends IsSecurityPrincipal>
extends RESTDelegateForLoginBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLCallBackOnLoginHandler _callbackOnLoginHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR 
/////////////////////////////////////////////////////////////////////////////////////////	
	public RESTDelegateForSAMLSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
											   final IdentityProvider identityProvider,
											   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
											   // handlers
											   final SAMLLoginEntryPointHandler loginEntryPointHandler,
									  		   final SAMLCallBackOnLoginHandler callbackOnLoginHandler,
									  		   final SAMLSecurityPrincipalDataFetchHandler<P> fetchPrincipalDataHandler,
									  		   // marshaller
									  		   final Marshaller marshaller,
									  		   // used to create [login entries] when the user logs-in
									  		   final CRUDServicesForLoginEntry loginEntryCrud) {
		super(identityProvider,AuthenticationMethod.SAML,
			  sysSecurityContextProvider,
			  securityProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchPrincipalDataHandler,
			  marshaller,
			  loginEntryCrud);
		_callbackOnLoginHandler = callbackOnLoginHandler;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Response loginCallBack(final IdentityProvider identityProvider,final SAMLIdPBinding idpBinding,
								  final String samlResponse,
								  final Url afterLoginRedirToUrl) {
		log.debug("[SAML ACS (assertion consumer service)] > Receiving [IdP]={} response ({}) =======================================",
				  _identityProvider,idpBinding);
		return _callbackOnLoginHandler.loginCallBack(_identityProvider,idpBinding,
											  		 samlResponse,
											  		 afterLoginRedirToUrl);
	}
}
