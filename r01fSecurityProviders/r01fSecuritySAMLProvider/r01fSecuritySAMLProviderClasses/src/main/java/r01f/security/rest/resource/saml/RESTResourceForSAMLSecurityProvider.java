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
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLIDs.SAMLIdPID;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.securitycontext.SecurityContext;
import r01f.types.url.Url;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/saml/users")
@Accessors(prefix="_")
public class RESTResourceForSAMLSecurityProvider<U extends User,L extends SAMLLogin,
												 E extends UserLoginEntry>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForSAMLSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
											   final SecurityProviderSecretConfig securityProviderSecretConfig,
											   final SAMLLoginEntryPointHandler loginEntryPointHandler,
									  		   final SAMLCallBackOnLoginHandler callbackOnLoginHandler,
									  		   final SAMLUserDataFetchHandler<U,L> fetchUserDataHandler,
									  		   final Marshaller marshaller,
									  		   // used to create [login entries] when the user logs-in
									  		   final Factory<E> loginEntryFactory,
									  		   final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(new RESTDelegateForSAMLSecurityProvider<>(sysSecurityContextProvider,
													    securityProviderSecretConfig,
														loginEntryPointHandler,
														callbackOnLoginHandler,
														fetchUserDataHandler,
														marshaller,
														loginEntryFactory,loginEntryCrud));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Redirected after a successful saml login
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/login/callback/{idp}")
	public Response loginCallBackGET(@PathParam("idp") final SAMLIdPID idp,
									 @QueryParam(SAMLAPI.HTTP_RESP_SAML_PARAM) final String samlResponse,
									 @QueryParam(SAMLAPI.HTTP_RELAY_STATE_PARAM) final Url afterLoginRedirToUrl) {
		return this.getDelegateAs(RESTDelegateForSAMLSecurityProvider.class)
				   .loginCallBack(idp,SAMLIdPBinding.REDIRECT,
						   		  samlResponse,
						   		  afterLoginRedirToUrl);
	}
	@POST
	@Path("/login/callback/{idp}")
	public Response loginCallBackPOST(@PathParam("idp") final SAMLIdPID idp,
									  @FormParam(SAMLAPI.HTTP_RESP_SAML_PARAM) final String samlResponse,
									  @FormParam(SAMLAPI.HTTP_RELAY_STATE_PARAM) final Url afterLoginRedirToUrl) {
		return this.getDelegateAs(RESTDelegateForSAMLSecurityProvider.class)
				   .loginCallBack(idp,SAMLIdPBinding.POST,
								  samlResponse,
								  afterLoginRedirToUrl);
	}
}