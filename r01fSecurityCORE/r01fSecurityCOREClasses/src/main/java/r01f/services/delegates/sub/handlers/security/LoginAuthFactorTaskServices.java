package r01f.services.delegates.sub.handlers.security;

import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseBase;
import r01f.securitycontext.SecurityContext;

/**
 * Services to expose methods related to authentication factors handling, at least the validation of the factor * 
 * @author PCI
 */
public interface LoginAuthFactorTaskServices {
		
	public  LoginAuthFactorValidationResponseBase validate (final SecurityContext securityContext,
			                                                final LoginAuthFactorValidationRequest request);
}
