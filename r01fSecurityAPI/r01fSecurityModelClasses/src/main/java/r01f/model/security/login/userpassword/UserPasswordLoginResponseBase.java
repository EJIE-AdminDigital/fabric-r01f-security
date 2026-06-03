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
import r01f.securitycontext.SecurityIDS.LoginID;

@Accessors(prefix="_")
abstract class UserPasswordLoginResponseBase
    implements UserPasswordLoginResponse {

	private static final long serialVersionUID = -8942725466362564199L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="responsedAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
			   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _responsedAt;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasFailed() {
		return this instanceof UserPasswordLoginResponseNOK;
	}
	@Override
	public boolean hasSucceeded() {
		return this instanceof UserPasswordLoginResponseOK;
	}
	@Override
	public boolean isPendingOnAuthFactor() {	
		return false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <R extends UserPasswordLoginResponse> R as(final Class<R> type) {
		return (R)this;
	}
	@Override @SuppressWarnings("unchecked")
	public <U extends User> UserPasswordLoginResponseOK<U> asResponseOKFor(final Class<U> userType) {
		return this.as(UserPasswordLoginResponseOK.class);
	}
	@Override
	public UserPasswordLoginResponseNOK asResponseNOK() {
		return this.as(UserPasswordLoginResponseNOK.class);
	}
}
