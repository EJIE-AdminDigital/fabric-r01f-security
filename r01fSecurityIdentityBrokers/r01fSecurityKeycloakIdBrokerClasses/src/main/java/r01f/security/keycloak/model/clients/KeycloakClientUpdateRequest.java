package r01f.security.keycloak.model.clients;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakRequestBase;


@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakClientUpdateRequest
	 extends KeycloakRequestBase<KeycloakClientUpdateRequest> {
/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="clientModel")
	@Getter KeycloakClientModel _clientModel;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR / BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public KeycloakClientUpdateRequest(final KeycloakRealm realm,
			                           final KeycloakClientModel clientModel) {
		
		super(realm);
		_clientModel = clientModel;
	}

}
