package r01f.security.principal.giltza;

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
import r01f.types.contact.PersonID;

@NoArgsConstructor
@MarshallType(as="securityPrincipalDataFromGiltza")
@Accessors(prefix="_")
public class SecurityPrincipalDataFromGiltza
  implements IsIdentityProviderReturnedData {

	private static final long serialVersionUID = 3189595900075153543L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="identityProvider",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private IdentityProvider _identityProvider = IdentityProvider.GILTZA;
	
	@MarshallField(as="dni",
			   	   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private PersonID _dni;

   	@MarshallField(as="mail",
   				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private EMail _mail;

 	@MarshallField(as="name",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String _name;

 	@MarshallField(as="surname1",
 					   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String _surname1;

 	@MarshallField(as="surname2",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String _surname2;

	@MarshallField(as="displayName",
 				   whenXml=@MarshallFieldAsXml(attr=true))
    @Getter @Setter private String _displayName;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public LoginID getLoginId() {
 		return _dni != null ? LoginID.fromPersonID(_dni) : null;
 	}
}
