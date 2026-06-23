package r01f.security.keycloak.model.clients;

import org.keycloak.representations.idm.ClientRepresentation;

import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;
import r01f.securitycontext.SecurityIDS.SecurityToken;

/**
 * Utility class responsible for transforming between different representations of keycloak objects.
 * This class provides methods to map between Model and Representation objects.
 */
public class KeycloakToClientModelTransferDelegate {

//////////////////////////////////////////////////////////////////////////////
//       CONSTRUCTORS
//////////////////////////////////////////////////////////////////////////////

    /**
     * Private constructor for static class
     */
    private KeycloakToClientModelTransferDelegate() {

    }

//////////////////////////////////////////////////////////////////////////////
///       METHODS
//////////////////////////////////////////////////////////////////////////////

    /**
     * Converts a Keycloak ClientRepresentation object into an DN00KeycloakClientModel.
     *
     * @param client The Keycloak ClientRepresentation to be converted.
     * @return An DN00KeycloakClientModel containing the mapped data from the ClientRepresentation.
     */
    public static KeycloakClientModel keycloakClientRepresentationToClientModel(final ClientRepresentation client){

    	KeycloakClientModel clientModel= new KeycloakClientModel();
        clientModel.setOid(KeycloakClientOID.forId(client.getId()));
        clientModel.setName(client.getName());
        if (client.getSecret() != null) {
            clientModel.setSecret(SecurityToken.forId(client.getSecret()));
        }
        clientModel.setClientId(KeycloakClientID.forId(client.getClientId()));
        clientModel.setEnabled(client.isEnabled());
        return clientModel;

    }
    
    

    /**
     * Converts a Keycloak ClientRepresentation object into an DN00KeycloakClientModel.
     *
     * @param model The Keycloak ClientRepresentation to be converted.
     * @return An DN00KeycloakClientModel containing the mapped data from the ClientRepresentation.
     */
    public static ClientRepresentation clientModelToKeycloakClientRepresentation(final KeycloakClientModel model){

        ClientRepresentation client=new ClientRepresentation();
        client.setId(model.getOid() != null ? model.getOid().asString() : null);
        client.setName(model.getName());
        client.setSecret(model.getSecret() != null ? model.getSecret().asString() : null);
        client.setClientId(model.getClientId() != null ? model.getClientId().asString() : null);
        client.setEnabled(model.isEnabled());
        return client;

    }
}
