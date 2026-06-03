package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix = "_")
public class KeycloakExchangeToken 
     extends KeycloakTokenBase {
    
    @Getter private final String _originProvider;

    public KeycloakExchangeToken(final SecurityToken value, long expiresIn, final String originProvider) {
       
        super(KeycloakTokenType.EXCHANGE, value, expiresIn);
        _originProvider = originProvider;
    }
}