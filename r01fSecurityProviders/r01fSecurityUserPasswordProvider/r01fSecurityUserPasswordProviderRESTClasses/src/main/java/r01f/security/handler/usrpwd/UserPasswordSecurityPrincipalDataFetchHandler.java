package r01f.security.handler.usrpwd;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.JWTDecoded;
import r01f.model.persistence.PersistenceException;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.operation.SecurityOperation;
import r01f.model.security.operation.SecurityOperationExecResultBuilder;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.model.services.COREServiceErrorTypes;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.handler.SecurityPrincipalDataFetchHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.StringBase64;
import r01f.util.types.Strings;

@Slf4j
public class UserPasswordSecurityPrincipalDataFetchHandler<P extends IsSecurityPrincipal>
	 extends SecurityPrincipalDataFetchHandlerBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordSecurityPrincipalDataFetchHandler(final Class<P> userType,
														 final Provider<SecurityContext> securityContextProvider,
														 final CRUDServicesForPrincipal<P> userCrud,final CRUDServicesForLogin loginCrud,
														 final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(userType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<SecurityPrincipalAndLogin<P>> fetchPrincipalAndLogin(final JWTDecoded jwtDecoded,
													   							  		  final StringBase64 base64SecurityProviderSession) {

		log.warn("INFORMATIVE Purpose: FETCH [principal] using JWT data, for provider type: user/password.");
		
		// [1] - Get the [principal] from jwt
		String principalOidAsString = jwtDecoded.getPayload()
										   	    .getCustomClaim(UserPasswordCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_OID)
										   	    .getValue();
		if (Strings.isNullOrEmpty(principalOidAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
																  	  							  .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
																  	  									   "JWT does not contain a valid userOid");
		// [2] - Load thte [login]
		SecurityPrincipalOID principalOid = SecurityPrincipalOID.forId(principalOidAsString);
		Login login = null;
		try {
			login =	_loginCrud.loadBy(_securityContextProvider.get(),
									  IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD,principalOid)
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
									  	  									 Strings.customized("The requested principalOid={} does NOT exist!",
									  	  									 					principalOid));
		// [3] - Load the [principal]
		P principal = null;
		try {
			principal = _principalCrud.load(_securityContextProvider.get(),
								 		    principalOid )
						   			  .getOrThrow();
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				String err = Strings.customized("The DB is in an INCONSISTENT STATUD: there exist a [login]={} associated with [principal]={}, " + 
												"BUT the [principal] does NOT exists!!!",
												login.getOid(),principalOid);
				log.error(err);
				return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
													  	 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
													  			  err);
			}
			throw pEx;
		}

		log.warn("INFORMATIVE Purpose: END FETCH USER, for provider type: user/password. User: {}", 
				 login.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
												  .returning(new SecurityPrincipalAndLogin<>(principal,login));
	}
}
