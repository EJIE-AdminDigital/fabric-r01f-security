package r01f.security.keycloak.model.users;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.keycloak.common.util.CollectionUtil;
import org.keycloak.representations.idm.UserRepresentation;

import com.google.common.collect.Maps;

import r01f.security.keycloak.model.KeycloakIDs.KeycloakAttributeID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.base.KeycloackIdentityModelAtttribute;
import r01f.security.keycloak.model.base.KeycloackIdentityModelAtttribute.KeycloakAttributeValue;
import r01f.types.contact.EMail;
import r01f.util.types.collections.Lists;

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
        UserRepresentation userRepresentation = new UserRepresentation();
        
        //[ UserName ] Important ! The UserId   is called "UsenName" at KeyCloak!!!
        userRepresentation.setUsername(model.getId().asString());
       
        // Common Fields
        userRepresentation.setFirstName(model.getFirstName());
        userRepresentation.setLastName(model.getLastName());
        userRepresentation.setEmail(model.getEmail() != null ? model.getEmail().asString() : null);
        userRepresentation.setEmailVerified(model.isEmailVerified());
        userRepresentation.setEnabled(true);
        
        if (CollectionUtil.isNotEmpty(model.getAttributes())) {        	
        	Map<String, List<String>> attributtes  = Maps.newHashMap();
        	for ( KeycloackIdentityModelAtttribute att : model.getAttributes()) {        		
        		List<String> value  =  att.getValue().asList();
				attributtes.put(att.getId().asString(), value);
        	}
			userRepresentation.setAttributes(attributtes);
        }
       
        return userRepresentation;
    }

    /**
     * Transforms a Keycloak UserRepresentation into a Model (DN00KeycloakUserModel).
     * This method maps a Keycloak user representation to an internal application model.
     * @param user The Keycloak UserRepresentation to transform.
     * @return The transformed model object.
     */
    public static KeycloakUserModel keycloakUserRepresentationToUserModel(final UserRepresentation user) {
    	KeycloakUserModel model = new KeycloakUserModel();
    	model.setOid(KeycloakUserOID.forId(user.getId()));
    	
        //[ Important ! ]The UserId   is called "UsarName" at KeyCloak!!!
        model.setId(KeycloakUserID.forId(user.getUsername()));  
        
        // Common Fields
        model.setFirstName(user.getFirstName());
        model.setLastName(user.getLastName());
        model.setEmail(EMail.create(user.getEmail()));
        model.setEmailVerified(user.isEmailVerified());
        
        
        if ( user.getAttributes() != null  
        		&&  user.getAttributes().size() > 0 ) {
        	Collection<KeycloackIdentityModelAtttribute> atts = Lists.newArrayList();
		
        	
        	for (Map.Entry<String, List<String>> entry : user.getAttributes().entrySet()) {
        	    String key = entry.getKey();
        	    List<String> values = entry.getValue();
        	    atts.add(new KeycloackIdentityModelAtttribute(KeycloakAttributeID.forId(key),
        	    		                                      KeycloakAttributeValue.of(values)));
        	   
        	}
        	model.setAttributes(atts);
        }
        
        
        return model;
    }
}
