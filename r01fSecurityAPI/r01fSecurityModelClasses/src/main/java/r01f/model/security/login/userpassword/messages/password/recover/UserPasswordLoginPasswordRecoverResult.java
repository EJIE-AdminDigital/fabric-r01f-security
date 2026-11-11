package r01f.model.security.login.userpassword.messages.password.recover;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginPasswordRecoverResult 
 implements EnumWithCode<Integer,UserPasswordLoginPasswordRecoverResult>,
 			IsSecurityRequestResult {
	RECOVER_REQ_REGISTERED		(400 + 0),
	
	// error conditions
	INVALID_LOGIN_ID			(400 + 1),		// client id not found
	INVALID_NOTIFICATION_DATA	(400 + 2),
	INVALID_NOTIFICATION_TYPE	(400 + 3),
	NOTIFICATION_ERROR			(400 + 4);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordLoginPasswordRecoverResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordLoginPasswordRecoverResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordLoginPasswordRecoverResult result) {
		return result != null && result.isNOTIn(RECOVER_REQ_REGISTERED);
	}
	public static boolean isOK(final UserPasswordLoginPasswordRecoverResult result) {
		return result != null && result.isIn(RECOVER_REQ_REGISTERED);
	}
}
