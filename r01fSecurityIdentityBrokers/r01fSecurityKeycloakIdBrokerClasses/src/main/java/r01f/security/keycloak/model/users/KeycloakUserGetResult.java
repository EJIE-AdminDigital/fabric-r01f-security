package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
public class KeycloakUserGetResult
	 extends KeycloakUserRequestResultBase<KeycloakUserGetResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	final @Getter KeycloakUserModel _userModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserGetResult(final KeycloakRealm realm,final KeycloakUserModel userModel) {
		super(realm,
			  userModel.getUserOid(),userModel.getUserId(),
			  KeycloakRequestedOperation.GET);
		_userModel = userModel;
	}
}

