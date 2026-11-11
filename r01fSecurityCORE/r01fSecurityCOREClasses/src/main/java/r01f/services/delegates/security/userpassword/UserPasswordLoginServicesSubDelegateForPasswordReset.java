package r01f.services.delegates.security.userpassword;

import java.util.concurrent.atomic.AtomicReference;

import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
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
import r01f.model.security.login.data.userpassword.LoginDataForUserPassword;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetRequest;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetResponse;
import r01f.model.security.login.userpassword.messages.password.reset.UserPasswordResetResponseBuilder;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.model.services.COREServiceErrorTypes;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.ids.ServiceIDs.CoreAppCode;

public class UserPasswordLoginServicesSubDelegateForPasswordReset<P extends IsSecurityPrincipal> 
	 extends UserPasswordLoginServicesSubDelegateBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForPasswordReset(final ServicesCoreModuleConfig coreCfg,
															    // services
																final CRUDServicesForPrincipal<P> principalCrud,
																final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud) {
		super(coreCfg,
			  principalCrud,
			  loginCrud,loginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET
/////////////////////////////////////////////////////////////////////////////////////////
	public IsSecurityOperationResult<UserPasswordResetResponse> resetPassword(final SecurityContext securityContext,
																	   		  final UserPasswordResetRequest request) {
		if (request.getLoginId() == null) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  "LoginID can't be null");
		}
		
		// [1] - Load the [principal] and the [login]
		AtomicReference<IsSecurityOperationResult<UserPasswordResetResponse>> err = new AtomicReference<>();
		SecurityPrincipalAndLogin<P> principalAndLogin = _loadSecurityPrincipalAndLogin(securityContext,
																						request.getLoginId(),
																						SecurityOperation.PASSWORD_RESET_REQUEST,
																						(theLoginOid,theLoginId) -> UserPasswordResetResponseBuilder.invalidLoginId()
																																			        .forLogin(theLoginOid,theLoginId)
																																			        .build(),
																						(error) -> err.set(error));
		if (principalAndLogin == null) return err.get();	// error loading the [principal] and [login]
		
		P principal = principalAndLogin.getSecurityPrincipal();
		Login login = principalAndLogin.getLogin();

		// [2] - Verfiy the JWT
		UserPasswordResetResponse errResponse = _verifyJWTToken(JWT.from(request.getToken()),
																_coreCfg.getCoreAppCode(),SecurityOperation.PASSWORD_RESET_REQUEST,
																login.getSecurityPrincipalOid(),
																login);
		if (errResponse != null) {
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RESET_REQUEST)
												    .returning(errResponse);
		}
	
		// [3] - The JWT is valid, change the password
		LoginDataForUserPassword loginData = login.getDataAsForUserPassword();
		if (loginData == null) throw new IllegalStateException("Login data is null for login " + login.getOid());
		loginData.setPassword(request.getTempPassword());	// set the new password
		
		CRUDResult<Login> updatedLogin = _loginCrud.update(securityContext,
														   login);

		if (updatedLogin.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .because(updatedLogin.asCRUDError());
		}
		
		// [4] - Create a NEW login entry
		_registerLoginEntry(securityContext,
							login,
							principal,
							LoginEntryEvent.PASSWORD_RESET);		

		// [99] - Return
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RESET_REQUEST)
												 .returning(UserPasswordResetResponseBuilder.changed()
														 												 .forLogin(login.getOid(),login.getId())
														 												 .build());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VERIFY JWT TOKEN
/////////////////////////////////////////////////////////////////////////////////////////	
	protected UserPasswordResetResponse _verifyJWTToken(final JWT signedJWT,
													    final CoreAppCode coreAppCode,final SecurityOperation securityOperation,
													    final SecurityPrincipalOID sharedSecret,
													    final Login login) {
		UserPasswordResetResponse outErrResponse = null;
		
		// [1] - Verification
		byte[] sharedSecretBytes = sharedSecret.asString().getBytes();
		JWTVerifier verifier = JWTVerifierBuilder.createVerifier()
												 .usingKey(sharedSecretBytes)
												 .checkingExpiration()
												 .forAudience(securityOperation.name())
												 .build();
		JWTVerificationResult jwtVerificationResult = verifier.verify(signedJWT);
		
		if (jwtVerificationResult.isNotValidJWT()) {
			// a) JWT is not valid, check the cause
			JWTVerificationResultAsNotValidToken notValidJWT = jwtVerificationResult.as(JWTVerificationResultAsNotValidToken.class);
			JWTVerificationResultInvalidCause firstJwtError = notValidJWT.getCause()
															     		 .stream()
															     		 .findFirst().orElse(null);
			if (JWTVerificationResultInvalidCause.INVALID_SIGNATURE.is(firstJwtError)) {
				outErrResponse = UserPasswordResetResponseBuilder.tokenWithInvalidSignature()
															     .forLogin(login.getOid(),login.getId())
															     .build();
			} else if (JWTVerificationResultInvalidCause.INVALID_AUDIENCE.is(firstJwtError)) {
				outErrResponse = UserPasswordResetResponseBuilder.tokenWithInvalidAudience()
																 .forLogin(login.getOid(),login.getId())
																 .build();
			} else if (JWTVerificationResultInvalidCause.EXPIRED.is(firstJwtError)) {
				outErrResponse = UserPasswordResetResponseBuilder.tokenExpired()
																 .forLogin(login.getOid(),login.getId())
																 .build();
			} else {
				outErrResponse = UserPasswordResetResponseBuilder.tokenWithOtherError()
													   		   	 .forLogin(login.getOid(),login.getId())
															     .build();
			}
		} else {
			// b) Check the issuer
			JWTDecoded decodedJWT = JWTParserBuilder.createParser()
												    .build()
												    .parse(signedJWT);
			String jwtIssuer = decodedJWT.getPayload() != null ? decodedJWT.getPayload()
																		   .getIssuer()
															   : null;
			if (!coreAppCode.asString().equals(jwtIssuer)) {
				outErrResponse = UserPasswordResetResponseBuilder.tokenWithInvalidIssuer()
													   		   	 .forLogin(login.getOid(),login.getId())
															     .build();
			}
		}
		return outErrResponse;
	}
}
