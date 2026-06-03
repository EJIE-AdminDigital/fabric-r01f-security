package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix = "_")
public class KeycloakTokenSet {
 
    @Getter KeycloakAccessToken _accessToken;
    @Getter KeycloakRefreshToken _refreshToken;   
    @Getter KeycloakExchangeToken _exchangeToken;  // Opcional: Solo presente si se realizó un flujo de Token Exchange

    /**
     * Is valid ?
     */
    public boolean isValid() {
        return _accessToken != null && !_accessToken.isExpired();
    }
}