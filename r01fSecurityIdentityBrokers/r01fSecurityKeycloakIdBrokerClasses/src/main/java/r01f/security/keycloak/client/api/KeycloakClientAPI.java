package r01f.security.keycloak.client.api;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForClientsAuthServices;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForClientsManagementServices;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForUsersAuthServices;
import r01f.security.keycloak.client.api.delegates.KeycloakClientAPIDelegateForUsersManagementServices;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;
import r01f.security.keycloak.model.auth.KeycloakClientAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakClientAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakRefreshToken;
import r01f.security.keycloak.model.auth.KeycloakUserAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakUserExchangeTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserRefreshTokenRequest;


/**
 * Base type for every API implementation of KeyCloack
 */
@Slf4j
@Accessors(prefix="_")
public class KeycloakClientAPI {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	private final Keycloak _keycloakClient;
	private final Keycloak _keycloakClientAdmin;
	private final Keycloak _keycloakClientUser;
	
	@SuppressWarnings("unused")
	@Getter private final KeycloakClientConfig _config;
/////////////////////////////////////////////////////////////////////////////////////////
// DELEGATES
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final KeycloakClientAPIDelegateForClientsManagementServices _forClientManagement;
	@Getter private final KeycloakClientAPIDelegateForUsersManagementServices _forUserManagement;
	@Getter private final KeycloakClientAPIDelegateForUsersAuthServices _forUserAuth;
	@Getter private final KeycloakClientAPIDelegateForClientsAuthServices _forClientAuth;
	
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
		_forClientAuth  = new KeycloakClientAPIDelegateForClientsAuthServices(config,_keycloakClientAdmin,marshaller);
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public KeycloakAccessTokenSubApi getAccessTokenSubApiFor(final KeycloakRealm realm) {
		return new KeycloakAccessTokenSubApi(realm);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class KeycloakAccessTokenSubApi {
		protected final KeycloakRealm _keycloadRealm;
		
		/**
	     * Gets an Identity Broker Access Token using the Token-Exchange protocol.
	     * * @param id The internal broker security identity identifier.
	     * @param originExternalSecurityToken The token issued by the external provider (e.g. Google).
	     * @param securityPrincipalSourceIdentityProvider The identifier of the external provider.
	     * @return The result containing the newly issued Keycloak authentication tokens.
	     * @throws IllegalArgumentException if any of the mandatory parameters are null.
	     */
	    public  KeycloakUserAuthRequestResult exchangeAccessToken(final IdentityProvider securityPrincipalSourceIdentityProvider,final SecurityPrincipalID originIdentityBrokerPrincipalId,
	                                                         	  final SecurityToken originExternalSecurityToken) {
	    	log.warn("#######################################################");
	    	log.warn("########  Keycloak Token Exchange Process  ############");
	    	log.warn("#######################################################");
	    	log.warn("Identity Broker Security ID: (origin Security Provider LoginId ) '{}'",
	    			 originIdentityBrokerPrincipalId.asString());
	    	log.warn("OriginExternalSecurityToken: '{}'", 
	    			  originExternalSecurityToken);
	    	log.warn("OriginExternalSecurityProviderID: '{}'",
	    			 securityPrincipalSourceIdentityProvider);
	
	        // [1] Validation
	        if (originIdentityBrokerPrincipalId == null
			 || originExternalSecurityToken == null
			 || securityPrincipalSourceIdentityProvider == null) {
	            throw new IllegalArgumentException("Neither identity ID, external token nor provider ID can be null");
	        }
	        if (_keycloadRealm == null) throw new IllegalStateException("The target Keycloak users realm has not been initialized");
	
	        // [2] Build token exchange request
	        KeycloakUserExchangeTokenRequest request = new KeycloakUserExchangeTokenRequest(_keycloadRealm,
	                        							 									KeycloakUserID.forId(originIdentityBrokerPrincipalId.asString()),
	                        							 									originExternalSecurityToken,
	                        							 									securityPrincipalSourceIdentityProvider);
	        // [3] Call to keycloack
	        return KeycloakClientAPI.this.getForUserAuth()
	                   					 .exchange(request);
	    }
	    /**
	     * Refreshes an Identity Broker Access Token using the OAuth {@code refresh_token} grant.
	     * @param refreshToken The refresh token previously issued by Keycloak.
	     * @return The result containing the newly issued Keycloak authentication tokens.
	     * @throws IllegalArgumentException if the refresh token is null.
	     */
	    public KeycloakUserAuthRequestResult getRefreshedAccessToken(final SecurityToken refreshToken) {
	    	log.warn("#######################################################");
	    	log.warn("########  Keycloak Token Refresh Process  #############");
	    	log.warn("#######################################################");
	
	        // [1] Validation
	        if (refreshToken == null)throw new IllegalArgumentException("The refresh token cannot be null");
	        if (_keycloadRealm == null) throw new IllegalStateException("The target Keycloak users realm has not been initialized");
	
	        // [2] Build refresh token request
	        KeycloakRefreshToken keycloakRefreshToken = new KeycloakRefreshToken(refreshToken,
	        																	   0);
	        KeycloakUserRefreshTokenRequest request = new KeycloakUserRefreshTokenRequest(_keycloadRealm,
	                        							 								  KeycloakClientAPI.this.getConfig()
	                        							 								  	  					.getUser()
	                        							 								  	  					.getClientId(),
	                        							 								  null,		// client password is resolved from config at the keycloak client layer
	                        							 								  keycloakRefreshToken);
	        // [3] Call to keycloak
	        return KeycloakClientAPI.this.getForUserAuth()
	                   					 .refresh(request);
	    }
	    public KeycloakClientAuthRequestResult getAccessTokenByClientCredentials(final KeycloakClientID clientId, final Password clientSecret) {
	    	log.warn("#######################################################");
	    	log.warn("######  Keycloak Client Credentials Process  ##########");
	    	log.warn("#######################################################");
	
	        // [1] Validation
	        if (clientId == null
			 || clientSecret == null) {
	            throw new IllegalArgumentException("Neither client ID nor client Secret can be null");
	        }
	        if (_keycloadRealm == null) throw new IllegalStateException("The target Keycloak admins realm has not been initialized");
	
	        // [2] Build token exchange request
	        KeycloakClientAuthTokenRequest request = new KeycloakClientAuthTokenRequest(_keycloadRealm,
                        							 									clientId,
                        							 									clientSecret);
	        // [3] Call to keycloack
	        return KeycloakClientAPI.this.getForClientAuth()
	                   					 .authenticate(request);
	    	
	    }
	}
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
