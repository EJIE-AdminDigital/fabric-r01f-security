package r01f.securitycontext.oauth;

import lombok.extern.slf4j.Slf4j;

import r01f.securitycontext.oauth.utils.R01FOAuthHttpUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthJsonUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthTokenCacheUtils;
import r01f.securitycontext.oauth.utils.R01FOAuthTokenCacheUtils.CachedToken;
import r01f.securitycontext.oauth.utils.R01FOAuthConstants;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

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
 *  - Returns Map<String,String> for easy integration with HTTP headers
 *
 * Since: 1.0
 */
@Slf4j
public class R01FOAuthSecurityContextProvider {

//////////////////////////////////////////////////////////////////////////////
//    TOKEN GENERATION
//////////////////////////////////////////////////////////////////////////////

    /**
     * Generates OAuth2 access token using client_credentials flow.
     *
     * @param tokenUrl OAuth2 token endpoint URL
     * @param clientId OAuth2 client identifier
     * @param clientSecret OAuth2 client secret
     * @param leewaySeconds Grace period before token expiration (null = default 60s)
     * @return Map containing Authorization header with Bearer token
     * @throws IllegalStateException if token generation fails
     */

    public Map<String, String> generateToken(String tokenUrl, 
                                              String clientId, 
                                              String clientSecret,
                                              Integer leewaySeconds) {
        
        String cacheKey = tokenUrl + "|" + clientId;
        int leeway = leewaySeconds != null ? leewaySeconds : R01FOAuthConstants.DEFAULT_LEEWAY_SECONDS;

        CachedToken cached = R01FOAuthTokenCacheUtils.get(cacheKey);
        if (cached != null && !cached.isExpired(leeway)) {
            log.debug("Returning cached token for key: {}", cacheKey);
            return _buildAuthHeaders(cached.token);
        }

        String response = _fetchNewToken(tokenUrl, clientId, clientSecret);
        String token = R01FOAuthJsonUtils.extractAccessToken(response);
        long expiresAt = System.currentTimeMillis() + (R01FOAuthJsonUtils.extractExpiresIn(response) * 1000);

        R01FOAuthTokenCacheUtils.put(cacheKey, token, expiresAt);
        
        return _buildAuthHeaders(token);
    }

//////////////////////////////////////////////////////////////////////////////
//    TOKEN VALIDATION
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

    public boolean validateToken(String token,
                                 String introspectionUrl,
                                 String clientId,
                                 String clientSecret) {

        try {
            String body = R01FOAuthHttpUtils.form("token", token);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(introspectionUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", R01FOAuthHttpUtils.basicAuth(clientId, clientSecret))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            return R01FOAuthJsonUtils.containsActiveTrue(response.body());

        } catch (Exception e) {
            log.error("Token validation failed", e);
            return false;
        }
    }


//////////////////////////////////////////////////////////////////////////////
//    PRIVATE METHODS
//////////////////////////////////////////////////////////////////////////////

    /**
     * Fetches new OAuth2 token from authorization server.
     */

    private String _fetchNewToken(String tokenUrl, String clientId, String clientSecret) {
        try {
            String body = "grant_type=client_credentials";

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(tokenUrl))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .header("Authorization", R01FOAuthHttpUtils.basicAuth(clientId, clientSecret))
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() / 100 != 2) {
                throw new IllegalStateException("HTTP " + response.statusCode() + ": " + response.body());
            }

            return response.body();

        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException("Failed to fetch OAuth token", e);
        }
    }

    /**
     * Builds HTTP headers map with Authorization Bearer token.
     */

    private Map<String, String> _buildAuthHeaders(String token) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        return headers;
    }
}
