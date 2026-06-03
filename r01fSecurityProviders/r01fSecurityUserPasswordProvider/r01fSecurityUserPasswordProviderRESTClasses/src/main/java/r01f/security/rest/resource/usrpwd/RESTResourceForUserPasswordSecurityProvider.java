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
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.usrpwd.UserPasswordCallBackOnLoginHandler;
import r01f.security.handler.usrpwd.UserPasswordLoginEntryPointHandler;
import r01f.security.handler.usrpwd.UserPasswordUserDataFetchHandler;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTResourceForSecurityServicesBase;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
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
public class RESTResourceForUserPasswordSecurityProvider<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin,
														 E extends UserLoginEntry>
	 extends RESTResourceForSecurityServicesBase {

	protected SecurityProviderSecretConfig _securityProviderSecretConfig;
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR /
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTResourceForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final SecurityProviderSecretConfig securityProviderSecretConfig,
													   // used to create delegates
													   final Class<U> userType,final Class<L> loginType,
													   final SecurityProviderSecretConfig config,
													   final CRUDServicesForUser<U> userCrud,final CRUDServicesForUserPasswordLogin<L> loginCrud,
													   final UserPasswordLoginServices<U,L> loginServices,
													   final Marshaller marshaller,
													   // used to create [login entries] when the user logs-in
													   final Factory<E> loginEntryFactory,
													   final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(sysSecurityContextProvider,
			 securityProviderSecretConfig,
			 new UserPasswordLoginEntryPointHandler(config),
			 new UserPasswordCallBackOnLoginHandler(config),
			 new UserPasswordUserDataFetchHandler<>(userType,loginType,
					  								sysSecurityContextProvider,
					  								userCrud,loginCrud,
					  								config),
			 loginServices,
			 marshaller,
			 loginEntryFactory,loginEntryCrud);

		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
	public RESTResourceForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final SecurityProviderSecretConfig securityProviderSecretConfig,
													   final UserPasswordLoginEntryPointHandler loginEntryPointHandler,
									  				   final UserPasswordCallBackOnLoginHandler callbackOnLoginHandler,
									  				   final UserPasswordUserDataFetchHandler<U,L> fetchUserDataHandler,
									  				   final UserPasswordLoginServices<U,L> loginServices,
									  				   final Marshaller marshaller,
									  				   // used to create [login entries] when the user logs-in
									  				   final Factory<E> loginEntryFactory,
									  				   final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		this(new RESTDelegateForUserPasswordSecurityProvider<>(sysSecurityContextProvider,
															   securityProviderSecretConfig,
															   loginEntryPointHandler,
												  			   callbackOnLoginHandler,
												  			   fetchUserDataHandler,
												  			   loginServices,
												  			   marshaller,
												  			   loginEntryFactory,loginEntryCrud));
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
	public <D extends RESTDelegateForUserPasswordSecurityProvider<U,L,E>> RESTResourceForUserPasswordSecurityProvider(final D restDelegateForUserPasswordSecurityProvider) {
		super(restDelegateForUserPasswordSecurityProvider);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	LOGIN METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Path("/login")
	public Response login(@FormParam("userCode") final LoginID userCode,
			              @FormParam("pass")     final Password pass,
			              @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .login(userCode,pass,url);
	}

	@POST
	@Path("/login/usingOTPBySMS")
	public Response loginUsingOTPBySMS(@FormParam("userCode") final LoginID userCode,
			                           @FormParam("pass")     final Password pass,
			                           @FormParam("phone")    final Phone phone,
			                           @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .loginUsingOTPBySMS(userCode,pass,phone,url);
	}

	@POST
	@Path("/login/usingOTPByEMail")
	public Response loginUsingOTPByEMail(@FormParam("userCode") final LoginID userCode,
			                             @FormParam("pass")     final Password pass,
			                             @FormParam("email")    final EMail email,
			                             @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .loginUsingOTPByEMail(userCode,pass,email,url);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// LOGIN AUTH FACTOR VALIDATION
/////////////////////////////////////////////////////////////////////////////////////////
	@POST
	@Path("/login/validate/authfactor/usingOTPBySMS")
	public Response validateAuthFactorOTPBySMS(@FormParam("userCode") final LoginID userCode,
			                                   @FormParam("secret")   final Password secret,
			                                   @FormParam("phone")    final Phone phone,
			                                   @FormParam("to")       final Url url) {
		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .validateAuthFactorOTPBySMS(userCode,secret,phone,url);
	}

	@POST
	@Path("/login/validate/authfactor/usingOTPByEMail")
	public Response validateAuthFactorOTPByEMail(@FormParam("userCode")   final LoginID userCode,
					                             @FormParam("secret")     final Password secret,
					                             @FormParam("email")      final EMail email,
					                             @FormParam("to")         final Url url) {

		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .validateAuthFactorOTPByEMail(userCode,secret,email,url);
	}

/////////////////////////////////////////////////////////////////////////////////////////
// LOGIN PASSWORD RECOVERY
/////////////////////////////////////////////////////////////////////////////////////////
	@GET
	@Path("/password/recovery")
	public Response recoveryPassword(@QueryParam("id")       final UserOID userOid,
									 @QueryParam("token")    final SecurityToken token,
									 @QueryParam("to") 		 final Url url) throws IOException {

		return this.getDelegateAs(RESTDelegateForUserPasswordSecurityProvider.class)
				   .passwordReset(userOid,token,
						          url);
	}
}