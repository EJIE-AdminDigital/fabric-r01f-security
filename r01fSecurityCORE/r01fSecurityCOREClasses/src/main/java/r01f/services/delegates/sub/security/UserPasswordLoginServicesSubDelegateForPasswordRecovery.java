package r01f.services.delegates.sub.security;

import java.time.Instant;



import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryErrorType;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponseBuilder;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorTypes;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.security.UserPasswordLoginServicesDelegateBase;
import r01f.services.security.CoreCRUDServicesImplForUserPasswordLogin;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.NotificationMedium;

@Slf4j
  public class UserPasswordLoginServicesSubDelegateForPasswordRecovery<U extends User & HasMailAddress & HasPhoneNumber,L extends UserPasswordLogin,
														               E extends UserLoginEntry>
extends UserPasswordLoginServicesDelegateBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForPasswordRecovery(final ServicesCoreModuleConfig coreCfg,
																   final EventBus eventBus,
																   // factories
																   final Factory<E> loginEntryFactory,
																   // services
																   final CRUDServicesForUser<U> userCrud,
																   final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
																   final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud) {
		super(coreCfg,
			  eventBus,
			  // services
			  userCrud,
			  userPasswordLoginCrud,
			  loginEntryFactory,userLoginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RECOVER
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<UserPasswordLoginRecoveryResponse> recoverPassword(final SecurityContext securityContext,
																					  final UserPasswordLoginRecoveryRequest request) {
	    // Password recovery
	    // 		1.- Create a [login entry] with id=PASSWORD_RECOVERY_REQUEST and a [token]
	    // 		2.- Send an [email] to the user with a url like http://site/securityRestEndPoint/?loginId={}&token={}
	    // 		3.- When the user clicks the link the request gets to the [security REST end point] and
	    // 				a) a [login entry] with id=PASSWORD_RECOVERY_REQUEST and loginId={} is loaded
	    // 				b) the [token] at the [login entry] is compared with the received one
	    // 				c) it the [token] matches, the user can be redirected to the [password change ui]

		if (request.getLoginId() == null) {
			log.error("login can't be null");
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  "LoginID can't be null");
		}

		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		CRUDResult<L> loginLoad = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)		// returns the DAO layer where the password is NOT removed
															   .loadBy(securityContext,
																	   request.getLoginId());
		if (loginLoad.hasFailed()) {
			if (loginLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
				UserPasswordLoginRecoveryResponse outResponse = UserPasswordLoginRecoveryResponseBuilder.forLoginId(request.getLoginId())
																										.withError(UserPasswordLoginRecoveryErrorType.INVALID_LOGIN_ID)
																										.build();
				return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
														  .returning(outResponse);
			}
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
													  .because(_getOperationErrorResultFromCRUDResult(loginLoad.asCRUDError()),
															   loginLoad.asCRUDError().getErrorMessage());
		}

		// [2] - The login exists; CREATE a [login entry] with PASSWORD_RECOVERY_REQUEST id AND the [token]
		// a) create the token
		L login = loginLoad.getOrThrow();
		JWT jwt = JWTBuilder.createJWToken()
							.forIssuer(_coreConfig.getCoreAppCode())
							.toAudience(SecurityOperation.PASSWORD_RECOVERY_REQUEST.name())
							.withExpirationSeconds(1800L)
							.withJWTId(request.getLoginId().getId())
							.withKey(login.getUserOid().asString().getBytes())	// the password hash!!! (the password is NEVER stored)
							.buildWithOptimalAlgorithm();
		log.info("JWT: {}",
				 jwt.asString());

		// b) create a [login entry] for [PASSWORD RECOVERY REQUEST] and with the created [token]
		E loginEntryForPasswordRecoveryRequest = _userLoginEntryFactory.create();
		loginEntryForPasswordRecoveryRequest.setId(UserLoginEntryID.forPasswordRecoveryRequest());
		loginEntryForPasswordRecoveryRequest.setDate(Instant.now());
		loginEntryForPasswordRecoveryRequest.setSecurityProviderId(SecurityProviderID.USER_PASSWORD);
		loginEntryForPasswordRecoveryRequest.setLoginId(login.getLoginId());
		loginEntryForPasswordRecoveryRequest.setUserOid(login.getUserOid());


		SecurityToken token = SecurityToken.forId(jwt.toString());
		loginEntryForPasswordRecoveryRequest.setToken(token);
		CRUDResult<E> passwordRecoveryRequestEntryCRUD = _userLoginEntryCrud.create(securityContext,
																					   loginEntryForPasswordRecoveryRequest);
		if (passwordRecoveryRequestEntryCRUD.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
													  .because(_getOperationErrorResultFromCRUDResult(passwordRecoveryRequestEntryCRUD.asCRUDError()),
															   passwordRecoveryRequestEntryCRUD.asCRUDError()
															   								   .getErrorMessage());
		}

		// [3] - Send a notification [email] & return
		// 		 The [email] contains a link like url?userOid={}&token={}
		//		 ... when the [user] clicks a the [password recovery request link] the request reaches the
		//			 [security rest services] and there [token] is checked to see if it's a legitimate one (it must match the previously stored one)
		//			 ... if so, the [user] is redirected again to the [password change ui]
		UserPasswordLoginRecoveryResponse outResponse = null;
		boolean notified = _notifyPasswordRecovery(securityContext,
												   request.getUserNotificationMedium(),
												   login.getUserOid(),
												   token);
		if (!notified) {
			// could not notify
			outResponse = UserPasswordLoginRecoveryResponseBuilder.forLoginId(request.getLoginId())
														  		  .withError(UserPasswordLoginRecoveryErrorType.INVALID_NOTIFICATION_DATA)
														  		  .build();
		} else {
			// everything ok
			outResponse = UserPasswordLoginRecoveryResponseBuilder.forLoginId(request.getLoginId())
														  		  .okWithToken(SecurityToken.forId(jwt.toString()))
														  		  .build();
		}
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
												 .returning(outResponse);
	}
	private boolean _notifyPasswordRecovery(final SecurityContext securityContext,
											final NotificationMedium medium,
											final UserOID userOid,
											final SecurityToken jwt) {
		// [1] - Load the user to get the email
		CRUDResult<U> userLoad =_userCrud.load(securityContext,
												  userOid);
		if (userLoad.hasFailed()) {
			log.warn("Could NOT notify to NON EXISTENT user={}",
					 userOid);
			return false;
		}

		// [2] - The user exists: notify
		U user = userLoad.getOrThrow();
		EMail email = user.getMailAddress();
		if (email == null) {
			log.warn("Could NOT notify to user={}: there's NO email configured!",userOid);
			return false;
		}

		// [3] - Create a message
		UserPasswordRecoverMessage pwdRecoverMsg = new UserPasswordRecoverMessage();
		pwdRecoverMsg.setUserOid(userOid);
		pwdRecoverMsg.setToken(jwt);
		pwdRecoverMsg.setMedium(NotificationMedium.EMAIL);
		pwdRecoverMsg.setEmail(email);

		// [4] - Post an event bus message
		log.info("notifying password recovery to user={} to email={}",
				 userOid,email);
		_eventBus.post(pwdRecoverMsg);
		return true;
	}
}
