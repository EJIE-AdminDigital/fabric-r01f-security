package r01f.model.security.login.userpassword.messages.password.change;

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

@MarshallType(as="userPasswordChangeRequest")
@Accessors(prefix="_")
public class UserPasswordChangeRequest
     extends SecurityRequestBase<UserPasswordChangeRequest> {

	private static final long serialVersionUID = -3008422140261489038L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="oldPassword")
	@Getter @Setter private Password _oldPassword;
	
	@MarshallField(as="newPassword")
	@Getter @Setter private Password _newPassword;

	@MarshallField(as="token")
	@Getter @Setter private SecurityToken _token;
}
