package r01f.security.keycloak.api.interfaces;

import r01f.security.keycloak.model.auth.KeycloakClientAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakClientAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakClientRefreshTokenRequest;


public interface KeycloakAuthServicesForClients {
/////////////////////////////////////////////////////////////////////////////////////////
// 
/////////////////////////////////////////////////////////////////////////////////////////
	/**
     * Initial Authentication
     */
    public KeycloakClientAuthRequestResult authenticate(final KeycloakClientAuthTokenRequest authRequest);

    /**
     * Renew of token [ Refresh ]
     */
    public KeycloakClientAuthRequestResult refresh(final KeycloakClientRefreshTokenRequest refreshRequest);
/////////////////////////////////////////////////////////////////////////////////////////
// 
/////////////////////////////////////////////////////////////////////////////////////////


}
