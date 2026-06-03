package r01f.model.security.login.entry;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.PersistableSecurityObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserLoginEntryOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

@Accessors(prefix="_")
@MarshallType(as="loginEntry")
public abstract class UserLoginEntryBase<SELF_TYPE extends UserLoginEntryBase<SELF_TYPE>>
			  extends PersistableSecurityObjectBase<UserLoginEntryOID,SELF_TYPE>
		   implements UserLoginEntry {

	private static final long serialVersionUID = 6847872821140655644L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="id",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserLoginEntryID _id;
	
	@MarshallField(as="date",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant  _date;
	
	@MarshallField(as="securityProviderId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityProviderID _securityProviderId;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginID  _loginId;
	
	@MarshallField(as="userOid",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private UserOID _userOid;
	
	@MarshallField(as="token",escape=true)
	@Getter @Setter private SecurityToken _token;
	
	@MarshallField(as="impersonatedUserOid")
	@Getter @Setter private UserOID _impersonatedUserOid;
}
