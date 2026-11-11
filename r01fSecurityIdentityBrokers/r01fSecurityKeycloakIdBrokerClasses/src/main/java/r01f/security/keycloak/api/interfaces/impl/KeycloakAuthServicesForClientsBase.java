package r01f.security.keycloak.api.interfaces.impl;

import java.net.URI;
import java.net.http.HttpClient;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;

import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.api.interfaces.KeycloakAuthServicesForClients;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.auth.KeycloakClientAuthRequestResult;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.util.types.Strings;


public abstract class KeycloakAuthServicesForClientsBase 
			  extends KeycloakServicesBaseImpl 
		   implements KeycloakAuthServicesForClients {	
	
	protected HttpClient _httpClient;
////////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
////////////////////////////////////////////////////////////////////////
	public KeycloakAuthServicesForClientsBase(final KeycloakClientConfig keyCloakClientConfig,
											  final Keycloak keycloakAdminClient,
											  final Marshaller marshaller) {
		super(keyCloakClientConfig, 			
			  keycloakAdminClient,marshaller);	
		_httpClient = HttpClient.newHttpClient();
	}	
////////////////////////////////////////////////////////////////////////
/// UTILITY METHODS
////////////////////////////////////////////////////////////////////////
    @SuppressWarnings("static-method")
	protected  String _urlEncodedSecurityPostParams(final Map<String, String> params) {
        StringBuilder encodedParams = new StringBuilder();
        params.entrySet().stream().forEach(entry -> {
            if (encodedParams.length() > 0) {
                encodedParams.append("&");
            }
            encodedParams.append(entry.getKey()).append("=").append(entry.getValue());
        });
        return encodedParams.toString();
    }
    
    @SuppressWarnings("static-method")
	protected KeycloakClientAuthRequestResult _handleAuthError(final KeycloakRealm realm, 
												             final String clientId, 
												             final String message, 
												             final Throwable throwable) {

        String error;
        if (throwable instanceof NotAuthorizedException) {
            // Error 401: Credenciales inválidas (User o Client)
        	error = Strings.customized("Authentication failed for client '{}' during [{}]: Invalid credentials.", clientId, message);
        } else if (throwable instanceof BadRequestException) {
            // Error 400: Token expirado, grant type inválido, etc.
        	error = Strings.customized("Bad request during [{}]. Message: {}", message, throwable.getMessage());
        } else {          
        	error = Strings.customized("Unexpected error during [{}] for client '{}': {}", message, clientId, throwable.getMessage(), throwable);
        }  
        //log.error(" oauth error {}" ,error);
        return new KeycloakClientAuthRequestResult(realm, error);
    }
    
    protected URI _composeAccesTokenUrl ( final Url serverBase , final KeycloakRealm realm) {    	
    	return serverBase.joinWith(UrlPath.from("realms"))
    	                 .joinWith(UrlPath.from(realm.asString()))
    	                 .joinWith(UrlPath.from("protocol/openid-connect/token")).asUriNoThrow();
    }
	
}
