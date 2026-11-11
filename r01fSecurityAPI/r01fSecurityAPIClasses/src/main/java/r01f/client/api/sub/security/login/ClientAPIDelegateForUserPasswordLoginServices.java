package r01f.client.api.sub.security.login;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.UserPasswordLoginServices;
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
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;

public class ClientAPIDelegateForUserPasswordLoginServices
     extends ClientAPIServiceDelegateBase<UserPasswordLoginServices> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPasswordLoginServices(final Provider<SecurityContext>  securityContextProvider,
												 		 final Marshaller marshaller,
												 		 final UserPasswordLoginServices usrPwdServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  usrPwdServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Login without second factor validation
	 * @param loginRequest
	 * @return
	 */
	public UserPasswordLoginResponse userPasswordLogin(final UserPasswordLoginRequest loginRequest) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .userPasswordLogin(this.getSecurityContext(),
									  loginRequest)
				   .getOrThrow();
	}
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
	 * @param loginAuthSecondFactorValidationRequest
	 * @return
	 */
	public UserPasswordLoginResponse validateAuthSecondFactor(final IsLoginAuthnSecondFactorValidationRequest loginAuthSecondFactorValidationRequest) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .validateAuthSecondFactor(this.getSecurityContext(),
											 loginAuthSecondFactorValidationRequest)
				   .getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *  When a user-password login is created a notification is sent automaticaly from core (...usually a mail ).
	 *    -> If requires retry (f.e resend a mail) this method is invoked from UI.
	 * @param request
	 * @return
	 */
	public Boolean notifyLoginRegistration(final LoginID loginId) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .notifyLoginRegistration(this.getSecurityContext(),
											loginId)
				   .getOrThrow();
	}
	/**
	 * Confirm a login
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public UserPasswordLoginRegistrationAcknowledgeResponse receiveLoginRegistrationAckowledge(final UserPasswordLoginRegistrationAcknowledgeRequest request) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .receiveLoginRegistrationAckowledge(this.getSecurityContext(),
													   request)
				   .getOrThrow();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CHANGE & RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Updates a login password BUT first the old password is checked
	 * @param login
	 * @param oldPassword
	 * @param newPassword
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public UserPasswordChangeResponse changePassword(final UserPasswordChangeRequest request) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .changePassword(this.getSecurityContext(),
								   request)
				   .getOrThrow();
	}
	/**
	 * Resets a password (sets a new password) without checking the old password. 
	 * This is usually used when the user has forgotten the password and needs to reset it.
	 * @param request
	 * @return
	 */
	public UserPasswordResetResponse resetPassword(final UserPasswordResetRequest request) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .resetPassword(this.getSecurityContext(),
								  request)
				   .getOrThrow();
	}
	/**
	 * Password recovery
	 * 1.- Create a [token] and store with the [login]
	 * 2.- Send an [email] to the user with a url like http://site/endPoint/?loginOid={}&token={}
	 * 3.- When the user clicks the link the request gets to the [security REST end point] and
	 * 		a) the [login] with the received [loginOid] is loaded (the [login] contains the previously created [token])
	 * 		b) the received [token] is compared with the [token] at the [login] and the token expiration is checked
	 * 		c) if the [token] matches, the user can be redirected to the [password change ui]
	 * @param request
	 * @return
	 */
	public UserPasswordRecoverResponse recoverPassword(final UserPasswordRecoverRequest request) {
		return this.getServiceProxyAs(UserPasswordLoginServices.class)
				   .recoverPassword(this.getSecurityContext(),
									request)
				   .getOrThrow();
	}
}
