package r01f.security.rest.resource.saml;

import jakarta.inject.Provider;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.saml.SAMLCallBackOnLoginHandler;
import r01f.security.handler.saml.SAMLLoginEntryPointHandler;
import r01f.security.handler.saml.SAMLSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.types.url.Url;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/saml/users")
@Accessors(prefix="_")
public class RESTResourceForSAMLSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForSAMLSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
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
		super(new RESTDelegateForSAMLSecurityProvider<>(sysSecurityContextProvider,
														identityProvider,
													    securityProviderSecretConfig,
														loginEntryPointHandler,
														callbackOnLoginHandler,
														fetchPrincipalDataHandler,
														marshaller,
														loginEntryCrud));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Redirected after a successful saml login
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/login/callback/{idp}")
	public Response loginCallBackGET(@PathParam("idp") final IdentityProvider identityProvider,
									 @QueryParam(SAMLAPI.HTTP_RESP_SAML_PARAM) final String samlResponse,
									 @QueryParam(SAMLAPI.HTTP_RELAY_STATE_PARAM) final Url afterLoginRedirToUrl) {
		return this.getDelegateAs(RESTDelegateForSAMLSecurityProvider.class)
				   .loginCallBack(identityProvider,SAMLIdPBinding.REDIRECT,
						   		  samlResponse,
						   		  afterLoginRedirToUrl);
	}
	@POST
	@Path("/login/callback/{idp}")
	public Response loginCallBackPOST(@PathParam("idp") final IdentityProvider identityProvider,
									  @FormParam(SAMLAPI.HTTP_RESP_SAML_PARAM) final String samlResponse,
									  @FormParam(SAMLAPI.HTTP_RELAY_STATE_PARAM) final Url afterLoginRedirToUrl) {
		return this.getDelegateAs(RESTDelegateForSAMLSecurityProvider.class)
				   .loginCallBack(identityProvider,SAMLIdPBinding.POST,
								  samlResponse,
								  afterLoginRedirToUrl);
	}
}