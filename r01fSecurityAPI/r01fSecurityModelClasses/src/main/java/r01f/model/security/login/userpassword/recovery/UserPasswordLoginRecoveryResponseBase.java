package r01f.model.security.login.userpassword.recovery;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.LoginID;

@Accessors(prefix="_")
abstract class UserPasswordLoginRecoveryResponseBase
    implements UserPasswordLoginRecoveryResponse {

	private static final long serialVersionUID = -4277867847616282406L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;

	@MarshallField(as="responsedAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _responsedAt;

	@MarshallField(as="notificationSent",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Boolean _notificationSent;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasFailed() {
		return this instanceof UserPasswordLoginRecoveryResponseNOK;
	}
	@Override
	public boolean hasSucceeded() {
		return this instanceof UserPasswordLoginRecoveryResponseOK;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <R extends UserPasswordLoginRecoveryResponse> R as(final Class<R> type) {
		return (R)this;
	}
	@Override
	public UserPasswordLoginRecoveryResponseOK asResponseOK() {
		return this.as(UserPasswordLoginRecoveryResponseOK.class);
	}
	@Override
	public UserPasswordLoginRecoveryResponseNOK asResponseNOK() {
		return this.as(UserPasswordLoginRecoveryResponseNOK.class);
	}
}
