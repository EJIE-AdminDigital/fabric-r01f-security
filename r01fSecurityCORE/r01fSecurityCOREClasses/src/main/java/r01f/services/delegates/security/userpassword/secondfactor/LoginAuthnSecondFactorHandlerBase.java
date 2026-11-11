package r01f.services.delegates.security.userpassword.secondfactor;

import lombok.extern.slf4j.Slf4j;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.model.security.login.authn.secondfactor.messages.dispatch.LoginAuthnSecondFactorDispatchResponse;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationResponse;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponseBuilder;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.services.COREServiceErrorType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.services.core.ServicesCoreModuleConfig;

@Slf4j
public abstract class LoginAuthnSecondFactorHandlerBase<P extends IsSecurityPrincipal> 
		   implements IsLoginAuthnSecondFactorHandler<P> {	
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorHandlerBase(final ServicesCoreModuleConfig coreCfg) {
		// nothing to do
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	DELIVER
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<UserPasswordLoginResponse> deliverIfNeeded(final SecurityContext securityContext, 
			                                                                    final LoginID loginId,
			                                                                    final IsLoginAuthnSecondFactor authFactor) {
		// [01] .. Checks if auth second factor must be delivered		
		if (authFactor.requiresDelivering() ) {
			LoginAuthnSecondFactorDispatchResponse dispatchResponse = _deliverImpl(securityContext,
																		    	   loginId,
																		    	   authFactor);
			if (!dispatchResponse.wasDispatched()) {
				return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.LOGIN_REQUEST)
														 .because(COREServiceErrorType.originatedAtClient()
																 					  .withName(dispatchResponse.getResult().name())
																 					  .build(),
																  dispatchResponse.getResult().name());
			}
		} else {
			log.warn("This kind of factor '{}' does not requires any delivering {}",
					 authFactor.getClass());
		}		
		// [1] .. Build a response pending on an auth factor
		P principal = (P)securityContext.getSecurityPrincipal();
		
		UserPasswordLoginResponse response = new UserPasswordLoginResponse();	
		response.setLoginId(loginId);
		response.setSecurityPrincipalOid(principal.getOid());
		response.setAuthnSecondFactor(authFactor);
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.LOGIN_REQUEST)
				                                 .returning(response);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALIDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  IsSecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
			                                                              final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest) {
		LoginAuthnSecondFactorValidationResponse validationResponse = _validateImpl(securityContext,
																                    loginValidationRequest);
		if  (!validationResponse.wasSecretValidated()) {
			log.warn("... the auth second factor validation has FAILED, because the secret was wrong");		
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.AUTH_FACTOR_VALIDATION_REQUEST)
					                                 .returning(UserPasswordLoginResponseBuilder.authSecondFactorSecretError()
					                                		 									.forLogin(loginValidationRequest.getLoginId())
													 .build());
		}
		log.info(" .. the auth factor validation was ok ");
		if (loginValidationRequest.hasMoreFactors()) {
			// At this point, once the factor has been validated, the application can send a new factor to validate
			return this.deliverIfNeeded(securityContext,
									    loginValidationRequest.getLoginId(),
										loginValidationRequest.getNextAuthFactor());
		} 
		UserPasswordLoginResponse response = UserPasswordLoginResponseBuilder.loggedIn()
																			 .forLogin(loginValidationRequest.getLoginId())
																			 .build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.AUTH_FACTOR_VALIDATION_REQUEST)
												 .returning(response);
		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////////		
	protected abstract LoginAuthnSecondFactorDispatchResponse _deliverImpl(final SecurityContext securityContext, 
					                                                       final LoginID loginId,
					                                                      final IsLoginAuthnSecondFactor authFactor);
	
	protected abstract LoginAuthnSecondFactorValidationResponse _validateImpl(final SecurityContext securityContext,
																		   	  final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest) ;
	
}
