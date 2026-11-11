package r01f.securitycontext.oauth.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * R01FOAuthConstants
 * -----------------------------------------------------------------------------
 * OAuth2 parameter keys and default values for the R01F security module.
 *
 * Responsibilities:
 *  - Define standard parameter names for OAuth2 operations.
 *  - Provide default values for common configuration options.
 *  - Ensure consistency across OAuth2 implementations.
 *
 * Design notes:
 *  - This class contains only static constants.
 *  - Parameter names follow OAuth2 RFC standards where applicable.
 *
 * Since: 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FOAuthConstants {

/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS (parameter and credential keys)
/////////////////////////////////////////////////////////////////////////////////////////
    public static final String K_TOKEN_URL        = "token_url";       // alias: auth_url
    public static final String K_AUTH_URL         = "auth_url";
    public static final String K_CLIENT_ID        = "client_id";
    public static final String K_CLIENT_SECRET    = "client_secret";
    public static final String K_CACHE_KEY        = "cache_key";
    public static final String K_LEEWAY_SECONDS   = "leeway_seconds";
    public static final String K_SOURCE_ID        = "source_id";       // optional traceability
    public static final String K_TOKEN                    = "token";
    public static final String K_INTROSPECTION_URL        = "introspection_url";
    public static final String K_INTROSPECTION_CLIENT_ID  = "introspection_client_id";
    public static final String K_INTROSPECTION_CLIENT_SEC = "introspection_client_secret";
    public static final int DEFAULT_LEEWAY_SECONDS = 60;

}


