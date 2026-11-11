package r01f.model.security.login.userpassword.messages.password.reset;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginPasswordResetResult 
 implements EnumWithCode<Integer,UserPasswordLoginPasswordResetResult>,
 			IsSecurityRequestResult {
	CHANGED							(300 + 0),
	
	// error conditions
	INVALID_LOGIN_ID				(300 + 1),		// client id not found
	UNREGISTERED_RESET_REQUEST		(300 + 2),
	TOKEN_WITH_INVALID_SIGNATURE	(300 + 3),
	TOKEN_WITH_INVALID_AUDIENCE		(300 + 4),
	TOKEN_WITH_INVALID_ISSUER		(300 + 5),
	TOKEN_EXPIRED					(300 + 6),
	TOKEN_WITH_OTHER_ERROR			(300 + 7);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordLoginPasswordResetResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordLoginPasswordResetResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordLoginPasswordResetResult result) {
		return result != null && result.isNOTIn(CHANGED);
	}
	public static boolean isOK(final UserPasswordLoginPasswordResetResult result) {
		return result != null && result.isIn(CHANGED);
	}
}
