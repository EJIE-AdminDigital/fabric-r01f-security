package r01f.security.principal.google;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.security.authn.handler.IsIdentityProviderReturnedData;
import r01f.types.contact.EMail;
import r01f.types.url.Url;

@NoArgsConstructor
@MarshallType(as="securityPrincipalDataFromGoogle")
@Accessors(prefix="_")
public class SecurityPrincipalDataFromGoogle
  implements IsIdentityProviderReturnedData {

	private static final long serialVersionUID = 3189595900075153543L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
   	@MarshallField(as="identityProvider",
   				   whenXml=@MarshallFieldAsXml(attr=true))
   	@Getter @Setter private IdentityProvider _identityProvider = IdentityProvider.GOOGLE;
   	
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
