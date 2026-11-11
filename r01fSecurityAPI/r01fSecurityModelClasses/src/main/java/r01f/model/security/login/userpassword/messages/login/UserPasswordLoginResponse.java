package r01f.model.security.login.userpassword.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.eventbus.Event;
import r01f.model.security.SecurityResponseBase;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@Accessors(prefix="_")
public class UserPasswordLoginResponse
     extends SecurityResponseBase<UserPasswordLoginResult,
     							  UserPasswordLoginResponse> 
  implements Event {

	private static final long serialVersionUID = -8942725466362564199L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _securityPrincipalOid;
	
	@MarshallField(as="pendingAuthnSecondFactor")
    @Getter @Setter private IsLoginAuthnSecondFactor _authnSecondFactor;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginResponse() {
		// default constructor
	}
	public UserPasswordLoginResponse(final UserPasswordLoginResult result) {
		super(result);
	}
	public UserPasswordLoginResponse(final UserPasswordLoginResult result,
								     final LoginID loginId) {
		super(result);
		_loginId = loginId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean isAwaitingAuthnSecondFactorVerification() {	
		return _result != null && _result == UserPasswordLoginResult.AWAITING_AUTHN_SECOND_FACTOR_VERIFICATION;
	}
}