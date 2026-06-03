package r01f.model.security.user;

import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

public class UserValidator<U extends User>
  implements Validates<U> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static <U extends User> UserValidator<U> create() {
		return new UserValidator<>();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ObjectValidationResult<U> validate(final U user) {
		// validate the oid
		if (user.getOid() == null) return ObjectValidationResultBuilder.on(user)
													.isNotValidBecause("The oid cannot be null");
		// Phone and Mail validations may be null in some UserData so dont do any validation
		return ObjectValidationResultBuilder.on(user)
										    .isValid();
	}
}
