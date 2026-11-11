package r01f.model.security.login.pairedphone.messages.login;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.PairedPhoneLoginConsentOID;

@MarshallType(as = "pairedPhoneLoginConsentRequest")
@Accessors(prefix = "_")
public class PairedPhoneLoginConsentRequest
  implements IsSecurityObject {

	private static final long serialVersionUID = 6971450910920130370L;
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="oid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private PairedPhoneLoginConsentOID _oid;
	
	@MarshallField(as="securityToken")
	@Getter	@Setter private SecurityToken _securityToken;
	
	@MarshallField(as="protocolData")
	@Getter	@Setter private PairedPhoneLoginConsentProtocolData _protocolData;	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginConsentRequest() {
		super();
	}
	public PairedPhoneLoginConsentRequest(final PairedPhoneLoginConsentOID loginConsentOid) {
		super();
		_oid = loginConsentOid;
	}
}
