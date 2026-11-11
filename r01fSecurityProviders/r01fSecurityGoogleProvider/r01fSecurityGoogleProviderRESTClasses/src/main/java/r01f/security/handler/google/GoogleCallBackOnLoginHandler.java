package r01f.security.handler.google;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.StringEncodeUtils;

/**
 * After successfully logged in Google, the user will be redirected to this handler
 * and here THREE things happen:
 * 		1. A Google [access token] is created using the received google [authorization code]
 * 		2. With the [access token] get the [user data]
 * 		3. Create a JWT token
 * 		2. The final url (the target app url) is composed with the JWT as a querystring param
 * The target app will check if the querystring-received JWT token is a legitimate one and if so
 * allow the user access
 */
@Slf4j
public class GoogleCallBackOnLoginHandler
	 extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_GOOGE_ACCESS_TOKEN = "access_token";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final GoogleApi  _googleApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GoogleCallBackOnLoginHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
		_googleApi = new GoogleApi(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called by google after being loged-in (see RESTDelegateBaseForGoogleLogin)
	 * @param googleAuthoritationCode
	 * @param afterLoginRedirToUrlBase64
	 * @return
	 */
	public Response loginCallBack(final String googleAuthoritationCode,
							 	  final String afterLoginRedirToUrlBase64) {
		log.info("=========================================================");
		log.warn("INFORMATIVE Purpose: AFTER LOGIN, by provider type: google.");
		log.info("=========================================================");
		// [1] - Get the google-returned access token using the received [authorization code]
		//		 (an http call to google with the [authorization code] is made to get the [google access token]
		log.info("### [1] - Get [google access token]. googleAuthoritationCode: {}. state: {}",
				 googleAuthoritationCode,
				 afterLoginRedirToUrlBase64);
		SecurityToken googleAccessTokenAux = null;
		try {
			googleAccessTokenAux = _googleApi.getGoogleAccessTokenUsing(googleAuthoritationCode);
		} catch (Exception e) {
			log.error("... error getting google access token. Code {}. state: {}. Message: {}",
					 googleAuthoritationCode,
					 afterLoginRedirToUrlBase64,
					 e.getMessage());
			throw e;
		}
		
		SecurityToken googleAccessToken = googleAccessTokenAux;

		// [2] - Use the access token to get the user email
		//  	 (another http call to google with the [access token] is made to get the [user email]
		log.info("### [2] - Get [google user data]");
		EMail userEMail = _googleApi.getGoogleUserEMailUsing(googleAccessToken);

		// [3] - Create a JWT token that contains the google access-token and the user's email
		log.info("### [3] - Create the JWT token");
		JWT jwt = _createJWT(_securityProviderSecretConfig.getKeyData().asString(),
							 IdentityProvider.GOOGLE,AuthenticationMethod.OAUTH_AUTH_CODE_FLOW,
							 SecurityPrincipalID.fromLoginId(LoginID.forId(userEMail.asString())),
							 userEMail,		// google provider uses the email as [loginId]
							 new HashMap<String,String>() {{
									put(JWT_CUSTOM_CLAIM_FOR_GOOGE_ACCESS_TOKEN,googleAccessToken.asString());
							 }});

		// [4] - Create a url like afterLoginRedirToUrl?jwt={the_jwt}
		Url afterLoginRedirToUrl = Url.from(StringEncodeUtils.decodeBase64(afterLoginRedirToUrlBase64).toString());
		UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("jwt",jwt.asString()));
		Url redirUrl = afterLoginRedirToUrl.joinWith(qryStr);
		log.info("### [4] - Get [login callback url] = {}",
				 redirUrl);

		// [5] - Redir
		URI targetURI = UriBuilder.fromUri(redirUrl.asString())
								  .build();
		
		log.warn("INFORMATIVE Purpose: END AFTER LOGIN. Login ID = {}",
				 LoginID.forId(userEMail.asString()));

		return Response.seeOther(targetURI)
					   .build();
	}
}
