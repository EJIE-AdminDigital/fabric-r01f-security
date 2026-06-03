package r01f.model.security.login;

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
import r01f.securitycontext.SecurityOIDs.UserOID;

@MarshallType(as="loginUserLinkageEntry")
@Accessors(prefix="_")
public class LoginWithUserLinkageEntry 
  implements SecurityObject {

	private static final long serialVersionUID = -4274871206541276137L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="date",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _date;
	
	@MarshallField(as="operation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginWithUserLinkageOperation _operation;

	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserOID _userOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginWithUserLinkageEntry() {
		// default no-args constructor
	}
	public LoginWithUserLinkageEntry(final Instant date,final LoginWithUserLinkageOperation operation,final UserOID userOid) {
		_date = date;
		_operation = operation;
		_userOid = userOid;
	}
	public static LoginWithUserLinkageEntry createForLinkageToUser(final UserOID userOid) {
		return new LoginWithUserLinkageEntry(Instant.now(),LoginWithUserLinkageOperation.LOGIN_LINKED_TO_USER,userOid);
	}
	public static LoginWithUserLinkageEntry createForUnLinkageFromUser(final UserOID userOid) {
		return new LoginWithUserLinkageEntry(Instant.now(),LoginWithUserLinkageOperation.LOGIN_UNLINKED_FROM_USER,userOid);
	}
}
