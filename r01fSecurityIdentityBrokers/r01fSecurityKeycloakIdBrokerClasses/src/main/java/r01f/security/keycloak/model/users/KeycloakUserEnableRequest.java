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
public class KeycloakUserEnableRequest
	 extends KeycloakRequestBase<KeycloakUserEnableRequest> {
	
	@MarshallField(as="criteria")
	@Getter @Setter protected KeycloakUserRequestCriteria  _criteria;
	@MarshallField(as="enable")
	@Getter @Setter protected boolean  _enable;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserEnableRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakUserEnableRequest on(final KeycloakRealm realm) {
		return new KeycloakUserEnableRequest(realm);
	}
	
	public KeycloakUserEnableRequest  by(final KeycloakUserID id) {
		_criteria = new  KeycloakUserGetRequestCriteriaByID(id);
		return this;
	}
	
	public KeycloakUserEnableRequest  by(final KeycloakUserOID oid) {
		_criteria = new  KeycloakUserGetRequestCriteriaByOID(oid);
		return this;
	}
	public KeycloakUserEnableRequest  enable() {
		_enable = true;
		return this;
	}
	public KeycloakUserEnableRequest  disable() {
		_enable = true;
		return this;
	}
}
