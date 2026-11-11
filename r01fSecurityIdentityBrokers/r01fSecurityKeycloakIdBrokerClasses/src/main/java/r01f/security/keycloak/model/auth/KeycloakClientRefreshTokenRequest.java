package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

@Accessors(prefix = "_")
@MarshallType(as="keycloakUserRefreshTokenRequest")
@NoArgsConstructor
public class KeycloakClientRefreshTokenRequest
		extends KeycloakUserAuthRequestBase {

	@MarshallField(as="refreshToken")
    @Getter private KeycloakRefreshToken _refreshToken;

    public KeycloakClientRefreshTokenRequest(final KeycloakRealm realm,
                                             final KeycloakRefreshToken refreshToken) {
        super(realm);
        _refreshToken = refreshToken;
    }
    
}