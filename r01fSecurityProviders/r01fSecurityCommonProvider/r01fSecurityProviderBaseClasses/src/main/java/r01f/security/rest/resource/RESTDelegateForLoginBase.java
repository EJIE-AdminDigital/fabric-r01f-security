package r01f.security.rest.resource;

import java.time.Instant;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorTypes;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.handler.LoginEntryPointHandlerBase;
import r01f.security.handler.UserDataFetchHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Slf4j
@RequiredArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class RESTDelegateForLoginBase<U extends User,L extends Login,
											   E extends UserLoginEntry>
		   implements RESTDelegate {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final SecurityProviderID _securityProviderId;

	// system-internal security context provider (used to query the CRUD services)
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	// security SECRET config
	protected final SecurityProviderSecretConfig _securityProviderSecretConfig;

	// handlers
	protected final LoginEntryPointHandlerBase _loginEntryPointHandler;
	protected final UserDataFetchHandlerBase<U,L> _userDataFetchHandler;

	// marshaller
	protected final Marshaller _marshaller;

	// login entry factory
	protected final Factory<E> _loginEntryFactory;

	// user login CRUD
	protected final CRUDServicesForUserLoginEntry<E> _loginEntryCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	REDIR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Redir to the [security provider] login [entry point] with the given url as param
	 * @param afterLoginRedirToUrl
	 * @param impl
	 * @return
	 */
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl) {
		return _loginEntryPointHandler.redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl);
	}
	/**
	 * Redir to the [security provider] login [entry point] with the given url as param
	 * @param afterLoginRedirToUrl
	 * @param impl
	 * @return
	 */
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl,
															   final String impl) {
		return _loginEntryPointHandler.redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl,
																				  impl);
	}
	/**
	 * Redir to the [security provider] login [entry point] with the given url as param
	 * @param afterLoginRedirToUrl
	 * @param afterLoginToUrlExtaInfoBase64
	 * @param impl
	 * @return
	 */
	public Response redirToSecurityProviderLoginEntryPointWith(final Url afterLoginRedirToUrl,final StringBase64 afterLoginToUrlExtaInfoBase64,
															   final String impl) {
		return _loginEntryPointHandler.redirToSecurityProviderLoginEntryPointWith(afterLoginRedirToUrl,afterLoginToUrlExtaInfoBase64,
																				  impl);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FETCH USER
/////////////////////////////////////////////////////////////////////////////////////////
	public Response fetchUser(final JWT jwt,
							  final StringBase64 base64SecurityProviderSession) {
		log.debug("=========================================================");
		log.debug("Fetch user from {} using JWT:\n{}",_securityProviderId,jwt);
		log.debug("BEWARE!! this is where:");
		log.debug("\t- if the user does NOT exist, it's created");
		log.debug("\t- the login entry is created");
		log.debug("=========================================================");

		// [0] - Validate the received JWT
		log.info("... [1] Validate received JWT {}",jwt);
		JWTVerificationResult verificationResult = this.isValidJWT(jwt);
		if (verificationResult.isNotValidJWT()) {
			SecurityOperationResult<U> err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
									  										   .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
									  												    Strings.customized("Error. Invalid jwt {}",
									  												    				   verificationResult.as(JWTVerificationResultAsNotValidToken.class).debugInfo()));
			return Response.status(Status.BAD_REQUEST)
						   .entity(_marshaller.forWriting()
										  	  .toJson(err))
						   .build();
		}

		// [1] - Parse the jwt
		JWTDecoded jwtDecoded = JWTParserBuilder.createParser()
											    .build()
												.parse(jwt);

		// [2] - Get the loginId contained at the JWT token (see CallBackOnLoginHandlerBase)
		String securityProviderIdAsString = jwtDecoded.getPayload()
													  .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PROVIDER)
													  .getValue();
		String loginIdAsString = jwtDecoded.getPayload()
										   .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID)
										   .getValue();
		
		SecurityProviderID securityProviderId = Strings.isNOTNullOrEmpty(securityProviderIdAsString) ? SecurityProviderID.forId(securityProviderIdAsString) : null;
		
		LoginID loginId = Strings.isNOTNullOrEmpty(loginIdAsString) ? LoginID.forId(loginIdAsString) : null;
		if (securityProviderId == null || securityProviderId.isNOT(_securityProviderId) || loginId == null) {
			
			// AzureAD callback to de SAML rest resources implementation /users/saml/login, 
			// saml, osaazuread and osaazuread provider is valid for saml jwt response
			// ADFS and AzureAD uses the same REST url login
			if (securityProviderId != null 
					&& securityProviderId == SecurityProviderID.OSAKIDETZA_AZURE_AD
					&& _securityProviderId != SecurityProviderID.SAML) {
				
				SecurityOperationResult<U> err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
										  										   .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
										  												    Strings.customized("Error. Invalid jwt: does NOT contains the {} or {} custom claims or the received {}={} is NOT {}",
										  												    				   CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PROVIDER,CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID,
										  												    				   CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PROVIDER,securityProviderId,_securityProviderId));
				return Response.status(Status.BAD_REQUEST)
							   .entity(_marshaller.forWriting()
											  	  .toJson(err))
							   .build();
			}
		}


		// [3] - Get the user data from the security provider
		//		 BEWARE!!! this is where the user is created if it does NOT previously exists (see UserDataFetchHandlerForThirdPartyProviderBase)
		SecurityOperationResult<U> opResult = _userDataFetchHandler.fetchUser(jwtDecoded,
																			  base64SecurityProviderSession);
		if (opResult.hasFailed()) {
			log.warn("Remote security service error trying to get user data from {}: {}",
					 _securityProviderId,opResult.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get user data from {}: {}",
												 	  _securityProviderId,opResult.debugInfo()))
						   .build();
		}
		U user = opResult.getOrThrow();

		// [4] - Register the login entrY
		//		 It's done at this point because here the user data is available
		E userLoginEntry = _loginEntryFactory.create();
		userLoginEntry.setId(UserLoginEntryID.LOGIN_OK);
		userLoginEntry.setDate(Instant.now());
		userLoginEntry.setSecurityProviderId(securityProviderId);
		userLoginEntry.setLoginId(loginId);
		userLoginEntry.setUserOid(user.getOid());

		_loginEntryCrud.create(_sysSecurityContextProvider.get(),
							   userLoginEntry);

		// [99] - Just return the user data as json
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(user))
					   .build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	JWT VERIFICATION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static JWTVerificationResult verifyJWT(final String key,
													 final JWT jwt) {
	   JWTVerifier verifier = JWTVerifierBuilder.createVerifier()
			   									.usingKey(key.getBytes())
			   									.checkingExpiration()
			   									.forAudience("r01.login")
			   									.build();
	   return verifier.verify(jwt);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	protected JWTVerificationResult isValidJWT(final JWT jwt) {
   	   JWTVerifier verifier =  JWTVerifierBuilder.createVerifier()
						   								.usingKey(_securityProviderSecretConfig.getKeyData().getBytes())
						   								.checkingExpiration()
						   								.forAudience("r01.login")
						   						 .build();
   	   JWTVerificationResult verificationResult =  verifier.verify(jwt);
   	   // Just log if not valid.
   	   if (verificationResult.isNotValidJWT()) {
   		   log.warn(" Not valid jwt {}  because of {}",
					   				   jwt,
					   				   verificationResult.as(JWTVerificationResultAsNotValidToken.class).debugInfo());
   	   }
   	   return verificationResult;
	   //return verificationResult.isValidJWT();
	}
}
