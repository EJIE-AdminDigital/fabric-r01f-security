package r01f.security.rest.resource.usrpwd;

import java.net.URI;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginRequestBuilder;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBySMS;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPBySMS;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponseErrorType;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.usrpwd.UserPasswordCallBackOnLoginHandler;
import r01f.security.handler.usrpwd.UserPasswordLoginEntryPointHandler;
import r01f.security.handler.usrpwd.UserPasswordUserDataFetchHandler;
import r01f.security.provider.SecurityProviderException;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.rest.resource.RESTDelegateForLoginBase;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.util.types.Strings;



@Slf4j
public class RESTDelegateForUserPasswordSecurityProvider<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin,
														 E extends UserLoginEntry>
	 extends RESTDelegateForLoginBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final UserPasswordCallBackOnLoginHandler _callbackOnLoginHandler;
	private final UserPasswordLoginServices<U,L> _loginServices;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTDelegateForUserPasswordSecurityProvider(final Provider<SecurityContext> sysSecurityContextProvider,
													   final SecurityProviderSecretConfig securityProviderSecretConfig,
													   // handlers
													   final UserPasswordLoginEntryPointHandler loginEntryPointHandler,
									  				   final UserPasswordCallBackOnLoginHandler callbackOnLoginHandler,
									  				   final UserPasswordUserDataFetchHandler<U,L> fetchUserDataHandler,
									  				   // login services
									  				   final UserPasswordLoginServices<U,L> loginServices,
									  				   // marshaller
									  				   final Marshaller marshaller,
									  				   // used to create [login entries] when the user logs-in
									  				   final Factory<E> loginEntryFactory,
									  				   final CRUDServicesForUserLoginEntry<E> loginEntryCrud) {
		super(SecurityProviderID.USER_PASSWORD,
			  sysSecurityContextProvider,
			  securityProviderSecretConfig,
			  loginEntryPointHandler,
			  fetchUserDataHandler,
			  marshaller,
			  loginEntryFactory,loginEntryCrud);

		_callbackOnLoginHandler = callbackOnLoginHandler;
		_loginServices = loginServices;
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
																			   .authenticationFactor(new LoginAuthFactorUsingOTPBySMS(phone))
																			   .build();
	 	// [2] - Do Login based on request
		return  _doLogin(loginRequest, to);
	}

	public Response loginUsingOTPByEMail(final LoginID loginId, final Password password,
            							 final EMail email,
            							 final Url to) {
		// [1] - Use the user/password login login request.
		UserPasswordLoginRequest loginRequest = UserPasswordLoginRequestBuilder.using(loginId)
															   .with(password)
															   .authenticationFactor(new LoginAuthFactorUsingOTPByEMail(email))
															   .build();
		// [2] - Do Login based on request
		return  _doLogin(loginRequest, to);
	}

	public Response validateAuthFactorOTPBySMS(final LoginID loginId, final Password secret,
			                                   final Phone phone,
			                                   final Url to) {
		// [ ] Validate user known secret.
		return _doLoginValidate(new LoginAuthFactorValidationRequestUsingOTPBySMS(loginId,secret,phone), to);
	}

	public Response validateAuthFactorOTPByEMail(final LoginID loginId, final Password secret,
            									 final EMail email,
            									 final Url to) {
	 // [ ] Validate user known secret.
		return _doLoginValidate(new LoginAuthFactorValidationRequestUsingOTPByEMail(loginId,secret,email), to);
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
	 * @param userOid
	 * @param token
	 * @return
	 * @throws PersistenceException
	 */
	public Response passwordReset(final UserOID userOid,final SecurityToken token,
			                      final Url url) {
		// [1] - Load the [login entry]
		CRUDResult<E> loginEntryLoad = _loginEntryCrud.loadForPasswordReset(_sysSecurityContextProvider.get(),
														  	 				userOid,token);
		// [2] - Redir to the [password] change ui if the [token] matches
		String redirectUrl;
		try {
			E loginEntry = loginEntryLoad.getOrThrow();
			if (url == null ) {
				redirectUrl = Strings.customized("?loginId={}&token={}", // ?email={}&token={}",
						                  		 loginEntry.getLoginId(),
						                  		 token);
			} else {
				redirectUrl = Strings.customized("{}?loginId={}&token={}",
					                                 url.asString(),
							                 		 loginEntry.getLoginId(),
							                 		 token);
			}
			return Response.seeOther(URI.create(redirectUrl))
						   .build();
		} catch (final Throwable ex) {
			log.error("Could NOT redirect to password reset url: {}",
					  ex.getMessage(),ex);
			redirectUrl = Strings.customized("?loginError={}",
											 UserPasswordLoginResetResponseErrorType.UNREGISTERED_RESET_REQUEST.name());
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
		SecurityOperationResult<UserPasswordLoginResponse> loginServiceCallResult = _loginServices.login(_sysSecurityContextProvider.get(),
																									      loginRequest);
		if (loginServiceCallResult.hasFailed()) {
				throw new SecurityProviderException( Strings.customized("Error while checking the user-entered login & password: \n {} {}" ,
						                                          			loginServiceCallResult.asCOREServiceMethodExecError().getDetailedMessage(),
						                                          			loginServiceCallResult.asCOREServiceMethodExecError().getError()));
		}
		UserPasswordLoginResponse loginResponse = loginServiceCallResult.getOrThrow();

		// [2] - Get the login response and use the callback on login to get the target url
		return _callbackOnLoginHandler.loginCallBack(loginResponse,
												     to);
	}

	public Response _doLoginValidate(final LoginAuthFactorValidationRequest authFactorValidationRequest,
			 				         final Url to) {
		// [1] - Use the user/password login services to check the user-entered login & password
		SecurityOperationResult<UserPasswordLoginResponse> loginServiceCallResult = _loginServices.validate(_sysSecurityContextProvider.get(),
				                                                                                             authFactorValidationRequest);
		if (loginServiceCallResult.hasFailed()) {
		throw new SecurityProviderException( Strings.customized("Error while checking the secret for the auth factor '{}' : \n {} {}" ,
				                                                    authFactorValidationRequest.getSecret(),
				                                          			loginServiceCallResult.asCOREServiceMethodExecError().getDetailedMessage(),
				                                          			loginServiceCallResult.asCOREServiceMethodExecError().getError()));
		}
		UserPasswordLoginResponse loginResponse = loginServiceCallResult.getOrThrow();

		// [2] - Get the login response and use the callback on login to get the target url
		return _callbackOnLoginHandler.loginCallBack(loginResponse,
										             to);
	}


}

