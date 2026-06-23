package r01f.security.keycloak.model.auth;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

@Accessors(prefix = "_")
@NoArgsConstructor
@AllArgsConstructor
public abstract class KeycloakUserAuthRequestBase {
	
	@MarshallField(as="realm")
	@Getter protected KeycloakRealm _realm;
}