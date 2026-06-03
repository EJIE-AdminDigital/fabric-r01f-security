package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.securitycontext.SecurityIDS.Password;

@Accessors(prefix = "_")
public class KeycloakUserRefreshTokenRequest 
		extends KeycloakUserAuthRequestBase {
    
	@Getter private final KeycloakClientID _clientId;
    @Getter private final Password _clientPassword; // Opcional si es cliente público
	
    @Getter private final KeycloakRefreshToken _refreshToken;

    public KeycloakUserRefreshTokenRequest( final KeycloakRealm realm, 
    		                                final KeycloakClientID clientId, Password clientPassword,
                                            final KeycloakRefreshToken refreshToken) {
        super(realm);
        _refreshToken = refreshToken;
        _clientId = clientId;
        _clientPassword = clientPassword;
    }
    
}