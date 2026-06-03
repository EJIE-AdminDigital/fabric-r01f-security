package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
public class KeycloakUserCreateRequest
	 extends KeycloakRequestBase<KeycloakUserCreateRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter final  KeycloakUserModel _userModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserCreateRequest(final KeycloakRealm realm,
			                         final KeycloakUserModel userModel) {		
		super(realm);
		_userModel = userModel;
	}	
}
