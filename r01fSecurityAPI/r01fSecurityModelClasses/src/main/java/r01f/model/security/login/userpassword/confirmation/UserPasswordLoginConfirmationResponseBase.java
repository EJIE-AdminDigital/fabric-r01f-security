package r01f.model.security.login.userpassword.confirmation;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.user.UserStatus;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

@Accessors(prefix="_")
public class UserPasswordLoginConfirmationResponseBase
  implements UserPasswordLoginConfirmationResponse {

	private static final long serialVersionUID = -6118544836852486080L;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private UserOID _userOid;
	
	@MarshallField(as="loginOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private LoginID _loginId;
	
	@MarshallField(as="userStatusAfterLoginConfirmation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Setter @Getter private UserStatus _userStatusAfterLoginConfirmation;

	@MarshallField(as="responsedAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _responsedAt;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean hasFailed() {
		return this instanceof UserPasswordLoginConfirmationResponseNOK;
	}
	@Override
	public boolean hasSucceeded() {
		return this instanceof UserPasswordLoginConfirmationResponseOK;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <R extends UserPasswordLoginConfirmationResponse> R as(final Class<R> type) {
		return (R)this;
	}
	@Override
	public UserPasswordLoginConfirmationResponseOK asResponseOK() {
		return this.as(UserPasswordLoginConfirmationResponseOK.class);
	}
	@Override
	public UserPasswordLoginConfirmationResponseNOK asResponseNOK() {
		return this.as(UserPasswordLoginConfirmationResponseNOK.class);
	}
}