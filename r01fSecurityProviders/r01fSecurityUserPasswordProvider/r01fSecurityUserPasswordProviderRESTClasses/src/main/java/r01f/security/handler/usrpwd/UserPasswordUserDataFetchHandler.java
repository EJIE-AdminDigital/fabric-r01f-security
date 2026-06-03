package r01f.security.handler.usrpwd;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.userpassword.CRUDServicesForUserPasswordLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.jwt.JWTDecoded;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorTypes;
import r01f.security.handler.UserDataFetchHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.StringBase64;
import r01f.util.types.Strings;

@Slf4j
public class UserPasswordUserDataFetchHandler<U extends User,L extends UserPasswordLogin>
	 extends UserDataFetchHandlerBase<U,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordUserDataFetchHandler(final Class<U> userType,final Class<L> loginType,
											final Provider<SecurityContext> securityContextProvider,
											final CRUDServicesForUser<U> userCrud,final CRUDServicesForUserPasswordLogin<L> loginCrud,
											final SecurityProviderSecretConfig securityProviderSecretConfig) {
		super(userType,loginType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<U> fetchUser(final JWTDecoded jwtDecoded,
												final StringBase64 base64SecurityProviderSession) {

		log.warn("INFORMATIVE Purpose: FETCH user data using JWT data, for provider type: user/password.");
		
		// [1] - Get the [user oid] from jwt
		String userOidAsString = jwtDecoded.getPayload()
										   .getCustomClaim(UserPasswordCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_USER_OID)
										   .getValue();
		if (Strings.isNullOrEmpty(userOidAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
															  	  							 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  	  									  "JWT does not contain a valid userOid");
		// [2] - use the login CRUD to load the login
		UserOID userOid = UserOID.forId(userOidAsString);
		L login = null;
		try {
			login =	_loginCrud.loadBy(_securityContextProvider.get(),
									  userOid)
							  .getOrThrow();
		} catch (final PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				login = null;
			} else {
				throw pEx;
			}
		}
		if (login == null) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
									  	  							.because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
									  	  									 Strings.customized("The requested userOid={} does NOT exist!",
									  	  									 					userOid));
		// [3] - use the user CRUD to load the user
		U user = null;
		try {
			user= _userCrud.load(_securityContextProvider.get(),
								 userOid )
						   .getOrThrow();
		} catch (final PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				log.error("The DB is in an INCONSISTENT STATUD: there exist a login with oid={} for userOid={}, BUT " +
						  "the user does NOT exists in the users table",
						  login.getOid(),userOid);
				return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
													  	 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
													  			  Strings.customized("The DB is in an INCONSISTENT STATUD: there exist a login with oid={} for userOid={}, BUT " +
													  			  					 "the user does NOT exists in the users table",
													  			  					 login.getOid(),userOid));
			}
			throw pEx;
		}

		log.warn("INFORMATIVE Purpose: END FETCH USER, for provider type: user/password. User: {}", 
				 login.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
												  .returning(user);
	}
}
