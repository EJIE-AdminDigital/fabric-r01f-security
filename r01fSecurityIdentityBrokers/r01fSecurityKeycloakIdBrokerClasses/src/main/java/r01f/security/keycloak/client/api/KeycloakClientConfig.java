package r01f.security.keycloak.client.api;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientSecret;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakClientConfig
	 implements Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
    @Getter @Setter private Url _endPoint;
    @Getter @Setter private KeycloakRealm        _realm;
    @Getter @Setter private KeycloakClientID     _keycloakClientId;     
    @Getter @Setter private KeycloakClientSecret _keycloakClientSecret;  
   
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public KeycloakClientConfig( final KeycloakRealm     realm,
	                             final KeycloakClientID   keycloakClientId,  final KeycloakClientSecret keycloakClientSecret ,
	                             final Url endPoint) {
		_realm  = realm;
		_keycloakClientId  = keycloakClientId;
		_keycloakClientSecret   = keycloakClientSecret;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
	    outDbgInfo.append("\n Keycloak Config :{ \n ").append(Strings.customized(" Endpoint {}",
	    																   _endPoint ));
	    outDbgInfo.append("\n").append(Strings.customized("_keycloakClientSecret {}",
	    		_keycloakClientSecret  != null ? _keycloakClientSecret : " is null!" ));
	    outDbgInfo.append("\n").append(Strings.customized("_keycloakClientId {}",
	    		_keycloakClientId  != null ? _keycloakClientId : " is null!" ));
	   
		outDbgInfo.append(" } \n")	;
	    return outDbgInfo.toString();
	}
}
