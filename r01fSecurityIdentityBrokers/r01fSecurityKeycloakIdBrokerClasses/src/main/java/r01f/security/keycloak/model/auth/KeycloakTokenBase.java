package r01f.security.keycloak.model.auth;

import java.time.Instant;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix = "_")
public abstract class KeycloakTokenBase 
		implements KeycloakToken {
    
    @Getter protected final SecurityToken _value;
    @Getter protected final KeycloakTokenType _type;
    @Getter protected final Instant _expiry;

    protected KeycloakTokenBase(final KeycloakTokenType type , final SecurityToken value, long expiresInSeconds) {
    	_value = value;     
    	_type  = type;
        _expiry = Instant.now().plusSeconds(expiresInSeconds);
    }
////////////////////////////////////////////////
/// METHODOS
//////////////////////////////////////////////
    @Override
    public SecurityToken asSecurityToken() {
        return _value;
    }
    @Override
    public boolean isExpired() {   
        return Instant.now().isAfter(_expiry.minusSeconds(5));
    }    
  
}