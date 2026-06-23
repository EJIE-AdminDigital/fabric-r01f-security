package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakUserUpdateRequest
	 extends KeycloakRequestBase<KeycloakUserUpdateRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
// FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userModel")
	@Getter KeycloakUserModel _userModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakUserUpdateRequest(final KeycloakRealm realm,
			                         final KeycloakUserModel userModel) {		
		super(realm);
		_userModel = userModel;
	}
	
}
