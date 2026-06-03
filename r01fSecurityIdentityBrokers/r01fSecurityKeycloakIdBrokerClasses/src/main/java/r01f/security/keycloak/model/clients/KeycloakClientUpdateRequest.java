package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
public class KeycloakClientUpdateRequest
	 extends KeycloakRequestBase<KeycloakClientUpdateRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	 @Getter final  KeycloakClientModel _clientModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientUpdateRequest(final KeycloakRealm realm,
			                           final KeycloakClientModel clientModel) {
		
		super(realm);
		_clientModel = clientModel;
	}

}
