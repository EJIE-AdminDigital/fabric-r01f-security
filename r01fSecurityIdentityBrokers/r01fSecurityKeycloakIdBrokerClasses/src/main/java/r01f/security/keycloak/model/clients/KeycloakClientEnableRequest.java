package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;
import r01f.security.keycloak.model.clients.KeycloakClientRequestCriteria.KeycloakClientGetRequestCriteriaByID;


@Accessors(prefix="_")
public class KeycloakClientEnableRequest
	 extends KeycloakRequestBase<KeycloakClientEnableRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter protected KeycloakClientRequestCriteria  _criteria;
	@Getter @Setter protected boolean  _enable;	
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientEnableRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakClientEnableRequest on(final KeycloakRealm realm) {
		return new KeycloakClientEnableRequest(realm);
	}	
	public KeycloakClientEnableRequest  by(final KeycloakClientID clientId) {
		_criteria = new  KeycloakClientGetRequestCriteriaByID(clientId);
		return this;
	}
	public KeycloakClientEnableRequest  enable() {
		_enable = true;
		return this;
	}
	public KeycloakClientEnableRequest  disable() {
		_enable = true;
		return this;
	}
}
