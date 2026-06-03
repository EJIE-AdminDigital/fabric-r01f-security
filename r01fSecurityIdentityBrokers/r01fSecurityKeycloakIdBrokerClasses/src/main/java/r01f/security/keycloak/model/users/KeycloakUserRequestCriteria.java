///////////////////////////////////////////////////////////////////////////////////////
// Interface  for locate a client using and oid or id
//////////////////////////////////////////////////////////////////////////////////////
package r01f.security.keycloak.model.users;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;

public interface  KeycloakUserRequestCriteria {
	
	@SuppressWarnings("unchecked")
	default <Q extends KeycloakUserRequestCriteria>  Q as (final Class<Q> as) {
		return(Q)this;			
	}
	default boolean byId() {
		return  this instanceof KeycloakUserGetRequestCriteriaByID;
	}
	default boolean byOid() {
		return  this instanceof KeycloakUserGetRequestCriteriaByOID;
	}
	public default KeycloakUserOID getKeycloakUserOid() {
		if (byOid()) {
			return  ((KeycloakUserGetRequestCriteriaByOID)this).getClientOid();
		}
		throw new IllegalArgumentException(" is not a valid oid");
	}
	public default KeycloakUserID getKeycloakUserId() {
		if (byId()) {
			return  this.getKeycloakUserId();
		}
		throw new IllegalArgumentException(" is not a valid id");
	}	
	
	@RequiredArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakUserGetRequestCriteriaByID 
		implements KeycloakUserRequestCriteria {
		
		@Getter  final protected KeycloakUserID _clientId;;
	}
	
	@RequiredArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakUserGetRequestCriteriaByOID 
		implements KeycloakUserRequestCriteria {
		
		@Getter  final protected KeycloakUserOID _clientOid;;
	}
		
	}

