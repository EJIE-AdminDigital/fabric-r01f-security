package r01f.security.authn.handler;

import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import r01f.internal.R01FAppCodes;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.contact.EMail;


@Slf4j
public abstract class CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_IDENTITY_PROVIDER = "provider";
	public static final String JWT_CUSTOM_CLAIM_FOR_AUTHN_METHOD = "authnMethod";
//	public static final String JWT_CUSTOM_CLAIM_FOR_LOGIN_ID = "loginId";
	public static final String JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_OID = "principalOid";
	public static final String JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID = "principalId";
	public static final String JWT_CUSTOM_CLAIM_FOR_EMAIL = "email";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	protected final AuthenticationProviderSecretConfig _securityProviderSecretConfig ;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CallBackOnLoginHandlerBase(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected JWT _createJWT(final String key,
 							 final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
 							 final SecurityPrincipalID principalId,
 							 final Map<String,String> otherFieldsAsClaims) {
 		return _createJWT(key, 
 						  identityProvider,authnMethod,
 						  principalId,
 						  null, 			// no email
 						  otherFieldsAsClaims);
 	}
 	@SuppressWarnings("static-method")
	protected JWT _createJWT(final String key,
 							 final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
 							 final SecurityPrincipalID principalId,
 							 final EMail email,
 							 final Map<String,String> otherFieldsAsClaims) {
 	 	byte keyBytes[] = key.getBytes();
 	 	JWT jwt = JWTBuilder.createJWToken()
	 	 			  			.forIssuer(R01FAppCodes.R01_APP_CODE)
	 	 			  			.toAudience("r01.login")
	 	 			  			.withExpirationSeconds(10L)
	 	 			  			.noId()
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_IDENTITY_PROVIDER,identityProvider.name())
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_AUTHN_METHOD,authnMethod.name())
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID,principalId.asString())
	 	 			  			.usingCustomClaim(JWT_CUSTOM_CLAIM_FOR_EMAIL,email != null ? email.asString() : null)
	 	 			  			.usingCustomClaims(otherFieldsAsClaims)
	 	 			  			.withKey(keyBytes)
 	 			  			.buildWithOptimalAlgorithm();
		log.info("JWT after login callback {}",
				 jwt.asString());
 	 	return jwt;
 	}
}
