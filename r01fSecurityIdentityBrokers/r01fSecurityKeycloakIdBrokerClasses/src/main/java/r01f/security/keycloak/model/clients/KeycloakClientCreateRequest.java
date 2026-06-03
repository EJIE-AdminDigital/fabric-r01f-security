package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRole;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
public class KeycloakClientCreateRequest
	 extends KeycloakRequestBase<KeycloakClientCreateRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter final  KeycloakClientModel _clientModel;
	@Getter final  KeycloakRole   _clientRole;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientCreateRequest(final KeycloakRealm realm,
			                           final KeycloakClientModel clientModel, final KeycloakRole  clientRole) {
		
		super(realm);
		_clientModel = clientModel;
		_clientRole  = clientRole;
	}
}
