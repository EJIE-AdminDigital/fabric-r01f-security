package r01f.security.handler.saml;

import jakarta.inject.Provider;
import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.JWTDecoded;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.patterns.FactoryFrom;
import r01f.security.authn.handler.SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.principal.saml.SAMLSecurityPrincipalDataBuilder;
import r01f.security.principal.saml.SecurityPrincipalDataFromSAMLIdentityProvider;
import r01f.types.StringBase64;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;


@Slf4j
@Accessors(prefix="_")
public class SAMLSecurityPrincipalDataFetchHandler<P extends IsSecurityPrincipal>
     extends SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase<P,SecurityPrincipalDataFromSAMLIdentityProvider> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final IdentityProvider _identityProvider;
	@Getter private final AuthenticationMethod _authenticationMethod = AuthenticationMethod.SAML;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLSecurityPrincipalDataFetchHandler(final IdentityProvider identityProvider,
												 final Class<P> userType,
												 final Provider<SecurityContext> securityContextProvider,
												 final AuthenticationProviderSecretConfig authnProviderSecretConfig,
												 final CRUDServicesForPrincipal<P> userCrud,final CRUDServicesForLogin loginCrud,
												 final FactoryFrom<SecurityPrincipalDataFromSAMLIdentityProvider,P> localUserFactoryFromSAMLPrincipal,final FactoryFrom<SecurityPrincipalDataFromSAMLIdentityProvider,Login> loginFactoryFromSAMLPrincipal) {
		super(userType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  authnProviderSecretConfig,
			  localUserFactoryFromSAMLPrincipal,loginFactoryFromSAMLPrincipal);
		_identityProvider = identityProvider;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THIRD PARTY USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityPrincipalDataFromSAMLIdentityProvider fetchThirdPartyPrincipalDataUsing(final JWTDecoded jwtDecoded,
												 					   					   final StringBase64 base64SecurityProviderSession) {
		log.info("=========================================================");
		log.info("FETCH Third Party User data using JWT data, for provider type: SAML.");
		log.info("=========================================================");

		// [1] - Decode the jwt token
		String loginIdStr = jwtDecoded.getPayload()
								      .getCustomClaim(SAMLCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID)
								      .getValue();
		String emailStr = jwtDecoded.getPayload()
								    .getCustomClaim(SAMLCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_EMAIL)
								    .getValue();
		String nameStr = jwtDecoded.getPayload()
								    .getCustomClaim(SAMLCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_USER_NAME)
								    .getValue();
		String surnameStr = jwtDecoded.getPayload()
									  .getCustomClaim(SAMLCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_USER_LAST_NAME)
									  .getValue();

		log.info("JWT-stored info: loginId={}, email={}, name/surname={}",
				 loginIdStr,emailStr,nameStr,surnameStr);

		// [2] - Get the user data from the [security provider]
		//		 BEWARE!!!
		//		 	- This is called from the [security filter] by issuing a server-to-server (app-to-security rest) call to fetch the [user data]
		//			- Security providers like google / xlnets issue a server-to-server http call to get the user data
		//		 	  (google security provider uses an [access token] that comes inside the JWT
		//			   while xlnets uses the [xlnets session] posted by the [security filter] to the [user data fetch] REST endpoint)
		//		 	- The problem with SAML is that the SAML flow is a client flow (using client redirs), BUT this method is called
		//			  server-to-server (app-to-security rest) from the [security filter], so a client redir cannot be used
		//			  (if a client redir had to be used, the redir should have been done at the [security filter])
		//			  ... anyway, there's NO need to contact the [SAML IdP] since all [user data] was receiven when the login was done
		//				  and encoded at the [JWT], so here is enough to decode the [user data] from the [JWT] and just return it
		SecurityPrincipalDataFromSAMLIdentityProvider principalData = SAMLSecurityPrincipalDataBuilder.createForLoginId(LoginID.forId(loginIdStr))
																		   		  .withEMail(Strings.isNOTNullOrEmpty(emailStr) ? EMail.of(emailStr) : null)
																		   		  .name(nameStr).surname(surnameStr)
																		   		  .build();

		log.warn("INFORMATIVE Purpose: END FETCH Third Party User, for provider type: SAML. User: {} / {}", loginIdStr, emailStr);

		return principalData;
	}
}
