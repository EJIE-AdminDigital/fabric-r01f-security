package r01f.security.keycloak.model.auth;

import java.util.Set;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRole;

@Accessors(prefix = "_")
public class KeycloakAccessToken
     extends KeycloakTokenBase {
	
    @Getter  private final Set<KeycloakRole> _roles;

    public KeycloakAccessToken(final SecurityToken value, long expiresIn, Set<KeycloakRole> roles) {
        super(KeycloakTokenType.ACCESS,value, expiresIn);
        _roles = roles;
    }
    
}