package r01f.securitycontext.bearer;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.securitycontext.bearer.utils.R01FBearerConstants;

/**
 * R01FBearerSecurityContextProvider
 * -----------------------------------------------------------------------------
 * Bearer token security context provider for r01f security context.
 *
 * Responsibilities:
 *  - Build HTTP headers map with Authorization: Bearer <token>
 *  - Validate incoming requests contain a valid Bearer token
 *
 * Design notes:
 *  - Stateless: token is provided externally (no OAuth flow, no caching)
 *  - Use R01FOAuthSecurityContextProvider when token must be obtained via OAuth2
 *
 * Since: 1.0
 */
@Slf4j
public class R01FBearerSecurityContextProvider {

//////////////////////////////////////////////////////////////////////////////
// HEADER GENERATION
//////////////////////////////////////////////////////////////////////////////
    /**
     * Builds HTTP headers map with Authorization: Bearer header.
     *
     * @param token  Bearer token value
     * @return Map containing the Authorization header
     */
    public Map<String,String> generateHeaders(final SecurityToken token) {
        if (token == null || token.toString().isBlank()) throw new IllegalArgumentException("token is required");

        log.debug("Building Bearer Authorization header");

        Map<String,String> headers = new HashMap<>();
        headers.put(R01FBearerConstants.HEADER_NAME,
                    R01FBearerConstants.BEARER_PREFIX + token);
        return headers;
    }

//////////////////////////////////////////////////////////////////////////////
// TOKEN VALIDATION
//////////////////////////////////////////////////////////////////////////////
    /**
     * Validates that the incoming Authorization header value matches the expected token.
     * Accepts both raw token and "Bearer <token>" formats in incomingValue.
     *
     * @param incomingValue  Value received in the Authorization header
     * @param expectedToken  Expected Bearer token value
     * @return true if tokens match, false otherwise
     */
    public boolean validateToken(final String incomingValue,
                                 final SecurityToken expectedToken) {
        if (expectedToken == null || expectedToken.toString().isBlank()) {
            log.warn("Expected Bearer token is not configured");
            return false;
        }
        if (incomingValue == null) return false;

        String normalized = incomingValue.startsWith(R01FBearerConstants.BEARER_PREFIX)
                                ? incomingValue.substring(R01FBearerConstants.BEARER_PREFIX.length())
                                : incomingValue;
        return expectedToken.equals(normalized);
    }
}
