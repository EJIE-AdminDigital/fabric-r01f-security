package r01f.security.user.giltza;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.user.UserDataFromThirdPartySecurityProvider;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;

@NoArgsConstructor
@MarshallType(as="giltzaUserData")
@Accessors(prefix="_")
public class GiltzaUserData
  implements UserDataFromThirdPartySecurityProvider {

	private static final long serialVersionUID = 3189595900075153543L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="dni",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private PersonID  _dni;

   	@MarshallField(as="mail",
   				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private EMail _mail;

 	@MarshallField(as="name",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String  _givenName;

 	@MarshallField(as="surname",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String  _familyName;

 	@MarshallField(as="displayName",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String  _displayName;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public LoginID getLoginId() {
 		return _dni != null ? LoginID.fromPersonID(_dni) : null;
 	}
}
