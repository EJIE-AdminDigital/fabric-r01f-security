package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum LoginAuthFactorTypeKey
 implements EnumWithCode<String,LoginAuthFactorTypeKey> { // todo String x LoginAuthFactorTypeKeyID
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	OTP_BY_EMAIL("otp_by_email"),
	OTP_BY_SMS("otp_by_sms");
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static final boolean canBe(final String code) {
		return EnumWithCode.canBeFromCode(code,LoginAuthFactorTypeKey.class);
	}
	public static final LoginAuthFactorTypeKey fromString(final String code) {
		return EnumWithCode.fromCode(code,LoginAuthFactorTypeKey.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + LoginAuthFactorTypeKey.class + " element with code=" + code));
	}
}
