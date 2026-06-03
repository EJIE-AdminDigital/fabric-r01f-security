package r01f.model.security.login.userpassword.recovery;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;
import r01f.types.contact.NotificationMedium;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
@MarshallType(as="userPasswordRecoveryRequest")
public class UserPasswordLoginRecoveryRequest
  implements SecurityObject {

	private static final long serialVersionUID = -2830273065723865959L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;

	@MarshallField(as="requestAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _requestAt;

	@MarshallField(as="email",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _email;

	@MarshallField(as="phone",
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone _phone;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public NotificationMedium getUserNotificationMedium() {
		if (_email != null) {
			return NotificationMedium.EMAIL;
		} else if (_phone != null) {
			return NotificationMedium.SMS;
		} 
		return null;
	}
}
