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
		_keycloakClient = _buildKeyCloakClient(config);
	
		// build every sub-api	
		_forClientManagement  = new KeycloakClientAPIDelegateForClientsManagementServices (config, _keycloakClient);
		_forUserManagement  =  new KeycloakClientAPIDelegateForUsersManagementServices(config, _keycloakClient);
		_forUserAuth  = new KeycloakClientAPIDelegateForUsersAuthServices(config,_keycloakClient,marshaller);
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//  SUB-APIs
/////////////////////////////////////////////////////////////////////////////////////////

/////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("resource")
	protected static Keycloak _buildKeyCloakClient(final KeycloakClientConfig config) {
		// Checks if client config is not null KeycloakClientConfig
		if (config == null) {
			Throwables.throwUnchecked(new IllegalArgumentException("In order to create instance of Keycloack, a client config, must be provided"));
		}

		// Minimun data for builder ( credentials & KeyCloak EnDpoint )
		 //..but don't call to build before checking the rest of parameters.
		/*KeycloakBuilder keycloakBuilder = 	KeycloakBuilder.builder()
				                                           .realm(config.getRealm().asString());
		
		if (config.getKeycloakClientId() != null 
				&& config.getKeycloakClientSecret() != null ) {
			keycloakBuilder.clientId(config.getKeycloakClientId().asString());
			keycloakBuilder.clientSecret(config.getKeycloakClientSecret().asString());
			
		} else {
			Throwables.throwUnchecked(new IllegalArgumentException("not valid client-id or secret"));
		}
		// ...next check endpoint (use the just created client builder)
		if (config.getEndPoint() != null) {
			URI endPoint = null;
			try {
				endPoint = config.getEndPoint().asUrl().toURI();
			} catch (final MalformedURLException |  URISyntaxException e) {
				Throwables.throwUnchecked(new IllegalArgumentException(e.getLocalizedMessage()));
			}
			keycloakBuilder.serverUrl(endPoint.toString());
		};
		
		keycloakBuilder.scope("client_credentials");
		//keycloakBuilder.grantType("openid");
		// return keycloak client.
		Keycloak kcClient = keycloakBuilder.build();
		return kcClient;*/
		
		
		Keycloak keyCloak =  KeycloakBuilder.builder()
							                .serverUrl(config.getEndPoint().asString())
							                .realm(config.getRealm().asString())
							                .clientId(config.getKeycloakClientId().asString())
							                .clientSecret(config.getKeycloakClientSecret().asString())
							                .grantType("client_credentials")
							                .scope("openid")
							               .build();
		
		
		
		return keyCloak;
	}

}
