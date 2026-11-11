package r01f.security.rest.resource;

import java.time.Instant;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.LoginEntry;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.model.services.COREServiceErrorTypes;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.authn.handler.LoginEntryPointHandlerBase;
import r01f.security.authn.handler.SecurityPrincipalDataFetchHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.util.types.Strings;

@Slf4j
public abstract class RESTDelegateForLoginBase<P extends IsSecurityPrincipal>
		   implements RESTDelegate {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final IdentityProvider _identityProvider;
	protected final AuthenticationMethod _authnMethod;

	// system-internal security context provider (used to query the CRUD services)
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	// security SECRET config
	protected final AuthenticationProviderSecretConfig _authnProviderSecretConfig;

	// handlers
	protected final LoginEntryPointHandlerBase _loginEntryPointHandler;
	protected final SecurityPrincipalDataFetchHandlerBase<P> _principalDataFetchHandler;

	// marshaller
	protected final Marshaller _marshaller;

	// user login CRUD
	protected final CRUDServicesForLoginEntry _loginEntryCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected RESTDelegateForLoginBase(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
									   final Provider<SecurityContext> sysSecurityContextProvider,
									   final AuthenticationProviderSecretConfig authnProviderSecretConfig,
									   // handlers
									   final LoginEntryPointHandlerBase loginEntryPointHandler,
									   final SecurityPrincipalDataFetchHandlerBase<P> fetchPrincipalDataHandler,
									   // marshaller
									   final Marshaller marshaller,
									   // used to create [login entries] when the user logs-in
									   final CRUDServicesForLoginEntry loginEntryCrud) {
		_identityProvider = identityProvider;
		_authnMethod = authnMethod;
		_sysSecurityContextProvider = sysSecurityContextProvider;
		_authnProviderSecretConfig = authnProviderSecretConfig;
		_loginEntryPointHandler = loginEntryPointHandler;
		_principalDataFetchHandler = fetchPrincipalDataHandler;
		_marshaller = marshaller;
		_loginEntryCrud = loginEntryCrud;
	}
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
	public Response fetchPrincipal(final JWT jwt,
							  	   final StringBase64 base64SecurityProviderSession) {
		log.debug("=========================================================");
		log.debug("Fetch [principal] from {}/{} using JWT:\n{}",_identityProvider,_authnMethod,jwt);
		log.debug("BEWARE!! this is where:");
		log.debug("\t- if the [principal] does NOT exist, it's created");
		log.debug("\t- the [login entry] is created");
		log.debug("=========================================================");

		// [0] - Validate the received JWT
		log.info("... [1] Validate received JWT {}",jwt);
		JWTVerificationResult verificationResult = this.isValidJWT(jwt);
		if (verificationResult.isNotValidJWT()) {
			IsSecurityOperationResult<P> err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
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
		String identityProviderAsString = jwtDecoded.getPayload()
													.getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_IDENTITY_PROVIDER)
													.getValue();
		String authnMethodAsString = jwtDecoded.getPayload()
											   .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_AUTHN_METHOD)
											   .getValue();
		String principalIdAsString = jwtDecoded.getPayload()
										   	   .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID)
										   	   .getValue();
		
		IdentityProvider identityProvider = Strings.isNOTNullOrEmpty(identityProviderAsString) ? IdentityProvider.forId(identityProviderAsString) : null;
		AuthenticationMethod authnMethod = Strings.isNOTNullOrEmpty(authnMethodAsString) ? AuthenticationMethod.forId(authnMethodAsString) : null;
		
		SecurityPrincipalID principalId = Strings.isNOTNullOrEmpty(principalIdAsString) ? SecurityPrincipalID.forId(principalIdAsString) : null;
		
		IsSecurityOperationResult<P> err = null;
		if (identityProvider == null || authnMethod == null) {
			err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
		  										    .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
		  												     "Invalid jwt: does NOT contains the [identity provider]/[authn method] custom claims");
		} else if (identityProvider.isNOT(_identityProvider) || authnMethod.isNOT(_authnMethod)) {
			err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
		  										    .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
		  												     Strings.customized("Invalid jwt: the received [identity provider]={} or [authn method]={} do NOT match the expected [identity provider]={} or [authn method]={}",
		  												    				     identityProvider,authnMethod,
		  												    				     _identityProvider,_authnMethod));
		} else if (principalId == null) {
			err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
		  										    .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
		  												     "Invalid jwt: does NOT contains the [security principal] custom claim");
		}
		if (err != null) {
			return Response.status(Status.BAD_REQUEST)
						   .entity(_marshaller.forWriting()
										  	  .toJson(err))
						   .build();
		}

		// [3] - Get the [principal] data from the [identity provider]
		//		 BEWARE!!! this is where the [principal] is created if it does NOT previously exists (see UserDataFetchHandlerForThirdPartyProviderBase)
		IsSecurityOperationResult<SecurityPrincipalAndLogin<P>> opResult = _principalDataFetchHandler.fetchPrincipalAndLogin(jwtDecoded,
																			  		 	  							 		 base64SecurityProviderSession);
		if (opResult.hasFailed()) {
			log.warn("Remote [identity provider] error trying to get [principal]={} data from {}: {}",
					 principalId,
					 _identityProvider,opResult.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote [identity provider] Error while trying to get [principal] from {}: {}",
												 	  _identityProvider,
												 	  opResult.debugInfo()))
						   .build();
		}
		SecurityPrincipalAndLogin<P> principalAndLogin = opResult.getOrThrow();
		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();

		// [4] - Register the login entrY
		//		 It's done at this point because here the user data is available
		LoginEntry loginEntry = new LoginEntry();
		loginEntry.setEvent(LoginEntryEvent.LOGIN_OK);
		loginEntry.setAt(Instant.now());
		loginEntry.setLoginOid(login.getOid());
		loginEntry.setLoginId(login.getId());
		loginEntry.setSecurityPrincipalOid(principal.getOid());

		_loginEntryCrud.create(_sysSecurityContextProvider.get(),
							   loginEntry);

		// [99] - Just return the [security principal] data as json
		return Response.ok()
					   .entity(_marshaller.forWriting()
										  .toJson(principal))
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
						   								.usingKey(_authnProviderSecretConfig.getKeyData().getBytes())
						   								.checkingExpiration()
						   								.forAudience("r01.login")
						   						 .build();
   	   JWTVerificationResult verificationResult =  verifier.verify(jwt);
   	   // Just log if not valid.
   	   if (verificationResult.isNotValidJWT()) {
   		   log.warn("Not valid jwt {}  because of {}",
   				    jwt,
   				    verificationResult.as(JWTVerificationResultAsNotValidToken.class).debugInfo());
   	   }
   	   return verificationResult;
	   //return verificationResult.isValidJWT();
	}
}
