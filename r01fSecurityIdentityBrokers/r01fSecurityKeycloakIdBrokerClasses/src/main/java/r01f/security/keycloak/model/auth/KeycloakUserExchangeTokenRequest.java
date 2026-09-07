package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix = "_")
@MarshallType(as="keycloakUserExchangeTokenRequest")
@NoArgsConstructor
public class KeycloakUserExchangeTokenRequest
		extends KeycloakUserAuthRequestBase {
	
    @MarshallField(as="userId")
    @Setter @Getter private KeycloakUserID _userId;
    
    @MarshallField(as="externalToken")
    @Setter @Getter private SecurityToken _externalToken;
    
    @MarshallField(as="originProvider")
    @Setter @Getter private SecurityProviderID  _originProvider; // Ej: "google", "oidc-customer", etc.

    public KeycloakUserExchangeTokenRequest( final KeycloakRealm realm, 
    										 final KeycloakUserID userId, 
                                             final SecurityToken externalToken, 
                                             final SecurityProviderID originProvider) {
        super(realm);
		 _userId = userId;
        _externalToken = externalToken;
        _originProvider = originProvider;
    }
}