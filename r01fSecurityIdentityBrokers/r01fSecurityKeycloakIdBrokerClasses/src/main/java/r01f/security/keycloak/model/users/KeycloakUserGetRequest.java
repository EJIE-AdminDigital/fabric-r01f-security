package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.KeycloakOIDs.KeycloakUserOID;
import r01f.security.keycloak.model.KeycloakRequestBase;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria.KeycloakUserGetRequestCriteriaByID;
import r01f.security.keycloak.model.users.KeycloakUserRequestCriteria.KeycloakUserGetRequestCriteriaByOID;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakUserGetRequest
	 extends KeycloakRequestBase<KeycloakUserGetRequest> {
	
	@MarshallField(as="criteria")
	@Getter @Setter protected KeycloakUserRequestCriteria  _criteria;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserGetRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	static KeycloakUserGetRequest on(final KeycloakRealm realm) {
		return new KeycloakUserGetRequest(realm);
	}
	
	public KeycloakUserGetRequest  by(final KeycloakUserID id) {
		_criteria = new  KeycloakUserGetRequestCriteriaByID(id);
		return this;
	}
	public KeycloakUserGetRequest  by(final KeycloakUserOID oid) {
		_criteria = new  KeycloakUserGetRequestCriteriaByOID(oid);
		return this;
	}
}
