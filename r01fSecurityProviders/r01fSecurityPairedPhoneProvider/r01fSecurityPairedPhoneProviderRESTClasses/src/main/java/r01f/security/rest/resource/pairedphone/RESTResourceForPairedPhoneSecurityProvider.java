package r01f.security.rest.resource.pairedphone;

import jakarta.inject.Provider;
import jakarta.ws.rs.Path;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.PairedPhoneLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.objectstreamer.Marshaller;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.pairedphone.PairedPhoneCallBackOnLoginHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginConsentHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginEntryPointHandler;
import r01f.security.handler.pairedphone.PairedPhoneSecurityPrincipalPrincipalFetchHandler;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;

/*
**
* The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
* the section about the methods (GET, PUT, POST, DELETE...)
*
* Log: see web.xml
*/
@Path("api/pairedphone/users")
@Accessors(prefix="_")
public class RESTResourceForPairedPhoneSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTResourceForSecurityServicesBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  final AuthenticationProviderSecretConfig authnProviderSecretConfig,
													  // marshaller
													  final Marshaller marshaller,
													  // used to create delegated services
													  final Class<P> principalType,
													  // services
													  final CRUDServicesForPrincipal<P> principalCrud,
													  final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
													  final PairedPhoneLoginServices pairedPhoneLoginServices) {
		this(sysSecurityContextProvider,
			 authnProviderSecretConfig,
			 marshaller,
			 // handlers
			 new PairedPhoneLoginEntryPointHandler(authnProviderSecretConfig),
			 new PairedPhoneCallBackOnLoginHandler(authnProviderSecretConfig),
			 new PairedPhoneSecurityPrincipalPrincipalFetchHandler<>(principalType,
					 												 authnProviderSecretConfig,
					  								  				 sysSecurityContextProvider,
					  								  				 principalCrud,
					  								  				 loginCrud),
			  new PairedPhoneLoginConsentHandler(sysSecurityContextProvider,
					  					         pairedPhoneLoginServices),
			  // services
			  loginEntryCrud);
	}
	public RESTResourceForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  final AuthenticationProviderSecretConfig securityProviderSecretConfig,
													  // marshaller
													  final Marshaller marshaller,
													  // handlers
													  final PairedPhoneLoginEntryPointHandler loginEntryPointHandler,
									  				  final PairedPhoneCallBackOnLoginHandler callbackOnLoginHandler,
									  				  final PairedPhoneSecurityPrincipalPrincipalFetchHandler<P> fetchPrincipalDataHandler,
									  				  final PairedPhoneLoginConsentHandler pairedPhoneLoginHandler,
									  				  final CRUDServicesForLoginEntry loginEntryCrud) {
		super(new RESTDelegateForPairedPhoneSecurityProvider<>(sysSecurityContextProvider,
															   securityProviderSecretConfig,
															   marshaller,
															   // handlers
															   loginEntryPointHandler,
													  		   callbackOnLoginHandler,
													  		   fetchPrincipalDataHandler,
													  		   pairedPhoneLoginHandler,
													  		   // services
													  		   loginEntryCrud));
	}
	public <D extends RESTDelegateForPairedPhoneSecurityProvider<P>> RESTResourceForPairedPhoneSecurityProvider(final D restDelegateForUserPasswordSecurityProvider) {
		super(restDelegateForUserPasswordSecurityProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
/*
	@GET @Path("/consent/{oid}")
	public Response load(@PathParam("oid") final PairedPhoneLoginConsentOID oid) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
				   .load(oid);
	}
	@POST @Path("/consent/request")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public Response requestConsent(final PairedPhoneLoginConsentRequest request) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
				   .request(request);
	}
	@GET @Path("/consent/request")
	@Produces(MediaType.APPLICATION_JSON)
	public Response requestConsentByGet(@QueryParam("phone") final Phone phone) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
				   .request(phone);
	}
	@POST @Path("/consent/response")
	@Consumes(MediaType.APPLICATION_JSON) @Produces(MediaType.APPLICATION_JSON)
	public Response consentRequest(final PairedPhoneLoginConsentResponseFromPairedPhone response) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
							.response(response);
	}
	@GET @Path("/login/callback")
	public Response loginCallBack(@QueryParam("consentOid") final PairedPhoneLoginConsentOID oid, @QueryParam("to") final Url urlTo) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
				   .loginCallBack(_req,oid, urlTo);

	}
*/
}