package r01f.security.rest.resource.usrpwd;

import java.net.URI;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.authn.secondfactor.LoginAuthnSecondFactorUsingOTP;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationRequestUsingOTP;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginRequestBuilder;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordLoginPasswordResetResult;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.Secret;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.handler.AuthenticationProviderException;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.usrpwd.UserPasswordCallBackOnLoginHandler;
import r01f.security.handler.usrpwd.UserPasswordLoginEntryPointHandler;
import r01f.security.handler.usrpwd.UserPasswordSecurityPrincipalDataFetchHandler;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Slf4j
public class RESTDelegateForUserPasswordSecurityProvider<P extends IsSecurityPrincipal>
	 extends RESTDelegateForLoginBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserPasswordCallBackOnLoginHandler _callbackOnLoginHandler;
	private final UserPasswordLoginServices _loginServices;
	private final CRUDServicesForLogin _loginCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTDelegateForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
									  				   // marshaller
									  				   final Marshaller marshaller,
													   // handlers
													   final UserPasswordLoginEntryPointHandler loginEntryPointHandler,
									  				   final UserPasswordCallBackOnLoginHandler callbackOnLoginHandler,
									  				   final UserPasswordSecurityPrincipalDataFetchHandler<P> fetchPrincipalDataHandler,
									  				   // login services
									  				   final UserPasswordLoginServices loginServices,
									  				   final CRUDServicesForLogin loginCrud,
									  				   // used to create [login entries] when the user logs-in
									  				   final CRUDServicesForLoginEntry loginEntryCrud) {
		super(IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,
			  sysSecurityContextProvider,
			  securityProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchPrincipalDataHandler,
			  marshaller,
			  loginEntryCrud);
		_callbackOnLoginHandler = callbackOnLoginHandler;
		_loginServices = loginServices;
		_loginCrud = loginCrud;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public Response login(final LoginID loginId,final Password password,
						  final Url to) {
		// [1] - Use the user/password login login request.
		UserPasswordLoginRequest loginRequest = UserPasswordLoginRequestBuilder.using(loginId)
																			   .with(password)
																			   .build();
		// [2] - Do Login based on request
		return  _doLogin(loginRequest, to);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN METHODS WITH OTP ATUTHENTICATION FACTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public Response loginUsingOTPBySMS(final LoginID loginId, final Password password,
			                           final Phone phone,
			                           final Url to) {
		// [1] - Use the user/password login login request.
		UserPasswordLoginRequest loginRequest = UserPasswordLoginRequestBuilder.using(loginId)
																			   .with(password)
																			   .requireAuthenticationSecondFactor(LoginAuthnSecondFactorUsingOTP.forPhone(phone))
																			   .build();
	 	// [2] - Do Login based on request
		return  _doLogin(loginRequest,to);
	}
	public Response loginUsingOTPByEMail(final LoginID loginId, final Password password,
            							 final EMail email,
            							 final Url to) {
		// [1] - Use the user/password login login request.
		UserPasswordLoginRequest loginRequest = UserPasswordLoginRequestBuilder.using(loginId)
																			   .with(password)
																			   .requireAuthenticationSecondFactor(LoginAuthnSecondFactorUsingOTP.forEMail(email))
																			   .build();
		// [2] - Do Login based on request
		return  _doLogin(loginRequest,to);
	}
	public Response validateAuthFactorOTPBySMS(final OTPTaskOID taskOid,
											   final LoginID loginId,final Secret secret,
											   final Phone phone,
											   final Url to) {
		// [ ] Validate user known secret.
		return _doLoginSecondFactorValidation(new LoginAuthnSecondFactorValidationRequestUsingOTP(taskOid.asJobOID(),
																				   loginId,
																				   secret,
																				   phone),
								to);
	}

	public Response validateAuthFactorOTPByEMail(final OTPTaskOID taskOid,
												 final LoginID loginId,final Secret secret,
												 final EMail email,
												 final Url to) {
	 // [ ] Validate user known secret.
		return _doLoginSecondFactorValidation(new LoginAuthnSecondFactorValidationRequestUsingOTP(taskOid.asJobOID(),
																				   loginId,
																				   secret,
																				   email),
							    to);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Password recovery flow:
	 * 		1.- Create a [login entry] with id=PASSWORD_RECOVERY_REQUEST and a [token]
	 * 		2.- Send an [email] to the user with a url like http://site/endPoint/?id={}&token={}
	 * 		3.- When the user clicks the link the request gets to the [security REST end point] and
	 * 				a) a [login entry] with id=PASSWORD_RECOVERY_REQUEST and loginId={} is loaded
	 * 				b) the [token] at the [login entry] is compared with the received one
	 * 				c) it the [token] matches, the user can be redirected to the [password change ui]
	 * @param securityContext
	 * @param principalOid
	 * @param token
	 * @return
	 * @throws PersistenceException
	 */
	public Response passwordReset(final SecurityPrincipalOID principalOid,final SecurityToken token,
			                      final Url url) {
		// [1] - Load the [login entry]
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(_sysSecurityContextProvider.get(),
														IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,principalOid);
		Login login = loginLoad.getOrThrow();
		LoginDataForUserPassword usrPwdLoginData = login.getDataAsForUserPassword();
		
		if (usrPwdLoginData == null
		 || usrPwdLoginData.getPasswordResetToken() == null) throw new IllegalStateException(Strings.customized("Login {} does NOT have the required user/password data",
													  		   													login.getId()));
		SecurityToken resetToken = usrPwdLoginData.getPasswordResetToken();
		if (resetToken.isNOT(token)) return Response.status(Response.Status.FORBIDDEN)
													.entity(Strings.customized("The received token {} does NOT match the stored one {}",
															  				   token,resetToken))
													.build();
		
		// [2] - Redir to the [password] change ui if the [token] matches
		String redirectUrl;
		try {
			if (url == null ) {
				redirectUrl = Strings.customized("?loginId={}&token={}", // ?email={}&token={}",
						                  		 login.getLoginId(),
						                  		 token);
			} else {
				redirectUrl = Strings.customized("{}?loginId={}&token={}",
					                             url.asString(),
							                 	 login.getLoginId(),
							                 	 token);
			}
			return Response.seeOther(URI.create(redirectUrl))
						   .build();
		} catch (Throwable ex) {
			log.error("Could NOT redirect to password reset url: {}",
					  ex.getMessage(),ex);
			redirectUrl = Strings.customized("?loginError={}",
											 UserPasswordLoginPasswordResetResult.UNREGISTERED_RESET_REQUEST);
			return Response.seeOther(URI.create(redirectUrl))
						   .build();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE / PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public Response _doLogin(final UserPasswordLoginRequest loginRequest,
							 final Url to) {
		// [1] - Use the user/password login services to check the user-entered login & password
		IsSecurityOperationResult<UserPasswordLoginResponse> loginServiceCallResult = _loginServices.userPasswordLogin(_sysSecurityContextProvider.get(),
																									      			   loginRequest);
		if (loginServiceCallResult.hasFailed()) throw new AuthenticationProviderException(Strings.customized("Error while checking the user-entered login & password: \n {} {}" ,
														                                          			 loginServiceCallResult.asCOREServiceMethodExecError().getDetailedMessage(),
														                                          			 loginServiceCallResult.asCOREServiceMethodExecError().getError()));
		UserPasswordLoginResponse loginResponse = loginServiceCallResult.getOrThrow();

		// [2] - Get the login response and use the callback on login to get the target url
		return _callbackOnLoginHandler.loginCallBack(loginResponse,
												     to);
	}
	public Response _doLoginSecondFactorValidation(final IsLoginAuthnSecondFactorValidationRequest authFactorValidationRequest,
			 				         			   final Url to) {
		// [1] - Use the user/password login services to check the user-entered login & password
		IsSecurityOperationResult<UserPasswordLoginResponse> loginServiceCallResult = _loginServices.validateAuthSecondFactor(_sysSecurityContextProvider.get(),
				                                                                                             				  authFactorValidationRequest);
		if (loginServiceCallResult.hasFailed()) throw new AuthenticationProviderException(Strings.customized("Error while checking the secret for the auth factor '{}' : \n {} {}" ,
				                                                    										 authFactorValidationRequest.getSecret(),
				                                                    										 loginServiceCallResult.asCOREServiceMethodExecError().getDetailedMessage(),
				                                                    										 loginServiceCallResult.asCOREServiceMethodExecError().getError()));
		UserPasswordLoginResponse loginResponse = loginServiceCallResult.getOrThrow();

		// [2] - Get the login response and use the callback on login to get the target url
		return _callbackOnLoginHandler.loginCallBack(loginResponse,
										             to);
	}
}

