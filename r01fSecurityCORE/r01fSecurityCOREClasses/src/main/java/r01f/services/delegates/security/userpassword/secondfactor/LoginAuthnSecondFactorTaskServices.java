package r01f.services.delegates.security.userpassword.secondfactor;

import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationResponse;

/**
 * Services to expose methods related to authentication factors handling, at least the validation of the factor * 
 * @author PCI
 */
public interface LoginAuthnSecondFactorTaskServices {
		
	public LoginAuthnSecondFactorValidationResponse validate(final SecurityContext securityContext,
			                                                 final IsLoginAuthnSecondFactorValidationRequest request);
}
