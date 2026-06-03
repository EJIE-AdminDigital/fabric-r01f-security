package r01f.security.keycloak.client.api.delegates;

import org.keycloak.admin.client.Keycloak;

import r01f.security.keycloak.api.interfaces.KeycloakManagementServicesForClients;
import r01f.security.keycloak.api.interfaces.impl.KeycloakManagementServicesForClientsImpl;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.clients.KeycloakClientCreateRequest;
import r01f.security.keycloak.model.clients.KeycloakClientCreateResult;
import r01f.security.keycloak.model.clients.KeycloakClientDeleteRequest;
import r01f.security.keycloak.model.clients.KeycloakClientDeleteResult;
import r01f.security.keycloak.model.clients.KeycloakClientEnableRequest;
import r01f.security.keycloak.model.clients.KeycloakClientFetchRequest;
import r01f.security.keycloak.model.clients.KeycloakClientFetchResult;
import r01f.security.keycloak.model.clients.KeycloakClientGetRequest;
import r01f.security.keycloak.model.clients.KeycloakClientGetResult;
import r01f.security.keycloak.model.clients.KeycloakClientUpdateRequest;
import r01f.security.keycloak.model.clients.KeycloakClientUpdateResult;

public class KeycloakClientAPIDelegateForClientsManagementServices 
    implements KeycloakManagementServicesForClients {
///////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final KeycloakManagementServicesForClientsImpl _managementService;
///////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientAPIDelegateForClientsManagementServices( final KeycloakClientConfig config , final Keycloak client) {
		_managementService = new  KeycloakManagementServicesForClientsImpl(config,client);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENTS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakClientGetResult load(final KeycloakClientGetRequest getRequest) {		
		return _managementService.load(getRequest);
	}

	@Override
	public KeycloakClientCreateResult create(final KeycloakClientCreateRequest postRequest) {		
		return _managementService.create(postRequest);
	}

	@Override
	public KeycloakClientUpdateResult update(final KeycloakClientUpdateRequest putRequest){		
		return _managementService.update(putRequest);
	}

	@Override
	public KeycloakClientDeleteResult delete(final KeycloakClientDeleteRequest deleteRequest) {	
		return _managementService.delete(deleteRequest);
	}

	@Override
	public KeycloakClientUpdateResult enable(final KeycloakClientEnableRequest enableRequest) {
		return _managementService.enable(enableRequest);
	}

	@Override
	public KeycloakClientFetchResult fetch(final KeycloakClientFetchRequest fetchRequest) {
		return _managementService.fetch(fetchRequest);
	}
	
}
