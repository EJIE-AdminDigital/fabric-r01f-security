package r01f.services.delegates.sub.handlers.security;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseBuilder;
import r01f.model.security.login.userpassword.UserPasswordLoginResponsePendingOnAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseBase;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.services.core.ServicesCoreModuleConfig;

@Slf4j
public abstract class LoginAuthFactorHandlerBase<U extends User> 
	implements  LoginAuthFactorHandler<U> {	
/////////////////////////////////////////////////////////////////////////////////////////
// SERVICES
/////////////////////////////////////////////////////////////////////////////////////////
	final CRUDServicesForUser<U> _userCrud;    //could be used at inherited classes 	
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorHandlerBase(final ServicesCoreModuleConfig coreCfg,
			                          //  services
                                      final CRUDServicesForUser<U> userCrud) {
		_userCrud = userCrud;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  SecurityOperationResult<UserPasswordLoginResponse> deliverIfNeeded(final SecurityContext securityContext, 
			                                                                   final LoginID  loginId, final U user,
			                                                                   final LoginAuthFactor authFactor) {
		// [ 0 ] .. Checks if auth factor must be delivered		
		if ( authFactor.requiresDelivering() ) {
			LoginAuthFactorDispatchResponse	dispatchResponse = _deliverImpl(securityContext,
																		    loginId,user,
						                                                    authFactor);
			if ( ! dispatchResponse.wasDispatched() ) {
				return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
															.returning(UserPasswordLoginResponseBuilder.forLoginId(loginId)
								                           .withError(dispatchResponse.asNOK().getErrorType())
								                          .build());
                        
			}
		} else {
			log.warn("This kind of factor '{}' does not requires any delivering {}" , authFactor.getClass());
		}		
		// [ 2] .. Build a response pending on an auth factor
		UserPasswordLoginResponsePendingOnAuthFactor<U> response = new UserPasswordLoginResponsePendingOnAuthFactor<>();	
		response.setLoginId(loginId);
		response.setAuthFactor(authFactor);
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
				                                    .returning(response);		
	}
	

	@Override
	public  SecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
			                                                            final LoginID  loginId, final U user,
																		final LoginAuthFactorValidationRequest loginValidationRequest) {
		
		LoginAuthFactorValidationResponseBase validationResponse = _validateImpl(securityContext,
																                 loginId,user,
																                 loginValidationRequest);
		if  ( ! validationResponse.wasSecretValidated()) {
			log.warn(" .. the auth factor validation has failed, because of an invalid input of the user");		
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.AUTH_FACTOR_VALIDATION_REQUEST)
					                                    .returning(UserPasswordLoginResponseBuilder.forLoginId(loginId)
																                           .withError(UserPasswordLoginErrorType.AUTH_FACTOR_SECRET_ERROR)
																	             .build());
		}
		log.info(" .. the auth factor validation was ok ");
		if ( loginValidationRequest.hasMoreFactors()) {
			//At this point, once the factor has been validated, the application can send a new factor to validate
			return  deliverIfNeeded(securityContext,
									loginId, user,
									loginValidationRequest.getNextAuthFactor());
		
		} 
		UserPasswordLoginResponse response = UserPasswordLoginResponseBuilder.forLoginId(loginId)
																				.loggedOn(user)
																			.build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.AUTH_FACTOR_VALIDATION_REQUEST)
												 .returning(response);
		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////		
	protected abstract LoginAuthFactorDispatchResponse _deliverImpl(final SecurityContext securityContext, 
			                                                        final LoginID loginId, final U user,
			                                                        final LoginAuthFactor authFactor);
	
	protected abstract LoginAuthFactorValidationResponseBase _validateImpl(final SecurityContext securityContext,
																		   final LoginID loginId,  final U user, 
																		   final LoginAuthFactorValidationRequest loginValidationRequest) ;
	
}
