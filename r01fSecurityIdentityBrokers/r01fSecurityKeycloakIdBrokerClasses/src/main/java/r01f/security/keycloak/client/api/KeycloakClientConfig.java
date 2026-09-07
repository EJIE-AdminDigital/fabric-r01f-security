package r01f.security.keycloak.client.api;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientSecret;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.types.url.Url;
import r01f.util.types.Strings;

/**
 * Models the {@code keycloak} config section.
 * <pre class='brush:yaml'>
 * keycloak:
 *   url: ${DENA_KEYCLOAK_URL}
 *   admin:
 *     token-url:         ${DENA_TOKEN_URL_ADMIN}
 *     introspection-uri: ${DENA_INTROSPECTION_URL_ADMIN}
 *     realm:             ${DENA_KEYCLOAK_REALM_ADMIN}
 *     client-id:         ${DENA_KEYCLOAK_CLIENT_ID_ADMIN}
 *     client-secret:     ${DENA_KEYCLOAK_CLIENT_SECRET_ADMIN}
 *   user:
 *     token-url:         ${DENA_TOKEN_URL_USER}
 *     introspection-uri: ${DENA_INTROSPECTION_URL_USER}
 *     realm:             ${DENA_KEYCLOAK_REALM_USER}
 *     client-id:         ${DENA_KEYCLOAK_CLIENT_ID_USER}
 *     client-secret:     ${DENA_KEYCLOAK_CLIENT_SECRET_USER}
 * </pre>
 */
@Accessors(prefix="_")
@NoArgsConstructor
public class KeycloakClientConfig
	 implements Debuggable {
	
/////////////////////////////////////////////////////////////////////////////////////////
//PROPERTIES PREFIX (yml)
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PROPERTIES_PREFIX = "keycloak";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * The keycloak server base url ({@code keycloak.url}), shared by every section
     */
    @Getter @Setter private Url _url;
    /**
     * The {@code keycloak.admin} section (realm / client used to manage keycloak)
     */
    @Getter @Setter private KeycloakRealmClientConfig _admin;
    /**
     * The {@code keycloak.user} section (realm / client used to authenticate users)
     */
    @Getter @Setter private KeycloakRealmClientConfig _user;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientConfig(final Url endPoint,
								final KeycloakRealmClientConfig admin,
								final KeycloakRealmClientConfig user) {
		_url = endPoint;
		_admin = admin;
		_user = user;
	}
	/**
	 * Backwards-compatible constructor: builds a config with a single (admin) section
	 */
	public KeycloakClientConfig(final KeycloakRealm realm,
	                            final KeycloakClientID keycloakClientId,final KeycloakClientSecret keycloakClientSecret,
	                            final Url endPoint) {
		_url = endPoint;
		_admin = new KeycloakRealmClientConfig(null,			// token-url
											   null,			// introspection-uri
											   realm,
											   keycloakClientId,keycloakClientSecret);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEBUG
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public CharSequence debugInfo() {
		StringBuilder outDbgInfo = new StringBuilder();
		outDbgInfo.append(Strings.customized("\nKeycloak Config : {\n\tEndpoint {}",
											 _url != null ? _url : " is null!"));
		outDbgInfo.append("\n\t[admin] ").append(_admin != null ? _admin.debugInfo() : " is null!");
		outDbgInfo.append("\n\t[user] ").append(_user  != null ? _user.debugInfo()  : " is null!");
		outDbgInfo.append("\n}\n");
		return outDbgInfo.toString();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	NESTED REALM / CLIENT CONFIG ([admin] or [user] section)
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Models a {@code keycloak.admin} / {@code keycloak.user} section:
	 * the realm + client credentials + the token url used against that realm
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor @AllArgsConstructor
	public static class KeycloakRealmClientConfig
		 implements Debuggable {

		@Getter @Setter private Url                  _tokenUrl;				// token-url
		@Getter @Setter private Url                  _introspectionUri;		// introspection-uri
		@Getter @Setter private KeycloakRealm        _realm;				// realm
		@Getter @Setter private KeycloakClientID	 _clientId;		// client-id
		@Getter @Setter private KeycloakClientSecret _clientSecret;	// client-secret

		@Override
		public CharSequence debugInfo() {
			StringBuilder outDbgInfo = new StringBuilder();
			outDbgInfo.append(Strings.customized("{ tokenUrl={}",
												 _tokenUrl != null ? _tokenUrl : " is null!"));
			outDbgInfo.append(Strings.customized(", introspectionUri={}",
												 _introspectionUri != null ? _introspectionUri : " is null!"));
			outDbgInfo.append(Strings.customized(", realm={}",
												 _realm != null ? _realm : " is null!"));
			outDbgInfo.append(Strings.customized(", clientId={}",
												 _clientId != null ? _clientId : " is null!"));
			outDbgInfo.append(Strings.customized(", clientSecret={}",
												 _clientSecret != null ? _clientSecret : " is null!"));
			outDbgInfo.append(" }");
			return outDbgInfo.toString();
		}
	}
}
