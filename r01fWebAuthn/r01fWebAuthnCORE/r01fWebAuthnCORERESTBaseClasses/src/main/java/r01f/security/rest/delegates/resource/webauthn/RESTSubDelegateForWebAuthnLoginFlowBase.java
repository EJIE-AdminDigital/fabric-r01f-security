package r01f.security.rest.delegates.resource.webauthn;


import java.time.Instant;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
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
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.login.entry.LoginEntry;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.model.services.COREServiceErrorTypes;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.util.types.Strings;

@Slf4j
public class RESTSubDelegateForWebAuthnLoginFlowBase<P extends IsSecurityPrincipal,C extends AuthenticatorCredential>
     extends RESTSubDelegateForWebAuthnAbstractBase<P,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CLAIM_CREDENTIAL_ID = "credentialId";
	public static final String JWT_CLAIM_LOGIN_ID = "loginId";
	public static final String JWT_CLAIM_IDENTITY_PROVIDER = "identityProvider";
	public static final String JWT_CLAIM_PRINCIPAL_OID = "principalOid";
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTSubDelegateForWebAuthnLoginFlowBase(final Provider<SecurityContext> _sysSecurityContextProvider,
											       final Marshaller marshaller,
											       final WebAuthnConfig webAuthnConfig,
											       final RelyingParty relyingParty,
											       final CRUDServicesForPrincipal<P> principalCrud,
			                                       final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud, 
			                                       final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
			                                       final CredentialFactoryFromRegistrationResponse<P,C> credentialFactoyFromRegistrationResponse) {

		super(_sysSecurityContextProvider,
			  marshaller, 
			  webAuthnConfig,
			  relyingParty, 
			  principalCrud, 
			  loginCrud,loginEntryCrud, 
			  authenticatorCredentialCrud, 
			  credentialFactoyFromRegistrationResponse);
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

		// [2] - Get the [principalOid | credential Id | loginId] contained at the JWT token (see Assertation Finish flow)
		String credetialIdAsString = jwtDecoded.getPayload()
											   .getCustomClaim(JWT_CLAIM_CREDENTIAL_ID)					
											   .getValue();
		String loginIdAsString = jwtDecoded.getPayload()
										   .getCustomClaim(JWT_CLAIM_LOGIN_ID)							
										   .getValue();
		String identityProviderAsString = jwtDecoded.getPayload()
										   			.getCustomClaim(JWT_CLAIM_IDENTITY_PROVIDER)		
										   			.getValue();
		String principalOidAsString = jwtDecoded.getPayload()
										   		.getCustomClaim(JWT_CLAIM_PRINCIPAL_OID)			
										   		.getValue();


		IdentityProvider identityProvider = Strings.isNOTNullOrEmpty(identityProviderAsString) ? IdentityProvider.fromName(identityProviderAsString) : null;
		CredentialID credentialId = Strings.isNOTNullOrEmpty(credetialIdAsString) ? CredentialID.fromBase64RawString(credetialIdAsString) : null;
		LoginID loginId = Strings.isNOTNullOrEmpty(loginIdAsString) ? LoginID.forId(loginIdAsString) : null;
		SecurityPrincipalOID securityPrincipalOid = SecurityPrincipalOID.forId(principalOidAsString);
		
		if (credentialId == null || loginId == null || identityProvider == null || principalOidAsString == null) {
			IsSecurityOperationResult<P> err = SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
									  										     .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
									  												      Strings.customized("Error. Invalid jwt: does NOT contains the {} / {} / {} / {} custom claims",
									  												    		             JWT_CLAIM_CREDENTIAL_ID,JWT_CLAIM_LOGIN_ID,JWT_CLAIM_IDENTITY_PROVIDER,JWT_CLAIM_PRINCIPAL_OID));
			return Response.status(Status.BAD_REQUEST)
						   .entity(_marshaller.forWriting()
										  	  .toJson(err))
						   .build();
		}

		// [3] - Get the [credential] data
		CRUDResult<C> opResultCredential = _authenticatorCredentialCrud.loadBy(_sysSecurityContextProvider.get(),
				                                                               credentialId,securityPrincipalOid);
		if (opResultCredential.hasFailed()) {
			log.warn("Remote security service error trying to get user credential data : {}",
					opResultCredential.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get usercredential data from {}: {}",
								   opResultCredential.debugInfo()))
						   .build();
		}
		C credential = opResultCredential.getOrThrow();

		// [4] - Get the [principal] data 
		CRUDResult<P> principalLoad = _principalCrud.load(_sysSecurityContextProvider.get(),
				                                          securityPrincipalOid);
		if (principalLoad.hasFailed()) {
			log.warn("Remote security service error trying to get [principal] data : {}",
					 principalLoad.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get [principal] from {}: {}",
												 	  principalLoad.debugInfo()))
						   .build();
		}
		P principal = principalLoad.getOrThrow();
		
		// [5] - Get the [login] data of the [loginId] 
		CRUDResult<Login> loginLoad = _loginCrud.loadBy(_sysSecurityContextProvider.get(),
				                                 		identityProvider,AuthenticationMethod.OAUTH_AUTH_CODE_FLOW,principal.getOid());
		if (loginLoad.hasFailed()) {
			log.warn("Remote security service error trying to get [login] data : {}",
					 loginLoad.getOrThrow());
			return Response.serverError()
						   .entity(Strings.customized("Remote Server Error while trying to get [login] from {}: {}",
												 	  principalLoad.debugInfo()))
						   .build();
		}
		Login login = loginLoad.getOrThrow();
		

		// [6] - Register the login entrY
		//		 It's done at this point because here the user data is available
		LoginEntry loginEntry = new LoginEntry();
		loginEntry.setEvent(LoginEntryEvent.LOGIN_OK);
		loginEntry.setAt(Instant.now());
		loginEntry.setLoginId(loginId);
		loginEntry.setLoginOid(login.getOid());
		loginEntry.setSecurityPrincipalOid(principal.getOid());

		_loginEntryCrud.create(_sysSecurityContextProvider.get(),
							   loginEntry);

		// [99] - Just return the user data as json
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
