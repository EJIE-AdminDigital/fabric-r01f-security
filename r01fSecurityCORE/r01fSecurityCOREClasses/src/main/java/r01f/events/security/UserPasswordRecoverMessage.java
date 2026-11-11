package r01f.events.security;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.eventbus.Event;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.NotificationMedium;

@MarshallType(as="userPasswordRecoveryMessage")
@Accessors(prefix="_")
public class UserPasswordRecoverMessage 
  implements IsSecurityObject, 
             Event {

	private static final long serialVersionUID = 8504987244849368908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="principalOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _principalOid;
	
	@MarshallField(as="displayName",escape=true)
	@Getter @Setter private String _displayName;
	
	@MarshallField(as="token",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityToken _token;
	
	@MarshallField(as="medium",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private NotificationMedium _medium;	// email, sms, etc
	
	@MarshallField(as="contactMean")
	@Getter @Setter private IsContactMean _contactMean;	// email, phone, etc
}
