package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.KeycloakRequestBase;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria.KeycloakUserGetRequestCriteriaByID;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria.KeycloakUserGetRequestCriteriaByOID;


@Accessors(prefix="_")
public class KeycloakUserDeleteRequest
	 extends KeycloakRequestBase<KeycloakUserDeleteRequest> {
	
	@Getter @Setter protected KeycloakUserRequestCriteria  _criteria;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserDeleteRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakUserDeleteRequest on(final KeycloakRealm realm) {
		return new KeycloakUserDeleteRequest(realm);
	}
	
	public KeycloakUserDeleteRequest  by(final KeycloakUserID id) {
		_criteria = new  KeycloakUserGetRequestCriteriaByID(id);
		return this;
	}
	public KeycloakUserDeleteRequest  by(final KeycloakUserOID oid) {
		_criteria = new  KeycloakUserGetRequestCriteriaByOID(oid);
		return this;
	}
}
