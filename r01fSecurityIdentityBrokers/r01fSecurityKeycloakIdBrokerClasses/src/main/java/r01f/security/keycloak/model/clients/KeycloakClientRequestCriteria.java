///////////////////////////////////////////////////////////////////////////////////////
// Interface  for locate a client using and oid or id
//////////////////////////////////////////////////////////////////////////////////////
package r01f.security.keycloak.model.clients;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;

/**
 * 	Interface  for locate a client using and oid or id
 */
public interface  KeycloakClientRequestCriteria {
	
	@SuppressWarnings("unchecked")
	default <Q extends KeycloakClientRequestCriteria>  Q as (final Class<Q> as) {
		return(Q)this;			
	}
	default boolean byId() {
		return  this instanceof KeycloakClientGetRequestCriteriaByID;
	}
	default boolean byOid() {
		return  this instanceof KeycloakClientGetRequestCriteriaByOID;
	}
	public default KeycloakClientOID getKeycloakClientOid() {
		if (byOid()) {
			return  ((KeycloakClientGetRequestCriteriaByOID)this).getClientOid();
		}
		throw new IllegalArgumentException(" is not a valid oid");
	}
	public default KeycloakClientID getKeycloakClientId() {
		if (byId()) {
			return  ((KeycloakClientGetRequestCriteriaByID)this).getKeycloakClientId();
		}
		throw new IllegalArgumentException(" is not a valida id");
	}	
	
	@RequiredArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakClientGetRequestCriteriaByID 
		implements KeycloakClientRequestCriteria {
		
		@Getter  final protected KeycloakClientID _clientId;;
	}
	
	@RequiredArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakClientGetRequestCriteriaByOID 
		implements KeycloakClientRequestCriteria {
		
		@Getter  final protected KeycloakClientOID _clientOid;;
	}
		
	}

