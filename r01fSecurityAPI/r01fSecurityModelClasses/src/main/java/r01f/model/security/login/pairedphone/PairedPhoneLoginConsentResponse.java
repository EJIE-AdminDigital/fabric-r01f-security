package r01f.model.security.login.pairedphone;

import java.time.Instant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.types.datetime.DateTimeMarshaller;



@MarshallType(as = "loginConsentResponse")
@Accessors(prefix = "_")
@RequiredArgsConstructor
public class PairedPhoneLoginConsentResponse 
			implements SecurityObject {
	private static final long serialVersionUID = -8135980254651198012L;
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	private final UserOID  _userOid;
	/**
	 * Related Paired Consent OID.
	 */
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	private final PairedPhoneLoginConsentOID _loginConsentOid;
	/**
	 * Response date
	 */
	@MarshallField(as="responseDate",dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ssX"),
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	private final Instant _responseDate;
	/**
	 * Accepted.
	 */
	@MarshallField(as="accepted",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	private final Boolean _accepted;
	
	/**
	 * Security Token.
	 */
	@MarshallField(as="securityToken")
	@Getter	private final SecurityToken  _securityToken;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public PairedPhoneLoginConsentResponse(@MarshallFrom("userOid")         final UserOID userOid,
										   @MarshallFrom("loginConsentOid") final PairedPhoneLoginConsentOID oid,
										   @MarshallFrom("responseDate")    final String responseDateAsString,
										   @MarshallFrom("accepted")        final Boolean accepted, 
										   @MarshallFrom("securityToken")   final SecurityToken securityToken) {
		this._responseDate = DateTimeMarshaller.forUnMarshallingFrom(responseDateAsString)
													 .asInstantUsingPattern("yyyy-MM-dd HH:mm:ssX", null)
													 .orElseThrow(() -> new IllegalArgumentException("Invalid responseDate"));
		_loginConsentOid = oid;
		_accepted = accepted;
		_userOid = userOid;
		_securityToken = securityToken;
	}
}
