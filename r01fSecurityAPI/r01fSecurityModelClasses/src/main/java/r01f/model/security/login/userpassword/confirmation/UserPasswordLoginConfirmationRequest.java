package r01f.model.security.login.userpassword.confirmation;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.model.security.user.UserStatus;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.UserOID;

@MarshallType(as="userPasswordLoginConfirmationRequest")
@Accessors(prefix="_")
public class UserPasswordLoginConfirmationRequest 
  implements SecurityObject {

	private static final long serialVersionUID = 4167730519554321812L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=false))
	@Getter @Setter private UserOID _userOid;
	
	@MarshallField(as="userStatusAfterLoginConfirmation",
				   whenXml=@MarshallFieldAsXml(attr=false))
	@Getter @Setter private UserStatus _userStatusAfterLoginConfirmation;
	
	@MarshallField(as="requestAt",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ss"),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _requestAt;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginConfirmationRequest() {
		// default no-args constructor
	}
	public UserPasswordLoginConfirmationRequest(final UserOID userOid) {
		this(userOid,
			 UserStatus.ACTIVE);
	}
	public UserPasswordLoginConfirmationRequest(final UserOID userOid,
												final UserStatus userStatusAfterLoginConfirmation) {
		this(userOid,
			 userStatusAfterLoginConfirmation,
			 Instant.now());
	}
	public UserPasswordLoginConfirmationRequest(final UserOID userOid,
												final UserStatus userStatusAfterLoginConfirmation,
												final Instant requestedAt) {
		_userOid = userOid;
		_userStatusAfterLoginConfirmation = userStatusAfterLoginConfirmation;
		_requestAt = requestedAt;
	}
}
