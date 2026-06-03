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
	public static <T extends PushToken> Validates<T> createPhoneTokenObjectValidator() {
		return new Validates<>() {
						@Override
						public ObjectValidationResult<T> validate(final T obj) {
							// this valid?
							ObjectValidationResult<T> thisValidResult = null;						
						
							if ((obj.getPushTokenId() == null || !obj.getPushTokenId().isValid())
							 || (obj.getSystemId() == null || !obj.getSystemId().isValid())
							 || (obj.getToken() != null && !obj.getToken().isValid())) {							
								thisValidResult = ObjectValidationResultBuilder.on(obj)
																			   .isNotValidBecause(Strings.customized("token id='{}', system id ='{}', token ='{}'",
																					   										obj.getPushTokenId(),
																					   										obj.getSystemId(),obj.getToken()));
							}
							// combine
							return thisValidResult;
						}
			  };
	}
}
