package r01f.security.rest.resource.google;

import jakarta.inject.Provider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.Response;

import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.google.GoogleLogin;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.google.GoogleCallBackOnLoginHandler;
import r01f.security.handler.google.GoogleLoginEntryPointHandler;
import r01f.security.handler.google.GoogleUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.securitycontext.SecurityContext;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/google/users")
@Accessors(prefix="_")
public class RESTResourceForGoogleSecurityProvider<U extends User,L extends GoogleLogin,
												   E extends UserLoginEntry>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForGoogleSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final SecurityProviderSecretConfig securityProviderSecretConfig,
												 final GoogleLoginEntryPointHandler loginEntryPointHandler,
									  			 final GoogleCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GoogleUserDataFetchHandler<U,L> fetchUserDataHandler,
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final Factory<E> loginEntryFactory,
									  			 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(new RESTDelegateForGoogleSecurityProvider<>(sysSecurityContextProvider,
														  securityProviderSecretConfig,
														  loginEntryPointHandler,
														  callbackOnLoginHandler,
														  fetchUserDataHandler,
														  marshaller,
														  loginEntryFactory,loginEntryCrud));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	Redirected after a successful google login
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/login/oauth2callback")
	public Response loginCallBack(@QueryParam("code") final String code,
		                          @QueryParam("state") final String state,
		                          @Context HttpServletRequest httpRequest) {

		return this.getDelegateAs(RESTDelegateForGoogleSecurityProvider.class)
				   .loginCallBack(code,state);
	}
}