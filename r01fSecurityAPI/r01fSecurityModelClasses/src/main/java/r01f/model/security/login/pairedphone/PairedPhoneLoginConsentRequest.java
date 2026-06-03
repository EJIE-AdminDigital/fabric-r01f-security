package r01f.model.security.login.pairedphone;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;



@MarshallType(as = "pairedPhoneLoginConsentRequest")
@Accessors(prefix = "_")
@NoArgsConstructor
public  class PairedPhoneLoginConsentRequest
		   implements SecurityObject {

	private static final long serialVersionUID = 6971450910920130370L;
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="phone",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	private  Phone _phone;
	/**
	 * Related Paired Consent OID.
	 */
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private PairedPhoneLoginConsentOID _loginConsentOid;
	/**
	 * Request date
	 */
	@MarshallField(as="requestedAt", 
			       dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ssX"),
			       whenXml=@MarshallFieldAsXml(attr=true))	
	@Getter @Setter private Instant _requestedAt;
	/**
	 * Protocol Data
	 */
	@MarshallField(as="protocolData")
	@Getter	@Setter private PairedPhoneLoginConsentProtocolData _protocolData;	
	
	/**
	 * Security Token.
	 */
	@MarshallField(as="securityToken")
	@Getter	@Setter private  SecurityToken  _securityToken;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneLoginConsentRequest( final Phone phone, 
			                               final PairedPhoneLoginConsentOID loginConsentOid, final  Instant requestedAt) {
		_phone = phone;
		_loginConsentOid = loginConsentOid;
		_requestedAt = requestedAt;
	}
	/**
	 * Constructor. This will be used at rest, at resques time.
	 * @param phone
	 */
	
	public PairedPhoneLoginConsentRequest(@MarshallFrom("phone") final Phone phone) {
		_phone = phone;		
	}
/////////////////////////////////////////////////////////////////////////////////////////
// FLUENT
/////////////////////////////////////////////////////////////////////////////////////////
	public static PairedPhoneLoginConsentRequest from (final Phone phone) {
		return new PairedPhoneLoginConsentRequest(phone,PairedPhoneLoginConsentOID.supply(),Instant.now());
	}
	public static PairedPhoneLoginConsentRequest from (final PairedPhoneLoginConsentRequest other) {
		return new PairedPhoneLoginConsentRequest(other.getPhone(), other.getLoginConsentOid(),other.getRequestedAt());
	}
}
