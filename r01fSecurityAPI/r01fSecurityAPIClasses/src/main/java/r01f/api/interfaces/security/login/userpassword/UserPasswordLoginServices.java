package r01f.api.interfaces.security.login.userpassword;

import r01f.api.interfaces.security.SecurityServiceInterface;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationRequest;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationResponse;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetRequest;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;

public interface UserPasswordLoginServices<U extends User & HasMailAddress & HasPhoneNumber,
										   L extends UserPasswordLogin>
		 extends SecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Create a login
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public SecurityOperationResult<UserPasswordLoginCreateResponse> createLogin(final SecurityContext securityContext,
																				final UserPasswordLoginCreateRequest<U> request);


	/**
	 *  When a user-password login is created a notification is sent automaticaly from core (..usually a mail ).
	 *    -> If requires  retry ( f.e resend a mail) this method is invoked from UI.
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public SecurityOperationResult<Boolean> notifyLoginRegistration(final SecurityContext securityContext,
			                                                        final LoginID loginId);

	/**
	 * Confirm a login
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public SecurityOperationResult<UserPasswordLoginConfirmationResponse> confirmLogin(final SecurityContext securityContext,
																		 			   final UserPasswordLoginConfirmationRequest request);


/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Login
	 * @param securityContext
	 * @param loginRequest
	 * @return
	 */
	public SecurityOperationResult<UserPasswordLoginResponse> login(final SecurityContext securityContext,
																    final UserPasswordLoginRequest loginRequest);

	/**
	 * Login validate auth factor :
		 //  Login auth factor flow
	    // 		1.- Deliver a secret to the user (if needed), for example an otp or other type of factors with no need of delivering ( f.e a user known data)
	    // 		2.- Return an UserLoginResponsePendingOnAuthFactor
	    // 		3.  Show a form asking the secret ( otp, etc. )
		//      4.- When the user post the secret, call to ' validate(..  final LoginAuthFactorValidationRequest<T> loginValidationRequest)
	    // 				a) Validate the secret		//
	    // 				b) it the [secret] matches and there is no more
	 *
	 * @param securityContext
	 * @param loginAuthFactorValidationRequest
	 * @return
	 */
	public  SecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
		                                                                final LoginAuthFactorValidationRequest loginAuthFactorValidationRequest);
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET & RECOVER PASSWORD
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Resets a password
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public SecurityOperationResult<UserPasswordLoginResetResponse> resetPassword(final SecurityContext securityContext,
																	   			 final UserPasswordLoginResetRequest request);

	/**
	 * Password recovery
	 * 		1.- Create a [login entry] with id=PASSWORD_RECOVERY_REQUEST and a [token]
	 * 		2.- Send an [email] to the user with a url like http://site/endPoint/?userOid={}&token={}
	 * 		3.- When the user clicks the link the request gets to the [security REST end point] and
	 * 				a) a [login entry] with id=PASSWORD_RECOVERY_REQUEST and loginId={} is loaded
	 * 				b) the [token] at the [login entry] is compared with the received one
	 * 				c) it the [token] matches, the user can be redirected to the [password change ui]
	 * @param securityContext
	 * @param request
	 * @return
	 */
	public SecurityOperationResult<UserPasswordLoginRecoveryResponse> recoverPassword(final SecurityContext securityContext,
																		  			  final UserPasswordLoginRecoveryRequest request);



}
