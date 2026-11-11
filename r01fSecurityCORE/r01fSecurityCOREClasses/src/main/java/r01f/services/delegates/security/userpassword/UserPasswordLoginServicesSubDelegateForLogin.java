package r01f.services.delegates.security.userpassword;	

import java.time.Instant;
import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.LoginStatus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponseBuilder;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.security.userpassword.secondfactor.IsLoginAuthnSecondFactorHandler;
import r01f.services.delegates.security.userpassword.secondfactor.LoginAuthnSecondFactorHandlerDefaultImpl;

@Slf4j
public class UserPasswordLoginServicesSubDelegateForLogin<P extends IsSecurityPrincipal> 
	 extends UserPasswordLoginServicesSubDelegateBase<P> {	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	final IsLoginAuthnSecondFactorHandler<P> _loginAuthSecondFactorHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginServicesSubDelegateForLogin(final ServicesCoreModuleConfig coreCfg,
														final EventBus eventBus,
											         	// services
											         	final CRUDServicesForPrincipal<P> principalCrud,
											         	final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
											         	// login authn second factor handler
											         	final IsLoginAuthnSecondFactorHandler<P> loginAuthnSecondFactorHandler) {
		super(coreCfg,
			  eventBus,
			  // services
			  principalCrud,
			  loginCrud,
			  loginEntryCrud);
		_loginAuthSecondFactorHandler = loginAuthnSecondFactorHandler;
	}
	public UserPasswordLoginServicesSubDelegateForLogin(final ServicesCoreModuleConfig coreCfg,
														final EventBus eventBus,
											         	// services
											         	final CRUDServicesForPrincipal<P> principalCrud,
											         	final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
											            // otp service for login auth factor handler
												        final OTPService otpService) {
		this(coreCfg,
			 eventBus,
			 // services
			 principalCrud,
			 loginCrud,
			 loginEntryCrud,
		     // default login authn second factor handler, by default based on otp services
		     otpService == null ? null 
		    		            : new LoginAuthnSecondFactorHandlerDefaultImpl<>(coreCfg,
		    	                		                                  otpService));
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<UserPasswordLoginResponse> userPasswordLogin(final SecurityContext securityContext,
																    			  final UserPasswordLoginRequest loginRequest) {
		if (loginRequest.getLoginId() == null
		 || loginRequest.getPassword() == null) throw new IllegalArgumentException("Login or password cannot be null!");
		
		// [1] - Load the [principal] and the [login]
		AtomicReference<IsSecurityOperationResult<UserPasswordLoginResponse>> err = new AtomicReference<>();
		SecurityPrincipalAndLogin<P> principalAndLogin = _loadSecurityPrincipalAndLogin(securityContext,
																						loginRequest.getLoginId(),
																						SecurityOperation.LOGIN_REQUEST,
																						(theLoginOid,theLoginId) -> UserPasswordLoginResponseBuilder.invalidLoginId()
																																			        .forLogin(theLoginOid,theLoginId)
																																			        .build(),
																						(error) -> err.set(error));
		if (principalAndLogin == null) return err.get();	// error loading the [principal] and [login]

		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();

		// [3] - Check the login
		// a) confirmation
		if (login.getStatus() != null
		 && login.getStatus().isNOT(LoginStatus.ACTIVE)) {
			log.warn("login={} is not active",
				     loginRequest.getLoginId());
			// a) store a login entry
			_registerLoginEntry(securityContext, 
								login,
								principal,
								LoginEntryEvent.NOT_CONFIRMED);
			// b) return a response
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(UserPasswordLoginResponseBuilder.loginNotActive()
																							    .forLogin(login.getOid(),login.getId())
																							    .build());
		}
		
		// [4] - Check the password expiration
		LoginDataForUserPassword loginData = login.getDataAsForUserPassword();
		
		if (loginData.getPasswordExpiresAt() != null
		 && loginData.getPasswordExpiresAt().isBefore(Instant.now())) {
			log.warn("Password is expired for user login ID: {}",
					 loginRequest.getLoginId());
			// a) store a login entry
			_registerLoginEntry(securityContext, 
								login,
								principal,
								LoginEntryEvent.LOGIN_ERROR);
			// b) return a response
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(UserPasswordLoginResponseBuilder.passwordExpired()
																							    .forLogin(login.getOid(),login.getId())
																							    .build());
		}
		// [4] - Check the password 
		if (!loginData.getPasswordHash()		// the stored login only contains the hash!
					  .matches(loginRequest.getPassword())) {
			log.info("Password does not match the stored hash. Login ID: ",
					 loginRequest.getLoginId());
			// a) store a login entry
			_registerLoginEntry(securityContext, 
								login,
								principal,
								LoginEntryEvent.LOGIN_ERROR);

			// b) return a response
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(UserPasswordLoginResponseBuilder.passwordError()
																								.forLogin(login.getOid(),login.getId())
																								.build());
		}
		
		// [4] - At this point verify if needed to pass a [authn second factor] flow
		//  	 Login authn second factor flow
	    // 			1.- Deliver a [secret] to the [principal] (if needed)
		//				for example an otp or other type of factors with no need of delivering (f.e a user known data)
	    // 			2.- Return an response indicating that the login is pending on auth factor validation
	    // 			3.  Show a form asking the secret (otp, etc. )
		//      	4.- When the [principal] post the [secret]
	    // 					a) Validate the [secret]	              
	    // 					b) if the [secret] matches and there is no more things to do
		if ( loginRequest.hasToPassAuthnSecondFactorVerification()) {
			// a) check if there is a [authn second factor] handler
			if (_loginAuthSecondFactorHandler == null) {
				throw new IllegalStateException("No Login [authn second factor] handler. " +
											    "By default, a LoginAuthHandler based on OTP services is created. " +
												"If it does not exist, probably the necessary services for the OTP have not been bootstraped correctly.\r\n");
			}			
			// b) call to login auth handler to deliver if needed
			var responseForAuthnSecondFactorValidationnPending = _loginAuthSecondFactorHandler.deliverIfNeeded(securityContext,
														    											 loginRequest.getLoginId(),
														    											 loginRequest.getAuthnSecondFactor());
			// c) store a login entry
			_registerLoginEntry(securityContext,
								login,
								principal,
					            LoginEntryEvent.AWAITING_AUTHN_SECOND_FACTOR_VALIDATION);			
			// d) return the response (a secret could has been sent to the user)		
			return responseForAuthnSecondFactorValidationnPending;
		}	
		
		// [5] - Login OK (no second factor needed)
		// a) store a login entry
		_registerLoginEntry(securityContext,
							login,
							principal,
							LoginEntryEvent.LOGIN_OK);

		// b) build the response		
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
												 .returning(UserPasswordLoginResponseBuilder.loggedIn()
																		  	   				.forLogin(login.getOid(),login.getId())
																		  	   				.build());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECOND FACTOR VALIDATION
/////////////////////////////////////////////////////////////////////////////////////////	
	public IsSecurityOperationResult<UserPasswordLoginResponse> validateAuthSecondFactor(final SecurityContext securityContext,
		                                                                				 final IsLoginAuthnSecondFactorValidationRequest loginAuthSecondFactorValidationRequest) {
		if (loginAuthSecondFactorValidationRequest.getLoginId() == null
	     || loginAuthSecondFactorValidationRequest.getSecret() == null) throw new IllegalArgumentException("LoginId or user known secret cannot be null!");
		
		// [1] - Load the [principal] and the [login]
		AtomicReference<IsSecurityOperationResult<UserPasswordLoginResponse>> err = new AtomicReference<>();
		SecurityPrincipalAndLogin<P> principalAndLogin = _loadSecurityPrincipalAndLogin(securityContext,
																						loginAuthSecondFactorValidationRequest.getLoginId(),
																						SecurityOperation.LOGIN_REQUEST,
																						(theLoginOid,theLoginId) -> UserPasswordLoginResponseBuilder.invalidLoginId()
																																			        .forLogin(theLoginOid,theLoginId)
																																			        .build(),
																						(error) -> err.set(error));
		if (principalAndLogin == null) return err.get();	// error loading the [principal] and [login]

		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();
		
		// [3] - Use the login auth second factor handler to validate the secret
		var responseAfterAuthSecondFactorValidation = _loginAuthSecondFactorHandler.validate(securityContext, 
							                                 								 loginAuthSecondFactorValidationRequest);
		
		UserPasswordLoginResponse response = responseAfterAuthSecondFactorValidation.getOrThrow();	
		
		// [4] - Store Login Entry depending on response.
		 if (response.hasSucceeded()) {
			 // .. the authn second factor has been validated and here finish the login with auth factor flow.
			 _registerLoginEntry(securityContext,
					 			 login,
					 			 principal,
					 			 LoginEntryEvent.LOGIN_OK);
		 } else if (response.isAwaitingAuthnSecondFactorVerification()) {
			 // .. the authn second factor has been validated, but another factor has been requested.
			 _registerLoginEntry(securityContext,
					 			 login,
					 			 principal,
		                         LoginEntryEvent.AWAITING_AUTHN_SECOND_FACTOR_VALIDATION);
		 } else {
			 // do nothing other cases ( f.e the auth factor has no been validated, the secret does not match...)
		 }		
		// [N] - Return response.		
		return responseAfterAuthSecondFactorValidation;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unqualified-field-access")
	protected void _sendLoginResponseEvent(final UserPasswordLoginResponse loginResponse) {
		if (loginResponse != null) _eventBus.post(loginResponse);
	}
}
