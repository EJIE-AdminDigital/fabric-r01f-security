package r01f.services.delegates.sub.security;	

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseBuilder;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.security.UserPasswordLoginServicesDelegateBase;
import r01f.services.delegates.sub.handlers.security.LoginAuthFactorHandler;
import r01f.services.delegates.sub.handlers.security.LoginAuthFactorHandlerDefaultImpl;
import r01f.services.security.CoreCRUDServicesImplForUserPasswordLogin;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

@Slf4j
  public class UserPasswordLoginServicesSubDelegateForLogin<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin,
												            E extends UserLoginEntry> 
extends UserPasswordLoginServicesDelegateBase<U,L,E> {	
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	final LoginAuthFactorHandler<U> _loginAuthFactorHandler;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public UserPasswordLoginServicesSubDelegateForLogin(final ServicesCoreModuleConfig coreCfg,
											         	final EventBus eventBus,
											         	// factories
											         	final Factory<E> loginEntryFactory,
											         	// services
											         	final CRUDServicesForUser<U> userCrud,
											         	final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
											         	final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud,
											         	// login auth factor handler
											         	final LoginAuthFactorHandler<U> loginAuthFactorHandler ) {
		super(coreCfg,
			  eventBus,
			  // services
			  userCrud,
			  userPasswordLoginCrud,
			  loginEntryFactory,userLoginEntryCrud);
		_loginAuthFactorHandler = loginAuthFactorHandler;
		
	}
	
	public UserPasswordLoginServicesSubDelegateForLogin(final ServicesCoreModuleConfig coreCfg,
											            final EventBus eventBus,
											            // factories
											            final Factory<E> loginEntryFactory,
											            // services
											            final CRUDServicesForUser<U> userCrud,
											            final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
											            final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud,
											            // otp service for login auth factor handler
												        final OTPService otpService ) {
		this(coreCfg,
		     eventBus,
		   // services
		    loginEntryFactory,
		    userCrud,
		    userPasswordLoginCrud,
		    userLoginEntryCrud,
		    // default login auth factor handler, by default based on otp services
		    otpService == null ? null 
		    		           : new LoginAuthFactorHandlerDefaultImpl<>(coreCfg,
		    	                		                                 userCrud,
		    	                		                                 otpService));
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public SecurityOperationResult<UserPasswordLoginResponse> login(final SecurityContext securityContext,
																	final UserPasswordLoginRequest loginRequest) {
		if (loginRequest.getLoginId() == null
		 || loginRequest.getPassword() == null) throw new IllegalArgumentException("Login or password cannot be null!");

		UserPasswordLoginResponse outResponse = null;

		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		CRUDResult<L> loginLoad = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)		// returns the DAO layer where the password is NOT removed
															   .loadBy(securityContext,
																	   loginRequest.getLoginId());
		if (loginLoad.hasFailed()) {
			if (loginLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
				log.warn("login with oid {} was NOT found!",loginRequest.getLoginId());
				outResponse = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
															  .withError(UserPasswordLoginErrorType.INVALID_USER_CODE)
															  .build();
				
				log.info("return by POINT 1, for login ID: {}", loginRequest.getLoginId());
				return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
														 .returning(outResponse);
			}
			log.info("return by POINT 2, for login ID: {}", loginRequest.getLoginId());
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.LOGIN_REQUEST)
													 .because(_getOperationErrorResultFromCRUDResult(loginLoad.asCRUDError()),
															  loginLoad.asCRUDError().getErrorMessage());
		}

		// [2] - Now the login is known: check confirmation and password
		L login = loginLoad.getOrThrow();

		// a) confirmation
		if (!login.isConfirmed()) {
			log.warn("User login is not activated yet. Login ID: {}", loginRequest.getLoginId());

			// a) store a login entry
			_createUserLoginEntry(securityContext, 
								  login, 
								  UserLoginEntryID.forUserNotConfirmed());

			// b) return a response
			outResponse = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
														  .withError(UserPasswordLoginErrorType.USER_NOT_CONFIRMED)
														  .build();
			
			log.info("return by POINT 3 (user not activated), for login ID: {}", loginRequest.getLoginId());
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(outResponse);
		}
		// b) the expiration
		else if (login.getPasswordExpirationDate() != null
		      && login.getPasswordExpirationDate().isBefore(Instant.now())) {
			log.warn("Password is expired for user login ID: {}", loginRequest.getLoginId());
			// a) store a login entry
			_createUserLoginEntry(securityContext, 
								  login, 
								  UserLoginEntryID.forLoginError());

			// b) return a response
			outResponse = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
														  .withError(UserPasswordLoginErrorType.PASSWORD_EXPIRED)
														  .build();
			
			log.info("return by POINT 4 (expiration), for login ID: {}", loginRequest.getLoginId());
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(outResponse);
		}
		// c) the password
		else if (!login.getPasswordHash()		// the stored login only contains the hash!
					   .matches(loginRequest.getPassword())) {
			log.info("Password does not match the stored hash. Login ID: ", loginRequest.getLoginId());
			// a) store a login entry
			_createUserLoginEntry(securityContext, 
								  login,
								  UserLoginEntryID.forLoginError());

			// b) return a response
			outResponse = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
														  .withError(UserPasswordLoginErrorType.PASSWORD_ERROR)
														  .build();

			log.info("return by POINT 5 (only hash stored), for login ID: {}", loginRequest.getLoginId());
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
													 .returning(outResponse);
		}

		// [3] - The password is OK: load the user data
		log.info("user/password login ({}) : ok!", loginRequest.getLoginId());

		// Build login response based on some user data
		UserOID userOid = login.getUserOid();
		CRUDResult<U> userLoad = _userCrud.load(securityContext,
											    userOid);
		if (userLoad.hasFailed()) {
			// Error while loading the user
			if (userLoad.asCRUDError().wasBecauseClientRequestedEntityWasNOTFound()) {
				log.warn("The login DB is unconsistent: there does NOT exists a user with oid={} for login oid={}",
						 userOid,login.getOid());
				outResponse = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
															  .withError(UserPasswordLoginErrorType.INVALID_USER_CODE)
															  .build();

				log.info("return by POINT 6 (not found), for login ID: {}", loginRequest.getLoginId());
				return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
														 .returning(outResponse);
			}

			log.info("return by POINT 7, for login ID: {}", loginRequest.getLoginId());
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.LOGIN_REQUEST)
													 .because(_getOperationErrorResultFromCRUDResult(loginLoad.asCRUDError()),
															  loginLoad.asCRUDError().getErrorMessage());
		}		
		User user = userLoad.getOrThrow();		
		// [4] - At this point verify if needed to pass to auth factor flow
		 //  Login auth factor flow
	    // 		1.- Deliver a secret to the user (if needed), for example an otp or other type of factors with no need of delivering ( f.e a user known data)
	    // 		2.- Return an UserLoginResponsePendingOnAuthFactor
	    // 		3.  Show a form asking the secret ( otp, etc. )
		//      4.- When the user post the secret, call to ' validate(..  final LoginAuthFactorValidationRequest<T> loginValidationRequest)
	    // 				a) Validate the secret		//              
	    // 				b) it the [secret] matches and there is no more 
		if ( loginRequest.hasAuthFactorToVerify()) {
			// a) check if there is a Login Auth Factor Handler
			if ( _loginAuthFactorHandler == null) {
				throw new IllegalStateException(" No Login Auth Factor Handler.By default, a LoginAuthHandler based on OTP services is created."
												+ " If it does not exist, probably the necessary services for the OTP have not been bootstraped correctly.\r\n");
			}			
			// b) call to login auth handler to deliver if needed
			SecurityOperationResult<UserPasswordLoginResponse> loginResponseOnAuthFactorValidationnPending =
					_loginAuthFactorHandler.deliverIfNeeded(securityContext,
														    loginRequest.getLoginId() ,(U) user,
														    loginRequest.getAuthFactor());
			// c) store a login entry
			_createUserLoginEntry(securityContext,
								 login, 
					             UserLoginEntryID.forLoginPendingOnAuthFactor());			
			// d) return the response (a secret could has been sent to the user)		
			log.info("return by POINT 8 (factor to verify), for login ID: {}", loginRequest.getLoginId());
			return loginResponseOnAuthFactorValidationnPending;
		}		
		// [5] - The user exists:
		// a) store a login entry
		_createUserLoginEntry(securityContext,
							  login, 
							  UserLoginEntryID.forLoginOk());

		// b) build the response		
		UserPasswordLoginResponse response = UserPasswordLoginResponseBuilder.forLoginId(loginRequest.getLoginId())
																			 .loggedOn(user)
																			 .build();

		log.warn("INFORMATIVE Purpose: User logged in from USER-PASSWORD. User: {}", loginRequest.getLoginId());
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
												 .returning(response);
	}
	
	
	public SecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
			                                                               final LoginAuthFactorValidationRequest loginAuthFactorValidationRequest) {
		if (loginAuthFactorValidationRequest.getLoginId() == null
				 || loginAuthFactorValidationRequest.getSecret() == null) 
								throw new IllegalArgumentException("LoginId or user known secret cannot be null!");		
		// [1] - Load the login .the only purpose is to load the user, because the login password has already been checked.		
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		CRUDResult<L> loginLoad = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)		// returns the DAO layer where the password is NOT removed
															   .loadBy(securityContext,
																	   loginAuthFactorValidationRequest.getLoginId());
		if (loginLoad.hasFailed()) {
			if (loginLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
					log.warn("login with id {} was NOT found!",loginAuthFactorValidationRequest.getLoginId());		
					return SecurityOperationExecResultBuilder.executed(SecurityOperation.AUTH_FACTOR_VALIDATION_REQUEST)
															 .returning(UserPasswordLoginResponseBuilder.forLoginId(loginAuthFactorValidationRequest.getLoginId())
																				  .withError(UserPasswordLoginErrorType.INVALID_USER_CODE)
																				  .build());
				}			
		}
		// [2] - Now the login is known, load the user and validate the auth factor.
		L login = loginLoad.getOrThrow();		
		CRUDResult<U> userLoad = _userCrud.load(securityContext,
				                                login.getUserOid());
		// [3] - Call to validate. The Validation Request contains the input secret to match.
		SecurityOperationResult<UserPasswordLoginResponse> loginResponseAfterAuthFactorValidation =
							_loginAuthFactorHandler.validate(securityContext, 
							                                 login.getLoginId(),userLoad.getOrThrow(),
							                                 loginAuthFactorValidationRequest);
		
		UserPasswordLoginResponse response = loginResponseAfterAuthFactorValidation.getOrThrow();		
		// [4] - Store Login Entry depending on response.
		 if ( response.hasSucceeded()) {
			 // .. the auth factor has been validated and here finish the login with auth factor flow.
			 _createUserLoginEntry(securityContext,
					 			   login, 
					 			   UserLoginEntryID.forLoginOk());
		 } else  if ( response.isPendingOnAuthFactor()) {
			 // .. the auth factor has been validated, but another factor has been requested.
			 _createUserLoginEntry(securityContext,
					 			   login, 
		                           UserLoginEntryID.forLoginPendingOnAuthFactor());
		 } else {
			 // do nothing other cases ( f.e the auth factor has no been validated, the secret does not match...)
		 }		
		// [N] - Return response.		
		return loginResponseAfterAuthFactorValidation;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unqualified-field-access")
	protected void _sendLoginResponseEvent(final UserPasswordLoginResponse loginResponse) {
		if (loginResponse != null) _eventBus.post(loginResponse);
	}
	@SuppressWarnings("unqualified-field-access")
	private void _createUserLoginEntry(final SecurityContext securityContext, 
									   final L login,final UserLoginEntryID entryId) {
		// create the entry object
		E loginEntry = _userLoginEntryFactory.create();
		loginEntry.setDate(Instant.now());
		loginEntry.setSecurityProviderId(SecurityProviderID.USER_PASSWORD);
		loginEntry.setLoginId(login.getLoginId());
		loginEntry.setId(entryId);
		loginEntry.setUserOid(login.getUserOid());
				
		// persist
		_userLoginEntryCrud.create(securityContext,
								   loginEntry);
	}
	
}
