package r01f.security.keycloak.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityOIDs.IsSecurityObjectOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

/**
 * -----------------------------------------------------------------------------------------------------------
 * KEYCLOAK IDENTIFIER REFERENCE TABLE
 *############################################################################################
 * Our  Fabric  Name   | Keycloak Native Field | Type     | Description
 * ---------------------------------------------------------------------------------------
 * KeycloakClientOID   | id                    | UUID   | Internal KC-DB primary key for clients . Required for updates/delete.
 * KeycloakClientID    | clientId              | String | Business name (e.g., "mi-app-backend"). Unique in Realm.
 * KeycloakUserOID     | id (or 'sub' in JWT)  | UUID   | Internal KC-DB primary key for ud . Required for updates/delete.
 * KeycloakUserID      | username              | String | Human-readable login name (e.g., "moshe.dayan").
 * ---------------------------------------------------------------------------------------------------------
 */

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class KeycloakOIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface IsKeycloakOID 
	  	     extends IsSecurityObjectOID {
		// just a marker interface
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
    /**
	 * Keycloak Client oid : Keyclock asigned oid for client
	 */
	@Immutable
	@MarshallType(as="keycloakClientOid")
	public record KeycloakClientOID(@Getter String id)
       implements IsKeycloakOID {
		
		public static KeycloakClientOID from(final String id) {
			return new KeycloakClientOID(id);
		}
		public static KeycloakClientOID forId(final String id) {
			return new KeycloakClientOID(id);
		}
		public static KeycloakClientOID valueOf(final String str) {
			return new KeycloakClientOID(str);
		}
		public static KeycloakClientOID fromString(final String id) {
			return new KeycloakClientOID(id);
		}
		public static KeycloakClientOID fromSecurityPrincipalOid(final SecurityPrincipalOID oid) {
			return new KeycloakClientOID(oid.asString());
		}
		public SecurityPrincipalOID asSecurityPrincipalOid() {
			return new SecurityPrincipalOID(this.id);
		}
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
	/**
	 * Keycloak Client oid : Keycloak asigned oid for user
	 */
	@Immutable
	@MarshallType(as="keycloakUserOid")
	public record KeycloakUserOID(@Getter String id)
       implements IsKeycloakOID {
		
		public static KeycloakUserOID from(final String id) {
			return new KeycloakUserOID(id);
		}
		public static KeycloakUserOID forId(final String id) {
			return new KeycloakUserOID(id);
		}
		public static KeycloakUserOID valueOf(final String str) {
			return new KeycloakUserOID(str);
		}
		public static KeycloakUserOID fromString(final String id) {
			return new KeycloakUserOID(id);
		}
		public static KeycloakUserOID fromSecurityPrincipalOid(final SecurityPrincipalOID oid) {
			return new KeycloakUserOID(oid.asString());
		}
		public SecurityPrincipalOID asSecurityPrincipalOid() {
			return new SecurityPrincipalOID(this.id);
		}
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
}
