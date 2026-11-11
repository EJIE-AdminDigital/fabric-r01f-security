package r01f.securitycontext.oauth;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import r01f.oids.SecurityIDs.Password;
import r01f.securitycontext.oauth.utils.R01FOAuthConstants;
import r01f.securitycontext.oauth.utils.R01FOAuthHttpUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthJsonUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthTokenCacheUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthTokenCacheUtils.CachedToken;
import r01f.types.url.Url;

/**
 * R01FOAuthSecurityContextProvider
 * -----------------------------------------------------------------------------
 * OAuth2 client_credentials provider for r01f security context.
 *
 * Responsibilities:
 *  - Generate OAuth2 access tokens using client_credentials flow
 *  - Cache tokens with configurable leeway to prevent expiration issues
 *  - Validate tokens via RFC 7662 introspection endpoint
 *  - Provide thread-safe token operations
 *
 * Design notes:
 *  - Uses in-memory cache for token storage
 *  - Supports configurable leeway seconds for token expiration
 *  - Client credentials are sent in the request body (client_secret_post)
 *  - Returns Map<String,String> for easy integration with HTTP headers
 *
 * Since: 1.0
 */
@Slf4j
public class R01FOAuthAccesTokenGenerator {

//////////////////////////////////////////////////////////////////////////////
// TOKEN GENERATION
//////////////////////////////////////////////////////////////////////////////
	@Deprecated
	public Map<String, String> generateToken(final String tokenUrl, 
								             final String clientId, 
								             final String clientSecret,
								             final Integer leewaySeconds) {
		return generateToken(Url.from(tokenUrl),
				             clientId,
				             Password.from(clientSecret),
				             leewaySeconds);
		   
	 }
	
    /**
     * Generates OAuth2 access token using client_credentials flow.    
     * @param tokenUrl OAuth2 token endpoint URL
     * @param clientId OAuth2 client identifier
     * @param clientSecret OAuth2 client secret
     * @param leewaySeconds Grace period before token expiration (null = default 60s)
     * @return Map containing Authorization header with Bearer token
     * @throws IllegalStateException if token generation fails
     */
    public Map<String, String> generateToken(final Url      tokenUrl, 
                                             final String   clientId, 
                                             final Password clientSecret,
                                             final Integer   leewaySeconds) {
        
        String cacheKey = tokenUrl + "|" + clientId;
        int leeway = leewaySeconds != null ? leewaySeconds
        		                  : R01FOAuthConstants.DEFAULT_LEEWAY_SECONDS;

        CachedToken cached = R01FOAuthTokenCacheUtils.get(cacheKey);
        if (cached != null && !cached.isExpired(leeway)) {
            log.debug("Returning cached token for key: {}", cacheKey);
            return _buildAuthHeaders(cached.token);
        }

        String response = _fetchNewToken(tokenUrl.asString(),
        		                         clientId, clientSecret.asString());
        String token = R01FOAuthJsonUtils.extractAccessToken(response);
        long expiresAt = System.currentTimeMillis() + (R01FOAuthJsonUtils.extractExpiresIn(response) * 1000);

        R01FOAuthTokenCacheUtils.put(cacheKey, token, expiresAt);
        
        return _buildAuthHeaders(token);
    }
//////////////////////////////////////////////////////////////////////////////
// 	TOKEN VALIDATION
//////////////////////////////////////////////////////////////////////////////
    /**
     * Validates access token using RFC 7662 introspection.
     *
     * @param token Access token to validate
     * @param introspectionUrl OAuth2 introspection endpoint URL
     * @param clientId Client ID for introspection authentication
     * @param clientSecret Client secret for introspection authentication
     * @return true if token is active, false otherwise
     */
    public boolean validateToken(final String token,
                                 final String introspectionUrl,
                                 final String clientId,
                                 final String clientSecret) {
        try {
            String body = String.join("&",
                                      R01FOAuthHttpUtils.form("token", token),
                                      _clientCredentialsForm(clientId, clientSecret));

            HttpResponse<String> response = _postForm(introspectionUrl, body);

            return R01FOAuthJsonUtils.containsActiveTrue(response.body());

        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }
//////////////////////////////////////////////////////////////////////////////
// 	PRIVATE METHODS
//////////////////////////////////////////////////////////////////////////////
    /**
     * Fetches new OAuth2 token from authorization server.
     */
    private String _fetchNewToken(final String tokenUrl,
    							  final String clientId, final String clientSecret) {
        try {
            String body = String.join("&",
                                      R01FOAuthHttpUtils.form("grant_type", "client_credentials"),
                                      _clientCredentialsForm(clientId, clientSecret));

            HttpResponse<String> response = _postForm(tokenUrl, body);

            if (response.statusCode() / 100 != 2) {
                throw new IllegalStateException("HTTP " + response.statusCode() + ": " + response.body());
            }

            return response.body();

        } catch (IOException e) {
            throw new IllegalStateException("Failed to fetch OAuth token", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Failed to fetch OAuth token", e);
        }
    }
    /**
     * Builds the client_id / client_secret form fragment (client_secret_post, RFC 6749 §2.3.1).
     */
    private static String _clientCredentialsForm(final String clientId, final String clientSecret) {
        return String.join("&",
                           R01FOAuthHttpUtils.form("client_id", clientId),
                           R01FOAuthHttpUtils.form("client_secret", clientSecret));
    }
    /**
     * Sends an application/x-www-form-urlencoded POST request.
     */
    private static HttpResponse<String> _postForm(final String url, final String body) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

        return HttpClient.newHttpClient()
                         .send(request, HttpResponse.BodyHandlers.ofString());
    }
    /**
     * Builds HTTP headers map with Authorization Bearer token.
     */
    private Map<String, String> _buildAuthHeaders(final String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
