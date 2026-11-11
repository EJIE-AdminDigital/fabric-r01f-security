package r01f.security.rest.resource.usrpwd;

import java.io.IOException;

import jakarta.inject.Provider;
import jakarta.ws.rs.FormParam;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.experimental.Accessors;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.Secret;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.usrpwd.UserPasswordCallBackOnLoginHandler;
import r01f.security.handler.usrpwd.UserPasswordLoginEntryPointHandler;
import r01f.security.handler.usrpwd.UserPasswordSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.types.url.Url;

/**
 * The implementation must follow the HTTP 1.1 spec (http://www.w3.org/Protocols/rfc2616/rfc2616.html), specifically
 * the section about the methods (GET, PUT, POST, DELETE...)
 *
 * Log: see web.xml
 */
@Path("api/usrpwd/users")
@Accessors(prefix="_")
public class RESTResourceForUserPasswordSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTResourceForSecurityServicesBase {

	protected AuthenticationProviderSecretConfig _securityProviderSecretConfig;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR /
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
													   final Marshaller marshaller,
													   // used to create delegates
													   final Class<P> principalType,
													   // config
													   final AuthenticationProviderSecretConfig config,
													   // services
													   final CRUDServicesForPrincipal<P> principalCrud,
													   final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
													   final UserPasswordLoginServices userPasswordLoginServices) {
		this(sysSecurityContextProvider,
			 securityProviderSecretConfig,
			 marshaller,
			 new UserPasswordLoginEntryPointHandler(config),
			 new UserPasswordCallBackOnLoginHandler(config),
			 new UserPasswordSecurityPrincipalDataFetchHandler<>(principalType,
					  											 sysSecurityContextProvider,
					  											 principalCrud,loginCrud,
					  											 config),
			 userPasswordLoginServices,
			 loginCrud,
			 loginEntryCrud);
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
	public RESTResourceForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
													   final Marshaller marshaller,
													   // entry point handlers
													   final UserPasswordLoginEntryPointHandler loginEntryPointHandler,
									  				   final UserPasswordCallBackOnLoginHandler callbackOnLoginHandler,
									  				   final UserPasswordSecurityPrincipalDataFetchHandler<P> fetchUserDataHandler,
									  				   final UserPasswordLoginServices loginServices,
									  				   final CRUDServicesForLogin loginCrud,
									  				   // used to create [login entries] when the user logs-in
									  				   final CRUDServicesForLoginEntry loginEntryCrud) {
		this(new RESTDelegateForUserPasswordSecurityProvider<>(sysSecurityContextProvider,
															   securityProviderSecretConfig,
															   marshaller,
															   loginEntryPointHandler,
												  			   callbackOnLoginHandler,
												  			   fetchUserDataHandler,
												  			   loginServices,
												  			   loginCrud,
												  			   loginEntryCrud));
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
	public <D extends RESTDelegateForUserPasswordSecurityProvider<P>> RESTResourceForUserPasswordSecurityProvider(final D restDelegateForUserPasswordSecurityProvider) {
		super(restDelegateForUserPasswordSecurityProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LOGIN METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Path("/login")
	public Response login(@FormParam("userCode") final LoginID loginId,
			              @FormParam("pass")     final Password pass,
			              @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .login(loginId,pass,
						  url);
	}
	@POST
	@Path("/login/usingOTPBySMS")
	public Response loginUsingOTPBySMS(@FormParam("userCode") final LoginID loginId,
			                           @FormParam("pass")     final Password pass,
			                           @FormParam("phone")    final Phone phone,
			                           @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .loginUsingOTPBySMS(loginId,pass,
						   			   phone,
						   			   url);
	}
	@POST
	@Path("/login/usingOTPByEMail")
	public Response loginUsingOTPByEMail(@FormParam("userCode") final LoginID loginId,
			                             @FormParam("pass")     final Password pass,
			                             @FormParam("email")    final EMail email,
			                             @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .loginUsingOTPByEMail(loginId,pass,
						   				 email,
						   				 url);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LOGIN AUTH FACTOR VALIDATION
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Path("/login/validate/authfactor/usingOTPBySMS")
	public Response validateAuthFactorOTPBySMS(@FormParam("userCode") final LoginID loginId,
											   @FormParam("task")     final OTPTaskOID taskOid,
											   @FormParam("secret")   final Secret secret,
											   @FormParam("phone")    final Phone phone,
											   @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .validateAuthFactorOTPBySMS(taskOid,
						   					   loginId,secret,
						   					   phone,
						   					   url);
	}

	@POST
	@Path("/login/validate/authfactor/usingOTPByEMail")
	public Response validateAuthFactorOTPByEMail(@FormParam("userCode") final LoginID loginId,
												 @FormParam("task")     final OTPTaskOID taskOid,
												 @FormParam("secret")   final Secret secret,
												 @FormParam("email")    final EMail email,
												 @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .validateAuthFactorOTPByEMail(taskOid,
						   						 loginId,secret,
						   						 email,
						   						 url);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LOGIN PASSWORD RECOVERY
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/password/recovery")
	public Response recoveryPassword(@QueryParam("id")       final SecurityPrincipalOID loginId,
									 @QueryParam("token")    final SecurityToken token,
									 @QueryParam("to") 		 final Url url) throws IOException {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .passwordReset(loginId,token,
						          url);
	}
}