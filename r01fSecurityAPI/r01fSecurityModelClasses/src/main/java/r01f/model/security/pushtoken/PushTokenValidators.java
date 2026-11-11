package r01f.model.security.pushtoken;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.util.types.Strings;
import r01f.validation.ObjectValidationResult;
import r01f.validation.ObjectValidationResultBuilder;
import r01f.validation.Validates;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class PushTokenValidators {
/////////////////////////////////////////////////////////////////////////////////////////
//	Push Notification Token
/////////////////////////////////////////////////////////////////////////////////////////
	public static <T extends IsPushToken> Validates<T> createPhoneTokenObjectValidator() {
		return new Validates<>() {
						@Override
						public ObjectValidationResult<T> validate(final T obj) {
							// this valid?
							ObjectValidationResult<T> thisValidResult = null;						
						
							if ((obj.getDestinationId() == null || !obj.getDestinationId().isValid())
							 || (obj.getSourceSystemId() == null || !obj.getSourceSystemId().isValid())
							 || (obj.getToken() != null && !obj.getToken().isValid())) {							
								thisValidResult = ObjectValidationResultBuilder.on(obj)
																			   .isNotValidBecause(Strings.customized("token id='{}', system id ='{}', token ='{}'",
																					   										obj.getDestinationId(),
																					   										obj.getSourceSystemId(),obj.getToken()));
							}
							// combine
							return thisValidResult;
						}
			  };
	}
}
