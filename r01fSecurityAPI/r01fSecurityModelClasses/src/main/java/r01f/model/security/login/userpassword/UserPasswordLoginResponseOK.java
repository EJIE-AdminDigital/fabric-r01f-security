package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.user.User;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;

@MarshallType(as="userPasswordResponseOK")
@Accessors(prefix="_")
public class UserPasswordLoginResponseOK<U extends User>
  	 extends UserPasswordLoginResponseBase {

	private static final long serialVersionUID = 3781076322206964020L;

/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="loggedOn",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _loggedOn;
	
	@MarshallField(as="user")
	@Getter @Setter private U _user;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid() {
		return _user != null ? _user.getOid()
							 : null;
	}
}
