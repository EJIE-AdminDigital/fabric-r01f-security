package r01f.security.keycloak.client.api.delegates;

import org.keycloak.admin.client.Keycloak;

import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.api.interfaces.KeycloakAuthServicesForClients;
import r01f.security.keycloak.api.interfaces.impl.KeycloakAuthServicesForClientsImpl;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.auth.KeycloakClientAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakClientAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakClientRefreshTokenRequest;

public class KeycloakClientAPIDelegateForClientsAuthServices
  implements KeycloakAuthServicesForClients {
	
///////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final KeycloakAuthServicesForClientsImpl _authService;	
///////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientAPIDelegateForClientsAuthServices(final KeycloakClientConfig config , 
			                                               final Keycloak client,final Marshaller marshaller) {
		_authService = new  KeycloakAuthServicesForClientsImpl(config,client,marshaller);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENTS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakClientAuthRequestResult authenticate(final KeycloakClientAuthTokenRequest authRequest) {		
		return _authService.authenticate(authRequest);
	}
	@Override
	public KeycloakClientAuthRequestResult refresh(final KeycloakClientRefreshTokenRequest refreshRequest) {		
		return _authService.refresh(refreshRequest);
	}

}
