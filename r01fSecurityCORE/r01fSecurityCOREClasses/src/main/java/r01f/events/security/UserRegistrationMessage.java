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
import r01f.types.contact.EMail;
import r01f.types.contact.NotificationMedium;
import r01f.types.contact.Phone;

@MarshallType(as="userPasswordRecoveryMessage")
@Accessors(prefix="_")
public class UserRegistrationMessage
  implements IsSecurityObject ,
  			 Event {

	private static final long serialVersionUID = 8504987244849368908L;
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="userOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityPrincipalOID _userOid;
	
	@MarshallField(as="userName",escape=true)
	@Getter @Setter private String _userName;
	
	@MarshallField(as="token",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private SecurityToken _token;
	
	@MarshallField(as="medium",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private NotificationMedium _medium;
	
	@MarshallField(as="phone",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Phone _phone;
	
	@MarshallField(as="email",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private EMail _email;
}