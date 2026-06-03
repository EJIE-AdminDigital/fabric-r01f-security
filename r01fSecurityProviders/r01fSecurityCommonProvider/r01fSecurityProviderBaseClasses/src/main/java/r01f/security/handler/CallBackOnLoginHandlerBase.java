package r01f.security.handler;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import r01f.internal.R01FAppCodes;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.contact.EMail;


@Slf4j
public abstract class CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_PROVIDER = "provider";
	public static final String JWT_CUSTOM_CLAIM_FOR_LOGIN_ID = "loginId";
	public static final String JWT_CUSTOM_CLAIM_FOR_USER_OID = "userOid";
	public static final String JWT_CUSTOM_CLAIM_FOR_EMAIL = "email";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final SecurityProviderSecretConfig _securityProviderSecretConfig ;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CallBackOnLoginHandlerBase(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected JWT _createJWT(final String key,
 							 final SecurityProviderID securityProviderId,
 							 final LoginID loginId,
 							 final  Map<String,String> otherFieldsAsClaims) {
 		return _createJWT(key, 
 						  securityProviderId, 
 						  loginId,
 						  null, 			// no email
 						  otherFieldsAsClaims);
 	}
 	@SuppressWarnings("static-method")
	protected JWT _createJWT(final String key,
 							 final SecurityProviderID securityProviderId,final LoginID loginId,final EMail email,
 							 final Map<String,String> otherFieldsAsClaims) {
 	 	byte keyBytes[] = key.getBytes();
 	 	JWT jwt = JWTBuilder.createJWToken()
	 	 			  			.forIssuer(R01FAppCodes.R01_APP_CODE)
	 	 			  			.toAudience("r01.login")
	 	 			  			.withExpirationSeconds(10L)
	 	 			  			.noId()
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_PROVIDER,securityProviderId.asString())
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_LOGIN_ID,loginId.asString())
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_EMAIL,email != null ? email.asString() : null)
	 	 			  			.usingCustomClaims(otherFieldsAsClaims)
	 	 			  			.withKey(keyBytes)
 	 			  			.buildWithOptimalAlgorithm();
		log.info("JWT after login callback {}",
				 jwt.asString());
 	 	return jwt;
 	}
}
