package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRole;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
public class KeycloakClientFetchRequest
	 extends KeycloakRequestBase<KeycloakClientFetchRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//  (PARAMS TO FETCH )
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter KeycloakRole  _role;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientFetchRequest(final KeycloakRealm realm) {
		super(realm);		
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakClientFetchRequest on(final KeycloakRealm realm) {
		return new KeycloakClientFetchRequest(realm);
	}
	public KeycloakClientFetchRequest all() {
		return this;
	}
	public KeycloakClientFetchRequest withRole(final KeycloakRole r) {
		_role = r;
		return this;
	}
	
	
}
