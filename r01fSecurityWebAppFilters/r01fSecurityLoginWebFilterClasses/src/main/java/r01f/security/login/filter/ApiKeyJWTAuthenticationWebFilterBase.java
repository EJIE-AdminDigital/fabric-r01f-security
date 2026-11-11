package r01f.security.login.filter;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.stream.Collectors;

import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.ContainsJWTBearer;
import r01f.jwt.JWT;
import r01f.jwt.JWTBearer;
import r01f.jwt.JWTDecoded;
import r01f.jwt.JWTDecoded.CustomClaim;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerificationResultInvalidCause;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.data.apikey.ApiKey;
import r01f.model.security.login.data.apikey.ApiKeyPair;
import r01f.model.security.login.data.apikey.LoginDataForApiKey;
import r01f.model.security.login.entry.LoginEntry;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.rest.RESTFilter;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.login.filter.ApiKeyJWTAuthenticationStepResult.ApiKeyJWTAuthenticationStepResultError;
import r01f.security.login.filter.ApiKeyJWTAuthenticationStepResult.ApiKeyJWTAuthenticationStepResultOK;
import r01f.types.AppVersion;
import r01f.types.contact.EMail;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;


@Slf4j
@RequiredArgsConstructor
public abstract class ApiKeyJWTAuthenticationWebFilterBase<P extends IsSecurityPrincipal>
		   implements RESTFilter {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOG
/////////////////////////////////////////////////////////////////////////////////////////
	boolean logEnabled = log.isTraceEnabled() || log.isDebugEnabled();
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final boolean _jwtVerificationEnabled;
	
	protected final IdentityProvider _identityProvider;
	
	protected final SecurityToken _audience;
	protected final Provider<SecurityContext> _sysSecurityContextProvider;
	protected final CRUDServicesForLogin _loginCrud;
	protected final CRUDServicesForLoginEntry _loginEntryCrud;
	protected final CRUDServicesForPrincipal<P> _principalCrud;

/////////////////////////////////////////////////////////////////////////////////////////
// 	GET THE JWTBearer WIHOUT knowing the bearer carrier impl
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * The sub-type must implement this method and return the JWTBearer
	 * that comes at an HTTP Authorization Header
	 * @return
	 */
	protected static JWTBearer _getApiKeyJWTBearer(final ContainsJWTBearer containsJwtBearer) {
		return containsJwtBearer.getJWTBearer();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// FILTER STEPS :
//		[STEP 1 ] Parse JWT
//		[STEP 2 ] Load ApiKey(login) and User [ must be done in one step depending on the data ]
//	   	[STEP 3 ] Verify JWT Signature.
//   	[STEP 4 ] Create a Login Entry
/////////////////////////////////////////////////////////////////////////////////////////
	protected void getLoginSession(final JWTBearer jwtBearer,
								   final ApiKeyJWTAuthenticationSubscriber<P> subscriber) {

		SecurityContext securityContext = _sysSecurityContextProvider.get();
		// [1] - Parse JWT ( checks if not null, if version exists, if payload exists, etc...
		ApiKeyJWTAuthenticationStepResult<P> parseJWTStep = _parseJWTStep(securityContext,
																	      jwtBearer);
		if (parseJWTStep.hasFailed() ) {
			// [NUL JWT ] Check if because developing mode or not
			if (parseJWTStep.asError().getJwtDecoded() == null ) {
				if (_jwtVerificationEnabled) {
					subscriber.onError(ApiKeyJWTAuthenticationError.NULL_JWT_BEARER,
									   "The JWTBearer which should be in the HTTP 'Authorization' header has not been received ");
					return;
				}
				log.warn("[WARNING !!!] The JWTBearer which should be in the HTTP 'Authorization' header has not been received \n "
						+ " ...BUT JWT Verification is NOT ENABLED..so the request will be preformed !!");
				return;  // Null JWT but verification disabled for developing purposes.

			// [NOT NULL JWT]
			}
			subscriber.onError(ApiKeyJWTAuthenticationError.NOT_VALID_JWT,
							   parseJWTStep.asError().getDetail());
			return;
		}
		JWTDecoded decodedJWT =  parseJWTStep.asOK()
											   .getJwtDecoded();
		CustomClaim principalIdClaim =  decodedJWT.getPayload()
											      .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID);
		SecurityPrincipalID principalId = principalIdClaim != null ? SecurityPrincipalID.forId(principalIdClaim.getValue())
																   : null;

	   // [2] [principal] and ApiKeyLogin session:
		/*	IMPORTANT !!! :  For a jwt based  APIKeyLogin Key there are two approach :
				  1. The JWT contains a LoginID claim :
					   From the JWT get first the ApiKeyLogin via the LoginID, and after the [principal].
				  2. The JWT contains any [principal] data ( usually the mail ):
					   From the JWT get first the [principal] via any [principal] data (by the default the mail...this could be overwitten..) 
					   and after the [principal], and after the ApiKeyLogin
		 */
		P principal = null;
		Login apiKeyLogin = null;

		// ..... Approach [1 ] JWT contains a LoginId.
		if (principalId != null ) {
			ApiKeyJWTAuthenticationStepResult<P> loginSessionByLoginIdOnJWT = _loginSessionByLoginIdOnJWTStep(securityContext,
																											  decodedJWT);
			if (loginSessionByLoginIdOnJWT.hasFailed()) {
				subscriber.onError(loginSessionByLoginIdOnJWT.asError().getError(),loginSessionByLoginIdOnJWT.asError().getDetail());
				return;
			}
			principal = loginSessionByLoginIdOnJWT.asOK().getPrincipal();
			apiKeyLogin = loginSessionByLoginIdOnJWT.asOK().getApiKeyLogin();
		//.... Approach [2] JWT doesn't contai a LoginId.
		} else {
			ApiKeyJWTAuthenticationStepResult<P> loginSessionByUserDatadOnJWT =_loginSessionByUserDataOnJWTStep(securityContext,
																											    decodedJWT);
			if (loginSessionByUserDatadOnJWT.hasFailed()) {
				subscriber.onError(loginSessionByUserDatadOnJWT.asError().getError(),loginSessionByUserDatadOnJWT.asError().getDetail());
				return;
			}
			principal = loginSessionByUserDatadOnJWT.asOK().getPrincipal();
			apiKeyLogin = loginSessionByUserDatadOnJWT.asOK().getApiKeyLogin();
		}
		// [3] - Use the [public key] stored at the [login] table to verify the JWT
		//		 (remember tha the JWT MUST have been signed with the client-owned [private key]
		ApiKeyJWTAuthenticationStepResult<P> jwtVerificationResult = _jwtVerificationStep(securityContext,
																						  jwtBearer.getJWT(),principal,apiKeyLogin);
		if (jwtVerificationResult.hasFailed()) {
			subscriber.onError(jwtVerificationResult.asError().getError(),jwtVerificationResult.asError().getDetail());
			return;
		}
		//[ 4] - Create the Login Entry.
		ApiKeyJWTAuthenticationStepResult<P>  loginEntryCreationStep =  _loginEntry(securityContext,
																				    decodedJWT,principal,apiKeyLogin);

		if (loginEntryCreationStep.hasFailed()) {
			subscriber.onError(loginEntryCreationStep.asError().getError(),loginEntryCreationStep.asError().getDetail());
			return;
		}
		// [99] - Return the user
		subscriber.onSuccess(principal,apiKeyLogin);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// STEPS :
//	[STEP 1] - Parse JWT
//	[STEP 2] - Load ApiKey and User [ must be done in one step depending on the data ]
//	[STEP 3] - Verify JWT Signature.
//	[STEP 4] - Create a Login Entry
//
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Parses the JWT an checks if payload and some mandatory data like the api version exists-
	 * @param securityContext
	 * @param jwtBearer
	 * @return
	 */
	protected ApiKeyJWTAuthenticationStepResult<P>  _parseJWTStep(final SecurityContext securityContext,
																  final JWTBearer jwtBearer) {
		//1. Null Bearer
		if (jwtBearer == null) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(null,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NULL_JWT_BEARER,
								  "Null JWT Bearer");
		}
		// [2] - Decode & parse the JWT
		JWT signedJwt = jwtBearer.getJWT();
		JWTDecoded jwtDecoded = JWTParserBuilder.createParser()
												.build()
												.parse(signedJwt);

		if (jwtDecoded.getPayload() == null) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(null,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NULL_JWT_BEARER,
								  "Null JWT Bearer");
		}
		if (jwtDecoded.getPayload().getCustomClaim("version").getValue() == null) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(null,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NOT_VALID_JWT,
								  "Null JWT Bearer");
		}
		// [3] Return spt as OK with the decoded JWT.
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,null,null,null);
	}
	/**
	 * From the JWT get first the ApiKeyLogin via the LoginID, and after the User.
	 * @param securityContext
	 * @param jwtDecoded
	 * @return
	 */
	protected ApiKeyJWTAuthenticationStepResult<P>  _loginSessionByLoginIdOnJWTStep(final SecurityContext securityContext,
																				    final JWTDecoded jwtDecoded) {
		AppVersion version = AppVersion.from(jwtDecoded.getPayload()
									   .getCustomClaim("version")
									   .getValue());

		SecurityPrincipalID principalId = SecurityPrincipalID.forId(jwtDecoded.getPayload()
											  			  	 .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID)
											  			  	 .getValue());
		// [1] - Load the [api key login] using the [loginId] & [apiKeyVersion]
		CRUDResult<Login> apiKeyLoginLoad = _loginCrud.loadBy(securityContext,
														  	  _identityProvider,AuthenticationMethod.API_KEY,principalId);
		if (apiKeyLoginLoad.hasFailed()
		 && apiKeyLoginLoad.asCRUDError()
						   .wasBecauseClientRequestedEntityWasNOTFound()) {

			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NOT_EXISTING_LOGIN,
								 Strings.customized("[api key login] NOT found by [loginId]/[apiKeyVersion]={}/{}",
													 principalId,version));
		} else if (apiKeyLoginLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,null,null,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
								  "Unknown error while loading login: " + apiKeyLoginLoad.asCRUDError().getDetailedMessage());
		}
		Login apiKeyLogin = apiKeyLoginLoad.getOrThrow();
		LoginDataForApiKey apiKeyData = apiKeyLogin.getDataAsForApiKey();

		// [2] - Load the [user]
		CRUDResult<P> principalLoad = _principalCrud.load(securityContext,
													 	  apiKeyLogin.getSecurityPrincipalOid());
		if (principalLoad.hasFailed()
		 && principalLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 apiKeyLogin,null)
							.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
								  Strings.customized("Could NOT found [principal]={} [loginOid]/[apiKeyVersion]={}/{}",
												 	 apiKeyLogin.getSecurityPrincipalOid(),
												 	 apiKeyLogin.getOid(),principalId,version));
		} else if (principalLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 apiKeyLogin,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
								  "Unknown error while loading user: " + principalLoad.asCRUDError().getDetailedMessage());
		}
		P principal = principalLoad.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,
														 principal,
														 apiKeyLogin,null);
	}
	/**
	 * From the JWT get first the User via any user data (by the default the mail...this could be overwitten..) and after the User, and after the ApiKeyLogin
	 * @param securityContext
	 * @param jwtDecoded
	 * @return
	 */
	protected ApiKeyJWTAuthenticationStepResult<P> _loginSessionByUserDataOnJWTStep(final SecurityContext securityContext,
																					final JWTDecoded jwtDecoded) {

		EMail email = EMail.create(jwtDecoded.getPayload()
											 .getCustomClaim("email")
											 .getValue());
		// [1] - Get the mail
		if (email == null)  {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 null,null)
								.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
									  Strings.customized("For user data based apikey login the JWT must contain any user data claim, by default the mail"));
		}

		// [2] - Load the [principal]
		CRUDResult<P> principalLoad = _principalCrud.loadBy(securityContext,
												  			_identityProvider,SecurityPrincipalID.fromLoginId(LoginID.fromEMail(email)));
		if (principalLoad.hasFailed()
		 && principalLoad.asCRUDError()
					.wasBecauseClientRequestedEntityWasNOTFound()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 null,null)
						.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
							  Strings.customized("Could NOT found [user] for [email]={} ",
											 	 email));
		} else if (principalLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 null,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
								  "Unknown error while loading user: " + principalLoad.asCRUDError().getDetailedMessage());
		}
		P principal = principalLoad.getOrThrow();

		// [3] - Load the [ApiKey]
		CRUDResult<Login> apiKeyLoginLoad = _loginCrud.loadBy(securityContext,
															  _identityProvider,AuthenticationMethod.API_KEY,principal.getOid());

		if (apiKeyLoginLoad.hasFailed()
					&& apiKeyLoginLoad.asCRUDError()
							.wasBecauseClientRequestedEntityWasNOTFound()) {

					return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																		 null,
																		 null,null)
										.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_LOGIN,
										Strings.customized("[api key login] NOT found by [UserOID]/{}",
																		 	principal.getOid()));
		} else if (apiKeyLoginLoad.hasFailed()) {
						return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																			 null,
																			 null,null)
						.with(ApiKeyJWTAuthenticationError.INTERNAL,
							   "Unknown error while loading login " + apiKeyLoginLoad.asCRUDError().getDetailedMessage());
		}

		Login apiKeyLogin = apiKeyLoginLoad.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,
														 principal,
														 apiKeyLogin,null);
	}
	protected ApiKeyJWTAuthenticationStepResult<P>  _jwtVerificationStep(final SecurityContext securityContext,
																		 final JWT signedJwt, 
																		 final P principal,  
																		 final Login apiKeyLogin) {

		JWTDecoded decodedJWT = JWTParserBuilder.createParser()
												.build()
												.parse(signedJwt);

	  // [1 ] get the [public key] (ensure the [private key] is NOT stored) -this time DO NOT tell the subscriber since it's an internal error-
		ApiKey apiKey = apiKeyLogin.getDataAsForApiKey()
								   .getCurrentApiKey();
		ApiKeyPair apiKeyPair = apiKey.getApiKeyPair();
		
		if (apiKeyPair.getPrivateKey() != null) {
			throw new IllegalStateException(Strings.customized("The [api key] oid={} contains a PRIVATE KEY!! the DB MUST only store the PUBLIC key; " +
															   "the PRIVATE part must be keept safe and private by the client!!",
															   apiKeyLogin.getOid()));
		}
		if (apiKeyPair.getPublicKey() == null) {
			throw new IllegalStateException(Strings.customized("The [api key] oid={} DOES NOT contain PUBLIC KEY!!",
															   apiKeyLogin.getOid()));
		}
		byte[] publicKey = StringEncodeUtils.decodeBase64(apiKeyPair.getPublicKey()
																 .getBytes());
		X509EncodedKeySpec x509PublicKey = new X509EncodedKeySpec(publicKey);
		RSAPublicKey rsaPublicKey = null;
		try {
			rsaPublicKey = (RSAPublicKey)KeyFactory.getInstance("RSA")
											   .generatePublic(x509PublicKey);
		} catch (InvalidKeySpecException keyEx) {
			log.error("Invalid [public key]: {}",
					  keyEx.getMessage(),keyEx);

			return new ApiKeyJWTAuthenticationStepResultError<P>(decodedJWT,principal,apiKeyLogin,null)
							.with(ApiKeyJWTAuthenticationError.INVALID_KEY_OR_ALGORITHM,
								  "Invalid [public key]: " + keyEx.getMessage());

		} catch (NoSuchAlgorithmException algEx) {
			log.error("Invalid algorithm (RSA): {}",
					  algEx.getMessage(),algEx);

			return new ApiKeyJWTAuthenticationStepResultError<P>(decodedJWT,principal,apiKeyLogin,null).
						   with(ApiKeyJWTAuthenticationError.INVALID_KEY_OR_ALGORITHM,
							    "Invalid algorithm (RSA): " + algEx.getMessage());
		}

		// b) verify the [public key]
		JWTVerifier verifier = JWTVerifierBuilder.createVerifier()
												  .usingPublicKey(rsaPublicKey)
												  .checkingExpiration(_jwtVerificationEnabled)
												  .forAudience(_audience.asString())
											  .build();

		JWTVerificationResult isJwtVerified = verifier.verify(signedJwt);

		if (isJwtVerified.isValidJWT()) {
			log.warn("VERIFIED JWT for loginId/apiKeyVersion={}/{} (login oid={})",
			apiKeyLogin.getLoginId(),apiKey.getApiKeyVersion(),apiKeyLogin.getOid());
			return new ApiKeyJWTAuthenticationStepResultOK<>(decodedJWT,principal,apiKeyLogin,null);

		}
		JWTVerificationResultAsNotValidToken notValidJWT = isJwtVerified.as(JWTVerificationResultAsNotValidToken.class);
		String errors = notValidJWT.getCause()
								   .stream()
								   .map(JWTVerificationResultInvalidCause::name)
								   .collect(Collectors.joining("\n-"));
		log.warn("NOT VERIFIED JWT for loginId/apiKeyVersion={}/{} (login oid={}):\n{}",
				 apiKeyLogin.getLoginId(),apiKey.getApiKeyVersion(),apiKeyLogin.getOid(),
				 errors);
		return new ApiKeyJWTAuthenticationStepResultError<P>(decodedJWT,principal,apiKeyLogin,null)
					   .with(ApiKeyJWTAuthenticationError.NOT_VERIFIED_JWT,
							 "JWT was NOT verified: " + errors);
	}
	protected ApiKeyJWTAuthenticationStepResult<P>  _loginEntry(final SecurityContext securityContext,
															    final JWTDecoded jwtDecoded,  
															    final P principal,
															    final Login apiKeyLogin) {
		// Register the login
		LoginEntry loginEntry = new LoginEntry();
		loginEntry.setAt(Instant.now());
		loginEntry.setEvent(LoginEntryEvent.LOGIN_OK);
		loginEntry.setLoginId(apiKeyLogin.getLoginId());
		loginEntry.setSecurityPrincipalOid(principal.getOid());

		CRUDResult<LoginEntry> createdEntry = _loginEntryCrud.create(securityContext,
																     loginEntry);
		if (createdEntry.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<P>(jwtDecoded,
																 null,
																 apiKeyLogin,null)
						.with(ApiKeyJWTAuthenticationError.INTERNAL,
							  "Unknown error while creating login entry: " + createdEntry.asCRUDError().getDetailedMessage());
		}
		LoginEntry createdLoginEntry = createdEntry.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,
														 principal,
														 apiKeyLogin,createdLoginEntry);
	}
}
