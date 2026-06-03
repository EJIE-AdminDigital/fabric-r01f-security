package r01f.model.security.operation;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.enums.EnumExtended;
import r01f.model.services.COREServiceMethod;

@Accessors(prefix="_")
public enum SecurityOperation
 implements EnumExtended<SecurityOperation> {
	REGISTER,
	LOGIN_REQUEST,
	PASSWORD_RECOVERY_REQUEST,
	NOTIFY_PASSWORD_RECOVERY,
	PASSWORD_RESET_REQUEST,
	PASSWORD_MODIFY_REQUEST,
	AUTH_FACTOR_VALIDATION_REQUEST,
	USER_DATA_REQUEST_VIA_JWT,
	ACTIVATE_USER,
	OTHER;
	
	@Getter private final COREServiceMethod _COREServiceMethod = COREServiceMethod.named(this.name());
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public static SecurityOperation fromName(final String name) {
		return EnumExtended.fromName(name,SecurityOperation.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + SecurityOperation.class + " element with name=" + name));
	}
	public static SecurityOperation from(final COREServiceMethod method) {
		return SecurityOperation.fromName(method.asString());
	}
}