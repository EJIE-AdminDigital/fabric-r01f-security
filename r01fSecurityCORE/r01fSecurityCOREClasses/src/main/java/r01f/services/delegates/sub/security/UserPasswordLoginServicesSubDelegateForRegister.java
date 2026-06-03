package r01f.services.delegates.sub.security;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.events.security.UserRegistrationMessage;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistencePerformedOperation;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationErrorType;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationRequest;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationResponse;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationResponseBuilder;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateErrorType;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponseBuilder;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.security.user.UserStatus;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.security.UserPasswordLoginServicesDelegateBase;
import r01f.services.security.CoreCRUDServicesImplForUserPasswordLogin;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.NotificationMedium;

@Slf4j
  public class UserPasswordLoginServicesSubDelegateForRegister<U extends User & HasMailAddress & HasPhoneNumber,
												               L extends UserPasswordLogin,
												               E extends UserLoginEntry>
extends UserPasswordLoginServicesDelegateBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Factory<L> _loginFactory;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForRegister(final ServicesCoreModuleConfig coreCfg,
												           final EventBus eventBus,
														   // object factory
														   final Factory<L> loginFactory,
														   final Factory<E> loginEntryFactory,
														   // services
														   final CRUDServicesForUser<U> userCrud,
														   final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
														    // used to create login entries
														   final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud) {
		super(coreCfg,
			  eventBus,
			  // services
			  userCrud,
			  userPasswordLoginCrud,
			  loginEntryFactory,userLoginEntryCrud);
		_loginFactory = loginFactory;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<UserPasswordLoginCreateResponse> createLogin(final SecurityContext securityContext,
																		  		final UserPasswordLoginCreateRequest<U> request) {
		// [1] - Check if the user's login already exists
		CRUDResult<U> userLoadUsingLoginId = _userCrud.loadBy(securityContext,
											 		  			 SecurityProviderID.USER_PASSWORD,request.getLogin().getLoginId());
		if (userLoadUsingLoginId.hasSucceeded()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
																						  .because(UserPasswordLoginCreateErrorType.USER_ALREADY_EXISTS.asCOREServiceErrorType(),
																								   "Registration can't be done because the user's [login] " +  request.getLogin().getLoginId() + " already exists");
		// [2] - Create the user
		// a) create the user if needed
		CRUDResult<U> userLoadOrCreate = _loadOrCreateUserToBeRegistered(securityContext,
																		 request);
		// b) if the user previously existed: update the status
		if (userLoadOrCreate.hasSucceeded()
		 && userLoadOrCreate.asCRUDOK().getPerformedOperation() == PersistencePerformedOperation.LOADED) {
			U user = userLoadOrCreate.getOrThrow();
			if (user.getStatus() != request.getInitialUserStatusWhenUserAlreadyExists()) {
				user.setStatus(request.getInitialUserStatusWhenUserAlreadyExists() != null ? request.getInitialUserStatusWhenUserAlreadyExists() : UserStatus.ACTIVE);
				CRUDResult<U> userUpdate = _userCrud.update(securityContext,
															   user);
				if (userUpdate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
															 						 .because(_getOperationErrorResultFromCRUDResult(userUpdate.asCRUDError()),
															 								  userLoadUsingLoginId.asCRUDError()
															 								  					  .getErrorMessage());
			}
		}
		// c) if error creating...
		if (userLoadOrCreate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
															 					   .because(_getOperationErrorResultFromCRUDResult(userLoadOrCreate.asCRUDError()),
															 								userLoadUsingLoginId.asCRUDError()
														 								  					    .getErrorMessage());
		// d) get the loaded or created user
		U registeredUser = userLoadOrCreate.getOrThrow();

		// [3] - create the [login]
		L newLogin = _loginFactory.create();
		newLogin.setUserOid(registeredUser.getOid());
		newLogin.setLoginId(request.getLogin().getLoginId());
		newLogin.setPassword(request.getLogin().getPassword());

		//Set login unconfirmed when user is not active
		newLogin.setConfirmed(UserStatus.ACTIVE.is(registeredUser.getStatus()));

		CRUDResult<L> loginCreate = _userPasswordLoginCrud.create(securityContext,
																	 newLogin);
		if (loginCreate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
																				   .because(_getOperationErrorResultFromCRUDResult(loginCreate.asCRUDError()),
																						    loginCreate.asCRUDError()
																						    		   .getErrorMessage());
		// BEWARE!!	DO NOT use loginCreate.getOrThrow() to get the created [login] since some data (password hash)
		//			is REMOVED at the [delegate] layer
		//			The [login] must be loaded again using the DAO layer (the deepest one)
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		L login = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)
											   .load(securityContext,
													 loginCreate.getOrThrow().getOid())
											   .getOrThrow();

		// [4] - If everything its OK, send a notification & return
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		UserPasswordLoginCreateResponse outResponse = null;
		try {
			// [99] - Build the response
			UserRegistrationMessage notifiedMessage = null;

			// do not send mail when user is active
			if (!UserStatus.ACTIVE.is(registeredUser.getStatus())) {
				notifiedMessage = _notifyUserRegistration(securityContext,
											   	          registeredUser,login,
											   	          request.getUserNotificationMedium());
			}

			if ( notifiedMessage == null ) {
				// could not notify (incorrect email)
				outResponse = UserPasswordLoginCreateResponseBuilder.of(registeredUser.getOid())
																		.withLoginId(login.getLoginId())
																  		.withError(UserPasswordLoginCreateErrorType.INVALID_NOTIFICATION_DATA)
															  		.build();
			} else {
				// everything ok
				outResponse = UserPasswordLoginCreateResponseBuilder.of(registeredUser.getOid())
																		.withLoginId(login.getLoginId())
																		.withLoginOid(login.getOid())
																		.withToken(notifiedMessage.getToken())
															 		.build();
			}
		} catch (final Throwable th) {
			log.error("Error notifying the [user/password login] create loginId={} oid={}: {}",
					  login.getLoginId(),login.getOid(),
					  th.getMessage(),th);
			outResponse = UserPasswordLoginCreateResponseBuilder.of(registeredUser.getOid())
																.withLoginId(login.getLoginId())
														  		.withError(UserPasswordLoginCreateErrorType.NOTIFICATION_ERROR)
														  		.build();
		}
		// [99] - Return
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.REGISTER)
												 .returning(outResponse);
	}
	private CRUDResult<U> _loadOrCreateUserToBeRegistered(final SecurityContext securityContext,
						  			  					  final UserPasswordLoginCreateRequest<U> request) {
		CRUDResult<U> outUser = null;

		// check if it's an existing user
		CRUDResult<U> userLoad = request.getUser().getOid() != null ? _userCrud.load(securityContext,
												   										request.getUser().getOid())
																	: null;
		boolean existingUser = userLoad != null && userLoad.hasSucceeded();
		boolean nonExistingUser = userLoad == null
							   || (userLoad.hasFailed() && userLoad.asCRUDError().wasBecauseClientRequestedEntityWasNOTFound());


		// create the user if needed
		if (existingUser) {
			outUser = userLoad;
		}
		// if not found: create a new user
		else if (nonExistingUser) {
			// a new user
			// ... crate it
			U userToRegister = request.getUser();
			userToRegister.setStatus(request.getInitialUserStatusWhenUserIsNew() != null ? request.getInitialUserStatusWhenUserIsNew() : UserStatus.ACTIVE);
			outUser = _userCrud.create(securityContext,
										  userToRegister);
		} else {
			// unknown error
			outUser = userLoad;
		}
		return outUser;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<UserPasswordLoginConfirmationResponse> confirmLogin(final SecurityContext securityContext,
																		 			   final UserPasswordLoginConfirmationRequest request) {
		if (request.getUserOid() == null) throw new IllegalArgumentException("The [user/password login] confirmation request is NOT valid! the userOid cannot be null!");

		// [0] - Load the user
		CRUDResult<U> usrLoad = _userCrud.load(securityContext,
						  						  request.getUserOid());
		if (usrLoad.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
													 .because(UserPasswordLoginConfirmationErrorType.USER_DOES_NOT_EXIST.asCOREServiceErrorType(),
															  "Confirmation can't be done because the user with oid=" + request.getUserOid() + " doesn't exist.");
		}
		U user = usrLoad.getOrThrow();

		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		CRUDResult<L> usrPwdLoad = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)		// returns the DAO layer where the password is NOT removed
																.loadBy(securityContext,
														  				request.getUserOid());
		if (usrPwdLoad.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
													 .because(UserPasswordLoginConfirmationErrorType.INVALID_LOGIN_ID.asCOREServiceErrorType(),
															  "Confirmation can't be done because the user doesn't exist.");
		}
		L usrPwdLogin = usrPwdLoad.getOrThrow();

		// [2] - Set the [login] as confirmed
		usrPwdLogin.setConfirmed(true);
		CRUDResult<L> updateResult = _userPasswordLoginCrud.update(securityContext,
																	  usrPwdLogin);
		if (updateResult.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
													 .because(_getOperationErrorResultFromCRUDResult(updateResult.asCRUDError()),
															  updateResult.asCRUDError().getErrorMessage());
		}
		// [3] - Change the user status
		if ((user.getStatus() == null && request.getUserStatusAfterLoginConfirmation() != null)
			 ||
			 (user.getStatus() != request.getUserStatusAfterLoginConfirmation())) {
			user.setStatus(request.getUserStatusAfterLoginConfirmation());
			_userCrud.update(securityContext,
								user);
		}

		// [99] - Send the response
		UserPasswordLoginConfirmationResponse resp = UserPasswordLoginConfirmationResponseBuilder.forUserOid(request.getUserOid())
													  											 .ok()
													  											 .withLogin(usrPwdLogin.getOid(),usrPwdLogin.getLoginId())
													  											 .withUserStatusAfterConfirmation(user.getStatus())
													  											 .build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.ACTIVATE_USER)
											     .returning(resp);
	}

/////////////////////////////////////////////////////////////////////////////////////////
// NOTIFY (..just used if user looses the notification for confirming login )
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			    													final LoginID loginId) {
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;

		L login = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)
													.loadBy(securityContext,
															loginId)
														.getOrThrow();
		CRUDResult<U> usrLoad = _userCrud.load(securityContext,
											  login.getUserOid());
		if (usrLoad.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
														.because(UserPasswordLoginConfirmationErrorType.USER_DOES_NOT_EXIST.asCOREServiceErrorType(),
															     "Confirmation can't be done because the user with loginID=" + loginId + " doesn't exist.");
		}
		U user = usrLoad.getOrThrow();
		@SuppressWarnings("unused")
		UserRegistrationMessage notifiedMessage = null;
		if (! UserStatus.ACTIVE.is(user.getStatus() )) {
			notifiedMessage = _notifyUserRegistration(securityContext,
											          user,login,
											          NotificationMedium.EMAIL);
		}
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.REGISTER)
				                                 .returning(true);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private UserRegistrationMessage _notifyUserRegistration(final SecurityContext securityContext,
											                final U user,final L login,
											                final NotificationMedium medium) {
		// [0] build a security token
		JWT jwt = JWTBuilder.createJWToken()
								.forIssuer(_coreConfig.getCoreAppCode())
								.toAudience(SecurityOperation.REGISTER.name())
								.withExpirationSeconds(1800L)
								.withJWTId(login.getLoginId().getId())
								.withKey(user.getOid().asString().getBytes())	// the password hash!!! (the password is NEVER stored)
							.buildWithOptimalAlgorithm();

		SecurityToken token = SecurityToken.forId(jwt.toString());

		// [1] get the email
		EMail email = user.getMailAddress();
		if (email == null) {
			log.error("Could NOT notify to user={}: there's NO email configured!",
					  user.getOid());
			return null;
		}

		// [3] - Create a message
		UserRegistrationMessage userRegMsg = new UserRegistrationMessage();
		userRegMsg.setUserOid(user.getOid());
		userRegMsg.setToken(token);
		userRegMsg.setMedium(NotificationMedium.EMAIL);
		userRegMsg.setEmail(email);

		// [4] - Post an event bus message
		log.info("notifying user {} registraion to {}",
				 user.getOid(),email);
		_eventBus.post(userRegMsg);
		return userRegMsg;
	}
}
