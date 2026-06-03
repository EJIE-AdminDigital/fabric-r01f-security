package r01f.security.keycloak.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.objectstreamer.annotations.MarshallType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class KeycloakIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	Keycloak
/////////////////////////////////////////////////////////////////////////////////////////
///
	/**
	 * Base for every Keycloack ID objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class KCIDBase
						 extends OIDBaseMutable<String> {
					 
		private static final long serialVersionUID = 4162366466990455545L;

		public KCIDBase(final String id) {
			super(id);
		}
	}
    /*
	 * Keycloak Client Id : : El ID alfanumérico que usas en tus aplicaciones (ej: mi-app-backend).
	 */
	@Immutable
	@MarshallType(as="keycloakClientId")
	@NoArgsConstructor
	public static final class KeycloakClientID
			 extends KCIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakClientID(final String oid) {
			super(oid);
		}
		public static KeycloakClientID forId(final String id) {
			return new KeycloakClientID(id);
		}
		public static KeycloakClientID valueOf(final String str) {
			return new KeycloakClientID(str);
		}
	}
	
	
	
 /*
	 * Keycloak Client Id : : El ID alfanumérico que usas en tus users (ej: mi-app-backend).
	 */
	@Immutable
	@MarshallType(as="keycloakUserId")
	@NoArgsConstructor
	public static final class KeycloakUserID
			 extends KCIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakUserID(final String oid) {
			super(oid);
		}
		public static KeycloakUserID forId(final String id) {
			return new KeycloakUserID(id);
		}
		public static KeycloakUserID valueOf(final String str) {
			return new KeycloakUserID(str);
		}
	}
		
	
  /*
	 * Keycloak Client Role
	 */
	@Immutable
	@MarshallType(as="keycloakClientRole")
	@NoArgsConstructor
	public static final class KeycloakRole
			 extends KCIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakRole(final String oid) {
			super(oid);
		}
		public static KeycloakRole forId(final String id) {
			return new KeycloakRole(id);
		}
		public static KeycloakRole valueOf(final String str) {
			return new KeycloakRole(str);
		}
	}
	

	
	/**
	 * Keycloak Client Secret
	 */
	@Immutable
	@MarshallType(as="keycloakClientSecret")
	@NoArgsConstructor
	public static final class KeycloakClientSecret
			 extends KCIDBase{
		
		private static final long serialVersionUID = -1546028791819011235L;
		public KeycloakClientSecret(final String oid) {
			super(oid);
		}
		public static KeycloakClientSecret forId(final String id) {
			return new KeycloakClientSecret(id);
		}
		public static KeycloakClientSecret valueOf(final String str) {
			return new KeycloakClientSecret(str);
		}
	}
	
	
	/**
	 * Keycloak Realm
	 */
	@Immutable
	@MarshallType(as="keycloakRealm")
	@NoArgsConstructor
	public static final class KeycloakRealm
					  extends KCIDBase {
		
		private static final long serialVersionUID = -5210327231117228216L;
		public KeycloakRealm(final String oid) {
			super(oid);
		}
		public static KeycloakRealm forId(final String id) {
			return new KeycloakRealm(id);
		}
		public static KeycloakRealm valueOf(final String str) {
			return new KeycloakRealm(str);
		}
	}
	

}
