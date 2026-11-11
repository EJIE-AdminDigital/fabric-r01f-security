package r01f.services.delegates.security.userpassword;

import java.util.concurrent.atomic.AtomicReference;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.eventbus.EventBus;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverRequest;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverResponse;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverResponseBuilder;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.model.services.COREServiceErrorTypes;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.EMail;
import r01f.types.contact.HasEMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.NotificationMedium;
import r01f.types.contact.Phone;

@Slf4j
public class UserPasswordLoginServicesSubDelegateForPasswordRecovery<P extends IsSecurityPrincipal>
     extends UserPasswordLoginServicesSubDelegateBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForPasswordRecovery(final ServicesCoreModuleConfig coreCfg,
																   final EventBus eventBus,
																   // services
																   final CRUDServicesForPrincipal<P> principalCrud,
																   final CRUDServicesForLogin loginCrud,
																   final CRUDServicesForLoginEntry loginEntryCrud) {
		super(coreCfg,
			  eventBus,
			  // services
			  principalCrud,
			  loginCrud,
			  loginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RECOVER
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<UserPasswordRecoverResponse> recoverPassword(final SecurityContext securityContext,
																		  	      final UserPasswordRecoverRequest pwdRecoverRequest) {
	    // Password recovery
	    // 		1.- Create a [login entry] with id=PASSWORD_RECOVERY_REQUEST and a [token]
	    // 		2.- Send an [email] to the user with a url like http://site/securityRestEndPoint/?loginId={}&token={}
	    // 		3.- When the user clicks the link the request gets to the [security REST end point] and
	    // 				a) a [login entry] with id=PASSWORD_RECOVERY_REQUEST and loginId={} is loaded
	    // 				b) the [token] at the [login entry] is compared with the received one
	    // 				c) it the [token] matches, the user can be redirected to the [password change ui]

		if (pwdRecoverRequest.getLoginId() == null) {
			log.error("login can't be null");
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  "LoginID can't be null");
		}

		// [1] - Load the [principal] and the [login]
		AtomicReference<IsSecurityOperationResult<UserPasswordRecoverResponse>> err = new AtomicReference<>();
		SecurityPrincipalAndLogin<P> principalAndLogin = _loadSecurityPrincipalAndLogin(securityContext,
																						pwdRecoverRequest.getLoginId(),
																						SecurityOperation.LOGIN_REQUEST,
																						(theLoginOid,theLoginId) -> UserPasswordRecoverResponseBuilder.invalidLoginId()
																																			          .forLogin(theLoginOid,theLoginId)
																																			          .build(),
																						(error) -> err.set(error));
		if (principalAndLogin == null) return err.get();	// error loading the [principal] and [login]
		
		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();
		
		// [3] - Create a [login entry] for [PASSWORD RECOVERY REQUEST]
		JWT jwt = JWTBuilder.createJWToken()
							.forIssuer(_coreCfg.getCoreAppCode())
							.toAudience(SecurityOperation.PASSWORD_RECOVERY_REQUEST.name())
							.withExpirationSeconds(1800L)
							.withJWTId(pwdRecoverRequest.getLoginId().getId())
							.withKey(login.getSecurityPrincipalOid().asString().getBytes())	
							.buildWithOptimalAlgorithm();
		log.info("JWT: {}",
				 jwt.asString());

		// store it
		LoginDataForUserPassword loginData = login.getDataAsForUserPassword();
		loginData.setPasswordResetToken(jwt.asSecurityToken());
		CRUDResult<Login> loginUpdate = _loginCrud.update(securityContext,
														  login);
		if (loginUpdate.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
													 .because(loginUpdate.asCRUDError());
		}
		login = loginUpdate.getOrThrow();
		loginData = login.getDataAsForUserPassword();

		// [4] - create a [login entry] for [PASSWORD RECOVERY REQUEST] and with the created [token]
		_registerLoginEntry(securityContext,
							login,
							principal,
							LoginEntryEvent.PASSWORD_RECORVERY_REQUEST);

		// [5] - Send a notification [email] & return
		// 		 The [email] contains a link like url?userOid={}&token={}
		//		 ... when the [user] clicks a the [password recovery request link] the request reaches the
		//			 [security rest services] and there [token] is checked to see if it's a legitimate one (it must match the previously stored one)
		//			 ... if so, the [user] is redirected again to the [password change ui]
		boolean notified = _notifyPasswordRecovery(securityContext,
												   pwdRecoverRequest.getUserNotificationMedium(),
												   principal,
												   loginData.getPasswordResetToken());
		UserPasswordRecoverResponse outResponse = notified
														? UserPasswordRecoverResponseBuilder.registered(loginData.getPasswordResetToken())
																							.forLogin(login.getOid(),login.getId())
																							.build()
														: UserPasswordRecoverResponseBuilder.invalidNotificationData()
													  	    								.forLogin(login.getOid(),login.getId())
													  	    								.build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RECOVERY_REQUEST)
												 .returning(outResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private boolean _notifyPasswordRecovery(final SecurityContext securityContext,
											final NotificationMedium medium,
											final P principal,
											final SecurityToken jwt) {
		UserPasswordRecoverMessage pwdRecoverMsg = new UserPasswordRecoverMessage();
		pwdRecoverMsg.setPrincipalOid(principal.getOid());
		pwdRecoverMsg.setToken(jwt);
		
		if (medium.is(NotificationMedium.EMAIL)) {			
			if (!(principal instanceof HasEMailAddress)) throw new IllegalArgumentException("Principal " + principal.getClass() + " is NOT an instance of " + HasEMailAddress.class);
			HasEMailAddress hasEmail = (HasEMailAddress)principal;
			EMail email = hasEmail.getEMailAddress();
			if (email == null) {
				log.warn("Could NOT notify to principal={}: there's NO email configured!",principal.getOid());
				return false;
			}
			
			pwdRecoverMsg.setMedium(NotificationMedium.EMAIL);
			pwdRecoverMsg.setContactMean(email);

		} else if (medium.is(NotificationMedium.SMS)) {
			if (!(principal instanceof HasPhoneNumber)) throw new IllegalArgumentException("Principal " + principal.getClass() + " is NOT an instance of " + HasPhoneNumber.class);
			HasPhoneNumber hasPhone = (HasPhoneNumber)principal;
			Phone phone = hasPhone.getPhoneNumber();
			if (phone == null) {
				log.warn("Could NOT notify to principal={}: there's NO email configured!",principal.getOid());
				return false;
			}
			
			pwdRecoverMsg.setMedium(NotificationMedium.EMAIL);
			pwdRecoverMsg.setContactMean(phone);
		} else {
			throw new IllegalArgumentException("Unknown notification medium " + medium);
		}

		// [4] - Post an event bus message
		log.info("notifying password recovery to principal={} to {}={}",
				 principal.getOid(),
				 pwdRecoverMsg.getMedium(),pwdRecoverMsg.getContactMean());
		_eventBus.post(pwdRecoverMsg);
		return true;
	}
}
