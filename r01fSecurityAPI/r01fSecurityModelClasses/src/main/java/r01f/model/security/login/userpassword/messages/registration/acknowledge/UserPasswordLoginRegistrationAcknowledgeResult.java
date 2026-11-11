package r01f.model.security.login.userpassword.messages.registration.acknowledge;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginRegistrationAcknowledgeResult 
 implements EnumWithCode<Integer,UserPasswordLoginRegistrationAcknowledgeResult>,
 			IsSecurityRequestResult {
	CONFIRMED					 (200 + 0),
	
	// error conditions
	INVALID_LOGIN_ID			 (200 + 2),
	lOGIN_COULD_NOT_BE_ACTIVATED (200 + 3);

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return UserPasswordLoginRegistrationAcknowledgeResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return UserPasswordLoginRegistrationAcknowledgeResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final UserPasswordLoginRegistrationAcknowledgeResult result) {
		return result != null && result.isNOTIn(CONFIRMED);
	}
	public static boolean isOK(final UserPasswordLoginRegistrationAcknowledgeResult result) {
		return result != null && result.isIn(CONFIRMED);
	}
}
