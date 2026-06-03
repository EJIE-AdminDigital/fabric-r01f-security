package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
public class KeycloakClientDeleteResult
	 extends KeycloakClientRequestResultBase<KeycloakClientDeleteResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	final @Getter KeycloakClientModel _clientModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientDeleteResult(final KeycloakRealm realm,final KeycloakClientModel clientModel) {
		super(realm,clientModel.getOid(),
			  KeycloakRequestedOperation.DELETE);
		_clientModel = clientModel;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
 	@Override
	public CharSequence debugInfo() {
 		StringBuilder dbg = new StringBuilder(super.debugInfo());
 		return dbg;
	}
}
