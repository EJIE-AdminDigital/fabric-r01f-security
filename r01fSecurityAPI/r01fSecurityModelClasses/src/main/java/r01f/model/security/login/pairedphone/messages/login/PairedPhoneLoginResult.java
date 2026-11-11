package r01f.model.security.login.pairedphone.messages.login;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PairedPhoneLoginResult
 implements EnumWithCode<Integer,PairedPhoneLoginResult>,
 			IsSecurityRequestResult {
	CONSENT_GRANTED		(90 + 1),
	CONSENT_NOT_GRANTED	(90 + 2),
	ERROR				(90 + 9);
	
	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return PairedPhoneLoginResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return PairedPhoneLoginResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final PairedPhoneLoginResult result) {
		return result != null && result.isNOTIn(CONSENT_GRANTED,CONSENT_NOT_GRANTED);
	}
	public static boolean isOK(final PairedPhoneLoginResult result) {
		return result != null && result.isIn(CONSENT_GRANTED,CONSENT_NOT_GRANTED);
	}
}
