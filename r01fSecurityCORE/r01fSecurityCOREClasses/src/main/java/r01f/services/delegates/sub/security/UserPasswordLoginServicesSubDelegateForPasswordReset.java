package r01f.services.delegates.sub.security;

import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.eventbus.EventBus;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.parser.JWTParser;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.jwt.verifier.JWTVerificationResult;
import r01f.jwt.verifier.JWTVerificationResultAsNotValidToken;
import r01f.jwt.verifier.JWTVerificationResultInvalidCause;
import r01f.jwt.verifier.JWTVerifier;
import r01f.jwt.verifier.JWTVerifierBuilder;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetRequest;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponse;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponseBuilder;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponseErrorType;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorTypes;
import r01f.patterns.Factory;
import r01f.securitycontext.SecurityContext;

import r01f.services.core.ServicesCoreModuleConfig;
import r01f.services.delegates.security.UserPasswordLoginServicesDelegateBase;
import r01f.services.security.CoreCRUDServicesImplForUserPasswordLogin;

  public class UserPasswordLoginServicesSubDelegateForPasswordReset<U extends User,
														            L extends UserPasswordLogin,
														            E extends UserLoginEntry>
extends UserPasswordLoginServicesDelegateBase<U,L,E> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginServicesSubDelegateForPasswordReset(final ServicesCoreModuleConfig coreCfg,
															    final EventBus eventBus,
															    // factories
															    final Factory<E> loginEntryFactory,
															   // services
																final CRUDServicesForUser<U> userCrud,
																final CRUDServicesForUserPasswordLogin<L> userPasswordLoginCrud,
																final CRUDServicesForUserLoginEntry<E> userLoginEntryCrud) {
		super(coreCfg,
			  eventBus,
			  // services
			  userCrud,
			  userPasswordLoginCrud,
			  loginEntryFactory,userLoginEntryCrud);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationResult<UserPasswordLoginResetResponse> resetPassword(final SecurityContext securityContext,
																				 final UserPasswordLoginResetRequest request) {
		UserPasswordLoginResetResponse outResponse = null;
		if (request.getLoginId() == null) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  "LoginID can't be null");
		}

		// [1] - Load the login
		//		 BEWARE!!! 	The DAO layer (the deepest one) is used since if the service layer was used (as usual),
		//					the password / password hash will NOT be present at the returned object since it's REMOVED at the delegate layer
		CoreCRUDServicesImplForUserPasswordLogin<L> crudForUsrPwdLoginCOREService = (CoreCRUDServicesImplForUserPasswordLogin<L>)_userPasswordLoginCrud;
		CRUDResult<L> loginLoad = crudForUsrPwdLoginCOREService.createDBCRUDFor(securityContext)		// returns the DAO layer where the password is NOT removed
															   .loadBy(securityContext,
																		  request.getLoginId());
		if (loginLoad.hasFailed()) {
			if (loginLoad.asCRUDError()
						 .wasBecauseClientRequestedEntityWasNOTFound()) {
				outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
																   .responsedNow()
																   .withError(UserPasswordLoginResetResponseErrorType.INVALID_USER_CODE)
																   .build();
				return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RESET_REQUEST)
														 .returning(outResponse);
			}
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .because(_getOperationErrorResultFromCRUDResult(loginLoad.asCRUDError()),
															  loginLoad.asCRUDError().getErrorMessage());
		}

		// [2] the user is OK, check the token
		L login = loginLoad.getOrThrow();
		JWT signedJWT = JWT.forValue(request.getToken().asString());
		JWTParser parserJWT = JWTParserBuilder.createParser().build();
		JWTDecoded decodedJWT = parserJWT.parse(signedJWT);

		// Warn! Check this... <== Code SMELS
		byte[] sharedSecret = login.getUserOid().asString().getBytes();		// the password hash!! (the password is NEVER stored)

		JWTVerifier verifier = JWTVerifierBuilder.createVerifier()
												 .usingKey(sharedSecret)
												 .checkingExpiration()
												 .forAudience(SecurityOperation.PASSWORD_RECOVERY_REQUEST.name())
												 .build();
		JWTVerificationResult isJwtVerified = verifier.verify(signedJWT);

		String jwtIssuer = decodedJWT.getPayload() != null ? decodedJWT.getPayload().getIssuer()
														   : null;

		if (isJwtVerified.isNotValidJWT()
		 || !_coreConfig.getCoreAppCode().asString().equals(jwtIssuer)) {
			JWTVerificationResultAsNotValidToken notValidJWT = isJwtVerified.as(JWTVerificationResultAsNotValidToken.class);
			String errors = notValidJWT.getCause()
									   .stream()
									   .map(JWTVerificationResultInvalidCause::name)
									   .findFirst().orElse("");
			if (JWTVerificationResultInvalidCause.INVALID_SIGNATURE.name().equals(errors)) {
				outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
																   .responsedNow()
																   .withError(UserPasswordLoginResetResponseErrorType.TOKEN_WITH_INVALID_SIGNATURE)
																   .build();
			} else if (JWTVerificationResultInvalidCause.INVALID_AUDIENCE.name().equals(errors)) {
				outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
																   .responsedNow()
																   .withError(UserPasswordLoginResetResponseErrorType.TOKEN_WITH_INVALID_AUDIENCE)
																   .build();
			} else if (JWTVerificationResultInvalidCause.EXPIRED.name().equals(errors)) {
				outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
																   .responsedNow()
																   .withError(UserPasswordLoginResetResponseErrorType.TOKEN_EXPIRED)
																   .build();
			} else {
				outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
																   .responsedNow()
																   .withError(UserPasswordLoginResetResponseErrorType.TOKEN_WITH_OTHER_ERROR)
																   .build();
			}
			return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .returning(outResponse);
		}

		// [3] - The JWT was valid, change the password
		CRUDResult<L> paswordChanged = _userPasswordLoginCrud.updatePassword(securityContext,
																			 request.getLoginId(),request.getPassword());

		if (paswordChanged.hasFailed()) {
			return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.PASSWORD_RESET_REQUEST)
													 .because(_getOperationErrorResultFromCRUDResult(paswordChanged.asCRUDError()),
															  loginLoad.asCRUDError().getErrorMessage());
		}
		// [4] - Update the login entry
		_userLoginEntryCrud.updateAfterPasswordReset(securityContext,
													 login.getUserOid(),request.getToken());
		// [5] - Return
		outResponse = UserPasswordLoginResetResponseBuilder.forLoginId(request.getLoginId())
														   .responsedNow()
														   .done()
														   .build();
		return SecurityOperationExecResultBuilder.executed(SecurityOperation.PASSWORD_RESET_REQUEST)
												 .returning(outResponse);
	}
}
