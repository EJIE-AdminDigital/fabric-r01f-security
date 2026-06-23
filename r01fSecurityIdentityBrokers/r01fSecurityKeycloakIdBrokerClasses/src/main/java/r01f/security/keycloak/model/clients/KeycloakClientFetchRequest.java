package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRole;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakClientFetchRequest
	 extends KeycloakRequestBase<KeycloakClientFetchRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//  (PARAMS TO FETCH )
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="role")
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
