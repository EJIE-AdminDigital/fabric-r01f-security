package r01f.model.security.login.userpassword.messages.password.change;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordChangeResult 
 implements EnumWithCode<Integer,UserPasswordChangeResult>,
 			IsSecurityRequestResult {
	CHANGED							(300 + 0),
	
	// error conditions
	INVALID_LOGIN_ID				(300 + 1),	// client id not found
	OLD_PASSWORD_NOT_MATCHES		(300 + 2);	// old password does not match

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordChangeResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordChangeResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordChangeResult result) {
		return result != null && result.isNOTIn(CHANGED);
	}
	public static boolean isOK(final UserPasswordChangeResult result) {
		return result != null && result.isIn(CHANGED);
	}
}
