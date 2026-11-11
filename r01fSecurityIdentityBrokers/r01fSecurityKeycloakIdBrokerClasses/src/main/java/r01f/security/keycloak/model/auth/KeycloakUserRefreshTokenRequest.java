package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.Password;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

@Accessors(prefix = "_")
@MarshallType(as="keycloakUserRefreshTokenRequest")
@NoArgsConstructor
public class KeycloakUserRefreshTokenRequest
		extends KeycloakUserAuthRequestBase {
    
	@MarshallField(as="clientId")
	@Getter private KeycloakClientID _clientId;
	@MarshallField(as="clientPassword")
    @Getter private Password _clientPassword; // Opcional si es cliente público

	@MarshallField(as="refreshToken")
    @Getter private KeycloakRefreshToken _refreshToken;

    public KeycloakUserRefreshTokenRequest( final KeycloakRealm realm, 
    		                                final KeycloakClientID clientId, Password clientPassword,
                                            final KeycloakRefreshToken refreshToken) {
        super(realm);
        _refreshToken = refreshToken;
        _clientId = clientId;
        _clientPassword = clientPassword;
    }
    
}