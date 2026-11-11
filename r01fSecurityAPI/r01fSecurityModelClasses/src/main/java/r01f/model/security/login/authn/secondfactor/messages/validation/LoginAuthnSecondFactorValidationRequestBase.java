package r01f.model.security.login.authn.secondfactor.messages.validation;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityRequestBase;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Secret;

@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class LoginAuthnSecondFactorValidationRequestBase<F extends IsLoginAuthnSecondFactor,
														   SELF_TYPE extends LoginAuthnSecondFactorValidationRequestBase<F,SELF_TYPE>>
	   extends SecurityRequestBase<SELF_TYPE>
    implements IsLoginAuthnSecondFactorValidationRequest {
	
	private static final long serialVersionUID = 1206292544725125704L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="loginId")
	@Getter protected final LoginID  _loginId;		
	
	@MarshallField(as="secret")
	@Getter protected final Secret _secret;
	
	@MarshallField(as="factor")
	@Getter @Setter protected F _factor;
	
	@MarshallField(as="nextAuthFactor")
    @Getter @Setter private IsLoginAuthnSecondFactor _nextAuthFactor;		
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorValidationRequestBase(final LoginID loginId,final Secret secret,
													   final F factor) {
		super();
		_loginId = loginId;
		_secret = secret;
		_factor = factor;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasMoreFactors() {		
		return _nextAuthFactor != null;
	}
}
