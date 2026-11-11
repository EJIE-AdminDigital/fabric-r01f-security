package r01f.securitycontext.apikey;

import java.util.HashMap;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

/**
 * R01FApiKeySecurityContextProvider
 * -----------------------------------------------------------------------------
 * API Key security context provider for r01f security context.
 *
 * Responsibilities:
 *  - Build HTTP headers map with the configured API key header
 *  - Validate incoming requests contain a valid API key header
 *
 * Design notes:
 *  - Stateless: no caching needed (unlike OAuth token flow)
 *  - Header name is configurable (not hardcoded to X-Api-Key)
 *
 * Since: 1.0
 */
@Slf4j
public class R01FApiKeySecurityContextProvider {

//////////////////////////////////////////////////////////////////////////////
// HEADER GENERATION
//////////////////////////////////////////////////////////////////////////////
    /**
     * Builds HTTP headers map with the API key header.
     *
     * @param headerName  Name of the HTTP header (e.g. "X-Api-Key")
     * @param apiKey      API key value
     * @return Map containing the configured header with the API key value
     */
    public Map<String,String> generateHeaders(final String headerName,
                                              final String apiKey) {
        if (headerName == null || headerName.isBlank()) throw new IllegalArgumentException("headerName is required");
        if (apiKey == null || apiKey.isBlank()) throw new IllegalArgumentException("apiKey is required");

        log.debug("Building API key header: {}", headerName);

        Map<String,String> headers = new HashMap<>();
        headers.put(headerName, apiKey);
        return headers;
    }

//////////////////////////////////////////////////////////////////////////////
// TOKEN VALIDATION
//////////////////////////////////////////////////////////////////////////////
    /**
     * Validates that the incoming header value matches the expected API key.
     *
     * @param incomingValue  Value received in the request header
     * @param expectedApiKey Expected API key value
     * @return true if values match, false otherwise
     */
    public boolean validateApiKey(final String incomingValue,
                                  final String expectedApiKey) {
        if (expectedApiKey == null || expectedApiKey.isBlank()) {
            log.warn("Expected API key is not configured");
            return false;
        }
        return expectedApiKey.equals(incomingValue);
    }
}
