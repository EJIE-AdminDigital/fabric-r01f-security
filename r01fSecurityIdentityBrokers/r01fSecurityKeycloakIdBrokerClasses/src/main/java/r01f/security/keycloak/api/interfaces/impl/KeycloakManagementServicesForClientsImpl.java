package r01f.security.keycloak.api.interfaces.impl;

import java.util.Collection;
import java.util.stream.Collectors;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.ClientResource;
import org.keycloak.admin.client.resource.ClientsResource;
import org.keycloak.representations.idm.ClientRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import jakarta.ws.rs.core.Response;
import r01f.security.keycloak.api.interfaces.KeycloakManagementServicesForClients;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;
import r01f.security.keycloak.model.clients.KeycloakClientCreateRequest;
import r01f.security.keycloak.model.clients.KeycloakClientCreateResult;
import r01f.security.keycloak.model.clients.KeycloakClientDeleteRequest;
import r01f.security.keycloak.model.clients.KeycloakClientDeleteResult;
import r01f.security.keycloak.model.clients.KeycloakClientEnableRequest;
import r01f.security.keycloak.model.clients.KeycloakClientFetchRequest;
import r01f.security.keycloak.model.clients.KeycloakClientFetchResult;
import r01f.security.keycloak.model.clients.KeycloakClientGetRequest;
import r01f.security.keycloak.model.clients.KeycloakClientGetResult;
import r01f.security.keycloak.model.clients.KeycloakClientModel;
import r01f.security.keycloak.model.clients.KeycloakClientRequestCriteria.KeycloakClientGetRequestCriteriaByOID;
import r01f.security.keycloak.model.clients.KeycloakClientUpdateRequest;
import r01f.security.keycloak.model.clients.KeycloakClientUpdateResult;
import r01f.security.keycloak.model.clients.KeycloakToClientModelTransferDelegate;
import r01f.securitycontext.SecurityIDS.SecurityToken;


public class KeycloakManagementServicesForClientsImpl 
		extends KeycloakServicesBaseImpl 
	implements KeycloakManagementServicesForClients {
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakManagementServicesForClientsImpl(final KeycloakClientConfig keyCloakClientConfig,
													final Keycloak keycloakAdminClient) {
		super(keyCloakClientConfig, keycloakAdminClient,null);	
	}	
///////////////////////////////////////////////////////////////////////////////////////////////////
/// METHODS TO IMPLEMENT
//////////////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public KeycloakClientGetResult load(final KeycloakClientGetRequest loadRequest) {		
		return _load(loadRequest);
	}

	@Override
	public KeycloakClientCreateResult create(final KeycloakClientCreateRequest postRequest) {	
		return _create(postRequest);
	}
	
	@Override
	public KeycloakClientUpdateResult update(final KeycloakClientUpdateRequest putRequest) {	
		return _update(putRequest);
	}
	
	@Override
	public KeycloakClientUpdateResult enable(final KeycloakClientEnableRequest enableRequest) {		
		return _enable(enableRequest);
	}

	@Override
	public KeycloakClientDeleteResult delete(final KeycloakClientDeleteRequest deleteRequest) {		
		return _delete(deleteRequest);
	}
	
	@Override
	public KeycloakClientFetchResult fetch(final KeycloakClientFetchRequest fetchRequest) {		
		return _fetch(fetchRequest);
	}
///////////////////////////////////////////////////////////////////////////////////////////////////
/// PROTECTED METHODS
//////////////////////////////////////////////////////////////////////////////////////////////////	
	protected KeycloakClientGetResult _load(final KeycloakClientGetRequest getRequest) {		
		ClientsResource clientsResource = _keycloakAdminRestAPI.realm(getRequest.getRealm().asString()).clients();		
		ClientRepresentation clientRepresentation = _clientRepresentation(clientsResource,getRequest.getCriteria());		
		if (clientRepresentation == null) {
			return null;
		}
		KeycloakClientModel model = KeycloakToClientModelTransferDelegate.keycloakClientRepresentationToClientModel(clientRepresentation);	
		return new KeycloakClientGetResult(getRequest.getRealm(),model);
	}

	
	protected KeycloakClientCreateResult _create(final KeycloakClientCreateRequest postRequest) {
	    ClientsResource clientsResource = _keycloakAdminRestAPI.realm(postRequest.getRealm().asString()).clients();
	    KeycloakClientModel model = postRequest.getClientModel();
	    
	    //[1]Validate that the CLINET ID (KeycloakClientID) MUST BE  provided externally
	    if (model.getClientId() == null 
	    		|| model.getClientId().asString().isBlank()) {
	        throw new IllegalArgumentException("The KeycloakClientOID must be provided externally for client creation.");
	    }	    
	    //[2] Map model to Representation
	    ClientRepresentation keycloakClient = KeycloakToClientModelTransferDelegate.clientModelToKeycloakClientRepresentation(model);

	    // 3. Set Keycloak specific defaults and force the provided ID
	    keycloakClient.setClientId(model.getClientId().asString());
	    keycloakClient.setAuthorizationServicesEnabled(true);
	    keycloakClient.setPublicClient(false);
	    keycloakClient.setDirectAccessGrantsEnabled(true);
	    keycloakClient.setStandardFlowEnabled(true);
	    keycloakClient.setServiceAccountsEnabled(true);
	    // 4. Execute Creation
	    Response response = clientsResource.create(keycloakClient);
	    try {
	        if (response.getStatus() == 201) {
	            // 5. EXTRACT the OID generated by Keycloak from the Location header
	            //  :::> The header looks like: .../clients/ae34-f231-ad32...
	            String path = response.getLocation().getPath();
	            String internalOid = path.substring(path.lastIndexOf('/') + 1);
	            
	            // Update the model with the OID that Keycloak just created
	            model.setOid(KeycloakClientOID.forId(internalOid));

	            // 6. Access the Resource using the NEW OID to perform post-creation steps
	            ClientResource newClientResource = clientsResource.get(internalOid);

	            // 7. Create the Admin Role
	            RoleRepresentation role = new RoleRepresentation();
	            role.setName(postRequest.getClientRole().asString()); 
	            role.setDescription("[ fabric ] admin role created during client initialization.");
	            newClientResource.roles().create(role);

	            // 8. Retrieve and set the Client Secret generated by Keycloak
	            String secretValue = newClientResource.getSecret().getValue();
	            model.setSecret(SecurityToken.forId(secretValue));
	            return new KeycloakClientCreateResult(postRequest.getRealm(), model);
	        }
	        
	       // log.error("Failed to create Keycloak client. HTTP Status: {}", response.getStatus());
	        return null;
	        
	    } finally {
	        response.close();
	    }
	}



	protected KeycloakClientUpdateResult _update(final KeycloakClientUpdateRequest putRequest) {		
		ClientsResource clientsResource = _keycloakAdminRestAPI.realm(putRequest.getRealm().asString()).clients();
		if (putRequest.getClientModel().getOid() == null ) {
			throw new IllegalArgumentException("..not provided a valid object with his oid");
		}		
		
		//[1] Obtain client resource
		ClientResource clientResource = _clientResource(clientsResource, 
													    new KeycloakClientGetRequestCriteriaByOID(putRequest.getClientModel().getOid() ));
		
		if (clientResource == null) {
			//log.error("Client not found for update");
			return null;
		}		
		//[2] Map updated model data to a Keycloak Representation
		ClientRepresentation updatedRep = KeycloakToClientModelTransferDelegate.clientModelToKeycloakClientRepresentation(putRequest.getClientModel());
		
		//[3] Execute update
		clientResource.update(updatedRep);		
		return new KeycloakClientUpdateResult(putRequest.getRealm(), putRequest.getClientModel());
	}
	


	protected KeycloakClientUpdateResult _enable(final KeycloakClientEnableRequest enableRequest) {
		ClientsResource clientsResource = _keycloakAdminRestAPI.realm(enableRequest.getRealm().asString()).clients();
		ClientResource  clientResource =  _clientResource(clientsResource,enableRequest.getCriteria());
		if (clientResource == null) {
			//log.error(" not found element to delete");
		}
		ClientRepresentation representation = clientResource.toRepresentation();
		representation.setEnabled(enableRequest.isEnable());
		clientResource.update(representation);
    	KeycloakClientModel model =  KeycloakToClientModelTransferDelegate.keycloakClientRepresentationToClientModel(representation);	;
		return new KeycloakClientUpdateResult(enableRequest.getRealm(),model);
	}

	
	protected KeycloakClientDeleteResult _delete(final KeycloakClientDeleteRequest deleteRequest) {
		ClientsResource clientsResource = _keycloakAdminRestAPI.realm(deleteRequest.getRealm().asString()).clients();
		ClientResource  clientResource =  _clientResource(clientsResource,deleteRequest.getCriteria());
		if (clientResource == null) {
			//log.error(" not found element to delete");
		}
		clientResource.remove();
    	KeycloakClientModel model =  KeycloakToClientModelTransferDelegate.keycloakClientRepresentationToClientModel(clientResource.toRepresentation());	;
		return new KeycloakClientDeleteResult(deleteRequest.getRealm(),model);
	
	}	
	
     protected KeycloakClientFetchResult _fetch(final KeycloakClientFetchRequest fetchRequest) {		
	    Collection<ClientRepresentation> clientRepresentations = _keycloakAdminRestAPI.realm(fetchRequest.getRealm().getId())
				                                                                	.clients()
				                                                                  .findAll();

	
	    Collection<KeycloakClientModel> clientModels = null;
	    if (fetchRequest.getRole() == null ) {
	        clientModels = clientRepresentations.stream()
	        										.map(KeycloakToClientModelTransferDelegate::keycloakClientRepresentationToClientModel)
									            .collect(Collectors.toList());
	    } else {	      
	        String targetRoleId = fetchRequest.getRole().getId();
	        String realmId = fetchRequest.getRealm().getId();

	        clientModels = clientRepresentations.stream()
	            .filter(client -> _hasRole(realmId, client.getId(), targetRoleId))
	            .map(KeycloakToClientModelTransferDelegate::keycloakClientRepresentationToClientModel)
	            .collect(Collectors.toList());
	    }		  
	    return new KeycloakClientFetchResult(fetchRequest.getRealm(), clientModels);
	}
	 
	 private boolean _hasRole(final String realmId, String clientId, String roleName) {
		    return _keycloakAdminRestAPI.realm(realmId)
					                    .clients()
					                    .get(clientId)
					                    .roles()
					                    .list() 
					                    .stream()
					                  .anyMatch(r -> r.getName().equals(roleName));
		}


}
