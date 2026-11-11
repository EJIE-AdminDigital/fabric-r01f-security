package r01f.services.delegates.security.userpassword.secondfactor;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.model.security.login.authn.secondfactor.LoginAuthnSecondFactorUsingOTP;
import r01f.model.security.login.authn.secondfactor.messages.dispatch.LoginAuthnSecondFactorDispatchResponse;
import r01f.model.security.login.authn.secondfactor.messages.dispatch.LoginAuthnSecondFactorDispatchResult;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationResponse;
import r01f.oids.SecurityIDs.LoginID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;

@Slf4j
public abstract class LoginAuthnSecondFactorHandlerUsingOTPImplBase<P extends IsSecurityPrincipal> 
 			  extends LoginAuthnSecondFactorHandlerBase<P>
		   implements IsLoginAuthnSecondFactorHandler<P> {	
/////////////////////////////////////////////////////////////////////////////////////////
// 	SERVICES
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final LoginAuthnSecondFactorTaskServicesUsingOTP _authSecondFactorTaskServicesUsingOTP;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorHandlerUsingOTPImplBase(final ServicesCoreModuleConfig coreCfg,
                                             	  		 // otp service
                                             	  		 final OTPService otpService ) {		
		 super(coreCfg);
		_authSecondFactorTaskServicesUsingOTP = new LoginAuthnSecondFactorTaskServicesUsingOTP(coreCfg.getCoreAppCode().asAppCode(),
																							  otpService);
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public LoginAuthnSecondFactorDispatchResponse _deliverImpl(final SecurityContext securityContext, 
				                                               final LoginID  logiId,
				                                               final IsLoginAuthnSecondFactor authFactor) {
		if (authFactor.usesOTP()) {
		  return _deliverByOTPImpl(securityContext, 
				                   logiId,
				                   authFactor);
		} 
		return _deliverOtherImpl(securityContext, 
				                 logiId,
				                 authFactor);		
	}
	//..
	@Override
	public LoginAuthnSecondFactorValidationResponse _validateImpl(final SecurityContext securityContext,
															      final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest) {
		if (loginValidationRequest.getFactor()
								  .usesOTP()) {
			  return _validateOTPImpl(securityContext, 
					                  loginValidationRequest);
		} 
		return _validateOtherImpl(securityContext, 
                 				  loginValidationRequest);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	DELIVER
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings({ "unused","unchecked" })
	private LoginAuthnSecondFactorDispatchResponse _deliverByOTPImpl(final SecurityContext securityContext, 
			                                                         final LoginID logiId, 
	                                                                 final IsLoginAuthnSecondFactor authFactor) {
		P principal = (P)securityContext.getSecurityPrincipal();
		
		LoginAuthnSecondFactorUsingOTP authFactorByOtp = authFactor.asLoginAuthSecondFactorUsingOTP();
		LoginAuthnSecondFactorDispatchResponse authnSecondFactorResponse = null;
		
		IsContactMean contactMean = authFactorByOtp.getContactMean();
		if (contactMean != null) {
			if (_isRegisteredContactMeanOfPrincipal(principal,
													contactMean)) {
				authnSecondFactorResponse = _authSecondFactorTaskServicesUsingOTP.deliver(securityContext,
				                                                                  		  contactMean);
			} else {
				return new LoginAuthnSecondFactorDispatchResponse(LoginAuthnSecondFactorDispatchResult.INVALID_CONTACT_MEAN);
			}
		} else {
			IsContactMean defaultPrincipalContactMean = _defaultContactMeanOfPrincipal(principal,
																					   authFactorByOtp.getContactMeanType());
			if (defaultPrincipalContactMean != null) {
				authnSecondFactorResponse = _authSecondFactorTaskServicesUsingOTP.deliver(securityContext,
																						  defaultPrincipalContactMean);
			} else {
				throw new  IllegalArgumentException ("No [contact mean] to send the OTP");
			}
		}
	    return authnSecondFactorResponse;
	}
	protected  LoginAuthnSecondFactorDispatchResponse _deliverOtherImpl(final SecurityContext securityContext, 
			                                                         	final LoginID logiId,
			                                                         	final IsLoginAuthnSecondFactor authFactor) {
		throw new UnsupportedOperationException(Strings.customized("Must extend this class in order to support '{}' auth second factor", 
																   authFactor.getClass()));
	}	
/////////////////////////////////////////////////////////////////////////////////////////
// PRIVATE/PROTECTED METHODS FOR VALIDATING AUTH FACTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	private LoginAuthnSecondFactorValidationResponse _validateOTPImpl(final SecurityContext securityContext,
														              final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest) {
		P principal = (P)securityContext.getSecurityPrincipal();
		LoginAuthnSecondFactorUsingOTP authSecondFactorUsingOtp = loginValidationRequest.getFactor()
																			  		   .asLoginAuthSecondFactorUsingOTP();				
		IsLoginAuthnSecondFactorValidationRequest theValidationRequest = loginValidationRequest;
		if (authSecondFactorUsingOtp.getContactMean() == null) {
			LoginAuthnSecondFactorUsingOTP theAuthSecondFactor = null;
			IsContactMean defaultPrincipalContactMean = _defaultContactMeanOfPrincipal(principal,
																					   authSecondFactorUsingOtp.getContactMeanType());
			theAuthSecondFactor = new LoginAuthnSecondFactorUsingOTP(authSecondFactorUsingOtp.getContactMeanType(),
																	 defaultPrincipalContactMean);
			log.warn("... no {} was provided, so the otp validation will be against the [principal]'s default one: {}",
					 theAuthSecondFactor.getContactMean().getType(),
					 theAuthSecondFactor.getContactMean());
			theValidationRequest = loginValidationRequest.cloneWith(theAuthSecondFactor);
		}
		
		// validate the OTP
		return _authSecondFactorTaskServicesUsingOTP.validate(securityContext,
		    		 										  theValidationRequest);
	}
	protected LoginAuthnSecondFactorValidationResponse _validateOtherImpl(final SecurityContext securityContext,
			                                                              final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest) {
		throw new UnsupportedOperationException(Strings.customized("Must extend this class in order to support '{} validation'",
																   loginValidationRequest.getClass()));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PROTECTED METHODS FOR USER DATA
/////////////////////////////////////////////////////////////////////////////////////////	
	protected boolean _isRegisteredContactMeanOfPrincipal(final P principal, 
			                           		    	 	  final IsContactMean contactMean) {
		boolean isRegistred = principal.hasContactMean(contactMean);
		if (!isRegistred ) {
			log.info("'{}' is not a registred {} of [principal]={}", 
					 contactMean,contactMean.getType(),
					 principal.getId());
		}
		return isRegistred;
	 }	 	 
	protected Phone _defaultContactMeanOfPrincipal(final P principal,final Class<? extends IsContactMean> contactMeanType) {
		return principal.getDefaultContactMeanOrAny(contactMeanType);
	}	
}
