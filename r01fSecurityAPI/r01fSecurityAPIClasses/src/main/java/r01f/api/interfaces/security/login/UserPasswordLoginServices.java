package r01f.api.interfaces.security.login;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.password.change.UserPasswordChangeRequest;
import r01f.model.security.login.userpassword.messages.password.change.UserPasswordChangeResponse;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverRequest;
import r01f.model.security.login.userpassword.messages.password.recover.UserPasswordRecoverResponse;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetRequest;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetResponse;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeRequest;
import r01f.model.security.login.userpassword.messages.registration.acknowledge.UserPasswordLoginRegistrationAcknowledgeResponse;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.oids.SecurityIDs.LoginID;

public interface UserPasswordLoginServices
		 extends IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Login without second factor validation
	 * @param securityContext
	 * @param loginRequest
	 * @return
	 */
	public IsSecurityOperationResult<UserPasswordLoginResponse> userPasswordLogin(final SecurityContext securityContext,
																    			  final UserPasswordLoginRequest loginRequest);

	/**
	 * Login validate auth factor :
	 * 	1.- Deliver a secret to the user (if needed), for example an otp or other type of factors with no need of delivering 
	 *      (eg_ a user known data)
	 * 	2.- Return an UserLoginResponsePendingOnAuthFactor
	 * 	3.- Show a form asking the secret ( otp, etc. )
	 *  4.- When the user post the secret, call to ' validate(..  final LoginAuthFactorValidationRequest<T> loginValidationRequest)
	 * 			a) Validate the secret		//
	 * 			b) it the [secret] matches and there is no more
	 *
	 * @param securityContext
	 * @param loginAuthSecondFactorValidationRequest
	 * @return
	 */
	public IsSecurityOperationResult<UserPasswordLoginResponse> validateAuthSecondFactor(final SecurityContext securityContext,
		                                                                				 final IsLoginAuthnSecondFactorValidationRequest loginAuthSecondFactorValidationRequest);
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *  When a user-password login is created a notification is sent automaticaly from core (...usually a mail ).
	 *    -> If requires retry (f.e resend a mail) this method is invoked from UI.
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public IsSecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			                                                          final LoginID loginId);
	/**
	 * Confirm a login
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public IsSecurityOperationResult<UserPasswordLoginRegistrationAcknowledgeResponse> receiveLoginRegistrationAckowledge(final SecurityContext securityContext,
																		 			   	 					   			  final UserPasswordLoginRegistrationAcknowledgeRequest request);
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGE & RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Updates a login password BUT first the old password is checked
	 * @param securityContext the user auth data & context info
	 * @param login
	 * @param oldPassword
	 * @param newPassword
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public IsSecurityOperationResult<UserPasswordChangeResponse> changePassword(final SecurityContext securityContext,
																				final UserPasswordChangeRequest request);
	/**
	 * Resets a password (sets a new password) without checking the old password. 
	 * This is usually used when the user has forgotten the password and needs to reset it.
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public IsSecurityOperationResult<UserPasswordResetResponse> resetPassword(final SecurityContext securityContext,
																	   		  final UserPasswordResetRequest request);
	/**
	 * Password recovery
	 * 1.- Create a [token] and store with the [login]
	 * 2.- Send an [email] to the user with a url like http://site/endPoint/?loginOid={}&token={}
	 * 3.- When the user clicks the link the request gets to the [security REST end point] and
	 * 		a) the [login] with the received [loginOid] is loaded (the [login] contains the previously created [token])
	 * 		b) the received [token] is compared with the [token] at the [login] and the token expiration is checked
	 * 		c) if the [token] matches, the user can be redirected to the [password change ui]
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public IsSecurityOperationResult<UserPasswordRecoverResponse> recoverPassword(final SecurityContext securityContext,
																		  	      final UserPasswordRecoverRequest request);
}
