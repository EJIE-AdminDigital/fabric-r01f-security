package r01f.security.rest.delegates.resource.webauthn;


import java.time.Instant;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerificationResultAsValidToken;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.model.services.COREServiceErrorTypes;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.util.types.Strings;

@Slf4j
public  class RESTSubDelegateForWebAuthnLoginFlowBase<U extends User,
                                                      E extends UserLoginEntry, C extends AuthenticatorCredential>
   extends RESTSubDelegateForWebAuthnAbstractBase<U,E,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTSubDelegateForWebAuthnLoginFlowBase(final Provider<SecurityContext> _sysSecurityContextProvider,
											       final Marshaller marshaller,
											       final WebAuthnConfig webAuthnConfig,
											       final RelyingParty relyingParty,
											       final CRUDServicesForUser<U> userCrud,
			                                       final CRUDServicesForUserLoginEntry<E> loginEntryCrud, final Factory<E> loginEntryFactory,
			                                       final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
			                                       final CredentialFactoryFromRegistrationResponse<U, C> credentialFactoyFromRegistrationResponse) {

		super(_sysSecurityContextProvider, marshaller, webAuthnConfig,relyingParty, userCrud, loginEntryCrud, loginEntryFactory,
			   authenticatorCredentialCrud, credentialFactoyFromRegistrationResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FETCH USER
/////////////////////////////////////////////////////////////////////////////////////////
	public Response fetchUser(final JWT jwt) {
		log.warn("=========================================================");
		log.warn("Fetch user from {} using webauthn passkey after assertion JWT:\n{}",jwt);
		log.warn("\t- the login entry will be created related to backmapped security provider");
		log.warn("=========================================================");

		// [0] - Validate the received JWT
		log.warn("... [1] Validate received JWT {}",jwt);
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

		// [2] - Get the [ userOid | credential Id !loginId ] contained at the JWT token (see Assertation Finish flow)
		String credetialIdAsString = jwtDecoded.getPayload()
													  .getCustomClaim("credentialId")
													  .getValue();
		String loginIdAsString = jwtDecoded.getPayload()
										   .getCustomClaim("loginId")
										   .getValue();

		String userOidAsString = jwtDecoded.getPayload()
										   .getCustomClaim("userOid")
										   .getValue();


		CredentialID credentialId = Strings.isNOTNullOrEmpty(credetialIdAsString) ? CredentialID.fromBase64RawString(credetialIdAsString) : null;

		LoginID loginId = Strings.isNOTNullOrEmpty(loginIdAsString) ? LoginID.forId(loginIdAsString) : null;
		if (credentialId == null || loginId == null) {

				SecurityOperationResult<U> err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
										  										   .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
										  												    Strings.customized("Error. Invalid jwt: does NOT contains the {} or {} custom claims or the received {}={} is NOT {}",
										  												    		"backmapped-provider-id","loginId"));
				return Response.status(Status.BAD_REQUEST)
							   .entity(_marshaller.forWriting()
											  	  .toJson(err))
							   .build();

		}
		UserOID userOid = UserOID.forId(userOidAsString);

		// [4] - Get the credential data from the credential crud
		CRUDResult<C> opResultCredential = _authenticatorCredentialCrud.loadBy(_sysSecurityContextProvider.get(),
				                                                               credentialId, userOid);
		if (opResultCredential.hasFailed()) {
			log.warn("Remote security service error trying to get user credential data : {}",
					opResultCredential.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get usercredential data from {}: {}",
								   opResultCredential.debugInfo()))
						   .build();
		}
		C credential = opResultCredential.getOrThrow();

		// [4] - Get the user data from the user crud
		CRUDResult<U> opResultUser =_userCrud.load(_sysSecurityContextProvider.get(),
				                                userOid);
		if (opResultUser.hasFailed()) {
			log.warn("Remote security service error trying to get user data : {}",
					 opResultUser.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get user data from {}: {}",
												 	  opResultUser.debugInfo()))
						   .build();
		}
		U user = opResultUser.getOrThrow();

		// [4] - Register the login entrY
		//		 It's done at this point because here the user data is available
		E userLoginEntry = _loginEntryFactory.create();
		userLoginEntry.setId(UserLoginEntryID.LOGIN_OK);
		userLoginEntry.setDate(Instant.now());
		userLoginEntry.setSecurityProviderId(credential.getBackMappedSecurityProviderOnRegistration());
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
   	   /*JWTVerifier verifier =  JWTVerifierBuilder.createVerifier()
						   								.usingKey(_webAuthnProviderConfig.getKeyData().getBytes())
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
   	   return verificationResult;*/
		return  new JWTVerificationResultAsValidToken();
	}
}
