package r01f.security.handler.giltza;

import java.net.URI;
import java.util.HashMap;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.jwt.JWT;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.provider.SecurityProvider;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.contact.PersonID;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.StringEncodeUtils;

/**
 * After successfully logged in Giltza, the user will be redirected to this handler
 * and here THREE things happen:
 * 		1. A Giltza [access token] is created using the received Giltza [authorization code]
 * 		2. With the [access token] get the [user data]
 * 		3. Create a JWT token
 * 		2. The final url (the target app url) is composed with the JWT as a querystring param
 * The target app will check if the querystring-received JWT token is a legitimate one and if so
 * allow the user access
 */
@Slf4j
public class GiltzaCallBackOnLoginHandler
	 extends CallBackOnLoginHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String JWT_CUSTOM_CLAIM_FOR_GILTZA_ACCESS_TOKEN = "access_token";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final GiltzaApi  _giltzaApi;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GiltzaCallBackOnLoginHandler(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		super(securityProviderSecretConfig);
		_giltzaApi = new GiltzaApi(securityProviderSecretConfig);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * This method is called by Giltza after being loged-in (see RESTDelegateBaseForGiltzaLogin)
	 * @param giltzaAuthoritationCode
	 * @param afterLoginRedirToUrlBase64
	 * @return
	 */
	public Response loginCallBack(final String giltzaAuthoritationCode,
							 	  final String afterLoginRedirToUrlBase64) {
		log.info("=========================================================");
		log.warn("INFORMATIVE Purpose: AFTER LOGIN, by provider type: Giltza.");
		log.info("=========================================================");
		// [1] - Get the Giltza-returned access token using the received [authorization code]
		//       (an http call to Giltza with the [authorization code] is made to get the [Giltza access token]
		log.info("### [1] - Get [Giltza access token]");

		// If the authorization code is null or blank it means the user cancelled the flow
		if (giltzaAuthoritationCode == null || giltzaAuthoritationCode.trim().isEmpty()) {
			// decode the afterLogin URL received in base64 and redirect to it
			final String decodedAfterLogin = StringEncodeUtils.decodeBase64(afterLoginRedirToUrlBase64).toString();
			final Url afterLoginRedirToUrl = Url.from(decodedAfterLogin);
			final URI targetURI = UriBuilder.fromUri(afterLoginRedirToUrl.asString()).build();
			log.warn("Giltza login cancelled by user - redirecting to {}", decodedAfterLogin);
			return Response.seeOther(targetURI)
						   .build();
		}

		SecurityToken giltzaAccessToken = _giltzaApi.getGiltzaAccessTokenUsing(giltzaAuthoritationCode);

		// [2] - Use the access token to get the user email
		//  	 (another http call to Giltza with the [access token] is made to get the [user email]
		log.info("### [2] - Get [Giltza user data]");
		PersonID personId = _giltzaApi.getGiltzaUserPersonIdUsing(giltzaAccessToken);

		// [3] - Create a JWT token that contains the Giltza access-token and the user's email
		log.info("### [3] - Create the JWT token");
		@SuppressWarnings("serial")
		JWT jwt = _createJWT(_securityProviderSecretConfig.getKeyData().asString(),
							 SecurityProvider.GILTZA.getCode(),
							 LoginID.fromPersonID(personId),
							 null,
							 new HashMap<String,String>() {{
									put(JWT_CUSTOM_CLAIM_FOR_GILTZA_ACCESS_TOKEN,giltzaAccessToken.asString());
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
				 LoginID.fromPersonID(personId));

		return Response.seeOther(targetURI)
					   .build();
	}
}
