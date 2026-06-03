package r01f.security.rest.resource.pairedphone;

import jakarta.inject.Provider;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLoginConsent;
import r01f.api.interfaces.security.login.pairedphone.PairedPhoneLoginConsentServices;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsent;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentRequest;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentResponse;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.patterns.FactoryFrom;
import r01f.security.handler.pairedphone.PairedPhoneCallBackOnLoginHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginConsentHandler;
import r01f.security.handler.pairedphone.PairedPhoneLoginEntryPointHandler;
import r01f.security.handler.pairedphone.PairedPhoneUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;
import r01f.types.url.Url;

/*
**
* The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
* the section about the methods (GET, PUT, POST, DELETE...)
*
* Log: see web.xml
*/
@Path("api/pairedphone/users")
@Accessors(prefix="_")
public class RESTResourceForPairedPhoneSecurityProvider<U extends User,L extends PairedPhoneLogin,
														E extends UserLoginEntry,
														C extends PairedPhoneLoginConsent>
	 extends RESTResourceForSecurityServicesBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  final SecurityProviderSecretConfig securityProviderSecretConfig,
													  // used to create [login entries] when the user logs-in
													  final Factory<E> loginEntryFactory,
													  final CRUDServicesForUserLoginEntry<E> loginEntryCrud,
													  // used to create delegated services
													  final Class<U> userType,final Class<L> loginType,
													  final CRUDServicesForUser<U> userCrud,final CRUDServicesForPairedPhoneLogin<L> loginCrud,
													  final CRUDServicesForPairedPhoneLoginConsent<C> consentServices,
													  final PairedPhoneLoginConsentServices<C> loginServices,
													  final Marshaller marshaller,
													  final FactoryFrom<U,L> loginFactoryFromUser) {
		this(sysSecurityContextProvider,
			 securityProviderSecretConfig,
			 new PairedPhoneLoginEntryPointHandler(securityProviderSecretConfig),
			 new PairedPhoneCallBackOnLoginHandler<>(securityProviderSecretConfig),
			 new PairedPhoneUserDataFetchHandler<>(userType,loginType,
					  								  sysSecurityContextProvider,
					  								  userCrud,loginCrud,
					  								  securityProviderSecretConfig,
					  								  loginFactoryFromUser),
			  new PairedPhoneLoginConsentHandler<>(sysSecurityContextProvider,
					                                consentServices,
					  					            loginServices),
			  marshaller,
			  loginEntryFactory,loginEntryCrud);
	}


	public RESTResourceForPairedPhoneSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													  final SecurityProviderSecretConfig securityProviderSecretConfig,
													  final PairedPhoneLoginEntryPointHandler loginEntryPointHandler,
									  				  final PairedPhoneCallBackOnLoginHandler<C> callbackOnLoginHandler,
									  				  final PairedPhoneUserDataFetchHandler<U,L> fetchUserDataHandler,
									  				  final PairedPhoneLoginConsentHandler<C> userPasswordLoginHandler,
									  				  final Marshaller marshaller,
									  				  // used to create [login entries] when the user logs-in
									  				  final Factory<E> loginEntryFactory,
									  				  final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(new RESTDelegateForPairedPhoneSecurityProvider<>(sysSecurityContextProvider,
															   securityProviderSecretConfig,
															   loginEntryPointHandler,
													  		   callbackOnLoginHandler,
													  		   fetchUserDataHandler,
													  		   userPasswordLoginHandler,
													  		    marshaller,
													  		   loginEntryFactory,loginEntryCrud));
	}
	public <D extends RESTDelegateForPairedPhoneSecurityProvider<U,L,E,C> > RESTResourceForPairedPhoneSecurityProvider(final D restDelegateForUserPasswordSecurityProvider) {
		super(restDelegateForUserPasswordSecurityProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
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
	public Response consentRequest(final PairedPhoneLoginConsentResponse response) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
							.response(response);
	}
	@GET @Path("/login/callback")
	public Response loginCallBack(@QueryParam("consentOid") final PairedPhoneLoginConsentOID oid, @QueryParam("to") final Url urlTo) {
		return this.getDelegateAs(RESTDelegateForPairedPhoneSecurityProvider.class)
				   .loginCallBack(_req,oid, urlTo);

	}
}