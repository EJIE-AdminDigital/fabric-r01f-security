package r01f.internal.notifier.security.user.pairedphone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;

@MarshallType(as="pairedPhoneLoginConsentMessageData")
@Accessors(prefix="_")
public class PairedPhoneLoginConsentMessageData
  implements IsSecurityObject {

	private static final long serialVersionUID = 8504987244849368908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	 FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Related Paired Consent OID.
	*/
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private PairedPhoneLoginConsentOID _loginConsentOid;

	@MarshallField(as="phone",
		       	   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private Phone _phone;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentMessageData() {
		// default no-args constructor
	}
	public PairedPhoneLoginConsentMessageData(final PairedPhoneLoginConsentOID loginConsentOid,
											  final Phone phone) {
		_loginConsentOid = loginConsentOid;
		_phone = phone;
	}
}

