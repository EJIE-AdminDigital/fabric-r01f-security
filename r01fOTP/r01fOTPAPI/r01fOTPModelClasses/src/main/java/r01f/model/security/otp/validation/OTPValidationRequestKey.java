package r01f.model.security.otp.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum OTPValidationRequestKey
 implements EnumWithCode<String,OTPValidationRequestKey> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	TASK_OID("taskOid"),
	EMAIL("email"),
	SMS("sms");
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;

/////////////////////////////////////////////////////////////////////////////////////////
//  WRAPPER
/////////////////////////////////////////////////////////////////////////////////////////
	public static final boolean canBe(final String code) {
		return EnumWithCode.canBeFromCode(code,OTPValidationRequestKey.class);
	}
	public static final OTPValidationRequestKey fromString(final String code) {
		return EnumWithCode.fromCode(code,OTPValidationRequestKey.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + OTPValidationRequestKey.class + " element with code=" + code));
	}
}
