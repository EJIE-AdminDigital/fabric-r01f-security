package r01f.model.security.login.userpassword.messages.password.recover;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityResponseBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.LoginOID;

@MarshallType(as="userPasswordLoginResetResponse")
@Accessors(prefix="_")
public class UserPasswordRecoverResponse
     extends SecurityResponseBase<UserPasswordLoginPasswordRecoverResult,
     							  UserPasswordRecoverResponse> {


	private static final long serialVersionUID = 96395944606115391L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="notificationSent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private boolean _notificationSent;
	
	@MarshallField(as="token",escape=true)
	@Getter @Setter private SecurityToken _token;
}
