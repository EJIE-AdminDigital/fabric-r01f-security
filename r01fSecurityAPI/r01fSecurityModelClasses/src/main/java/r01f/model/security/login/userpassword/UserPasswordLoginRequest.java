package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;

@Accessors(prefix="_")
@MarshallType(as="userPasswordLoginRequest")
public class UserPasswordLoginRequest
  implements SecurityObject {

	private static final long serialVersionUID = -127968625327879670L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId")
	@Getter @Setter private LoginID  _loginId;

	@MarshallField(as="password")
	@Getter @Setter private Password _password;

	@MarshallField(as="requestedAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
			        whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _requestedAt;

	@MarshallField(as="authFactor")
    @Getter @Setter private LoginAuthFactor _authFactor;	
/////////////////////////////////////////////////////////////////////////////////////////
// METHODS                                                                       
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean hasAuthFactorToVerify() {
		return _authFactor != null;
	}	
}
