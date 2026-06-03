package r01f.security.user.google;

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
import r01f.types.url.Url;

@NoArgsConstructor
@MarshallType(as="googleUserData")
@Accessors(prefix="_")
public class GoogleUserData
  implements UserDataFromThirdPartySecurityProvider {

	private static final long serialVersionUID = 3189595900075153543L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
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

 	@MarshallField(as="profilePhoto",
 				   whenXml=@MarshallFieldAsXml(attr=true))
 	@Getter @Setter private Url _profilePhoto;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public LoginID getLoginId() {
 		return _mail != null ? LoginID.fromEMail(_mail) : null;
 	}
}
