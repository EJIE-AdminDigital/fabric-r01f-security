package r01f.security.handler.giltza;

import java.nio.charset.Charset;

import jakarta.inject.Provider;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.giltza.CRUDServicesForGiltzaLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.httpclient.HttpResponseCode;
import r01f.httpclient.HttpResponseResult;
import r01f.jwt.JWTDecoded;
import r01f.model.security.login.giltza.GiltzaLogin;
import r01f.model.security.user.User;
import r01f.patterns.FactoryFrom;
import r01f.security.handler.UserDataFetchHandlerForThirdPartyProviderBase;
import r01f.security.handler.giltza.GiltzaApi.GiltzaJSonFieldAccessor;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.user.giltza.GiltzaUserData;
import r01f.security.user.giltza.GiltzaUserDataBuilder;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.StringBase64;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;
import r01f.util.types.Strings;


@Slf4j
public class GiltzaUserDataFetchHandler<U extends User,L extends GiltzaLogin>
     extends UserDataFetchHandlerForThirdPartyProviderBase<U,L,GiltzaUserData> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final GiltzaApi _giltzaApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GiltzaUserDataFetchHandler(final Class<U> userType,final Class<L> loginType,
									  final Provider<SecurityContext> securityContextProvider,
									  final SecurityProviderSecretConfig securityProviderSecretConfig,
									  final CRUDServicesForUser<U> userCrud,final CRUDServicesForGiltzaLogin<L> loginCrud,
									  final FactoryFrom<GiltzaUserData,U> localUserFactoryFromGoogleUser,final FactoryFrom<GiltzaUserData,L> loginFactoryFromGoogleUser) {
		super(userType,loginType,
			  securityContextProvider,
			  userCrud,loginCrud,
			  securityProviderSecretConfig,
			  localUserFactoryFromGoogleUser,loginFactoryFromGoogleUser);
		_giltzaApi = new GiltzaApi(_securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	THIRD PARTY USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public GiltzaUserData fetchThirdPartyUserUsing(final JWTDecoded jwtDecoded,
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
			throw  new  IllegalStateException("It does not contain an access token or it is invalid. "
													+ "This is usually a programming or application state error");
		}
		log.info("JWT-stored info: email={}, access-token={}",
				 email,accessToken);
		// [2] - Get the user data from the [security provider]
		//		 BEWARE!!!
		//		 	- This is called from the [security filter] by issuing a server-to-server (app-to-security rest) call to fetch the [user data]
		//			- To issue a server-to-server (security rest-to-google api) the [google access token] that was received encoded at the [JWT] is used
		HttpResponseResult userInfoResponse = _giltzaApi.getGiltzaUserInfoHttpResponseUsing(SecurityToken.from(accessToken));

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
		EMail mail = (  jsonFieldAccessor.getValueOfFieldNamed("email") != null ) ?
				                          EMail.of(jsonFieldAccessor.getValueOfFieldNamed("email")):null;

		String givenName = jsonFieldAccessor.getValueOfFieldNamed("given_name") != null ?
				                                jsonFieldAccessor.getValueOfFieldNamed("given_name"):null;

		String familyName =  jsonFieldAccessor.getValueOfFieldNamed("family_name") != null ?
				                  jsonFieldAccessor.getValueOfFieldNamed("family_name") : null;

		PersonID dni =   jsonFieldAccessor.getValueOfFieldNamed("dni") != null ?
				                             PersonID.forId(jsonFieldAccessor.getValueOfFieldNamed("dni")) : null;

		String displayName = Strings.customized("{} {}",
												givenName,familyName);

		// [3] - Return the third-party user data
		GiltzaUserData giltzaUserData =
				   GiltzaUserDataBuilder.forPersonId(dni)
										 .givenName(givenName)
										 .familiyName(familyName)
										 .andDisplayName(displayName)
										 .withMail(mail)
									 .build();

		log.warn("INFORMATIVE Purpose: END FETCH Third Party User, for provider type: Giltza. User: {} / {}", displayName, mail);
		
		return giltzaUserData;
	}
}
