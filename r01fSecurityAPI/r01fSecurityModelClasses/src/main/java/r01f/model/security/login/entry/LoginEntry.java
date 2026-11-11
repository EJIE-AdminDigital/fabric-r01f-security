package r01f.model.security.login.entry;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityPersistableObject;
import r01f.model.security.SecurityPersistableObjectBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@Accessors(prefix="_")
@MarshallType(as="loginEntry")
public class LoginEntry
	 extends SecurityPersistableObjectBase<LoginEntryOID,
			  							   LoginEntry>
  implements IsSecurityPersistableObject<LoginEntryOID> {

	private static final long serialVersionUID = 6847872821140655644L;	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="event",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginEntryEvent _event;

	@MarshallField(as="at",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant  _at;
	
////////// --- LOGIN 	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginOID _loginOid;
	
	@MarshallField(as="loginId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginID _loginId;
	
////////// --- PRINCIPAL
	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _securityPrincipalOid;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public LoginEntry() {
		super();
	}
	public LoginEntry(final LoginEntryEvent event,
					  final LoginOID loginOid,final LoginID loginId,
					  final SecurityPrincipalOID principalOid) {
		this(event,
			 Instant.now(),
			 loginOid,loginId,
			 principalOid);
	}
	public LoginEntry(final LoginEntryEvent event,
					  final Instant at,
					  final LoginOID loginOid,final LoginID loginId,
					  final SecurityPrincipalOID principalOid) {
		super();
		_event = event;
		_at = at;
		_loginOid = loginOid;
		_loginId = loginId;
		_securityPrincipalOid = principalOid;
	}
}
