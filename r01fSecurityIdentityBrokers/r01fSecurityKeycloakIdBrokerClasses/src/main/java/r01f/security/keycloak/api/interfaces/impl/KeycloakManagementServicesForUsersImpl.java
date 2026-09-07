package r01f.security.keycloak.api.interfaces.impl;

import org.jboss.resteasy.client.jaxrs.internal.proxy.ResteasyClientProxy;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.security.keycloak.api.interfaces.KeycloakManagementServicesForUsers;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.users.KeycloakToUserModelTransferDelegate;
import r01f.security.keycloak.model.users.KeycloakUserCreateRequest;
import r01f.security.keycloak.model.users.KeycloakUserCreateResult;
import r01f.security.keycloak.model.users.KeycloakUserDeleteRequest;
import r01f.security.keycloak.model.users.KeycloakUserDeleteResult;
import r01f.security.keycloak.model.users.KeycloakUserEnableRequest;
import r01f.security.keycloak.model.users.KeycloakUserGetRequest;
import r01f.security.keycloak.model.users.KeycloakUserGetResult;
import r01f.security.keycloak.model.users.KeycloakUserHeadRequest;
import r01f.security.keycloak.model.users.KeycloakUserHeadResult;
import r01f.security.keycloak.model.users.KeycloakUserModel;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria.KeycloakUserGetRequestCriteriaByOID;
import r01f.security.keycloak.model.users.KeycloakUserUpdateRequest;
import r01f.security.keycloak.model.users.KeycloakUserUpdateResult;
import r01f.securitycontext.SecurityIDS.Password;

@Slf4j
public class KeycloakManagementServicesForUsersImpl
		extends KeycloakServicesBaseImpl
	implements KeycloakManagementServicesForUsers {
////////////////////////////////////////////////////////////////////////////////---------
//CONSTRUCTORS
////////////////////////////////////////////////////////////////////////////////---------
	public KeycloakManagementServicesForUsersImpl(final KeycloakClientConfig keyCloakClientConfig,
												  final	Keycloak keycloakAdminClient) {
		super(keyCloakClientConfig, keycloakAdminClient,null);
	}
////////////////////////////////////////////////////////////////////////////////
//METHODS
////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakUserGetResult load(final KeycloakUserGetRequest getRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(getRequest.getRealm().asString()).users();
		UserRepresentation userRepresentation = _userRepresentation(usersResource,
				                                                    getRequest.getCriteria());
		if (userRepresentation == null) {
		    return null;
		}
		log.warn("[Keycloak.load user '{}'] ", getRequest );
		KeycloakUserModel model = KeycloakToUserModelTransferDelegate.keycloakUserRepresentationToUserModel(userRepresentation);
		return new KeycloakUserGetResult(getRequest.getRealm(), model);
	}


	@Override
	public KeycloakUserHeadResult exists(final KeycloakUserHeadRequest headRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(headRequest.getRealm().asString()).users();

		UserRepresentation userRepresentation = _userRepresentation(usersResource,
				                                                    headRequest.getCriteria());
		if (userRepresentation == null) {
			return new KeycloakUserHeadResult(headRequest.getRealm(),
					                          false,
					                          headRequest.getCriteria().byOid() ? headRequest.getCriteria().getKeycloakUserOid(): null,
					                          headRequest.getCriteria().byId() ? headRequest.getCriteria().getKeycloakUserId(): null
					                          );

		}
		return new KeycloakUserHeadResult(headRequest.getRealm(),
							              false,
							              headRequest.getCriteria().byOid() ? headRequest.getCriteria().getKeycloakUserOid(): null,
							              headRequest.getCriteria().byId() ? headRequest.getCriteria().getKeycloakUserId(): null
                );
	}





	@Override
	public KeycloakUserCreateResult create(final KeycloakUserCreateRequest postRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(postRequest.getRealm().asString()).users();

		KeycloakUserModel model = postRequest.getUserModel();

		// 1. Validate Business ID (Username)
		if (model.getId() == null || model.getId().asString().isBlank()) {
		    throw new IllegalArgumentException("The KeycloakUserID (username) must be provided for user creation.");
		}
		log.warn("[Keycloak.create user '{}'] ", model.getId() );

		// 2. Map model to Representation
		UserRepresentation keycloakUser = KeycloakToUserModelTransferDelegate.userModelToKeycloakUserRepresentation(model);

		// 3. Set Keycloak User specific defaults
		keycloakUser.setUsername(model.getId().asString().toUpperCase());
		keycloakUser.setEnabled(true);
		keycloakUser.setEmailVerified(true); // Optional: based on your business logic
		keycloakUser.setFirstName(model.getFirstName());
		keycloakUser.setLastName(model.getLastName());

		// 4. Execute Creation
		log.warn("....execute creation {}", keycloakUser.getUsername());
		Response response = usersResource.create(keycloakUser);

		try {
		    if (response.getStatus() == 201) {
		        // 5. EXTRACT the OID generated by Keycloak from the Location header
		        String path = response.getLocation().getPath();
		        String internalOid = path.substring(path.lastIndexOf('/') + 1);

		        // Update the model with the OID that Keycloak just created
		        model.setOid(KeycloakUserOID.forId(internalOid));

		        // 6. Access the Resource using the NEW OID for post-creation steps (like setting password)
		        UserResource newUserResource = usersResource.get(internalOid);

		        // 7. Set a temporary/initial password if provided in the request
	            CredentialRepresentation passwordCred = new CredentialRepresentation();
	            passwordCred.setTemporary(false);
	            passwordCred.setType(CredentialRepresentation.PASSWORD);
	            passwordCred.setValue(Password.CHANGE_ME.asString());

	            newUserResource.resetPassword(passwordCred);
		        return new KeycloakUserCreateResult(postRequest.getRealm(), model);
		    }
		     log.error("Failed to create Keycloak user. HTTP Status: {}", response.getStatus());
		    return null;

		} finally {
		    response.close();
		}
	}

	@Override
	public KeycloakUserUpdateResult update(final KeycloakUserUpdateRequest putRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(putRequest.getRealm().asString()).users();

		if (putRequest.getUserModel().getOid() == null) {
		    throw new IllegalArgumentException("User OID not provided for update.");
		}

		// 1. Get the Resource
		UserResource userResource = _userResource(usersResource,
		                                          new KeycloakUserGetRequestCriteriaByOID(putRequest.getUserModel().getOid()));

		if (userResource == null) {
		   // log.error("User not found for update");
		    return null;
		}

		// 2. Map updated model to Representation
		UserRepresentation updatedRep = KeycloakToUserModelTransferDelegate.userModelToKeycloakUserRepresentation(putRequest.getUserModel());
		// 3. Execute update
		userResource.update(updatedRep);
		return new KeycloakUserUpdateResult(putRequest.getRealm(), putRequest.getUserModel());
	}

	@Override
	public KeycloakUserUpdateResult enable(final KeycloakUserEnableRequest enableRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(enableRequest.getRealm().asString()).users();
		UserResource userResource = _userResource(usersResource, enableRequest.getCriteria());

		if (userResource == null) {
		    return null;    // log.error("User not found to enable/disable");
		}

		UserRepresentation representation = userResource.toRepresentation();
		representation.setEnabled(enableRequest.isEnable());
		userResource.update(representation);

		KeycloakUserModel model = KeycloakToUserModelTransferDelegate.keycloakUserRepresentationToUserModel(representation);
		return new KeycloakUserUpdateResult(enableRequest.getRealm(), model);
	}

	@Override
	public KeycloakUserDeleteResult delete(final KeycloakUserDeleteRequest deleteRequest) {
		UsersResource usersResource = _keycloakAdminRestAPI.realm(deleteRequest.getRealm().asString()).users();
		UserResource userResource = _userResource(usersResource, deleteRequest.getCriteria());

		if (userResource == null) {
		    //log.error("User not found to delete");
		    return null;
		}

		// Fetch representation before deleting to return the data in the result
		UserRepresentation rep = userResource.toRepresentation();
		userResource.remove();

		KeycloakUserModel model = KeycloakToUserModelTransferDelegate.keycloakUserRepresentationToUserModel(rep);
		return new KeycloakUserDeleteResult(deleteRequest.getRealm(), model);
	}
}