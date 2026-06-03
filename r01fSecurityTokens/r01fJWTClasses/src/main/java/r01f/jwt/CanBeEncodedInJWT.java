package r01f.jwt;

import r01f.securitycontext.SecurityContext;

/**
 * Represents a JWT Convertible SecurityContext
 * For example: 
 * <pre class='brush:java'>
 *		JWT jwt = JWTBuilder.createJWToken()
 *								.forIssuer(_appCode)
 *								.toAudience("r01.apikey")
 *								.withExpirationDays(30)
 *								.noId()
 *								.usingCustomClaim("loginId",this.getLoginId())
 *								.usingCustomClaim("version",this.getApiKeyVersion()))
 *								.withPrivateKey(this.getPrivateKey())
 *								.build();
 * </pre>
 */
public interface CanBeEncodedInJWT
         extends SecurityContext {
	public JWT asJWT();
}
