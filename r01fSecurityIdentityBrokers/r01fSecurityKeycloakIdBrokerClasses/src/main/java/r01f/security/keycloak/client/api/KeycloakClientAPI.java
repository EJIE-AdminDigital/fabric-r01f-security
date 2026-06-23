package r01f.security.keycloak.client.api;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.exceptions.Throwables;
import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForClientsManagementServices;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForUsersAuthServices;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForUsersManagementServices;


/**
 * Base type for every API implementation of KeyCloack
 */

@Accessors(prefix="_")
public class KeycloakClientAPI {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private final Keycloak _keycloakClient;
	private final Keycloak _keycloakClientAdmin;
	private final Keycloak _keycloakClientUser;
	@SuppressWarnings("unused")
	private final KeycloakClientConfig _config;
/////////////////////////////////////////////////////////////////////////////////////////
// DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final KeycloakClientAPIDelegateForClientsManagementServices _forClientManagement;
	@Getter private final KeycloakClientAPIDelegateForUsersManagementServices   _forUserManagement;
	@Getter private final KeycloakClientAPIDelegateForUsersAuthServices        _forUserAuth;
	
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakClientAPI(final Marshaller marshaller,
			                 final KeycloakClientConfig config) {

		// Checks if client config is not null KeycloakClientConfig
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of KeyCloack"+ ", a client config, must be provided"));
		}
		_config  = config;
		_keycloakClientAdmin = _buildKeyCloakClientAdmin(config);
		_keycloakClientUser = _buildKeyCloakClientUser(config);
		_keycloakClient = _keycloakClientAdmin;

		// build every sub-api	
		_forClientManagement  = new KeycloakClientAPIDelegateForClientsManagementServices (config, _keycloakClientAdmin);
		_forUserManagement  =  new KeycloakClientAPIDelegateForUsersManagementServices(config, _keycloakClientUser);
		_forUserAuth  = new KeycloakClientAPIDelegateForUsersAuthServices(config,_keycloakClientUser,marshaller);
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//  SUB-APIs
/////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("resource")
	protected static Keycloak _buildKeyCloakClient(final KeycloakClientConfig config) {
		return _buildKeyCloakClientAdmin(config);
	}

	@SuppressWarnings("resource")
	protected static Keycloak _buildKeyCloakClientAdmin(final KeycloakClientConfig config) {
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of Keycloack, a client config, must be provided"));
		}
		if (config.getAdmin() == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of Keycloack admin, an admin section config must be provided"));
		}

		Keycloak keyCloak =  KeycloakBuilder.builder()
							                .serverUrl(config.getUrl().getUrl())
							                .realm(config.getAdmin().getRealm().asString())
							                .clientId(config.getAdmin().getClientId().asString())
							                .clientSecret(config.getAdmin().getClientSecret().asString())
							                .grantType("client_credentials")
							                .scope("openid")
							               .build();
		return keyCloak;
	}

	@SuppressWarnings("resource")
	protected static Keycloak _buildKeyCloakClientUser(final KeycloakClientConfig config) {
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of Keycloack, a client config, must be provided"));
		}
		if (config.getUser() == null) {
			// Fallback to admin if user config is not specified
			return _buildKeyCloakClientAdmin(config);
		}

		Keycloak keyCloak =  KeycloakBuilder.builder()
							                .serverUrl(config.getUrl().getUrl())
							                .realm(config.getUser().getRealm().asString())
							                .clientId(config.getUser().getClientId().asString())
							                .clientSecret(config.getUser().getClientSecret().asString())
							                .grantType("client_credentials")
							                .scope("openid")
							               .build();

		return keyCloak;
	}

}
