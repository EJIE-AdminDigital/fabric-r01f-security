package r01f.model.security.login.userpassword.reset;

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
abstract class UserPasswordLoginResetResponseBase
    implements UserPasswordLoginResetResponse {

	private static final long serialVersionUID = -6088053849496707570L;
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
		return this instanceof UserPasswordLoginResetResponseNOK;
	}
	@Override
	public boolean hasSucceeded() {
		return this instanceof UserPasswordLoginResetResponseOK;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <R extends UserPasswordLoginResetResponse> R as(final Class<R> type) {
		return (R)this;
	}
	@Override
	public UserPasswordLoginResetResponseOK asResponseOK() {
		return this.as(UserPasswordLoginResetResponseOK.class);
	}
	@Override
	public UserPasswordLoginResetResponseNOK asResponseNOK() {
		return this.as(UserPasswordLoginResetResponseNOK.class);
	}
}
