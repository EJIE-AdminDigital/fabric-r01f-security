package r01f.model.security.login.data.apikey;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsSecurityObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.types.AppVersion;

@Accessors(prefix="_")
public class ApiKey 
  implements IsSecurityObject {
	
	private static final long serialVersionUID = 6530094049068114471L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="apiKeyVersion",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private AppVersion _apiKeyVersion;
	
	@MarshallField(as="apiKeyPair")
	@Getter @Setter private ApiKeyPair _apiKeyPair;	// BEWARE! it only contains the private key when issued
	
	@MarshallField(as="name",escape=true)
	@Getter @Setter private String _name;

	@MarshallField(as="description",escape=true)
	@Getter @Setter private String _description;
}
