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
public class KeycloakUserHeadRequest
	 extends KeycloakRequestBase<KeycloakUserHeadRequest> {
	
	@MarshallField(as="criteria")
	@Getter @Setter protected KeycloakUserRequestCriteria  _criteria;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserHeadRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	static KeycloakUserHeadRequest on(final KeycloakRealm realm) {
		return new KeycloakUserHeadRequest(realm);
	}
	
	public KeycloakUserHeadRequest  by(final KeycloakUserID id) {
		_criteria = new  KeycloakUserGetRequestCriteriaByID(id);
		return this;
	}
	public KeycloakUserHeadRequest  by(final KeycloakUserOID oid) {
		_criteria = new  KeycloakUserGetRequestCriteriaByOID(oid);
		return this;
	}
}
