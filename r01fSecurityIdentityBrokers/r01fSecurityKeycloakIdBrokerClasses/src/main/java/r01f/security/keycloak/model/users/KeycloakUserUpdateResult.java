package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakUserUpdateResult
	 extends KeycloakUserRequestResultBase<KeycloakUserUpdateResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="usertModel")
	@Getter KeycloakUserModel _usertModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserUpdateResult(final KeycloakRealm realm,final KeycloakUserModel userModel) {
		super(realm,
			 userModel.getUserOid(),userModel.getUserId(),
			  KeycloakRequestedOperation.POST);
		_usertModel = userModel;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
 	
}
