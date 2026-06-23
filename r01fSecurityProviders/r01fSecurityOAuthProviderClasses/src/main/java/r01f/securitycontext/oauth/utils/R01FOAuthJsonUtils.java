package r01f.securitycontext.oauth.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * R01FOAuthJsonUtils
 * -----------------------------------------------------------------------------
 * Minimal JSON parser for OAuth2 responses.
 *
 * Responsibilities:
 *  - Extract access_token from OAuth2 token responses
 *  - Extract expires_in from OAuth2 token responses
 *  - Check active status in RFC 7662 introspection responses
 *
 * Design notes:
 *  - Lightweight string-based JSON parsing (no external dependencies)
 *  - Handles malformed JSON gracefully with defaults
 *  - Optimized for OAuth2 response formats
 *
 * Since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FOAuthJsonUtils {

    /**
     * Checks if JSON contains "active": true for token introspection.
     *
     * @param json JSON response from introspection endpoint
     * @return true if token is marked as active
     */

    public static boolean containsActiveTrue(final String json) {
        if (json == null) return false;
        final String normalized = json.replaceAll("\\s+", "");
        return normalized.contains("\"active\":true");
    }

    /**
     * Extracts access_token value from OAuth2 token response.
     *
     * @param json JSON response from token endpoint
     * @return Access token string, or null if not found
     */

    public static String extractAccessToken(final String json) {
        return extractJsonString(json, "access_token");
    }

    /**
     * Extracts expires_in value from OAuth2 token response.
     *
     * @param json JSON response from token endpoint
     * @return Expiration time in seconds, defaults to 3600 if not found
     */

    public static long extractExpiresIn(final String json) {
        final String expStr = extractJsonNumber(json, "expires_in");
        if (expStr == null) return 3600;
        try {
            return Long.parseLong(expStr);
        } catch (NumberFormatException e) {
            return 3600;
        }
    }

    private static String extractJsonString(final String json, final String key) {
        if (json == null) return null;
        final int start = json.indexOf("\"" + key + "\"");
        if (start < 0) return null;
        final int colon = json.indexOf(":", start);
        final int q1 = json.indexOf("\"", colon + 1);
        final int q2 = json.indexOf("\"", q1 + 1);
        if (q1 < 0 || q2 < 0) return null;
        return json.substring(q1 + 1, q2);
    }

    private static String extractJsonNumber(final String json, final String key) {
        if (json == null) return null;
        final int start = json.indexOf("\"" + key + "\"");
        if (start < 0) return null;
        final int colon = json.indexOf(":", start);
        if (colon < 0) return null;

        int i = colon + 1;
        while (i < json.length() && Character.isWhitespace(json.charAt(i))) i++;

        int j = i;
        while (j < json.length() && Character.isDigit(json.charAt(j))) j++;

        if (j == i) return null;
        return json.substring(i, j);
    }
}
