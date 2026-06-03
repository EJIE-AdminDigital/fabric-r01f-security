package r01f.services.delegates.sub.handlers.security;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseNOK;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBySMS;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPBySMS;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseBase;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

@Slf4j
public abstract class LoginAuthFactorHandlerUsingOTPImplBase<U extends User & HasMailAddress & HasPhoneNumber> 
 			  extends LoginAuthFactorHandlerBase<U>
		   implements LoginAuthFactorHandler<U> {	
/////////////////////////////////////////////////////////////////////////////////////////
// SERVICES
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final LoginAuthFactorTaskServicesUsingOTPByEMail _authFactorTaskServicesForOTPWithMail;
	protected final LoginAuthFactorTaskServicesUsingOTPBySMS   _authFactorTaskServicesForOTPWithSMS;	
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorHandlerUsingOTPImplBase(final ServicesCoreModuleConfig coreCfg,
			                                     //  services
                                             	  final CRUDServicesForUser<U> userCrud,  
                                             	  // otp service
                                             	  final OTPService otpService ) {		
		 super(coreCfg,
			   userCrud);
		_authFactorTaskServicesForOTPWithMail = new LoginAuthFactorTaskServicesUsingOTPByEMail(coreCfg.getCoreAppCode(),otpService);
		_authFactorTaskServicesForOTPWithSMS =  new LoginAuthFactorTaskServicesUsingOTPBySMS(coreCfg.getCoreAppCode(),otpService);
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  LoginAuthFactorDispatchResponse _deliverImpl(final SecurityContext securityContext, 
			                                             final LoginID  logiId, final U user,
			                                             final LoginAuthFactor authFactor) {
		if (authFactor.isByOTP()) {
		  return _deliverByOTPImpl(securityContext, 
				                   logiId, user, 
				                   authFactor);
		} 
		return _deliverOtherImpl(securityContext, 
				                 logiId, user, 
				                 authFactor);		
	}
	//..
	@Override
	public  LoginAuthFactorValidationResponseBase _validateImpl(final SecurityContext securityContext,
			                                                    final LoginID  logiId, final U user,
															    final LoginAuthFactorValidationRequest loginValidationRequest) {
		if (loginValidationRequest.getFactor().isByOTP()) {
			  return _validateOTPImpl(securityContext, 
					                  logiId, user, 
					                  loginValidationRequest);
		} 
		return _validateOTPImpl(securityContext, 
                 				 logiId, user, 
                 				 loginValidationRequest);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE/PROTECTED METHODS FOR DELIVERING
/////////////////////////////////////////////////////////////////////////////////////////	
	
	@SuppressWarnings("unused")
	private LoginAuthFactorDispatchResponseBase _deliverByOTPImpl(final SecurityContext securityContext, 
			                                                      final LoginID  logiId, final U user,
	                                                              final LoginAuthFactor authFactor) {		
		LoginAuthFactorUsingOTPBase authFactorByOtp = authFactor.as(LoginAuthFactorUsingOTPBase.class);
		LoginAuthFactorDispatchResponseBase loginAuthFactorDispatchResponse = null;
		//	...by sms
		if ( authFactorByOtp.isBySms()) {
			Phone phone = authFactor.as(LoginAuthFactorUsingOTPBySMS.class)
								    .getPhone();
			if ( phone != null  ) {
				if (_isRegisteredPhoneOfUser(user,phone)) {
					loginAuthFactorDispatchResponse = _authFactorTaskServicesForOTPWithSMS.deliver(securityContext,
					                                                                              phone);
				} else {
					return new LoginAuthFactorDispatchResponseNOK(Instant.now(),
							                                      UserPasswordLoginErrorType.AUTH_FACTOR_PHONE_OR_MAIL_NOT_REGISTRED);
				}				
			} else {
				Phone defaultUserPhone = _defaultPhoneOfUser(user);
				if (defaultUserPhone != null) {
					loginAuthFactorDispatchResponse = _authFactorTaskServicesForOTPWithSMS.deliver(securityContext,
																							       defaultUserPhone);
				} else {
					throw new  IllegalArgumentException ("No phone to send the OTP");
				}
			}
		//	...by mail	
		} else if ( authFactorByOtp.isByMail()) {
			EMail email = authFactor.as(LoginAuthFactorUsingOTPByEMail.class)
									.getMail();
			if  ( email != null ) {				
				if ( _isRegistredEMailOfUser(user,email)) {
					loginAuthFactorDispatchResponse = _authFactorTaskServicesForOTPWithMail.deliver(securityContext,
																									email);
				} else {
					return new LoginAuthFactorDispatchResponseNOK(Instant.now(),
							                                      UserPasswordLoginErrorType.AUTH_FACTOR_PHONE_OR_MAIL_NOT_REGISTRED);
				}		
				
			} else {				
				EMail defaultUserEmail  = _defaultEMailOfUser(user);
				if (defaultUserEmail != null) {
					loginAuthFactorDispatchResponse = _authFactorTaskServicesForOTPWithMail.deliver(securityContext,
																									defaultUserEmail);
				} else {
					throw new  IllegalArgumentException (" No mail to send the OTP");
				}				
			}
		} 
	    return loginAuthFactorDispatchResponse;
	}
	protected  LoginAuthFactorDispatchResponseBase _deliverOtherImpl(final SecurityContext securityContext, 
			                                                         final LoginID  logiId, final U user,
	                                                                 final LoginAuthFactor authFactor) {
		throw new UnsupportedOperationException(Strings.customized("Must extend this class in order tu support '{}' auth afactor", authFactor.getClass()));
	
	}	
/////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE/PROTECTED METHODS FOR VALIDATING AUTH FACTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	private  LoginAuthFactorValidationResponseBase _validateOTPImpl(final SecurityContext securityContext,
														            final LoginID  logiId, final U user,
														            final LoginAuthFactorValidationRequest loginValidationRequest) {
		
		LoginAuthFactorUsingOTPBase authFactorByOtp = loginValidationRequest.getFactor().as(LoginAuthFactorUsingOTPBase.class);
		LoginAuthFactorValidationResponseBase validationResponse = null;		
		if (authFactorByOtp.isBySms()) {			
			LoginAuthFactorValidationRequestUsingOTPBySMS authFactorValidationByPhone = loginValidationRequest.as(LoginAuthFactorValidationRequestUsingOTPBySMS.class);
			if (authFactorValidationByPhone.getPhone() == null) {
				log.warn(" .. no phone was provided, so the otp validation will be against the user's default one");
				authFactorValidationByPhone.setFactor(new LoginAuthFactorUsingOTPBySMS(_defaultPhoneOfUser(user)));				
			}
		    validationResponse = _authFactorTaskServicesForOTPWithSMS.validate(securityContext,
		    		 														   authFactorValidationByPhone);
		} else  if (authFactorByOtp.isByMail()) {			
			LoginAuthFactorValidationRequestUsingOTPByEMail authFactorValidationByMail = loginValidationRequest.as(LoginAuthFactorValidationRequestUsingOTPByEMail.class);
			if (authFactorValidationByMail.getEMail() == null) {
				log.warn(" .. no email was provided, so the otp validation will be against the user's default one");
				authFactorValidationByMail.setFactor(new LoginAuthFactorUsingOTPByEMail(_defaultEMailOfUser(user)));				
			}

			validationResponse = _authFactorTaskServicesForOTPWithMail.validate(securityContext,
					                                                            authFactorValidationByMail);
		} else {
			throw new IllegalArgumentException(" Not implemented auth factortype");
		}
		return validationResponse;
	}
	
	protected  LoginAuthFactorValidationResponseBase _validateOtherImpl(final SecurityContext securityContext,
			                                                            final LoginID  logiId, final U user,
			                                                            final LoginAuthFactorValidationRequest loginValidationRequest) {
		throw new UnsupportedOperationException(Strings.customized("Must extend this class in order tu support '{} validation'", loginValidationRequest.getClass()));
		
	}
	
/////////////////////////////////////////////////////////////////////////////////////////
//	PROTECTED METHODS FOR USER DATA
/////////////////////////////////////////////////////////////////////////////////////////	
	 protected boolean _isRegisteredPhoneOfUser(final U user, 
			                           		    final Phone phone ) {
		 // ..if the user phone is included into the ContactData use this method ,  otherwise ( f.e  another table, etc... ) @override this method
		 boolean isRegistred = user.getPhoneNumber() != null;
		 if (! isRegistred ) {
			 log.info("'{}' is not a registred phone of user '{}'. Registred phone is : {}", 
					  phone,
					  user.getPhoneNumber() != null ? "<<<<Not contact data or no phones configured : Check User Data Descriptor at DB, etc..(xml,json..)>>>"
					 								: user.getPhoneNumber());
		 }
		 return isRegistred;
	 }	 	 
	 protected Phone _defaultPhoneOfUser(final U user) {
		 // ..if the user phone is included use this method ,  otherwise ( f.e  another table, etc... ) @override this method
		 return user.getPhoneNumber();
	 }	
	 protected boolean _isRegistredEMailOfUser(final U user, 
			 								   final EMail email ) {
		 // ..if the user email is included use this method ,  otherwise ( f.e  another table, etc... ) @override this method
		 return user.getMailAddress() != null ? user.getMailAddress().equals(email) 
				 							  : false;
	 }		 
	 protected EMail _defaultEMailOfUser(final U user) {
		 // ..if the user email is included use this method,  otherwise ( f.e, another table, etc... ) @override this method
		 return user.getMailAddress();
	}	
}
