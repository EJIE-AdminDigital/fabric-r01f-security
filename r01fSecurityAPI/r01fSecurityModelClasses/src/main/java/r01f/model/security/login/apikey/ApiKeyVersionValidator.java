package r01f.model.security.login.apikey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class ApiKeyVersionValidator {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public static Validates<ApiKeyVersion> createApiKeyVersionValidator() {
		return new Validates<ApiKeyVersion>() {
						@Override
						public ObjectValidationResult<ApiKeyVersion> validate(final ApiKeyVersion version) {
							if (version.getOid() == null) return ObjectValidationResultBuilder.on(version)
																							 .isNotValidBecause("The oid cannot be null");
							if (version.getName() == null) return ObjectValidationResultBuilder.on(version)
																							 .isNotValidBecause("The name cannot be null");
							if (version.getUsageRange() == null
							 || !version.getUsageRange().hasLowerBound()) return ObjectValidationResultBuilder.on(version)
																							 .isNotValidBecause("The usage range cannot be null and MUST have a lower bound");
							if (version.getId() == null) return ObjectValidationResultBuilder.on(version)
																					 		 .isNotValidBecause("The version code cannot be null");
							return ObjectValidationResultBuilder.on(version)
															    .isValid();
						}
				};
	}
}
