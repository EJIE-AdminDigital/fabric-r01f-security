package r01f.securitycontext.oauth.utils;

import java.util.concurrent.ConcurrentHashMap;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * R01FOAuthTokenCacheUtils
 * -----------------------------------------------------------------------------
 * Thread-safe in-memory cache for OAuth2 tokens.
 *
 * Responsibilities:
 *  - Store and retrieve cached tokens by key.
 *  - Provide thread-safe access to the token cache.
 *  - Support cache clearing for testing or maintenance.
 *
 * Design notes:
 *  - This class is stateless and contains only static methods.
 *  - Uses ConcurrentHashMap for thread-safe operations.
 *
 * Since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FOAuthTokenCacheUtils {

/////////////////////////////////////////////////////////////////////////////////////////
//  STATE
/////////////////////////////////////////////////////////////////////////////////////////
    /** Thread-safe map holding tokens by cache key. */
    private static final ConcurrentHashMap<String, CachedToken> _map = new ConcurrentHashMap<>();

/////////////////////////////////////////////////////////////////////////////////////////
//    METHODS
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Retrieves a token from the cache by key.
     *
     * @param key Non-null cache key.
     * @return The corresponding CachedToken, or null if not present.
     */
    public static CachedToken get(final String key) {
        return _map.get(key);
    }
    /**
     * Stores a token in the cache.
     *
     * @param key       Non-null cache key.
     * @param token     Non-null token string to store.
     * @param expiresAt Expiration timestamp in milliseconds.
     */
    public static void put(final String key, final String token, final long expiresAt) {
        _map.put(key, new CachedToken(token, expiresAt));
    }
    /**
     * Clears all cached tokens.
     * Primarily used for testing or maintenance purposes.
     */
    public static void clear() {
        _map.clear();
    }
/////////////////////////////////////////////////////////////////////////////////////////
//    INNER CLASS
/////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Represents a cached OAuth2 token with expiration information.
     */
    public static class CachedToken {
        public final String token;
        public final long expiresAt;

        CachedToken(final String token, final long expiresAt) {
            this.token = token;
            this.expiresAt = expiresAt;
        }
        /**
         * Checks if the token is expired considering a leeway period.
         *
         * @param leewaySeconds Grace period in seconds before actual expiration.
         * @return true if the token should be considered expired.
         */
        public boolean isExpired(final int leewaySeconds) {
            return System.currentTimeMillis() + (leewaySeconds * 1000L) >= expiresAt;
        }
    }
}
