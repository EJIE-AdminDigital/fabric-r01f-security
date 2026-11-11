package r01f.model.security.login.userpassword.messages.registration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginCreateResult 
 implements EnumWithCode<Integer,UserPasswordLoginCreateResult>,
 			IsSecurityRequestResult {
	CREATED						(700 + 0),
	
	// error conditions
	PRINCIPAL_ALREADY_EXISTS	(700 + 1),	
	LOGIN_ID_ALREADY_EXISTS		(700 + 2),
	INVALID_NOTIFICATION_DATA	(700 + 3),
	INVALID_NOTIFICATION_TYPE	(700 + 4),
	NOTIFICATION_ERROR			(700 + 5);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordLoginCreateResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordLoginCreateResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordLoginCreateResult result) {
		return result != null && result.isNOTIn(CREATED);
	}
	public static boolean isOK(final UserPasswordLoginCreateResult result) {
		return result != null && result.isIn(CREATED);
	}
}
