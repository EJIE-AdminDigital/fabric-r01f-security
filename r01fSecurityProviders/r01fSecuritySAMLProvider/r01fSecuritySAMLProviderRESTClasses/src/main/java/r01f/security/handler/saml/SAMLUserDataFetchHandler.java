package r01f.security.handler.saml;

import jakarta.inject.Provider;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.saml.CRUDServicesForSAMLLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.jwt.JWTDecoded;
import r01f.model.security.login.saml.SAMLLogin;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.security.handler.UserDataFetchHandlerForThirdPartyProviderBase;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.user.saml.SAMLUserData;
import r01f.security.user.saml.SAMLUserDataBuilder;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.StringBase64;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;


@Slf4j
public class SAMLUserDataFetchHandler<U extends User,L extends SAMLLogin>
     extends UserDataFetchHandlerForThirdPartyProviderBase<U,L,SAMLUserData> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLUserDataFetchHandler(final Class<U> userType,final Class<L> loginType,
									final Provider<SecurityContext> securityContextProvider,
									final SecurityProviderSecretConfig securityProviderSecretConfig,
									final CRUDServicesForUser<U> userCrud,final CRUDServicesForSAMLLogin<L> loginCrud,
									final FactoryFrom<SAMLUserData,U> localUserFactoryFromSAMLUser,final FactoryFrom<SAMLUserData,L> loginFactoryFromSAMLUser) {
		super(userType,loginType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig,
			  localUserFactoryFromSAMLUser,loginFactoryFromSAMLUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THIRD PARTY USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SAMLUserData fetchThirdPartyUserUsing(final JWTDecoded jwtDecoded,
												 final StringBase64 base64SecurityProviderSession) {
		log.info("=========================================================");
		log.info("FETCH Third Party User data using JWT data, for provider type: SAML.");
		log.info("=========================================================");

		// [1] - Decode the jwt token
		String loginIdStr = jwtDecoded.getPayload()
								      .getCustomClaim(SAMLCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID)
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
		SAMLUserData samlUserData = SAMLUserDataBuilder.createForLoginId(LoginID.forId(loginIdStr))
													   .withEMail(Strings.isNOTNullOrEmpty(emailStr) ? EMail.of(emailStr) : null)
													   .name(nameStr).surname(surnameStr)
													   .build();

		log.warn("INFORMATIVE Purpose: END FETCH Third Party User, for provider type: SAML. User: {} / {}", loginIdStr, emailStr);

		return samlUserData;
	}
}
