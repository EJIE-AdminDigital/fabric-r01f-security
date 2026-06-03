package r01f.security.keycloak.model.clients;

import java.util.Collection;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestResultBase;
import r01f.security.keycloak.model.KeycloakRequestedOperation;


@Accessors(prefix="_")
public class KeycloakClientFetchResult
	 extends KeycloakRequestResultBase<KeycloakClientFetchResult> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	final @Getter Collection<KeycloakClientModel> _clientModels;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientFetchResult(final KeycloakRealm realm,final Collection<KeycloakClientModel> clientModels) {
		super(realm,
			  KeycloakRequestedOperation.FETCH);
		_clientModels = clientModels;
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
