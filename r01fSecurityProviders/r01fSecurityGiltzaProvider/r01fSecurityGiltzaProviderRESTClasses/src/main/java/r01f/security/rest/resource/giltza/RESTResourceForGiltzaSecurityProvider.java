package r01f.security.rest.resource.giltza;

import jakarta.inject.Provider;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

import lombok.experimental.Accessors;
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
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.securitycontext.SecurityContext;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/giltza/users")
@Accessors(prefix="_")
public class RESTResourceForGiltzaSecurityProvider<U extends User,L extends GiltzaLogin,
												   E extends UserLoginEntry>
	 extends RESTResourceForSecurityServicesBase {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForGiltzaSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
												 final SecurityProviderSecretConfig securityProviderSecretConfig,
												 final GiltzaLoginEntryPointHandler loginEntryPointHandler,
									  			 final GiltzaCallBackOnLoginHandler callbackOnLoginHandler,
									  			 final GiltzaUserDataFetchHandler<U,L> fetchUserDataHandler,
									  			 final Marshaller marshaller,
									  			 // used to create [login entries] when the user logs-in
									  			 final Factory<E> loginEntryFactory,
									  			 final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(new RESTDelegateForGiltzaSecurityProvider<U,L,E>(sysSecurityContextProvider,
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
		                          @QueryParam("state") final String state) {
		return this.getDelegateAs(RESTDelegateForGiltzaSecurityProvider.class)
				   .loginCallBack(code,state);
	}
}