package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;
import r01f.security.keycloak.model.clients.KeycloakClientRequestCriteria.KeycloakClientGetRequestCriteriaByID;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakClientDeleteRequest
	 extends KeycloakRequestBase<KeycloakClientDeleteRequest> {
	
	@MarshallField(as="criteria")
	@Getter @Setter protected KeycloakClientRequestCriteria  _criteria;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientDeleteRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	static KeycloakClientDeleteRequest on(final KeycloakRealm realm) {
		return new KeycloakClientDeleteRequest(realm);
	}
	
	public KeycloakClientDeleteRequest  by(final KeycloakClientID clientId) {
		_criteria = new  KeycloakClientGetRequestCriteriaByID(clientId);
		return this;
	}
}
