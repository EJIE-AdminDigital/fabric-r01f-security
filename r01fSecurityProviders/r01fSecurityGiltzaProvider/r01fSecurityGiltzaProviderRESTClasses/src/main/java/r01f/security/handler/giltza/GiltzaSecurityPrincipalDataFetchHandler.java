package r01f.security.handler.giltza;

import java.nio.charset.Charset;
import java.util.Optional;

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
import r01f.security.handler.giltza.GiltzaApi.GiltzaJSonFieldAccessor;
import r01f.security.principal.giltza.GiltzaUserDataBuilder;
import r01f.security.principal.giltza.SecurityPrincipalDataFromGiltza;
import r01f.types.StringBase64;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;


@Slf4j
public class GiltzaSecurityPrincipalDataFetchHandler<P extends IsSecurityPrincipal>
     extends SecurityPrincipalDataFetchHandlerForThirdPartyProviderBase<P,SecurityPrincipalDataFromGiltza> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final GiltzaApi _giltzaApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GiltzaSecurityPrincipalDataFetchHandler(final Class<P> userType,
												   final Provider<SecurityContext> securityContextProvider,
												   final AuthenticationProviderSecretConfig securityProviderSecretConfig,
												   final CRUDServicesForPrincipal<P> userCrud,final CRUDServicesForLogin loginCrud,
												   final FactoryFrom<SecurityPrincipalDataFromGiltza,P> localUserFactoryFromGiltzaData,final FactoryFrom<SecurityPrincipalDataFromGiltza,Login> loginFactoryFromGiltzaData) {
		super(userType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig,
			  localUserFactoryFromGiltzaData,loginFactoryFromGiltzaData);
		_giltzaApi = new GiltzaApi(_securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IDENTITY PROVIDER
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public IdentityProvider getIdentityProvider() {
		return IdentityProvider.GILTZA;
	}
	@Override
	public AuthenticationMethod getAuthenticationMethod() {
		return AuthenticationMethod.OAUTH_AUTH_CODE_FLOW;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THIRD PARTY USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityPrincipalDataFromGiltza fetchThirdPartyPrincipalDataUsing(final JWTDecoded jwtDecoded,
												   						 	 final StringBase64 base64SecurityProviderSession) {
		log.info("=========================================================");
		log.info("FETCH Third Party User data using JWT data, for provider type: Giltza.");
		log.info("=========================================================");
		// [1] - Decode the jwt token
		String email =  null;
		if (jwtDecoded.getPayload()
				 		.getCustomClaim(GiltzaCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_EMAIL) != null ) {
			 email = jwtDecoded.getPayload()
									 .getCustomClaim(GiltzaCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_EMAIL)
									 .getValue();
		} else {
			log.warn("JWT does NOT contain a custom claim for mail");
		}
		String accessToken = null ;
		if (jwtDecoded.getPayload()
				   	  .getCustomClaim(GiltzaCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_GILTZA_ACCESS_TOKEN) != null ) {
			accessToken = jwtDecoded.getPayload()
								    .getCustomClaim(GiltzaCallBackOnLoginHandler.JWT_CUSTOM_CLAIM_FOR_GILTZA_ACCESS_TOKEN)
								    .getValue();
		} else {
			throw  new  IllegalStateException("It does not contain an access token or it is invalid. " + 
											  "This is usually a programming or application state error");
		}
		log.info("JWT-stored info: email={}, access-token={}",
				 email,accessToken);
		// [2] - Get the user data from the [security provider]
		//		 BEWARE!!!
		//		 	- This is called from the [security filter] by issuing a server-to-server (app-to-security rest) call to fetch the [user data]
		//			- To issue a server-to-server (security rest-to-google api) the [google access token] that was received encoded at the [JWT] is used
		HttpResponseResult userInfoResponse = _giltzaApi.getGiltzaPrincipalDataHttpResponseUsing(SecurityToken.from(accessToken));

		if (!userInfoResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = userInfoResponse.loadAsString();
			log.error("Remote Server Error in Giltza Auth Service, check params: (error code={}) > {}",
					  userInfoResponse.getCode(),errorStream);
			return null;
		}

		String userInfoResponseAsJson = userInfoResponse.loadAsString(Charset.forName("UTF-8"));
		log.info("... Received user data from GILTZA : {}",
											userInfoResponseAsJson);

		GiltzaJSonFieldAccessor jsonFieldAccessor = _giltzaApi.fieldsAccessorFor(userInfoResponseAsJson);

		EMail mail = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("email"))
							 .map(EMail::of)
							 .orElse(null);
		String name = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("given_name"))
							  .orElse(null);
		String surname1 = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("surname1"))
							      .orElse(null);
		String surname2 = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("surname2"))
				                  .orElse(null);
		PersonID dni =  Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("dni"))
								.map(PersonID::forId)
								.orElse(null);
		String displayName = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("name"))
									 .orElse(null);

		// [N] - Return the giltzaUserData
		SecurityPrincipalDataFromGiltza giltzaPrincipalData = GiltzaUserDataBuilder.forPersonId(dni)
										 										   .name(name)
										 										   .surname1(surname1)
										 										   .surname2(surname2)
										 										   .withMail(mail)
										 										   .displayName(displayName)
										 										   .build();
		log.warn("INFORMATIVE Purpose: END FETCH Third Party User, for provider type: Giltza. User: {} / {}", displayName, mail);

		return giltzaPrincipalData;
	}
}
