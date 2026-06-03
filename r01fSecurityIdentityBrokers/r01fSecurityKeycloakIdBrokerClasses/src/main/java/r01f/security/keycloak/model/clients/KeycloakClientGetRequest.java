package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;
import r01f.security.keycloak.model.clients.KeycloakClientRequestCriteria.KeycloakClientGetRequestCriteriaByID;


@Accessors(prefix="_")
public class KeycloakClientGetRequest
	 extends KeycloakRequestBase<KeycloakClientGetRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//  MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter protected KeycloakClientRequestCriteria  _criteria;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientGetRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakClientGetRequest on(final KeycloakRealm realm) {
		return new KeycloakClientGetRequest(realm);
	}
	
	public KeycloakClientGetRequest  by(final KeycloakClientID clientId) {
		_criteria = new  KeycloakClientGetRequestCriteriaByID(clientId);
		return this;
	}
}
