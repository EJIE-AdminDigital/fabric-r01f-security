package r01f.security.rest.resource.google;

import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.google.GoogleCallBackOnLoginHandler;
import r01f.security.handler.google.GoogleLoginEntryPointHandler;
import r01f.security.handler.google.GoogleSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/google/users")
@Accessors(prefix="_")
public class RESTResourceForGoogleSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForGoogleSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final AuthenticationProviderSecretConfig authnProviderSecretConfig,
												 final GoogleLoginEntryPointHandler loginEntryPointHandler,
									  			 final GoogleCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GoogleSecurityPrincipalDataFetchHandler<P> fetchPrincipalDataHandler,
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final CRUDServicesForLoginEntry loginEntryCrud) {
		super(new RESTDelegateForGoogleSecurityProvider<>(sysSecurityContextProvider,
														  authnProviderSecretConfig,
														  loginEntryPointHandler,
														  callbackOnLoginHandler,
														  fetchPrincipalDataHandler,
														  marshaller,
														  loginEntryCrud));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Redirected after a successful google login
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/login/oauth2callback")
	public Response loginCallBack(@QueryParam("code") final String code,
		                          @QueryParam("state") final String state,
		                          @Context final HttpServletRequest httpRequest) {

		return this.getDelegateAs(RESTDelegateForGoogleSecurityProvider.class)
				   .loginCallBack(code,state);
	}
}