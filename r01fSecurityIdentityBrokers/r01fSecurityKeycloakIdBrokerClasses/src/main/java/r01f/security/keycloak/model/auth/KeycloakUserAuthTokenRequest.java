package r01f.security.keycloak.model.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.Password;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakClientID;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakUserID;

/**
*
KeycloakUserAuthTokenRequest: Object Model Breakdown
The model encapsulates four critical pieces of information required by Keycloak to issue an Access Token:

1. [ User Identity ]
   ------------------------------
	userId: The unique identifier (username or email) of the human being attempting to log in.
	
	userPassword: The secret credential known only to the user.

	This represents the Resource Owner in OAuth2 terminology.

2. [ Client Application Identity ]
   ---------------------------------
	clientId: The logical name of the application (e.g., "mobile-app", "admin-portal") as defined in the Keycloak Console.
	
	clientPassword: Also known as the Client Secret. It is used if the application is configured as "Confidential" (Server-side).
	
	This identifies the OAuth2 Client. Keycloak needs this to verify that the login attempt is coming from a trusted and authorized application.
 */

@Accessors(prefix = "_")
@MarshallType(as="keycloakUserAuthTokenRequest")
@NoArgsConstructor
public class KeycloakUserAuthTokenRequest
		extends KeycloakUserAuthRequestBase {
    
	// [ User Identity ]
    @MarshallField(as="userId")
    @Getter private KeycloakUserID _userId;
    @MarshallField(as="userPassword")
    @Getter private Password _userPassword;


    // [ Client Application Identity ]
    @MarshallField(as="clientId")
    @Getter private KeycloakClientID _clientId;
    @MarshallField(as="clientPassword")
    @Getter private Password _clientPassword;  // Opcional, según el tipo de cliente

////////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
////////////////////////////////////////////////////////////////////////
    public KeycloakUserAuthTokenRequest(final KeycloakRealm realm, 
                                        final KeycloakUserID   userId,  final Password  userPassword,
                                        final KeycloakClientID clientId, final Password  clientPassword
                                        ) {
        super(realm);
        _userId = userId;
        _clientId = clientId;
        _userPassword = userPassword;
        _clientPassword = clientPassword;
    }
}