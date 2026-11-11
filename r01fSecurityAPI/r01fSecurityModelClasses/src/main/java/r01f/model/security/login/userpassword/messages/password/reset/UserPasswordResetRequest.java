package r01f.model.security.login.userpassword.messages.password.reset;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityToken;

@MarshallType(as="userPasswordLoginResetRequest")
@Accessors(prefix="_")
public class UserPasswordResetRequest
     extends SecurityRequestBase<UserPasswordResetRequest> {

	private static final long serialVersionUID = -3008422140261489038L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="password")
	@Getter @Setter private Password _tempPassword;

	@MarshallField(as="token")
	@Getter @Setter private SecurityToken _token;
}
