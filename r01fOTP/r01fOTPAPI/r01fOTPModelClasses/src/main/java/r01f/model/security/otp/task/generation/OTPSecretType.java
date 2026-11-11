package r01f.model.security.otp.task.generation;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.enums.EnumWithCodeAndData;

@Accessors(prefix="_")
public enum OTPSecretType
 implements EnumWithCodeAndData<Integer,String,OTPSecretType> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	DIGITS_ONLY(0,"0123456789"),
	LETTERS_ONLY(1,"abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	LETTERS_ONLY_MAY(2,"ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	LETTERS_ONLY_MIN(3,"abcdefghijklmnopqrstuvwxyz"),
	ALPHANUMERIC(4,"0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	ALPHANUMERIC_MAY(5,"0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"),
	ALPHANUMERIC_MIN(6,"0123456789abcdefghijklmnopqrstuvwxyz");

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Integer _code;
	@Getter private final String _pattern;
	@Getter private final Class<Integer> _codeType = Integer.class;

	 OTPSecretType(final int code,final String pattern) {
		_code = Integer.valueOf(code);
		_pattern = pattern;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public String getData() {
		return _pattern;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	 
	public static final boolean canBe(final Integer code) {
		return EnumWithCode.canBeFromCode(code,OTPSecretType.class);
	}
	public static final OTPSecretType fromString(final Integer code) {
		return EnumWithCode.fromCode(code,OTPSecretType.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + OTPSecretType.class + " enum element with code=" + code));
	}
}
