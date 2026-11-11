package r01f.model.security.login.data.oauthflow;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLoginData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.IdentityBroker;
import r01f.oids.SecurityIDs.SecurityPrincipalID;

@MarshallType(as="loginDataForOAUTHFlow")
@Accessors(prefix="_")
public class LoginDataForOAUTHFlow 
  implements IsLoginData {
	
	private static final long serialVersionUID = -8714103611726673164L;
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- PRINCIPAL ID
	@MarshallField(as="securityPrincipalId",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected SecurityPrincipalID _securityPrincipalId;
	
////////// --- IDENTITY BROKER (optional)
	@MarshallField(as="identityBroker",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter protected IdentityBroker _identityBroker;
	

}
