package r01f.model.security.login.userpassword.recovery;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginRecoveryErrorType
 implements EnumWithCode<Integer,UserPasswordLoginRecoveryErrorType> {
	INVALID_LOGIN_ID			(100 + 1,true),		// client id not found
	INVALID_NOTIFICATION_DATA	(100 + 2,true),
	INVALID_NOTIFICATION_TYPE	(100 + 3,true),
	NOTIFICATION_ERROR			(100 + 4,false);

	@Getter private final Class<Integer> _codeType = Integer.class;
	@Getter private final Integer _code;			// this do not mandatory have to be an integer
			private final boolean _clientError;
			
	public COREServiceErrorType asCOREServiceErrorType() {
		return COREServiceErrorType.originatedAt(_clientError ? COREServiceErrorOrigin.CLIENT : COREServiceErrorOrigin.SERVER)
					   			   .withName(this.name())
					   			   .coded(_code,0)
					   			   .severity(ExceptionSeverity.DEFAULT)
					   			   .build();
	}
}
