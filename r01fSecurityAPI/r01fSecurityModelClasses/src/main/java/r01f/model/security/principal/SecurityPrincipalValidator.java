package r01f.model.security.principal;

import r01f.model.security.IsSecurityPrincipal;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

public class SecurityPrincipalValidator<P extends IsSecurityPrincipal>
  implements Validates<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static <P extends IsSecurityPrincipal> SecurityPrincipalValidator<P> create() {
		return new SecurityPrincipalValidator<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<P> validate(final P securityPrincipal) {
		// validate the oid
		if (securityPrincipal.getOid() == null) return ObjectValidationResultBuilder.on(securityPrincipal)
																					.isNotValidBecause("The oid cannot be null");
		return ObjectValidationResultBuilder.on(securityPrincipal)
										    .isValid();
	}
}
