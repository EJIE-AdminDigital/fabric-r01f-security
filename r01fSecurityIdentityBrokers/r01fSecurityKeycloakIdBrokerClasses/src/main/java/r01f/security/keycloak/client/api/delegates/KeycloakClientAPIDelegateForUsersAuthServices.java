package r01f.security.keycloak.client.api.delegates;

import org.keycloak.admin.client.Keycloak;

import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.api.interfaces.KeycloakAuthServicesForUsers;
import r01f.security.keycloak.api.interfaces.impl.KeycloakAuthServicesForUsersImpl;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.auth.KeycloakUserAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakUserAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserExchangeTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserRefreshTokenRequest;

public class KeycloakClientAPIDelegateForUsersAuthServices
	implements KeycloakAuthServicesForUsers {
	
///////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final KeycloakAuthServicesForUsersImpl _authtService;	
///////////////////////////////////////////////////////////////////////////////////////////
//CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientAPIDelegateForUsersAuthServices( final KeycloakClientConfig config , 
			                                              final Keycloak client,final Marshaller marshaller) {
		_authtService = new  KeycloakAuthServicesForUsersImpl(config,client,marshaller);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENTS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakUserAuthRequestResult authenticate(final KeycloakUserAuthTokenRequest authRequest) {		
		return _authtService.authenticate(authRequest);
	}
	@Override
	public KeycloakUserAuthRequestResult refresh(final KeycloakUserRefreshTokenRequest refreshRequest) {		
		return _authtService.refresh(refreshRequest);
	}
	@Override
	public KeycloakUserAuthRequestResult exchange(final KeycloakUserExchangeTokenRequest exchangeRequest) {
		return _authtService.exchange(exchangeRequest);
	}

}
