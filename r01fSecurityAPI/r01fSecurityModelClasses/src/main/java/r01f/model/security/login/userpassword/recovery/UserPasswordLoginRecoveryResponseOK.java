package r01f.model.security.login.userpassword.recovery;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@MarshallType(as="userPasswordRecoveryResponseOK")
@Accessors(prefix="_")
public class UserPasswordLoginRecoveryResponseOK
     extends UserPasswordLoginRecoveryResponseBase {

	private static final long serialVersionUID = -7740491946832131146L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="token",escape=true)
	@Getter @Setter private SecurityToken _token;
}
