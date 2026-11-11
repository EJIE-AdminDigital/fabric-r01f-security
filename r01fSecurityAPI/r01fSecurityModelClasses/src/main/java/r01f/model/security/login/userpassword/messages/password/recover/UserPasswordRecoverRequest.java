package r01f.model.security.login.userpassword.messages.password.recover;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginID;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.NotificationMedium;
import r01f.types.contact.Phone;

@MarshallType(as="userPasswordLoginRecoverRequest")
@Accessors(prefix="_")
public class UserPasswordRecoverRequest
     extends SecurityRequestBase<UserPasswordRecoverRequest> {

	private static final long serialVersionUID = -3008422140261489038L;
////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;

	@MarshallField(as="contactMean")
	@Getter @Setter private IsContactMean _contactMean;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	public NotificationMedium getUserNotificationMedium() {
		if (_contactMean == null) return null;
		
		if (_contactMean instanceof EMail) {
			return NotificationMedium.EMAIL;
		} else if (_contactMean instanceof Phone) {
			return NotificationMedium.SMS;
		} else {
			throw new IllegalStateException("User notification medium is not set: either email or phone must be set");
		}
	}
}
