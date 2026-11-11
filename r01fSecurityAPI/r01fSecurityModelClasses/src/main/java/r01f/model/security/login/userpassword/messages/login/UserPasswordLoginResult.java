package r01f.model.security.login.userpassword.messages.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum UserPasswordLoginResult 
 implements EnumWithCode<Integer,UserPasswordLoginResult>,
 			IsSecurityRequestResult {
	LOGGED_IN								 (100 + 0),
	AWAITING_AUTHN_SECOND_FACTOR_VERIFICATION(100 + 1),
	
	// error conditions
	INVALID_LOGIN_ID	                     (100 + 10),		// client id not found
	PASSWORD_ERROR		                     (100 + 11),
	PASSWORD_EXPIRED	                     (100 + 12),
	LOGIN_NOT_ACTIVE                         (100 + 13),
	AUTHN_SECOND_FACTOR_MEAN_NOT_REGISTRED 	 (100 + 14),
	AUTHN_SECOND_FACTOR_SECRET_ERROR         (100 + 15);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordLoginResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordLoginResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordLoginResult result) {
		return result != null && result.isNOTIn(LOGGED_IN,AWAITING_AUTHN_SECOND_FACTOR_VERIFICATION);
	}
	public static boolean isOK(final UserPasswordLoginResult result) {
		return result != null && result.isIn(LOGGED_IN,AWAITING_AUTHN_SECOND_FACTOR_VERIFICATION);
	}
}
