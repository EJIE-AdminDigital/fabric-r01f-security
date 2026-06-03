package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@Accessors(prefix = "_")
public class KeycloakUserExchangeTokenRequest 
		extends KeycloakUserAuthRequestBase {
	
    @Getter private final KeycloakUserID _userId;
    @Getter private final SecurityToken _externalToken;
    @Getter private final String _originProvider; // Ej: "google", "oidc-customer", etc.

    public KeycloakUserExchangeTokenRequest( final KeycloakRealm realm, 
    										 final KeycloakUserID userId, 
                                             final SecurityToken externalToken, 
                                             final String originProvider) {
        super(realm);
		 _userId = userId;
        _externalToken = externalToken;
        _originProvider = originProvider;
    }
}