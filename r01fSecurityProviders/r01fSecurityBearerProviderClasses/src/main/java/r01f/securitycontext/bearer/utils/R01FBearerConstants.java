package r01f.securitycontext.bearer.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class R01FBearerConstants {

    public static final String K_TOKEN       = "token";
    public static final String HEADER_NAME   = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
}
