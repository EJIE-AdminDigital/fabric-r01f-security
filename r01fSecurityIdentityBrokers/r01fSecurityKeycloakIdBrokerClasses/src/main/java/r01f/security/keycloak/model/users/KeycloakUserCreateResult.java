package r01f.security.keycloak.model.users;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakUserCreateResult
	 extends KeycloakUserRequestResultBase<KeycloakUserCreateResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="usertModel")
	@Getter KeycloakUserModel _usertModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakUserCreateResult(final KeycloakRealm realm,final KeycloakUserModel clientModel) {
		super(realm,
			  clientModel.getOid(),clientModel.getId(),
			  KeycloakRequestedOperation.POST);
		_usertModel = clientModel;
	}
}
