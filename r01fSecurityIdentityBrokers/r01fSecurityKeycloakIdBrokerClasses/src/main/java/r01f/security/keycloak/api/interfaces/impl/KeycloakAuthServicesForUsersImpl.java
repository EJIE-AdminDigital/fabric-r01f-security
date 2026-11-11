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
import r01f.security.keycloak.api.interfaces.KeycloakAuthServicesForUsers;
import r01f.security.keycloak.client.api.KeycloakClientConfig;
import r01f.security.keycloak.model.KeycloakIDs.KeycloakRealm;
import r01f.security.keycloak.model.auth.KeycloakAuthRequestResponse;
import r01f.security.keycloak.model.auth.KeycloakToAuthModelTransferDelegate;
import r01f.security.keycloak.model.auth.KeycloakUserAuthRequestResult;
import r01f.security.keycloak.model.auth.KeycloakUserAuthTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserExchangeTokenRequest;
import r01f.security.keycloak.model.auth.KeycloakUserRefreshTokenRequest;
import r01f.types.url.Url;

@Slf4j
public class KeycloakAuthServicesForUsersImpl 
		extends KeycloakAuthServicesForUsersBase 
	implements KeycloakAuthServicesForUsers {	
	
////////////////////////////////////////////////////////////////////////
/// CONSTRUCTOR
////////////////////////////////////////////////////////////////////////
	public KeycloakAuthServicesForUsersImpl(final KeycloakClientConfig keyCloakClientConfig,
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
	public KeycloakUserAuthRequestResult authenticate(final KeycloakUserAuthTokenRequest authRequest) {	
		return _authenticate(authRequest);
	}
	
	
	@Override
	public KeycloakUserAuthRequestResult refresh(final KeycloakUserRefreshTokenRequest refreshRequest) {		
		return _refresh(refreshRequest);
	}

	@Override
	public KeycloakUserAuthRequestResult exchange(final KeycloakUserExchangeTokenRequest exchangeRequest) {		
		return _exchange(exchangeRequest);
	}
	
////////////////////////////////////////////////////////////////////////
/// PROTECTED METHODS
////////////////////////////////////////////////////////////////////////
	public KeycloakUserAuthRequestResult _authenticate(final KeycloakUserAuthTokenRequest authRequest) {	
	    Url serverUrl = _keycloakAdminClientConfig.getUrl();
	    KeycloakRealm realm = authRequest.getRealm();
	    
	    // 1. Client Identity [ Application ]
	    String clientId = authRequest.getClientId().getId();
	    String clientSecret = (authRequest.getClientPassword() != null) 
	                          ? authRequest.getClientPassword().asString() 
	                          : null;

	    // 2.User( "human")
	    String userIdAsString = authRequest.getUserId().getId();
	    String userPassword = authRequest.getUserPassword().asString();
	    try {
	    	//[1] Create a client for this user :
			//     Important! This method must be only  used with Password Grant Flow!
	        Keycloak keycloakAuth = KeycloakBuilder.builder()
								                    .serverUrl(serverUrl.asString())
								                    .realm(realm.asString())
								                    .clientId(clientId)
								                    .clientSecret(clientSecret) 
								                    .username(userIdAsString)
								                    .password(userPassword)
								                    .grantType(OAuth2Constants.PASSWORD)
								                  .build();

	        // [2]. Request access token
	        AccessTokenResponse tokenResponse = keycloakAuth.tokenManager().getAccessToken();

	        // 4. Map(KeycloakAuthRequestResponse)
	        KeycloakAuthRequestResponse tokenSet = KeycloakToAuthModelTransferDelegate.toKeycloalAuthRequestResponse(tokenResponse);
	                                       

	        // 5. Devolver el resultado exitoso
	        return new KeycloakUserAuthRequestResult(authRequest.getRealm(), tokenSet);

	    } catch (final NotAuthorizedException e) {
	    	 return _handleAuthError(authRequest.getRealm(), userIdAsString,"invalid_credentials", e);
	    } catch (final Throwable e) {
	   	 	return _handleAuthError(authRequest.getRealm(), userIdAsString,"undhandled error", e);
	    }
	}
	
	
	
	public KeycloakUserAuthRequestResult _exchange(final KeycloakUserExchangeTokenRequest exchangeRequest) {	
	   
	    
		Url serverUrl = _keycloakAdminClientConfig.getUrl();
	    KeycloakRealm realm = exchangeRequest.getRealm();
	    
	    String	clientIdAsString  = _keycloakAdminClientConfig.getUser().getClientId().asString();
	    String  clientSecretAsString = _keycloakAdminClientConfig.getUser().getClientSecret().asString();

	    String  userIdAString = exchangeRequest.getUserId().asString();
	    
	    
	    log.warn("...exchange  token request for user id '{}'",userIdAString);
	    try {
	    	Map<String, String> params = new HashMap<>();
	        params.put(KeycloakAuthConstants.CLIENT_ID_PARAM, clientIdAsString);
	        params.put(KeycloakAuthConstants.CLIENT_SECRET_PARAM,clientSecretAsString);
	        
	        params.put(KeycloakAuthConstants.GRANT_TYPE_PARAM, KeycloakAuthConstants.GRANT_TYPE);
	        params.put(KeycloakAuthConstants.SUBJECT_TOKEN_PARAM, _keycloakAdminRestAPI.tokenManager().getAccessTokenString());
	        params.put(KeycloakAuthConstants.REQUESTED_SUBJECT_PARAM, userIdAString);
	    	

	        //Create the body in x-www-form-urlencoded format
	        String requestBody = _urlEncodedSecurityPostParams(params);

	        //Create HTTP request	        
	        URI  uriToPost = _composeAccesTokenUrl(serverUrl,realm);
	        log.warn("uri to post: {} " ,uriToPost );
	        HttpRequest request = HttpRequest.newBuilder()
									                .uri(uriToPost)
									                .header(KeycloakAuthConstants.CONTENT_TYPE_HEADER, KeycloakAuthConstants.APPLICATION_FORM_URLENCODED)
									                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
								                .build();
	        
	        HttpResponse<String> rawResponse = _httpClient.send(request,
	        		                                         HttpResponse.BodyHandlers.ofString());
	 
            //Make the HTTP request
            /*HttpResponse<Supplier<KeycloakAuthRequestResponse>> response  = _httpClient.send(request,
																                             HttpResponseBodyHandlerUsingMarshaller
																                             				.using(_marshaller)
																                             				.toMarshallTo(KeycloakAuthRequestResponse.class));*/

            //get response code
            int code = rawResponse.statusCode();
            String rawResponseAsString  = rawResponse.body();
            //return body if code is 2xx
            if (code >= 200 && code < 300) {
	           KeycloakAuthRequestResponse kcResponse = _marshaller.forReading()
	            													.fromJson(rawResponseAsString, KeycloakAuthRequestResponse.class);	           
               return new KeycloakUserAuthRequestResult(realm,
            		                                    kcResponse);
            }
             String error = rawResponse.toString();
			 return new KeycloakUserAuthRequestResult(realm,
                                                     error);
            
	    } catch (final NotAuthorizedException e) {	     
	    	e.printStackTrace();
	        return _handleAuthError(exchangeRequest.getRealm(), clientIdAsString,"invalid_credentials", e);
	    } catch (final Throwable e) {
	    	e.printStackTrace();
	    	return _handleAuthError(exchangeRequest.getRealm(), clientIdAsString,"Unexpected error", e);
	    }		
	}
	
	
	public KeycloakUserAuthRequestResult _refresh(final KeycloakUserRefreshTokenRequest refreshRequest) {	
	    Url serverUrl = _keycloakAdminClientConfig.getUrl();
	    KeycloakRealm realm = refreshRequest.getRealm();
	    
	    // [1] Client Identity [ Application ]
	    String clientIdAsString  =    _keycloakAdminClientConfig.getUser().getClientId().asString();
	    String clientSecretAsString = clientSecretAsString = _keycloakAdminClientConfig.getUser().getClientSecret().asString();

	    log.warn("[token refresh] realm='{}' clientId='{}'", realm.asString(), clientIdAsString);
	    try {
	    	// [2] Prepare the parameters for refresh_token grant
	        Map<String, String> params = new HashMap<>();
	        params.put(KeycloakAuthConstants.GRANT_TYPE_PARAM,    KeycloakAuthConstants.REFRESH_TOKEN_PARAM);
	        params.put(KeycloakAuthConstants.CLIENT_ID_PARAM,     clientIdAsString);
	        params.put(KeycloakAuthConstants.CLIENT_SECRET_PARAM, clientSecretAsString);
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
	            return new KeycloakUserAuthRequestResult(realm, kcResponse);
	        }
	        String error = response.toString();
	        return new KeycloakUserAuthRequestResult(realm, error);

	    } catch (final NotAuthorizedException e) {
	        return _handleAuthError(refreshRequest.getRealm(), clientIdAsString, "invalid_credentials", e);
	    } catch (final Throwable e) {
	    	return _handleAuthError(refreshRequest.getRealm(), clientIdAsString, "Unexpected error", e);
	    }
	}
}
