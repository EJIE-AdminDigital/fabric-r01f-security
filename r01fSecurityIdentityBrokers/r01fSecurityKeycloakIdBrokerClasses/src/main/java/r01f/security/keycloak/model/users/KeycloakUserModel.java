package r01f.security.keycloak.model.users;

import java.util.Collection;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakAttributeID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.base.KeycloackIdentityModelAtttribute;
import r01f.security.keycloak.model.base.KeycloackIdentityModelAtttribute.KeycloakAttributeValue;
import r01f.security.keycloak.model.base.KeycloackIdentityModelObjectBase;
import r01f.types.contact.EMail;

/**
 * Model for KeycloakUserModel
 */
@NoArgsConstructor
@Accessors(prefix="_")
@MarshallType(as="keycloakUser")
public class KeycloakUserModel
		extends KeycloackIdentityModelObjectBase<KeycloakUserOID,KeycloakUserID,KeycloakUserModel> {
	private static final long serialVersionUID = -8389048195998131206L;
//////////////////////////////////////////////////////////////////////////////
//  FIELDS
//////////////////////////////////////////////////////////////////////////////
    // The given name of the user.
    @MarshallField(as="given_name")
    @Getter @Setter private String _firstName;

    // The family name of the user.
    @MarshallField(as="family_name")
    @Getter @Setter private String _lastName;

    // The email of the user.
    @MarshallField(as="email")
    @Getter @Setter private EMail _email;

    // The email verified status of the user.
    @MarshallField(as="email_verified")
    @Getter @Setter private boolean _emailVerified;
//////////////////////////////////////////////////////////////////////////////
// FIELDS
//////////////////////////////////////////////////////////////////////////////
	
  

}
