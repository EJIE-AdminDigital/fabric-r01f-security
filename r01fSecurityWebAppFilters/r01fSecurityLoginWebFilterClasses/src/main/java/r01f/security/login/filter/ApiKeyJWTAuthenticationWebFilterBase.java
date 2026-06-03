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
import r01f.api.interfaces.security.login.apikey.CRUDServicesForApiKeyLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
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
import r01f.model.security.login.apikey.ApiKeyLogin;
import r01f.model.security.login.apikey.ApiKeyPair;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.patterns.Factory;
import r01f.rest.RESTFilter;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.login.filter.ApiKeyJWTAuthenticationStepResult.ApiKeyJWTAuthenticationStepResultError;
import r01f.security.login.filter.ApiKeyJWTAuthenticationStepResult.ApiKeyJWTAuthenticationStepResultOK;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.types.AppVersion;
import r01f.types.contact.EMail;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;


@Slf4j
@RequiredArgsConstructor
public abstract class ApiKeyJWTAuthenticationWebFilterBase<U extends User,L extends ApiKeyLogin,
														   E extends UserLoginEntry>
		   implements RESTFilter {
/////////////////////////////////////////////////////////////////////////////////////////
//	LOG
/////////////////////////////////////////////////////////////////////////////////////////
	boolean logEnabled = log.isTraceEnabled() || log.isDebugEnabled();
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final boolean _jwtVerificationEnabled;
	protected final SecurityToken _audience;
	protected final Provider<SecurityContext> _sysSecurityContextProvider;
	protected final CRUDServicesForApiKeyLogin<L> _apiKeyLoginCrud;
	protected final CRUDServicesForUser<U> _userCrud;
	protected final Factory<E> _userLoginEntryFactory;
	protected final CRUDServicesForUserLoginEntry<E> _userLoginEntryCrud;

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
								   final ApiKeyJWTAuthenticationSubscriber<U,L> subscriber) {

		SecurityContext securityContext = _sysSecurityContextProvider.get();
		// [1] - Parse JWT ( checks if not null, if version exists, if payload exists, etc...
		ApiKeyJWTAuthenticationStepResult<U,L,E> parseJWTStep = _parseJWTStep(securityContext,
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
		CustomClaim loginIdClaim =  decodedJWT.getPayload()
											   .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID);
		LoginID loginId =  loginIdClaim == null? null : LoginID.forId(loginIdClaim.getValue());

	   // [2] User and ApiKeyLogin session:
		/*	IMPORTANT !!! :  For a jwt based  APIKeyLogin Key there are two approach :
				  1. The JWT contains a LoginID claim :
					   From the JWT get first the ApiKeyLogin via the LoginID, and after the User.
				  2. The JWT contains any user data ( usually the mail ):
					   From the JWT get first the User via any user data (by the default the mail...this could be overwitten..) and after the User, and after the ApiKeyLogin
		 */
		U user = null;
		L apiKeyLogin = null;

		// ..... Approach [1 ] JWT contains a LoginId.
		if (loginId != null ) {
			ApiKeyJWTAuthenticationStepResult<U,L,E> loginSessionByLoginIdOnJWT = _loginSessionByLoginIdOnJWTStep(securityContext,
																												  decodedJWT);
			if (loginSessionByLoginIdOnJWT.hasFailed()) {
				subscriber.onError(loginSessionByLoginIdOnJWT.asError().getError(),loginSessionByLoginIdOnJWT.asError().getDetail());
				return;
			}
			user = loginSessionByLoginIdOnJWT.asOK().getUser();
			apiKeyLogin = loginSessionByLoginIdOnJWT.asOK().getApiKeyLogin();
		//.... Approach [2] JWT doesn't contai a LoginId.
		} else {
			ApiKeyJWTAuthenticationStepResult<U,L,E> loginSessionByUserDatadOnJWT =_loginSessionByUserDataOnJWTStep(securityContext,
																													decodedJWT);
			if (loginSessionByUserDatadOnJWT.hasFailed()) {
				subscriber.onError(loginSessionByUserDatadOnJWT.asError().getError(),loginSessionByUserDatadOnJWT.asError().getDetail());
				return;
			}
			user = loginSessionByUserDatadOnJWT.asOK().getUser();
			apiKeyLogin = loginSessionByUserDatadOnJWT.asOK().getApiKeyLogin();
		}
		// [3] - Use the [public key] stored at the [login] table to verify the JWT
		//		 (remember tha the JWT MUST have been signed with the client-owned [private key]
		ApiKeyJWTAuthenticationStepResult<U,L,E> jwtVerificationResult = _jwtVerificationStep(securityContext,
																							  jwtBearer.getJWT(),user,apiKeyLogin);
		if (jwtVerificationResult.hasFailed()) {
			subscriber.onError(jwtVerificationResult.asError().getError(),jwtVerificationResult.asError().getDetail());
			return;
		}
		//[ 4] - Create the Login Entry.
		ApiKeyJWTAuthenticationStepResult<U,L,E>  loginEntryCreationStep =  _loginEntry(securityContext,
																						decodedJWT,user,apiKeyLogin);

		if (loginEntryCreationStep.hasFailed()) {
			subscriber.onError(loginEntryCreationStep.asError().getError(),loginEntryCreationStep.asError().getDetail());
			return;
		}

		// [99] - Return the user
		subscriber.onSuccess(user,apiKeyLogin);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// STEPS :
//	[STEP 1 ] Parse JWT
//	[STEP 2 ] Load ApiKey and User [ must be done in one step depending on the data ]
//	[STEP 3 ] Verify JWT Signature.
//	[STEP 4 ] Create a Login Entry
//
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Parses the JWT an checks if payload and some mandatory data like the api version exists-
	 * @param securityContext
	 * @param jwtBearer
	 * @return
	 */
	protected ApiKeyJWTAuthenticationStepResult<U,L,E>  _parseJWTStep(final SecurityContext securityContext,
																	  final JWTBearer jwtBearer) {
		//1. Null Bearer
		if (jwtBearer == null) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(null,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NULL_JWT_BEARER,
								  "Null JWT Bearer");
		}
		// [2] - Decode & parse the JWT
		JWT signedJwt = jwtBearer.getJWT();
		JWTDecoded jwtDecoded = JWTParserBuilder.createParser()
												.build()
												.parse(signedJwt);

		if (jwtDecoded.getPayload() == null) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(null,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NULL_JWT_BEARER,
								  "Null JWT Bearer");
		}
		if (jwtDecoded.getPayload().getCustomClaim("version").getValue() == null) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(null,null,null,null)
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
	protected ApiKeyJWTAuthenticationStepResult<U,L,E>  _loginSessionByLoginIdOnJWTStep(final SecurityContext securityContext,
																						final JWTDecoded jwtDecoded) {
		AppVersion version = AppVersion.from(jwtDecoded.getPayload()
									   .getCustomClaim("version")
									   .getValue());

		LoginID loginId = LoginID.forId(jwtDecoded.getPayload()
									  			  .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID)
									  			  .getValue());
		// [1] - Load the [api key login] using the [loginId] & [apiKeyVersion]
		CRUDResult<L> apiKeyLoginLoad = _apiKeyLoginCrud.loadBy(securityContext,
																loginId,version);

		if (apiKeyLoginLoad.hasFailed()
		 && apiKeyLoginLoad.asCRUDError()
						   .wasBecauseClientRequestedEntityWasNOTFound()) {

			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
						   .with(ApiKeyJWTAuthenticationError.NOT_EXISTING_LOGIN,
								 Strings.customized("[api key login] NOT found by [loginId]/[apiKeyVersion]={}/{}",
													 loginId,version));
		} else if (apiKeyLoginLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
								  "Unknown error while loading login: " + apiKeyLoginLoad.asCRUDError().getDetailedMessage());
		}
		L apiKeyLogin = apiKeyLoginLoad.getOrThrow();

		// [2] - Load the [user]
		CRUDResult<U> userLoad = _userCrud.load(securityContext,
												apiKeyLogin.getApiKeyOwnerOid());
		if (userLoad.hasFailed()
		 && userLoad.asCRUDError()
					.wasBecauseClientRequestedEntityWasNOTFound()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,apiKeyLogin,null)
						.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
						  Strings.customized("Could NOT found [user] for [loginId]={} [loginId]/[apiKeyVersion]={}/{}",
											 apiKeyLogin.getUserOid(),
											 apiKeyLogin.getOid(),loginId,version));
		} else if (userLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,apiKeyLogin,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
								  "Unknown error while loading user: " + userLoad.asCRUDError().getDetailedMessage());
		}
		U user = userLoad.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,user,apiKeyLogin,null);
	}
	/**
	 * From the JWT get first the User via any user data (by the default the mail...this could be overwitten..) and after the User, and after the ApiKeyLogin
	 * @param securityContext
	 * @param jwtDecoded
	 * @return
	 */
	protected ApiKeyJWTAuthenticationStepResult<U,L,E> _loginSessionByUserDataOnJWTStep (final SecurityContext securityContext,
																						 final JWTDecoded jwtDecoded) {

		EMail email = EMail.create(jwtDecoded.getPayload()
										.getCustomClaim("email")
									.getValue());
		//[ 2] - Get the mail
		if (email == null)  {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
										.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
										  Strings.customized("For user data based apikey login the JWT must contain any user data claim, by default the mail"));
		}

		//[ 2] - Load the [user]
		CRUDResult<U> userLoad = _userCrud.loadBy(securityContext,
												  email);
		if (userLoad.hasFailed()
		 && userLoad.asCRUDError()
					.wasBecauseClientRequestedEntityWasNOTFound()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
						.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_USER,
						  Strings.customized("Could NOT found [user] for [email]={} ",
											 email));
		} else if (userLoad.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
							.with(ApiKeyJWTAuthenticationError.INTERNAL,
											"Unknown error while loading user: " + userLoad.asCRUDError().getDetailedMessage());
		}
		U user = userLoad.getOrThrow();

		//[ 2] - Load the [ApiKey]
		CRUDResult<L> apiKeyLoginLoad = _apiKeyLoginCrud.loadBy(securityContext,
																user.getOid());

		if (apiKeyLoginLoad.hasFailed()
					&& apiKeyLoginLoad.asCRUDError()
							.wasBecauseClientRequestedEntityWasNOTFound()) {

					return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
										.with(ApiKeyJWTAuthenticationError.NOT_EXISTING_LOGIN,
										Strings.customized("[api key login] NOT found by [UserOID]/{}",
																		 	user.getOid()));
		} else if (apiKeyLoginLoad.hasFailed()) {
						return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,null,null)
						.with(ApiKeyJWTAuthenticationError.INTERNAL,
							   "Unknown error while loading login  " + apiKeyLoginLoad.asCRUDError().getDetailedMessage());
		}

		L apiKeyLogin = apiKeyLoginLoad.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,user,apiKeyLogin,null);


	}


	protected ApiKeyJWTAuthenticationStepResult<U,L,E>  _jwtVerificationStep(final SecurityContext scx,
																			 final JWT signedJwt,  final U user,  final L apiKeyLogin) {

		JWTDecoded decodedJWT = JWTParserBuilder.createParser()
												.build()
												.parse(signedJwt);

	  // [1 ] get the [public key] (ensure the [private key] is NOT stored) -this time DO NOT tell the subscriber since it's an internal error-
		ApiKeyPair apiKeyPair = apiKeyLogin.getApiKeyPair();
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
		} catch (final InvalidKeySpecException keyEx) {
			log.error("Invalid [public key]: {}",keyEx.getMessage(),keyEx);

			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(decodedJWT,user,apiKeyLogin,null)
							.with(ApiKeyJWTAuthenticationError.INVALID_KEY_OR_ALGORITHM,
								  "Invalid [public key]: " + keyEx.getMessage());

		} catch (final NoSuchAlgorithmException algEx) {
			log.error("Invalid algorithm (RSA): {}",algEx.getMessage(),algEx);

			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(decodedJWT,user,apiKeyLogin,null).
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
			apiKeyLogin.getLoginId(),apiKeyLogin.getApiKeyVersion(),apiKeyLogin.getOid());
			return new ApiKeyJWTAuthenticationStepResultOK<>(decodedJWT,user,apiKeyLogin,null);

		}
		JWTVerificationResultAsNotValidToken notValidJWT = isJwtVerified.as(JWTVerificationResultAsNotValidToken.class);
		String errors = notValidJWT.getCause()
								   .stream()
								   .map(JWTVerificationResultInvalidCause::name)
								   .collect(Collectors.joining("\n-"));
		log.warn("NOT VERIFIED JWT for loginId/apiKeyVersion={}/{} (login oid={}):\n{}",
				 apiKeyLogin.getLoginId(),apiKeyLogin.getApiKeyVersion(),apiKeyLogin.getOid(),
				 errors);
		return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(decodedJWT,user,apiKeyLogin,null)
					   .with(ApiKeyJWTAuthenticationError.NOT_VERIFIED_JWT,
							 "JWT was NOT verified: " + errors);
	}


	protected ApiKeyJWTAuthenticationStepResult<U,L,E>  _loginEntry(final SecurityContext scx,
																	   final JWTDecoded jwtDecoded,  final U user,  final L apiKeyLogin) {
		//Register the login
		E loginEntry = _userLoginEntryFactory.create();
		loginEntry.setDate(Instant.now());
		loginEntry.setId(UserLoginEntryID.LOGIN_OK);
		loginEntry.setSecurityProviderId(SecurityProviderID.API_KEY);
		loginEntry.setLoginId(apiKeyLogin.getLoginId());
		loginEntry.setUserOid(user.getOid());

		CRUDResult<E> createdEntry = _userLoginEntryCrud.create(scx,
																   loginEntry);
		if (createdEntry.hasFailed()) {
			return new ApiKeyJWTAuthenticationStepResultError<U,L,E>(jwtDecoded,null,apiKeyLogin,null)
						.with(ApiKeyJWTAuthenticationError.INTERNAL,
										"Unknown error while creating login entryr: " + createdEntry.asCRUDError().getDetailedMessage());
		}

		E entry = createdEntry.getOrThrow();
		return new ApiKeyJWTAuthenticationStepResultOK<>(jwtDecoded,user,apiKeyLogin,entry);
	}
}
