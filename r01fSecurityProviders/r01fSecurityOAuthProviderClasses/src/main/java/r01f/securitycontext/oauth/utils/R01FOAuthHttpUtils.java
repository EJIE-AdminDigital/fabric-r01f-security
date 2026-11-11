package r01f.securitycontext.oauth.utils;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.types.StringBase64;

/**
 * R01FOAuthHttpUtils
 * -----------------------------------------------------------------------------
 * HTTP utility methods for OAuth2 support in r01f fabric.
 *
 * Responsibilities:
 *  - Generate HTTP Basic authentication headers
 *  - Create URL-encoded form data for OAuth2 requests
 *  - Provide safe string truncation for logging
 *
 * Design notes:
 *  - All methods are static and stateless
 *  - Uses r01f StringBase64 for encoding
 *  - Follows OAuth2 RFC standards for authentication
 *
 * Since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FOAuthHttpUtils {
    /**
     * Creates HTTP Basic authentication header value.
     *
     * @param clientId OAuth2 client identifier
     * @param clientSecret OAuth2 client secret
     * @return Basic authentication header value (e.g., "Basic base64...")
     */
    public static String basicAuth(final String clientId, final String clientSecret) {
        final String creds = clientId + ":" + clientSecret;
        final String b64 = StringBase64.encode(creds).asString();
        return "Basic " + b64;
    }
    /**
     * Creates URL-encoded form parameter.
     *
     * @param key Parameter name
     * @param value Parameter value
     * @return URL-encoded key=value pair
     */
    public static String form(final String key,final String value) {
        return url(key) + "=" + url(value);
    }
    /**
     * URL-encodes a string using UTF-8.
     *
     * @param s String to encode
     * @return URL-encoded string
     */
    public static String url(final String s) {
        return URLEncoder.encode(s,StandardCharsets.UTF_8);
    }
    /**
     * Safely truncates string for logging purposes.
     *
     * @param s Input string (may be null)
     * @return Truncated string (max 300 chars) or empty string if null
     */
    public static String safeTrim(final String s) {
        if (s == null) return "";
        return s.length() > 300 ? s.substring(0, 300) + "..." : s;
    }
}
