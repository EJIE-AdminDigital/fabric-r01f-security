package r01f.security.keycloak.api.interfaces.impl;

import java.util.List;
import java.util.Optional;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.UserRepresentation;

import lombok.extern.slf4j.Slf4j;
import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.clients.KeycloakClientRequestCriteria;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria;


@Slf4j
abstract class KeycloakServicesBaseImpl {
/////////////////////////////////////////////////////////////////////////////////////////
// 	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Marshaller _marshaller;
	protected final KeycloakClientConfig  _keycloakAdminClientConfig;	
	protected final Keycloak _keycloakAdminRestAPI;	 
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakServicesBaseImpl(final KeycloakClientConfig  keyCloakClientConfig,
			                        final Keycloak keyCloakClient, 
			                        final Marshaller marshaller ) {
		_keycloakAdminRestAPI = keyCloakClient;
		_keycloakAdminClientConfig = keyCloakClientConfig;
		_marshaller  = marshaller;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	protected ClientRepresentation _clientRepresentation(final ClientsResource clientsResource, final KeycloakClientRequestCriteria criteria) {
		 try {
			if (criteria.byOid()) {
		        //[] Direct fetch using the Internal UUID (KeycloakClientOID)
		        return clientsResource.get(criteria.getKeycloakClientOid().asString()).toRepresentation();
		    } 		    
	        //[] Search using the Business ID (KeycloakClientID)
	    	List<ClientRepresentation> clients =  clientsResource.findByClientId(criteria.getKeycloakClientId().asString());	
	    	return (clients == null || clients.isEmpty()) ? null : clients.get(0);
	    } catch ( final jakarta.ws.rs.ProcessingException proceEx) {
	    	proceEx.printStackTrace();
	    	throw new IllegalStateException("Cannot process request against KeyCloak: " +proceEx.getLocalizedMessage());
	    }
	   
	}

	@SuppressWarnings("static-method")
	protected UserRepresentation _userRepresentation(final UsersResource usersResource, final KeycloakUserRequestCriteria criteria) {
		  try {
			if (criteria.byOid()) {
		        // Direct fetch using the Internal UUID (KeycloakUserOID)
		        return usersResource.get(criteria.getKeycloakUserOid().asString()).toRepresentation();
		    } 		    
		    // Search using Username (KeycloakUserID) with exact match = true
		    log.warn("...Search using Username [ KeycloakUserID ] ='{}'",criteria.getKeycloakUserId().asString() );
		    List<UserRepresentation> users = usersResource.searchByUsername(criteria.getKeycloakUserId().asString(), 
                                                  true);//exact    
		    return (users == null 
    					|| users.isEmpty()) ? null : users.get(0);
	    } catch ( final jakarta.ws.rs.ProcessingException proceEx) {
	    	proceEx.printStackTrace();
	    	throw new IllegalStateException("Cannot process request against KeyCloak: " +proceEx.getLocalizedMessage());
	    }		
	   
	}

	protected ClientResource _clientResource(final ClientsResource clientsResource, final KeycloakClientRequestCriteria criteria) {
	    // Optimization: If we already have the OID, we don't need to fetch the whole Representation
	    String oid = criteria.byOid() 
			                 ? criteria.getKeycloakClientOid().asString() 
			                 : Optional.ofNullable(_clientRepresentation(clientsResource, criteria))
			                           .map(ClientRepresentation::getId)
			                           .orElse(null);

	    return (oid == null) ? null : clientsResource.get(oid);
	}

	protected UserResource _userResource(final UsersResource usersResource, final KeycloakUserRequestCriteria criteria) {	    
	    String oid = criteria.byOid() 
	                 ? criteria.getKeycloakUserOid().asString() 
	                 : Optional.ofNullable(_userRepresentation(usersResource, criteria))
	                           .map(UserRepresentation::getId)
	                           .orElse(null);
	    return (oid == null) ? null : usersResource.get(oid);
	}
	
}
