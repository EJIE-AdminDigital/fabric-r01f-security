package r01f.model.security.login.data.apikey;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.IsLoginData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.Range;

@MarshallType(as="loginDataForApiKey")
@Accessors(prefix="_")
public class LoginDataForApiKey 
  implements IsLoginData {
	
	private static final long serialVersionUID = -8714103611726673164L;

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- CURRENT VERSION	
	@MarshallField(as="createdAt",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private Instant _createdAt;	// the api key id (the public key)
	
	@MarshallField(as="currentApiKeyVersions")
	@Getter @Setter private ApiKey _currentApiKey;
	
////////// --- OLD VERSIONS
	@MarshallField(as="previousVersions",
				   whenXml=@MarshallFieldAsXml(collectionElementName="version"))
	@Getter @Setter private Collection<ApiKeyVersion> _previousVersions;	// all the versions of the api key
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public void setCurrent(final ApiKey newApiKey) {
		// archive the current version
		ApiKeyVersion versionToArchive = new ApiKeyVersion(_currentApiKey,
														   Range.closed(_createdAt,Instant.now()));
		if (_previousVersions == null) _previousVersions = new ArrayList<>();
		_previousVersions.add(versionToArchive);
		
		// set the new version
		_createdAt = Instant.now();
		_currentApiKey = newApiKey;
	}
}
