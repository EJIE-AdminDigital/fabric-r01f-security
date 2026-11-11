package r01f.security.keycloak.api.interfaces.impl;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.representations.AccessTokenResponse;

import jakarta.ws.rs.NotAuthorizedException;
import lombok.extern.slf4j.Slf4j;
import r01f.httpclient.responsebodyhandlers.HttpResponseBodyHandlerUsingMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.security.keycloak.api.interfaces.KeycloakAuthServicesForClients;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.auth.KeycloakAuthRequestResponse;
import r01f.security.keycloak.model.auth.KeycloakClientAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakClientAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakClientRefreshTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakToAuthModelTransferDelegate;
import r01f.types.url.Url;

@Slf4j
public class KeycloakAuthServicesForClientsImpl 
	 extends KeycloakAuthServicesForClientsBase 
  implements KeycloakAuthServicesForClients {	
	
////////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
////////////////////////////////////////////////////////////////////////
	public KeycloakAuthServicesForClientsImpl(final KeycloakClientConfig keyCloakClientConfig,
											  final Keycloak keycloakAdminClient,
											  final Marshaller marshaller) {
		super(keyCloakClientConfig, 			
			  keycloakAdminClient,marshaller);	
	}	
////////////////////////////////////////////////////////////////////////
///  METHODS TO IMPLEMENT
////////////////////////////////////////////////////////////////////////
	@Override
	/**
	 *  Important! This auth  method must be only  used with Password Grant Flow!
	 */
	public KeycloakClientAuthRequestResult authenticate(final KeycloakClientAuthTokenRequest authRequest) {	
		return _authenticate(authRequest);
	}
	
	
	@Override
	public KeycloakClientAuthRequestResult refresh(final KeycloakClientRefreshTokenRequest refreshRequest) {		
		return _refresh(refreshRequest);
	}
	
////////////////////////////////////////////////////////////////////////
/// PROTECTED METHODS
////////////////////////////////////////////////////////////////////////
	public KeycloakClientAuthRequestResult _authenticate(final KeycloakClientAuthTokenRequest authRequest) {	
	    Url serverUrl = _keycloakAdminClientConfig.getUrl();
	    KeycloakRealm realm = authRequest.getRealm();
	    
	    // 1. Client Identity [ Application ]
	    String clientId = authRequest.getClientId().getId();
	    String clientSecret = (authRequest.getClientPassword() != null) 
	                          ? authRequest.getClientPassword().asString() 
	                          : null;
	    
	    try {
	    	//[1] Create a client for this user
	        Keycloak keycloakAuth = KeycloakBuilder.builder()
							                       .serverUrl(serverUrl.asString())
							                       .realm(realm.asString())
							                       .clientId(clientId)
							                       .clientSecret(clientSecret)
							                       .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
								                   .build();

	        // [2]. Request access token
	        AccessTokenResponse tokenResponse = keycloakAuth.tokenManager().getAccessToken();

	        // 4. Map(KeycloakAuthRequestResponse)
	        KeycloakAuthRequestResponse tokenSet = KeycloakToAuthModelTransferDelegate.toKeycloalAuthRequestResponse(tokenResponse);
	                                       

	        // 5. Devolver el resultado exitoso
	        return new KeycloakClientAuthRequestResult(authRequest.getRealm(), tokenSet);

	    } catch (final NotAuthorizedException e) {
	    	 return _handleAuthError(authRequest.getRealm(), clientId,"invalid_credentials", e);
	    } catch (final Throwable e) {
	   	 	return _handleAuthError(authRequest.getRealm(), clientId,"undhandled error", e);
	    }
	}
	
	
	public KeycloakClientAuthRequestResult _refresh(final KeycloakClientRefreshTokenRequest refreshRequest) {	
	    Url serverUrl = _keycloakAdminClientConfig.getUrl();
	    KeycloakRealm realm = refreshRequest.getRealm();

	    log.warn("[token refresh] realm='{}'", realm.asString());
	    try {
	    	// [2] Prepare the parameters for refresh_token grant
	        Map<String, String> params = new HashMap<>();
	        params.put(KeycloakAuthConstants.GRANT_TYPE_PARAM,    KeycloakAuthConstants.REFRESH_TOKEN_PARAM);
	        params.put(KeycloakAuthConstants.REFRESH_TOKEN_PARAM, refreshRequest.getRefreshToken().asSecurityToken().asString());

	        // [3] Build and send the HTTP request
	        String requestBody = _urlEncodedSecurityPostParams(params);
	        URI uriToPost = _composeAccesTokenUrl(serverUrl, realm);
	        log.warn("[token refresh] uri to post: {}", uriToPost);

	        HttpRequest request = HttpRequest.newBuilder()
	                                         .uri(uriToPost)
	                                         .header(KeycloakAuthConstants.CONTENT_TYPE_HEADER, KeycloakAuthConstants.APPLICATION_FORM_URLENCODED)
	                                         .POST(HttpRequest.BodyPublishers.ofString(requestBody))
	                                       .build();

	        HttpResponse<Supplier<KeycloakAuthRequestResponse>> response = _httpClient.send(request,
	                                                                                        HttpResponseBodyHandlerUsingMarshaller
	                                                                                                        .using(_marshaller)
	                                                                                                        .toMarshallTo(KeycloakAuthRequestResponse.class));
	        // [4] Handle response
	        int code = response.statusCode();
	        log.warn("[token refresh] response code: {}", code);
	        KeycloakAuthRequestResponse kcResponse = response.body().get();

	        if (code >= 200 && code < 300) {
	            return new KeycloakClientAuthRequestResult(realm, kcResponse);
	        }
	        String error = response.toString();
	        return new KeycloakClientAuthRequestResult(realm, error);

	    } catch (final NotAuthorizedException e) {
	        return _handleAuthError(refreshRequest.getRealm(), null, "invalid_credentials", e);
	    } catch (final Throwable e) {
	    	return _handleAuthError(refreshRequest.getRealm(), null, "Unexpected error", e);
	    }
	}
}
