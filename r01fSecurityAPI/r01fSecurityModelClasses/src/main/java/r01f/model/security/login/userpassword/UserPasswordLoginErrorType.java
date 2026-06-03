package r01f.model.security.login.userpassword;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginErrorType
 implements EnumWithCode<Integer,UserPasswordLoginErrorType> {
	INVALID_USER_CODE	                    (100 + 1),		// client id not found
	PASSWORD_ERROR		                    (100 + 2),
	PASSWORD_EXPIRED	                    (100 + 3),
	USER_NOT_CONFIRMED                      (100 + 4),
	AUTH_FACTOR_PHONE_OR_MAIL_NOT_REGISTRED (100 + 5),
	AUTH_FACTOR_SECRET_ERROR                (100 + 6);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
}
