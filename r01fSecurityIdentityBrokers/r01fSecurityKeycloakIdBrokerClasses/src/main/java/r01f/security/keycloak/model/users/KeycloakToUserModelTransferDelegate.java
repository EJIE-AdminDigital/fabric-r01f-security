package r01f.security.keycloak.model.users;

import org.keycloak.representations.idm.UserRepresentation;

import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.types.contact.EMail;

/**
 * Utility class responsible for transforming between different representations of keycloak objects.
 * This class provides methods to map between Model and Representation objects.
 */
public class KeycloakToUserModelTransferDelegate {

//////////////////////////////////////////////////////////////////////////////
///       METHODS
//////////////////////////////////////////////////////////////////////////////
    /**
     * Transform method: UserRepresentation to Model
     * @param user Object to transform
     * @return Model transformed
     */
    public static UserRepresentation userModelToKeycloakUserRepresentation(final KeycloakUserModel model) {
        UserRepresentation user = new UserRepresentation();
        //Important ! The UserId   is called "UsarName" at KeyCloak!!!
        user.setUsername(model.getUserId().asString());
       
        user.setFirstName(model.getFirstName());
        user.setLastName(model.getLastName());
        user.setEmail(model.getEmail() != null ? model.getEmail().asString() : null);
        user.setEmailVerified(model.isEmailVerified());
        user.setEnabled(true);
        return user;
    }

    /**
     * Transforms a Keycloak UserRepresentation into a Model (DN00KeycloakUserModel).
     * This method maps a Keycloak user representation to an internal application model.
     * @param user The Keycloak UserRepresentation to transform.
     * @return The transformed model object.
     */
    public static KeycloakUserModel keycloakUserRepresentationToUserModel(final UserRepresentation user) {
    	KeycloakUserModel model = new KeycloakUserModel();
    	model.setUserOid(KeycloakUserOID.forId(user.getId()));
    	
        //Important ! The UserId   is called "UsarName" at KeyCloak!!!
        model.setUserId(KeycloakUserID.forId(user.getUsername()));  
        
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(EMail.create(user.getEmail()));
        model.setEmailVerified(user.isEmailVerified());
        return model;
    }
}
