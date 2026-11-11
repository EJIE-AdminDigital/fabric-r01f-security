package r01f.services.delegates.security.userpassword.secondfactor;

import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.userpassword.messages.login.UserPasswordLoginResponse;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.oids.SecurityIDs.LoginID;

public interface IsLoginAuthnSecondFactorHandler<U extends IsSecurityPrincipal> {

	IsSecurityOperationResult<UserPasswordLoginResponse> deliverIfNeeded(final SecurityContext securityContex,
			                                                             final LoginID  logiId,
			                                                             final IsLoginAuthnSecondFactor authSecondFactor);
	
	IsSecurityOperationResult<UserPasswordLoginResponse> validate(final SecurityContext securityContext,
                                                                  final IsLoginAuthnSecondFactorValidationRequest loginValidationRequest);
}
