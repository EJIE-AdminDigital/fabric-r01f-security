package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
public class KeycloakUserDeleteResult
	 extends KeycloakUserRequestResultBase<KeycloakUserDeleteResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	final @Getter KeycloakUserModel _userModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserDeleteResult(final KeycloakRealm realm,final KeycloakUserModel userModel) {
		super(realm,
			  userModel.getUserOid(),userModel.getUserId(),
			  KeycloakRequestedOperation.DELETE);
		_userModel = userModel;
	}
}

