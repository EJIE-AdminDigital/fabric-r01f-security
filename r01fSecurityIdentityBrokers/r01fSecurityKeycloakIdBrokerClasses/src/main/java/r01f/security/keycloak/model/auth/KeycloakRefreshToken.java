package r01f.security.keycloak.model.auth;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.SecurityToken;

@Accessors(prefix = "_")
@MarshallType(as="keycloakRefreshToken")
@NoArgsConstructor
public class KeycloakRefreshToken
     extends KeycloakTokenBase {

    public KeycloakRefreshToken(final SecurityToken value, long expiresIn) {  
        super(KeycloakTokenType.REFRESH, value, expiresIn);
    }
}