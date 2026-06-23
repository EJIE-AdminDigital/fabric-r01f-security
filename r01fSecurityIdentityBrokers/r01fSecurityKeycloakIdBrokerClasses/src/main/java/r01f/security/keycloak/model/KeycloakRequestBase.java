package r01f.security.keycloak.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

/**
 * Request Base
 */
@Accessors(prefix="_")
@NoArgsConstructor
public abstract class KeycloakRequestBase<SELF_TYPE extends KeycloakRequestBase<SELF_TYPE>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="realm")
	@Getter @Setter protected KeycloakRealm _realm;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakRequestBase(final KeycloakRealm realm) {
		_realm = realm;		
	}
}
