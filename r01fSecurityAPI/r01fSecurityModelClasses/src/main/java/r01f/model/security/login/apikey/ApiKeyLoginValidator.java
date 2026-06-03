package r01f.model.security.login.apikey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class ApiKeyLoginValidator {
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public static <A extends ApiKeyLogin> Validates<A> createApiKeyValidator() {
		return new Validates<A>() {
						@Override
						public ObjectValidationResult<A> validate(final A apiKey) {
							// validate the oid
							if (apiKey.getOid() == null) return ObjectValidationResultBuilder.on(apiKey)
																							 .isNotValidBecause("The oid cannot be null");
							if (apiKey.getLoginId() == null) return ObjectValidationResultBuilder.on(apiKey)
																								 .isNotValidBecause("The [loginId] (the same as the [public key] cannot be null");
							if (apiKey.getApiKeyOwnerOid() == null) return ObjectValidationResultBuilder.on(apiKey)
																										.isNotValidBecause("The owner cannot be null");
							if (apiKey.getApiKeyUsageRange() == null
							 || !apiKey.getApiKeyUsageRange().hasLowerBound()) return ObjectValidationResultBuilder.on(apiKey)
									 																			   .isNotValidBecause("The usage range cannot be null and MUST have a lower bound");
							if (apiKey.getApiKeyVersion() == null) return ObjectValidationResultBuilder.on(apiKey)
																							 		   .isNotValidBecause("The version cannot be null");
							return ObjectValidationResultBuilder.on(apiKey)
															    .isValid();
						}
				};
	}
}
