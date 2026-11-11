package r01f.security.handler.pairedphone;

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
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.handler.SecurityPrincipalDataFetchHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.StringBase64;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;


@Slf4j
public class PairedPhoneSecurityPrincipalPrincipalFetchHandler<P extends IsSecurityPrincipal>
	 extends SecurityPrincipalDataFetchHandlerBase<P> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairedPhoneSecurityPrincipalPrincipalFetchHandler(final Class<P> principalType,
															 final AuthenticationProviderSecretConfig authnProviderSecretConfig,
														     final Provider<SecurityContext> securityContextProvider,
														     // services
														     final CRUDServicesForPrincipal<P> principalCrud,
														     final CRUDServicesForLogin loginCrud) {
		super(principalType,
			  securityContextProvider,
			  principalCrud,loginCrud,
			  authnProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IsSecurityOperationResult<SecurityPrincipalAndLogin<P>> fetchPrincipalAndLogin(final JWTDecoded jwtDecoded,
															   							  final StringBase64 base64SecurityProviderSession) {

		log.warn("INFORMATIVE Purpose: FETCH user data using JWT data, for provider type: Paired Phone.");
		
		// [1] - Get the [user oid] from jwt
		String identityProviderAsString = jwtDecoded.getPayload()
											    	.getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_IDENTITY_PROVIDER)
											    	.getValue();
		String authMethodAsString = jwtDecoded.getPayload()
											  .getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_AUTHN_METHOD)
											  .getValue();
		String principalOidAsString = jwtDecoded.getPayload()
											    .getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_OID)
											    .getValue();
		String pairedPhoneAsString = jwtDecoded.getPayload()
											   .getCustomClaim(PairedPhoneCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_PAIRED_PHONE)
											   .getValue();
		if (Strings.isNullOrEmpty(identityProviderAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
															  	  							 	      .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  	  							 			       "JWT does not contain a valid [identity provider]");
		if (Strings.isNullOrEmpty(authMethodAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
														  	  							 	    .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
														  	  							 			     "JWT does not contain a valid [authn method]");
		if (Strings.isNullOrEmpty(principalOidAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
															  	  							 	  .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
															  	  							 			   "JWT does not contain a valid [principal] oid");
		if (Strings.isNullOrEmpty(pairedPhoneAsString)) return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
																  	  							 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
																  	  									  "JWT does not contain a valid [phone]");

		// [2] - use the login CRUD to load the login
		IdentityProvider identityProvider = IdentityProvider.fromName(principalOidAsString);
		AuthenticationMethod authnMethod = AuthenticationMethod.fromName(authMethodAsString);
		SecurityPrincipalOID principalOid = SecurityPrincipalOID.forId(principalOidAsString);
		Login login = null;
		try {
			login =	_loginCrud.loadBy(_securityContextProvider.get(),
									  identityProvider,AuthenticationMethod.PAIRED_PHONE,principalOid)
							   .getOrThrow();
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				login = null;
			} else {
				throw pEx;
			}
		} //...If Login is null , it will be created after User load at step [4]

		// [3] - use the [principal] CRUD to load the [principal]
		P principal = null;
		try {
			principal = _principalCrud.load(_securityContextProvider.get(),
								 	        principalOid )
						   			  .getOrThrow();
		} catch (PersistenceException pEx) {
			if (pEx.isEntityNotFound()) {
				return SecurityOperationExecResultBuilder.notExecuted(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
													  	 .because(COREServiceErrorTypes.BAD_CLIENT_REQUEST,
													  			  Strings.customized("The DB is in an INCONSISTENT STATUS: there exist a [login] with oid={} for [principal]={}, BUT " +
													  			  					 "the [principal] does NOT exists the system",
													  			  					 login != null ? login.getId() : null,
													  			  					 principalOid));
			}
			throw pEx;
		}
		// [4] - Create login entry from [principal].
		if (login == null) {
			Phone pairedPhone = Phone.of(pairedPhoneAsString);
			
			login = new Login(LoginID.fromPhone(pairedPhone));
			login.setSecurityPrincipalOid(principal.getOid());
			login.setSecurityPrincipalId(principal.getId());
			
			_loginCrud.create(_securityContextProvider.get(),
							  login);
		}

		log.warn("INFORMATIVE Purpose: END FETCH USER, for provider type: Paired Phone. User: {}", 
				 login.getOid());

		return 	SecurityOperationExecResultBuilder.executed(SecurityOperation.USER_DATA_REQUEST_VIA_JWT)
												  .returning(new SecurityPrincipalAndLogin<>(principal,login));
	}
}
