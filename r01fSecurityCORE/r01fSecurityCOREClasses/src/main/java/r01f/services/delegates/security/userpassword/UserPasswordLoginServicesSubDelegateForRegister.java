package r01f.services.delegates.security.userpassword;



import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.events.security.UserRegistrationMessage;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.persistence.PersistencePerformedOperation;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.LoginStatus;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.userpassword.messages.registration.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.messages.registration.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.messages.registration.UserPasswordLoginCreateResponseBuilder;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeRequest;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeResponse;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeResponseBuilder;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.EMail;
import r01f.types.contact.HasEMailAddress;
import r01f.types.contact.NotificationMedium;
import r01f.util.types.Strings;

@Slf4j
public class UserPasswordLoginServicesSubDelegateForRegister<P extends IsSecurityPrincipal>
     extends UserPasswordLoginServicesSubDelegateBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForRegister(final ServicesCoreModuleConfig coreCfg,
												           final EventBus eventBus,
														   // services
														   final CRUDServicesForPrincipal<P> principalCrud,
														   final CRUDServicesForLogin loginCrud,
														   final CRUDServicesForLoginEntry loginEntryCrud) {
		super(coreCfg,
			  eventBus,
			  // services
			  principalCrud,
			  loginCrud,loginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<UserPasswordLoginCreateResponse> createLogin(final SecurityContext securityContext,
																		  		  final UserPasswordLoginCreateRequest<P> request) {
		// [1] - Check if the login already exists
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,request.getSecurityPrincipal().getSecurityPrincipalId()); 
		if (loginLoad.hasSucceeded()) { 
			Login login = loginLoad.getOrThrow();
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.REGISTER)
												     .returning(UserPasswordLoginCreateResponseBuilder.loginIdAlreadyExists()
														   											  .forPrincipal(request.getSecurityPrincipal().getOid())
														   											  .withLogin(login.getOid(),login.getLoginId())
														   											  .build());
		}
		// [2] - Create the [principal]
		CRUDResult<P> principalLoadOrCreate = _loadOrCreatePrincipal(securityContext,
																	 request);
		if (principalLoadOrCreate.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
													 .because(principalLoadOrCreate.asCRUDError());
		}
		P principal = principalLoadOrCreate.getOrThrow();

		// [3] - create the [login]
		Login newLogin = new Login(request.getLoginId());
		newLogin.setSecurityPrincipalSourceIdentityProvider(IdentityProvider.FABRIC);
		newLogin.setAuthenticationMethod(AuthenticationMethod.USER_PASSWORD);
		newLogin.setSecurityPrincipalOid(principal.getOid());
		newLogin.setSecurityPrincipalId(principal.getSecurityPrincipalId());
		
		LoginDataForUserPassword usrPwdData = new LoginDataForUserPassword();
		usrPwdData.setPassword(request.getPassword());
		usrPwdData.setPasswordHash(request.getPassword().hash());
		newLogin.setData(usrPwdData);
		
		if (principalLoadOrCreate.asCRUDOK()
								 .getPerformedOperation() == PersistencePerformedOperation.LOADED) {
			newLogin.setStatus(request.getInitialLoginStatusWhenPrincipalAlreadyExists() != null ? request.getInitialLoginStatusWhenPrincipalAlreadyExists() 
																						 		 : LoginStatus.ACTIVE);
		} 
		CRUDResult<Login> loginCreate = _loginCrud.create(securityContext,
														  newLogin);
		if (loginCreate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
																			  .because(loginCreate.asCRUDError());
		Login login = loginCreate.getOrThrow();

		// [4] - If everything its OK, send a notification & return
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		UserPasswordLoginCreateResponse outResponse = null;
		try {
			// [99] - Build the response
			UserRegistrationMessage notifiedMessage = null;

			// do not notification when [principal] is active
			if (SecurityPrincipalStatus.ACTIVE.isNOT(principal.getStatus())) {
				notifiedMessage = _notifyPrincipalRegistration(securityContext,
											   	          	   principal,login);
			}
			if (notifiedMessage == null) {
				// could not notify (incorrect email)
				outResponse = UserPasswordLoginCreateResponseBuilder.invalidNotificationData()
																	.forPrincipal(principal.getOid())
																	.withLogin(login.getOid(),login.getLoginId())
																	.build();
			} else {
				// everything ok
				outResponse = UserPasswordLoginCreateResponseBuilder.created()
																	.forPrincipal(principal.getOid())
																	.withLogin(login.getOid(),login.getLoginId())
																	.build();
			}
		} catch (Throwable th) {
			log.error("Error notifying the [user/password login] create loginId={} oid={}: {}",
					  login.getLoginId(),login.getOid(),
					  th.getMessage(),th);
			outResponse = UserPasswordLoginCreateResponseBuilder.notificationError()
																.forPrincipal(principal.getOid())
																.withLogin(login.getOid(),login.getLoginId())
																.build();
		}
		// [99] - Return
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.REGISTER)
												 .returning(outResponse);
	}
////////// --- LOAD OR CREATE PRINCIPAL	
	private CRUDResult<P> _loadOrCreatePrincipal(final SecurityContext securityContext,
						  			  			 final UserPasswordLoginCreateRequest<P> request) {
		CRUDResult<P> outPrincipalCRUDResult = null;

		// check if it's an existing user
		CRUDResult<P> principalLoad = request.getSecurityPrincipalOid() != null 
												? _principalCrud.load(securityContext,
												   					  request.getSecurityPrincipalOid())
												: null;
		if (principalLoad.hasSucceeded()) {
			outPrincipalCRUDResult = principalLoad;
		} else if (principalLoad.asCRUDError()
								.wasBecauseClientRequestedEntityWasNOTFound()) {
			// CREATE a new principal
			P principal = request.getSecurityPrincipal();
			
			principal.setStatus(request.getInitialPrincipalStatusWhenNew() != null ? request.getInitialPrincipalStatusWhenNew() 
																				   : SecurityPrincipalStatus.ACTIVE);
			outPrincipalCRUDResult = _principalCrud.create(securityContext,
										  	  	 		   principal);
		} else {
			// unknown error
			outPrincipalCRUDResult = principalLoad;
		}
		return outPrincipalCRUDResult;
	}
////////// --- NOTIFICATION
	private UserRegistrationMessage _notifyPrincipalRegistration(final SecurityContext securityContext,
											                	 final P principal,final Login login) {
		// [0] - build a security token
		JWT jwt = JWTBuilder.createJWToken()
								.forIssuer(_coreCfg.getCoreAppCode())
								.toAudience(SecurityOperation.REGISTER.name())
								.withExpirationSeconds(1800L)
								.withJWTId(login.getLoginId().getId())
								.withKey(principal.getOid().asString().getBytes())	// the password hash!!! (the password is NEVER stored)
							.buildWithOptimalAlgorithm();
		SecurityToken token = SecurityToken.forId(jwt.toString());

		// [1] - get the email
		if (!(principal instanceof HasEMailAddress hasEMail)) {
			throw new IllegalArgumentException(Strings.customized("The [principal] type {} does NOT implement {}",
					  											  principal.getClass(),HasEMailAddress.class));
		}
		EMail email = hasEMail.getEMailAddress();
		if (email == null) {
			log.error("Could NOT notify to [principal]={}: there's NO email configured!",
					  principal.getOid());
			return null;
		}

		// [3] - Create a message
		UserRegistrationMessage userRegMsg = new UserRegistrationMessage();
		userRegMsg.setUserOid(principal.getOid());
		userRegMsg.setToken(token);
		userRegMsg.setMedium(NotificationMedium.EMAIL);
		userRegMsg.setEmail(email);

		// [4] - Post an event bus message
		log.info("notifying [principal] {} registraion to {}",
				 principal.getOid(),email);
		_eventBus.post(userRegMsg);
		return userRegMsg;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
// NOTIFY (..just used if user looses the notification for confirming login )
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			    													  final LoginID loginId) {
		// [1] - Load the login
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(securityContext,
														IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,loginId);
		if (loginLoad.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
																		    .because(loginLoad.asCRUDError());
		Login login = loginLoad.getOrThrow();
		
		// [2] - Load the principal
		CRUDResult<P> principalLoad = _principalCrud.load(securityContext,
											  			  login.getSecurityPrincipalOid());
		if (principalLoad.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
																				.because(principalLoad.asCRUDError());
		P principal = principalLoad.getOrThrow();
		
		UserRegistrationMessage notifiedMessage = null;
		if (SecurityPrincipalStatus.ACTIVE.isNOT(principal.getStatus() )) {
			notifiedMessage = _notifyPrincipalRegistration(securityContext,
											          	   principal,login);
		}
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.REGISTER)
				                                 .returning(true);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<UserPasswordLoginRegistrationAcknowledgeResponse> acknowledgeLoginRegistration(final SecurityContext securityContext,
																		 		   		   							final UserPasswordLoginRegistrationAcknowledgeRequest request) {
		if (request.getSecurityPrincipalOid() == null) throw new IllegalArgumentException("The [user/password login] confirmation request is NOT valid! the [principal] cannot be null!");

		// [1] - Load the [principal] and the [login]
		AtomicReference<IsSecurityOperationResult<UserPasswordLoginRegistrationAcknowledgeResponse>> err = new AtomicReference<>();
		SecurityPrincipalAndLogin<P> principalAndLogin = _loadSecurityPrincipalAndLogin(securityContext,
																						request.getLoginOid(),
																						SecurityOperation.ACTIVATE_USER,
																						(theLoginOid,theLoginId) -> UserPasswordLoginRegistrationAcknowledgeResponseBuilder.invalidLoginId()
																																			        					   .forLogin(theLoginOid,theLoginId)
																																			        					   .build(),
																						(error) -> err.set(error));
		if (principalAndLogin == null) return err.get();	// error loading the [principal] and [login]
		
		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();
		
		// [2] - Set the [login] as confirmed
		LoginDataForUserPassword usrPwdLogin = login.getDataAsForUserPassword();
		usrPwdLogin.setConfirmed(true);
		CRUDResult<Login> loginUpdate = _loginCrud.update(securityContext,
														  login);
		if (loginUpdate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.REGISTER)
													 						   .because(loginUpdate.asCRUDError());
		// [3] - Change the [principal] status
		if ((principal.getStatus() == null && request.getPrincipalStatusAfterAcknowledgement() != null)
			 ||
			 (principal.getStatus() != request.getPrincipalStatusAfterAcknowledgement())) {
			principal.setStatus(request.getPrincipalStatusAfterAcknowledgement());
			CRUDResult<P> principalUpdate = _principalCrud.update(securityContext,
								  								  principal);
			if (principalUpdate.hasFailed()) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.ACTIVATE_USER)
														 							  .because(principalUpdate.asCRUDError());
		}
		// [99] - Send the response
		var resp = UserPasswordLoginRegistrationAcknowledgeResponseBuilder.confirmed()
																		  .forSecurityPrincipal(principal.getOid(),principal.getId())
																		  .withPrincipalStatusAfterConfirmation(principal.getStatus())
																		  .forLogin(login.getOid(),login.getLoginId())
																		  .build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.ACTIVATE_USER)
											     .returning(resp);
	}
}
