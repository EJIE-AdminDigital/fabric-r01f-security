package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakUserDeleteResult
	 extends KeycloakUserRequestResultBase<KeycloakUserDeleteResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="userModel")
	@Getter KeycloakUserModel _userModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserDeleteResult(final KeycloakRealm realm,final KeycloakUserModel userModel) {
		super(realm,
			  userModel.getOid(),userModel.getId(),
			  KeycloakRequestedOperation.DELETE);
		_userModel = userModel;
	}
}
