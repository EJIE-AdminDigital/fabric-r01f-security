package r01f.model.security.login.userpassword.create;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum UserPasswordLoginCreateErrorType
 implements EnumWithCode<Integer,UserPasswordLoginCreateErrorType> {
	USER_ALREADY_EXISTS			(100 + 1,true),
	LOGIN_ID_ALREADY_EXISTS		(100 + 2,true),		
	INVALID_NOTIFICATION_DATA	(100 + 3,true),
	INVALID_NOTIFICATION_TYPE	(100 + 4,true),
	NOTIFICATION_ERROR			(100 + 5,false);

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
