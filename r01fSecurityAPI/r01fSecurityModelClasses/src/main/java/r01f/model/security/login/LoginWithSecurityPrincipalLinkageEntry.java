package r01f.model.security.login;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

@MarshallType(as="loginSecurityPrincipalLinkageEntry")
@Accessors(prefix="_")
public class LoginWithSecurityPrincipalLinkageEntry 
  implements IsSecurityObject {

	private static final long serialVersionUID = -4274871206541276137L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="at",dateFormat=@MarshallDateFormat(use=DateFormat.ISO8601),
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _at;
	
	@MarshallField(as="operation",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private LoginWithSecurityPrincipalLinkageOperation _operation;

	@MarshallField(as="securityPrincipalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _securityPrincipalOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginWithSecurityPrincipalLinkageEntry() {
		// default no-args constructor
	}
	public LoginWithSecurityPrincipalLinkageEntry(final Instant date,final LoginWithSecurityPrincipalLinkageOperation operation,final SecurityPrincipalOID securityPrincipalOid) {
		_at = date;
		_operation = operation;
		_securityPrincipalOid = securityPrincipalOid;
	}
	public static LoginWithSecurityPrincipalLinkageEntry createForLinkageToSecurityPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
		return new LoginWithSecurityPrincipalLinkageEntry(Instant.now(),LoginWithSecurityPrincipalLinkageOperation.LOGIN_LINKED_TO_SECURITY_PRINCIPAL,securityPrincipalOid);
	}
	public static LoginWithSecurityPrincipalLinkageEntry createForUnLinkageFromSecurityPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
		return new LoginWithSecurityPrincipalLinkageEntry(Instant.now(),LoginWithSecurityPrincipalLinkageOperation.LOGIN_UNLINKED_FROM_SECURITY_PRINCIPAL,securityPrincipalOid);
	}
}
