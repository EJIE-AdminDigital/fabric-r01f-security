package r01f.security.keycloak.client.api.delegates;

import org.keycloak.admin.client.Keycloak;

import r01f.security.keycloak.api.interfaces.KeycloakManagementServicesForUsers;
import r01f.security.keycloak.api.interfaces.impl.KeycloakManagementServicesForUsersImpl;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
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

public class KeycloakClientAPIDelegateForUsersManagementServices 
    implements KeycloakManagementServicesForUsers {
///////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
///////////////////////////////////////////////////////////////////////////////////////////
	protected final KeycloakManagementServicesForUsersImpl _serviceForUsersImpl;
///////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUTCTOR
///////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientAPIDelegateForUsersManagementServices( final KeycloakClientConfig config , final Keycloak client) {
		_serviceForUsersImpl = new  KeycloakManagementServicesForUsersImpl(config,client);
	}
///////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENTS
///////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakUserGetResult load(final KeycloakUserGetRequest getRequest) {		
		return _serviceForUsersImpl.load(getRequest);
	}

	@Override
	public KeycloakUserCreateResult create(final KeycloakUserCreateRequest postRequest) {		
		return _serviceForUsersImpl.create(postRequest);
	}

	@Override
	public KeycloakUserUpdateResult update(final KeycloakUserUpdateRequest putRequest){		
		return _serviceForUsersImpl.update(putRequest);
	}

	@Override
	public KeycloakUserDeleteResult delete(final KeycloakUserDeleteRequest deleteRequest) {	
		return _serviceForUsersImpl.delete(deleteRequest);
	}

	@Override
	public KeycloakUserUpdateResult enable(final KeycloakUserEnableRequest enableRequest) {
		return _serviceForUsersImpl.enable(enableRequest);
	}
	@Override
	public KeycloakUserHeadResult exists(final KeycloakUserHeadRequest headRequest) {
		return _serviceForUsersImpl.exists(headRequest);
	}

	
}
