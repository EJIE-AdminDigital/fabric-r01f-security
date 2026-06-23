///////////////////////////////////////////////////////////////////////////////////////
// Interface  for locate a client using and oid or id
//////////////////////////////////////////////////////////////////////////////////////
package r01f.security.keycloak.model.users;


import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;

@MarshallPolymorphicTypeInfo(typeIdPropertyName = "type")
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
			return  ((KeycloakUserGetRequestCriteriaByID)this).getClientId();
		}
		throw new IllegalArgumentException(" is not a valid id");
	}	
	
	@MarshallType(as="userGetRequestCriteriaByID", typeId="byID")
	@NoArgsConstructor
	@AllArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakUserGetRequestCriteriaByID 
		implements KeycloakUserRequestCriteria {
		
		@r01f.objectstreamer.annotations.MarshallField(as="clientId")
		@lombok.Getter @lombok.Setter protected KeycloakUserID _clientId;
	}
	
	@MarshallType(as="userGetRequestCriteriaByOID", typeId="byOID")
	@NoArgsConstructor
	@AllArgsConstructor
	@Accessors(prefix="_")
	public static class KeycloakUserGetRequestCriteriaByOID 
		implements KeycloakUserRequestCriteria {
		
		@r01f.objectstreamer.annotations.MarshallField(as="clientOid")
		@lombok.Getter @lombok.Setter protected KeycloakUserOID _clientOid;
	}
		
	}

