///////////////////////////////////////////////////////////////////////////////////////
// Interface  for locate a client using and oid or id
//////////////////////////////////////////////////////////////////////////////////////
package r01f.security.keycloak.model.clients;


import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakClientOID;

/**
 * 	Interface  for locate a client using and oid or id
 */
@MarshallPolymorphicTypeInfo(typeIdPropertyName = "type")
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
			return  ((KeycloakClientGetRequestCriteriaByID)this).getClientId();
		}
		throw new IllegalArgumentException(" is not a valida id");
	}	
	
	@MarshallType(as="clientGetRequestCriteriaByID", typeId="byID")
	@lombok.NoArgsConstructor
	@lombok.AllArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakClientGetRequestCriteriaByID 
		implements KeycloakClientRequestCriteria {
		
		@r01f.objectstreamer.annotations.MarshallField(as="clientId")
		@lombok.Getter @lombok.Setter protected KeycloakClientID _clientId;
	}
	
	@MarshallType(as="clientGetRequestCriteriaByOID", typeId="byOID")
	@lombok.NoArgsConstructor
	@lombok.AllArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakClientGetRequestCriteriaByOID 
		implements KeycloakClientRequestCriteria {
		
		@r01f.objectstreamer.annotations.MarshallField(as="clientOid")
		@lombok.Getter @lombok.Setter protected KeycloakClientOID _clientOid;
	}
		
	}

