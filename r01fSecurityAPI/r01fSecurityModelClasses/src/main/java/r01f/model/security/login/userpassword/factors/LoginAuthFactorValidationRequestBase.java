package r01f.model.security.login.userpassword.factors;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;



@Accessors(prefix="_")
@RequiredArgsConstructor
abstract class LoginAuthFactorValidationRequestBase<F extends LoginAuthFactor>
    implements LoginAuthFactorValidationRequest ,
               SecurityObject {
	private static final long serialVersionUID = 1206292544725125704L;
///////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
///////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="loginId")
	@Getter protected final LoginID  _loginId;		
	
	@MarshallField(as="secret")
	@Getter protected final Password _secret;
	
	@MarshallField(as="factor")
	@Getter @Setter protected  F _factor;
	
	@MarshallField(as="nextAuthFactor")
    @Getter @Setter private LoginAuthFactor _nextAuthFactor;		
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorValidationRequestBase(final LoginID loginId, final Password secret,
												final F factor) {
		_loginId = loginId;
		_secret = secret;
		_factor = factor;
	}

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public <V extends LoginAuthFactorValidationRequest> V as(final Class<V> type) {
		return (V)this;
	}
	@Override
	public boolean hasMoreFactors() {		
		return _nextAuthFactor != null;
	}
	
}