package r01f.services.delegates.sub.handlers.security;

import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;

public interface LoginAuthFactorHandler<U extends User> {

	SecurityOperationResult<UserPasswordLoginResponse>  deliverIfNeeded( final SecurityContext securityContex,
			                                                             final LoginID  logiId, final U user,
			                                                             final LoginAuthFactor authFactor);
	
	SecurityOperationResult<UserPasswordLoginResponse> validate( final SecurityContext securityContext,
			                                                     final LoginID  logiId, final U user,
                                                                 final LoginAuthFactorValidationRequest loginValidationRequest);
}
