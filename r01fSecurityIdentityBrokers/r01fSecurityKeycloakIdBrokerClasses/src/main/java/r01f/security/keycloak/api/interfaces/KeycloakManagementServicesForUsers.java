package r01f.security.keycloak.api.interfaces;

import r01f.security.keycloak.model.users.KeycloakUserCreateRequest;
import r01f.security.keycloak.model.users.KeycloakUserCreateResult;
import r01f.security.keycloak.model.users.KeycloakUserDeleteRequest;
import r01f.security.keycloak.model.users.KeycloakUserDeleteResult;
import r01f.security.keycloak.model.users.KeycloakUserEnableRequest;
import r01f.security.keycloak.model.users.KeycloakUserGetRequest;
import r01f.security.keycloak.model.users.KeycloakUserGetResult;
import r01f.security.keycloak.model.users.KeycloakUserHeadRequest;
import r01f.security.keycloak.model.users.KeycloakUserHeadResult;
import r01f.security.keycloak.model.users.KeycloakUserUpdateRequest;
import r01f.security.keycloak.model.users.KeycloakUserUpdateResult;


public interface KeycloakManagementServicesForUsers {
/////////////////////////////////////////////////////////////////////////////////////////
//  [ LOAD | CREATE | UPDATE | ENABLE |DELETE ]
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserGetResult load(final KeycloakUserGetRequest  putRequest);
	
	public KeycloakUserCreateResult create(final KeycloakUserCreateRequest putRequest);

	public KeycloakUserUpdateResult update(final KeycloakUserUpdateRequest putRequest);
	
	public KeycloakUserUpdateResult enable(final KeycloakUserEnableRequest putRequest);
	
	public KeycloakUserDeleteResult delete(final KeycloakUserDeleteRequest putRequest);
	
	public KeycloakUserHeadResult   exists(final KeycloakUserHeadRequest headRequest);
/////////////////////////////////////////////////////////////////////////////////////////
//  [ FETCH]
/////////////////////////////////////////////////////////////////////////////////////////

	

}
