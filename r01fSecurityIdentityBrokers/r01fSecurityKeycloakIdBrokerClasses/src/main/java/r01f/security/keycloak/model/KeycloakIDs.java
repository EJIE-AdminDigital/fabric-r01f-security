package r01f.security.keycloak.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.IsSecurityObjectID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class KeycloakIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public interface IsKeycloakID 
	  	     extends IsSecurityObjectID {
		// just a marker interface
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
    /**
	 * Keycloak Client Id : : El ID alfanumérico que usas en tus aplicaciones (ej: mi-app-backend).
	 */
	@Immutable
	@MarshallType(as="keycloakClientId")
	public record KeycloakClientID(@Getter String id)
	   implements IsKeycloakID {
		
		public static KeycloakClientID from(final String id) {
			return new KeycloakClientID(id);
		}
		public static KeycloakClientID forId(final String id) {
			return new KeycloakClientID(id);
		}
		public static KeycloakClientID valueOf(final String str) {
			return new KeycloakClientID(str);
		}
		public static KeycloakClientID fromString(final String id) {
			return new KeycloakClientID(id);
		}
		public static KeycloakClientID fromSecurityPrincipalId(final SecurityPrincipalID id) {
			return new KeycloakClientID(id.asString());
		}
		public SecurityPrincipalID asSecurityPrincipalId() {
			return new SecurityPrincipalID(this.id.toUpperCase());	// keycloak username
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
	 * Keycloak username
	 */
	@Immutable
	@MarshallType(as="keycloakUserId")
	public record KeycloakUserID(@Getter String id)
	   implements IsKeycloakID {
		
		public static KeycloakUserID from(final String id) {
			return new KeycloakUserID(id);
		}
		public static KeycloakUserID forId(final String id) {
			return new KeycloakUserID(id);
		}
		public static KeycloakUserID valueOf(final String str) {
			return new KeycloakUserID(str);
		}
		public static KeycloakUserID fromString(final String str) {
			return new KeycloakUserID(str);
		}
		public static KeycloakUserID fromSecurityPrincipalId(final SecurityPrincipalID id) {
			return new KeycloakUserID(id.asString());
		}
		public SecurityPrincipalID asSecurityPrincipalId() {
			return new SecurityPrincipalID(this.id.toUpperCase());	// keycloak username
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
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Immutable
	@MarshallType(as="keycloakAttributeId")
	public record KeycloakAttributeID(@Getter String id)
	    implements IsKeycloakID {

		public static KeycloakAttributeID from(final String id) {
			return new KeycloakAttributeID(id);
		}
		public static KeycloakAttributeID forId(final String id) {
			return new KeycloakAttributeID(id);
		}
		public static KeycloakAttributeID valueOf(final String str) {
			return new KeycloakAttributeID(str);
		}
		public static KeycloakAttributeID fromString(final String id) {
			return new KeycloakAttributeID(id);
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
	 * Keycloak Client Role
	 */
	@Immutable
	@MarshallType(as="keycloakClientRole")
	public record KeycloakRole(@Getter String id)
	   implements IsKeycloakID {
		
		public static KeycloakRole from(final String id) {
			return new KeycloakRole(id);
		}
		public static KeycloakRole forId(final String id) {
			return new KeycloakRole(id);
		}
		public static KeycloakRole valueOf(final String str) {
			return new KeycloakRole(str);
		}
		public static KeycloakRole fromString(final String id) {
			return new KeycloakRole(id);
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
	 * Keycloak Client Secret
	 */
	@Immutable
	@MarshallType(as="keycloakClientSecret")
	public record KeycloakClientSecret(@Getter String id)
	   implements IsKeycloakID {
		
		public static KeycloakClientSecret from(final String id) {
			return new KeycloakClientSecret(id);
		}
		public static KeycloakClientSecret forId(final String id) {
			return new KeycloakClientSecret(id);
		}
		public static KeycloakClientSecret valueOf(final String str) {
			return new KeycloakClientSecret(str);
		}
		public static KeycloakClientSecret fromString(final String id) {
			return new KeycloakClientSecret(id);
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
	 * Keycloak Realm
	 */
	@Immutable
	@MarshallType(as="keycloakRealm")
	public record KeycloakRealm(@Getter String id)
	   implements IsKeycloakID {
		
		public static KeycloakRealm from(final String id) {
			return new KeycloakRealm(id);
		}
		public static KeycloakRealm forId(final String id) {
			return new KeycloakRealm(id);
		}
		public static KeycloakRealm valueOf(final String str) {
			return new KeycloakRealm(str);
		}
		public static KeycloakRealm fromString(final String id) {
			return new KeycloakRealm(id);
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
