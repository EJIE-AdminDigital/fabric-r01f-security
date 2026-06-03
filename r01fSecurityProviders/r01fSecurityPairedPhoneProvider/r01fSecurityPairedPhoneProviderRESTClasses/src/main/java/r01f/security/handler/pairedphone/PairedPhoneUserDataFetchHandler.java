package r01f.security.handler.pairedphone;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.pairedphone.CRUDServicesForPairedPhoneLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.jwt.JWTDecoded;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.login.pairedphone.PairedPhoneLogin;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.model.services.COREServiceErrorTypes;
import r01f.patterns.FactoryFrom;
import r01f.security.handler.UserDataFetchHandlerBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.StringBase64;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;


@Slf4j
public class PairedPhoneUserDataFetchHandler<U extends User,L extends PairedPhoneLogin>
	 extends UserDataFetchHandlerBase<U,L> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final FactoryFrom<U,L> _loginFactoryFromUser;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneUserDataFetchHandler(final Class<U> userType,final Class<L> loginType,
										   final Provider<SecurityContext> securityContextProvider,
										   final CRUDServicesForUser<U> userCrud,final CRUDServicesForPairedPhoneLogin<L> loginCrud,
										   final SecurityProviderSecretConfig securityProviderSecretConfig,
										   final FactoryFrom<U,L> loginFactoryFromUser) {
		super(userType,loginType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig);
		_loginFactoryFromUser = loginFactoryFromUser;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationResult<U> fetchUser(final JWTDecoded jwtDecoded,
												final StringBase64 base64SecurityProviderSession) {

		log.warn("INFORMATIVE Purpose: FETCH user data using JWT data, for provider type: Paired Phone.");
		
		// [1] - Get the [user oid] from jwt
		String userOidAsString = jwtDecoded.getPayload()
										   .getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_USER_OID)
										   .getValue();
		String pairedPhoneAsString = jwtDecoded.getPayload()
											   .getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_PAIRED_PHONE)
											   .getValue();
		if (Strings.isNullOrEmpty(userOidAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
															  	  							 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  	  									  "JWT does not contain a valid userOid");
		if (Strings.isNullOrEmpty(pairedPhoneAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
																  	  							 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
																  	  									  "JWT does not contain a valid pairedPhone");

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
		} //...If Login is null , it will be created after User load at step [4]

		// [3] - use the user CRUD to load the user
		U user = null;
		try {
			user= _userCrud.load(_securityContextProvider.get(),
								 userOid )
						   .getOrThrow();
		} catch (final PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
													  	 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
													  			  Strings.customized("The DB is in an INCONSISTENT STATUD: there exist a login with oid={} for userOid={}, BUT " +
													  			  					 "the user does NOT exists in the users table",
													  			  					 null,userOid));
			}
			throw pEx;
		}
		// [4] - Create login entry from User.
		if (login == null) {
			login = _loginFactoryFromUser.from(user);	             // BEWARE!! this does NOT set the phone oid
			login.setPairedPhone(Phone.of(pairedPhoneAsString));     //  DO NOT FORGET!!!
			login.setLoginId(LoginID.forId(pairedPhoneAsString));
			_loginCrud.create(_securityContextProvider.get(),
							  login);
		}

		log.warn("INFORMATIVE Purpose: END FETCH USER, for provider type: Paired Phone. User: {}", 
				 login.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
												  .returning(user);
	}
}
