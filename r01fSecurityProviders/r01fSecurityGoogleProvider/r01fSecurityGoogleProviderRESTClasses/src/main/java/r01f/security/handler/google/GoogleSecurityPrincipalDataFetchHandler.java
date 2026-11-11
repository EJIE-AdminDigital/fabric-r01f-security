package r01f.security.handler.google;

import java.nio.charset.Charset;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.httpclient.HttpResponseCode;
import r01f.httpclient.HttpResponseResult;
import r01f.jwt.JWTDecoded;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.Login;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.patterns.FactoryFrom;
import r01f.security.authn.handler.SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.handler.google.GoogleApi.GoogleJSonFieldAccessor;
import r01f.security.principal.google.GoogleUserDataBuilder;
import r01f.security.principal.google.SecurityPrincipalDataFromGoogle;
import r01f.types.StringBase64;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.util.types.Strings;


@Slf4j
public class GoogleSecurityPrincipalDataFetchHandler<P extends IsSecurityPrincipal>
     extends SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase<P,SecurityPrincipalDataFromGoogle> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final GoogleApi _googleApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GoogleSecurityPrincipalDataFetchHandler(final Class<P> userType,
												   final Provider<SecurityContext> securityContextProvider,
												   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
												   final CRUDServicesForPrincipal<P> userCrud,final CRUDServicesForLogin loginCrud,
												   final FactoryFrom<SecurityPrincipalDataFromGoogle,P> localUserFactoryFromGoogleUser,final FactoryFrom<SecurityPrincipalDataFromGoogle,Login> loginFactoryFromGoogleUser) {
		super(userType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig,
			  localUserFactoryFromGoogleUser,loginFactoryFromGoogleUser);
		_googleApi = new GoogleApi(_securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IdentityProvider getIdentityProvider() {
		return IdentityProvider.GOOGLE;
	}	
	@Override
	public AuthenticationMethod getAuthenticationMethod() {
		return AuthenticationMethod.OAUTH_AUTH_CODE_FLOW;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THIRD PARTY USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityPrincipalDataFromGoogle fetchThirdPartyPrincipalDataUsing(final JWTDecoded jwtDecoded,
												   			final StringBase64 base64SecurityProviderSession) {
		log.info("=========================================================");
		log.info("Fetch Third Party User data using JWT data, for provider type: google.");
		log.info("=========================================================");

		// [1] - Decode the jwt token
		String email = jwtDecoded.getPayload()
								 .getCustomClaim(GoogleCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_EMAIL)
								 .getValue();
		String accessToken = jwtDecoded.getPayload()
									   .getCustomClaim(GoogleCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_GOOGE_ACCESS_TOKEN)
									   .getValue();
		log.info("JWT-stored info: email={}, access-token={}",
				 email,accessToken);

		// [2] - Get the user data from the [security provider]
		//		 BEWARE!!!
		//		 	- This is called from the [security filter] by issuing a server-to-server (app-to-security rest) call to fetch the [user data]
		//			- To issue a server-to-server (security rest-to-google api) the [google access token] that was received encoded at the [JWT] is used
		HttpResponseResult userInfoResponse = _googleApi.getGoogleUserInfoHttpResponseUsing(SecurityToken.from(accessToken));

		if (!userInfoResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = userInfoResponse.loadAsString();
			log.error("Remote Server Error int Google Auth Service, check params: (error code={}) > {}",
					  userInfoResponse.getCode(),errorStream);
			return null;
		}
		String userInfoResponseAsJson = userInfoResponse.loadAsString(Charset.forName("UTF-8"));
		log.info("Received user data from google : {}",
				 userInfoResponseAsJson);

		GoogleJSonFieldAccessor jsonFieldAccessor = _googleApi.fieldsAccessorFor(userInfoResponseAsJson);
		EMail googleMail = EMail.of(jsonFieldAccessor.getValueOfFieldNamed("email"));
		String givenName = jsonFieldAccessor.getValueOfFieldNamed("given_name");
		String familyName = jsonFieldAccessor.getValueOfFieldNamed("family_name");
		Url uri = Url.from(jsonFieldAccessor.getValueOfFieldNamed("picture"));
		String displayName = Strings.customized("{} {}",
												givenName,familyName);

		// [3] - Return the third-party user data
		SecurityPrincipalDataFromGoogle googleUserData = GoogleUserDataBuilder.createForMail(googleMail)
													 					  .givenName(givenName)
													 					  .familiyName(familyName)
													 					  .andDisplayName(displayName)
													 					  .andProfile(uri)
													 					  .build();
		log.warn("INFORMATIVE Purpose: END FETCH Third Party User, for provider type: SAML. User: {} / {}", displayName, googleMail);

		return googleUserData;
	}
}
