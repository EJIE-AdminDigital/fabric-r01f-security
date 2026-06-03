package r01f.security.keycloak.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.objectstreamer.annotations.MarshallType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class KeycloakOIDs {
/**-----------------------------------------------------------------------------------------------------------
 * KEYCLOAK IDENTIFIER REFERENCE TABLE
 *############################################################################################
 * Our  Fabric  Name   | Keycloak Native Field | Type     | Description
 * ---------------------------------------------------------------------------------------
 * KeycloakClientOID   | id                    | UUID   | Internal KC-DB primary key for clients . Required for updates/delete.
 * KeycloakClientID    | clientId              | String | Business name (e.g., "mi-app-backend"). Unique in Realm.
 * KeycloakUserOID     | id (or 'sub' in JWT)  | UUID   | Internal KC-DB primary key for ud . Required for updates/delete.
 * KeycloakUserID      | username              | String | Human-readable login name (e.g., "moshe.dayan").
 * ---------------------------------------------------------------------------------------------------------
	/**
	 * Base for every Keycloack OID objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class KeyCloackOIDBase
						 extends OIDBaseMutable<String> {
					 
		private static final long serialVersionUID = 4162366466990455545L;

		public KeyCloackOIDBase(final String id) {
			super(id);
		}
	}
    /*
	 * Keycloak Client oid : Keyclock asigned oid for client
	 */
	@Immutable
	@MarshallType(as="keycloakClientOid")
	@NoArgsConstructor
	public static final class KeycloakClientOID
			 extends KeyCloackOIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakClientOID(final String oid) {
			super(oid);
		}
		public static KeycloakClientOID forId(final String id) {
			return new KeycloakClientOID(id);
		}
		public static KeycloakClientOID valueOf(final String str) {
			return new KeycloakClientOID(str);
		}
	}
	
	/*
	* Keycloak Client oid : Keyclock asigned oid for user
	 */
	@Immutable
	@MarshallType(as="keycloakUserOid")
	@NoArgsConstructor
	public static final class KeycloakUserOID
			 extends KeyCloackOIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakUserOID(final String oid) {
			super(oid);
		}
		public static KeycloakUserOID forId(final String id) {
			return new KeycloakUserOID(id);
		}
		public static KeycloakUserOID valueOf(final String str) {
			return new KeycloakUserOID(str);
		}
	}
	

}
