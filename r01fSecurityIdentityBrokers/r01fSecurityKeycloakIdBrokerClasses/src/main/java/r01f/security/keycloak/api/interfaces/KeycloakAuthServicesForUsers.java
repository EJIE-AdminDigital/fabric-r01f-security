package r01f.security.keycloak.api.interfaces;

import r01f.security.keycloak.model.auth.KeycloakUserAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakUserAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserExchangeTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserRefreshTokenRequest;


public interface KeycloakAuthServicesForUsers {
/////////////////////////////////////////////////////////////////////////////////////////
// 
/////////////////////////////////////////////////////////////////////////////////////////
	/**
     * Initial Authentication
     */
    public KeycloakUserAuthRequestResult authenticate(final KeycloakUserAuthTokenRequest authRequest);

    /**
     * Renew of token [ Refresh ]
     */
    public KeycloakUserAuthRequestResult refresh(final KeycloakUserRefreshTokenRequest refreshRequest);

    /**
     * Exchange of token [Exchange]
     */
    public KeycloakUserAuthRequestResult exchange(final KeycloakUserExchangeTokenRequest exchangeRequest);
/////////////////////////////////////////////////////////////////////////////////////////
// 
/////////////////////////////////////////////////////////////////////////////////////////


}
