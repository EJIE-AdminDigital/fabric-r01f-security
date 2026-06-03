package r01f.cwt.model;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
public enum CWTRegistredClaims
	 implements EnumWithCode<Integer,CWTRegistredClaims> {

		ISS (1),
		SUB (2),
		AUD (3),
		EXP(4),
		NBF(5),
		IAT(6),
		CTI (7),
		CNF (8),
		SCOPE (9),
		NONCE (10);


	@Getter private final  Integer _code;
	@Getter private final  Class<Integer>  _codeType = Integer.class;


	CWTRegistredClaims(final int code) {
			_code = code;
	}
	public static boolean exists(final int value) {
		return EnumWithCode.canBeFromCode(value,CWTRegistredClaims.class);
	}
}


