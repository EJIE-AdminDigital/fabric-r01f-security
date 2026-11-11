package r01f.securitycontext.apikey.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FApiKeyConstants {

    public static final String K_HEADER_NAME = "header_name";
    public static final String K_API_KEY     = "api_key";
    public static final String DEFAULT_HEADER_NAME = "X-Api-Key";
}
