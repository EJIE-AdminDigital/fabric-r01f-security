package r01f.model.security.login.userpassword.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityRequestBase;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;

@Accessors(prefix="_")
@MarshallType(as="userPasswordLoginRequest")
public class UserPasswordLoginRequest
     extends SecurityRequestBase<UserPasswordLoginRequest> {

	private static final long serialVersionUID = -127968625327879670L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId")
	@Getter @Setter private LoginID  _loginId;

	@MarshallField(as="password")
	@Getter @Setter private Password _password;

	@MarshallField(as="authnSecondFactor")
    @Getter @Setter private IsLoginAuthnSecondFactor _authnSecondFactor;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginRequest() {
		super();
	}
	public UserPasswordLoginRequest(final LoginID loginId,final Password password) {
		super();
		_loginId = loginId;
		_password = password;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS                                                                       
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasToPassAuthnSecondFactorVerification() {
		return _authnSecondFactor != null;
	}	
}
