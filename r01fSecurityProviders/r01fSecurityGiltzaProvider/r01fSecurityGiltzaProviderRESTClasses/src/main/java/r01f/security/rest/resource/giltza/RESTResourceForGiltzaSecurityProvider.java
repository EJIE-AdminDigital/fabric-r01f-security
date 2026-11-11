package r01f.security.rest.resource.giltza;

import jakarta.inject.Provider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.giltza.GiltzaCallBackOnLoginHandler;
import r01f.security.handler.giltza.GiltzaLoginEntryPointHandler;
import r01f.security.handler.giltza.GiltzaSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/giltza/users")
@Accessors(prefix="_")
public class RESTResourceForGiltzaSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForGiltzaSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final AuthenticationProviderSecretConfig authnProviderSecretConfig,
												 final GiltzaLoginEntryPointHandler loginEntryPointHandler,
									  			 final GiltzaCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GiltzaSecurityPrincipalDataFetchHandler<P> fetchPrincipalDataHandler,
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final CRUDServicesForLoginEntry loginEntryCrud) {
		super(new RESTDelegateForGiltzaSecurityProvider<>(sysSecurityContextProvider,
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
		                          @QueryParam("state") final String state) {
		return this.getDelegateAs(RESTDelegateForGiltzaSecurityProvider.class)
				   .loginCallBack(code,state);
	}
}