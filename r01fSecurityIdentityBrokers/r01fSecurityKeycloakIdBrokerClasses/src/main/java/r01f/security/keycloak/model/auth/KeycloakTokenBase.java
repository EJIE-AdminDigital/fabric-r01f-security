package r01f.security.keycloak.model.auth;

import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.oids.SecurityIDs.SecurityToken;

@Accessors(prefix = "_")
@NoArgsConstructor
public abstract class KeycloakTokenBase
		implements KeycloakToken {
    
    @MarshallField(as="value")
    @Getter protected SecurityToken _value;
    @MarshallField(as="type")
    @Getter protected KeycloakTokenType _type;
    // computed from 'expiresInSeconds' at construction time; not marshalled
    @Getter protected Instant _expiry;

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