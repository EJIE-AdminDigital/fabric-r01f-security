package r01f.security.keycloak.model;

import r01f.enums.EnumExtended;

public enum KeycloakRequestedOperation
 implements EnumExtended<KeycloakRequestedOperation> {
	HEAD,
	GET,
	PUT,
	DELETE,
	POST,
	FETCH;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeycloakRequestedOperation fromName(final String name) {
		return EnumExtended.fromName(name,
									 KeycloakRequestedOperation.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + KeycloakRequestedOperation.class + " element with name=" + name));
	}
}
