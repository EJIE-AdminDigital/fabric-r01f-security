package r01f.security.keycloak.model.auth;

import lombok.experimental.Accessors;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix = "_")
public class KeycloakRefreshToken 
     extends KeycloakTokenBase {

    public KeycloakRefreshToken(final SecurityToken value, long expiresIn) {  
        super(KeycloakTokenType.REFRESH, value, expiresIn);
    }
}