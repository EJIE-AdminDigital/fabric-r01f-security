package r01f.security.keycloak.model.clients;


import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.base.KeycloackIdentityModelObjectBase;
import r01f.security.keycloak.model.users.KeycloakUserModel;
import r01f.securitycontext.SecurityIDS.SecurityToken;

/**
 * Model for KeycloakClientModel
 */
@Accessors(prefix="_")
public class KeycloakClientModel
	extends KeycloackIdentityModelObjectBase<KeycloakClientOID,KeycloakClientID,KeycloakClientModel> {

	private static final long serialVersionUID = 2425688459351488280L;
//////////////////////////////////////////////////////////////////////////////
//   FIELDS
//////////////////////////////////////////////////////////////////////////////
	/** Unique identifier of the client asigned by KeyCloak */
    /*@MarshallField(as="oid")
    @Getter @Setter private KeycloakClientOID _oid;    

    /** Identifier assigned to the client in Keycloak. */
    /*@MarshallField(as="clientId")
    @Getter @Setter private KeycloakClientID _clientId;*/

    /** Name of the client. */
    @MarshallField(as="name")
    @Getter @Setter private String _name;

    /** Secret key associated with the client for secure communication. */
    @MarshallField(as="secret")
    @Getter @Setter private SecurityToken _secret;

    /** Indicates whether the client is enabled or disabled. */
    @MarshallField(as="enabled")
    @Getter @Setter private boolean _enabled;
    

}
