package r01f.internal.notifier.security.user.pairedphone;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityToken;


@MarshallType(as="pairedPhoneLoginConsentMessage")
@Accessors(prefix="_")
public class PairedPhoneLoginConsentMessage 
  implements SecurityObject {

	private static final long serialVersionUID = 8504987244849368908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	 FIELDS                                                                         
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="token",      // The Firebase Security Token for the paired phone
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityToken     _securityTokenForPush;		
	
	@MarshallField(as="pushMessage", // The Push Message [ Title + Body + Data (PairedPhoneLoginConsentMessageData as json) ]
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private NotifierPushMessage _pushMesagge;
	
	
}

