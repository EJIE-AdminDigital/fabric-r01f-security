package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

@Accessors(prefix = "_")
@RequiredArgsConstructor
public abstract class KeycloakUserAuthRequestBase {
	
	@Getter protected final KeycloakRealm _realm;
}