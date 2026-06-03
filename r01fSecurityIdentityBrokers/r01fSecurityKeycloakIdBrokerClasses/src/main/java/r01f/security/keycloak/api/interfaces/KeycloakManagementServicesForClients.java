package r01f.security.keycloak.api.interfaces;

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


public interface KeycloakManagementServicesForClients {
/////////////////////////////////////////////////////////////////////////////////////////
//  [ LOAD | CREATE | UPDATE | ENABLE |DELETE ]
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientGetResult load(final KeycloakClientGetRequest  putRequest);
	
	public KeycloakClientCreateResult create(final KeycloakClientCreateRequest putRequest);

	public KeycloakClientUpdateResult update(final KeycloakClientUpdateRequest putRequest);
	
	public KeycloakClientUpdateResult enable(final KeycloakClientEnableRequest putRequest);
	
	public KeycloakClientDeleteResult delete(final KeycloakClientDeleteRequest putRequest);
/////////////////////////////////////////////////////////////////////////////////////////
//  [ FETCH]
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientFetchResult fetch(final KeycloakClientFetchRequest fetchRequest);


}
