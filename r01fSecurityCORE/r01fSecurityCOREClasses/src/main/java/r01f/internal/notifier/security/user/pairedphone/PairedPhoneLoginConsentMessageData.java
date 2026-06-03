package r01f.internal.notifier.security.user.pairedphone;


import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.SecurityObject;
import r01f.model.security.login.pairedphone.PairedPhoneLoginConsentBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.DateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallDateFormat;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityOIDs.PairedPhoneLoginConsentOID;
import r01f.types.contact.Phone;


@MarshallType(as="pairedPhoneLoginConsentMessageData")
@Accessors(prefix="_")
public class PairedPhoneLoginConsentMessageData
  implements SecurityObject {

	private static final long serialVersionUID = 8504987244849368908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	 MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	* Related Paired Consent OID.
	*/
	@MarshallField(as="loginConsentOid",
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private PairedPhoneLoginConsentOID _loginConsentOid;


	@MarshallField(as="phone",
		       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter	@Setter private  Phone _phone;

	/**
	* Request date
	*/
	@MarshallField(as="requestedAt",
			       dateFormat=@MarshallDateFormat(use=DateFormat.CUSTOM,format="yyyy-MM-dd HH:mm:ssX"),
			       whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _requestedAt;

	public static < C extends PairedPhoneLoginConsentBase> PairedPhoneLoginConsentMessageData from(final C loginConsent) {
		PairedPhoneLoginConsentMessageData consentMessage = new PairedPhoneLoginConsentMessageData();
		consentMessage.setLoginConsentOid(loginConsent.getOid());
		consentMessage.setRequestedAt(loginConsent.getCreateDate());
		consentMessage.setPhone(loginConsent.getPairedPhone());
		return consentMessage;
	}
}

