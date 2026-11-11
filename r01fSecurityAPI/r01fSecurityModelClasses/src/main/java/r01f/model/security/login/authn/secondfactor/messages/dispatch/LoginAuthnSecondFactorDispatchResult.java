package r01f.model.security.login.authn.secondfactor.messages.dispatch;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.model.security.IsSecurityRequestResult;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum LoginAuthnSecondFactorDispatchResult 
 implements EnumWithCode<Integer,LoginAuthnSecondFactorDispatchResult>,
 			IsSecurityRequestResult {
	DISPATCHED								 (90 + 0),
	
	// error conditions
	NOT_REGISTERED_CONTACT_MEAN			  	 (90 + 1),		// the user has no contact mean registered
	INVALID_CONTACT_MEAN                     (90 + 2);		// client id not found

	@Getter private final Integer _code;			// this do not mandatory have to be an integer
	@Getter private final Class<Integer> _codeType = Integer.class;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isError() {
		return LoginAuthnSecondFactorDispatchResult.isError(this);
	}
	@Override
	public boolean isOK() {
		return LoginAuthnSecondFactorDispatchResult.isOK(this);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static boolean isError(final LoginAuthnSecondFactorDispatchResult result) {
		return result != null && result.isNOTIn(DISPATCHED);
	}
	public static boolean isOK(final LoginAuthnSecondFactorDispatchResult result) {
		return result != null && result.isIn(DISPATCHED);
	}
}
