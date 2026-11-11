package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.Password;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;

/**
*
KeycloakClientAuthTokenRequest: Object Model Breakdown
The model encapsulates two critical pieces of information required by Keycloak to issue an Access Token:

   [ Client Application Identity ]
   ---------------------------------
	clientId: The logical name of the application (e.g., "mobile-app", "admin-portal") as defined in the Keycloak Console.
	
	clientPassword: Also known as the Client Secret. It is used if the application is configured as "Confidential" (Server-side).
	
	This identifies the OAuth2 Client. Keycloak needs this to verify that the login attempt is coming from a trusted and authorized application.
 */

@Accessors(prefix = "_")
@MarshallType(as="keycloakUserAuthTokenRequest")
@NoArgsConstructor
public class KeycloakClientAuthTokenRequest
		extends KeycloakUserAuthRequestBase {


    // [ Client Application Identity ]
    @MarshallField(as="clientId")
    @Getter private KeycloakClientID _clientId;
    @MarshallField(as="clientPassword")
    @Getter private Password _clientPassword;

////////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
////////////////////////////////////////////////////////////////////////
    public KeycloakClientAuthTokenRequest(final KeycloakRealm realm,
                                          final KeycloakClientID clientId, final Password  clientPassword
                                         ) {
        super(realm);
        _clientId = clientId;
        _clientPassword = clientPassword;
    }
}