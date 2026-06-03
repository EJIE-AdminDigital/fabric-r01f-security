package r01f.security.login.filter;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.guids.CommonOIDs.WebSessionID;
import r01f.httpclient.HttpResponseCode;
import r01f.httpclient.HttpResponseResult;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded.CustomClaim;
import r01f.jwt.parser.JWTParser;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.security.handler.CallBackOnLoginHandlerBase;
import r01f.security.login.LoginSession;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.collections.CollectionUtils;


/**
 * This utility queries a LOCAL endpoint that returns the LOCAL [user data]
 * BEWARE!!
 * 		The LOCAL [user data] IS NOT the same as the one returned by a [security provider] (ie google, xlnets, etc)
 * 		Usually the LOCAL [user data] is created FROM the one returned by google, xlnets, etc,
 * 		BUT might have more (or less) properties
 * @param <U>
 */
@Slf4j
public class UserDataFromAfterLoginJWT<U extends User> {

	public static final String JWT_AUDIENCE_FOR_LOGIN = "r01.login";
	public static final String JWT_AUDIENCE_FOR_WEBAUTHN = "r01.webauthn";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<U> _userType;
	private final Marshaller _marshaller;
	private final SecurityLoginFilterConfig _loginPublicConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserDataFromAfterLoginJWT(final Class<U> userType,
						   	  		 final SecurityLoginFilterConfig loginFilterConfig,
						   	  		 final Marshaller marshaller) {
		_userType = userType;
		_marshaller = marshaller;
		_loginPublicConfig = loginFilterConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PROVIDER & LOGIN ID
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	public SecurityProviderID getSecurityProviderFromJWT(final JWT jwt) {
		SecurityProviderID providerId = _securityProviderIdFromJWT(jwt);
		return providerId;
	}
	@SuppressWarnings("static-method")
	public LoginID getLoginIdFromJWT(final JWT jwt) {
		LoginID loginId = _loginIdFromJWT(jwt);
		return loginId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER DATA
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the {@link User} data stored at the [after login JWT] token
	 * @param afterLoginJWT
	 * @return
	 */
	public U getUserDataFromJWT(final JWT afterLoginJWT,
								final StringBase64 securityProviderSerializedSession) {
		// [0] Checks the audience
        String audience = _audienceFromJWT(afterLoginJWT);
        log.debug(" ..jwt audience {}", audience);
        if (audience.equalsIgnoreCase(JWT_AUDIENCE_FOR_WEBAUTHN))
        	return getUserDataFromJWTOnWebAuthFlow(afterLoginJWT);

		// [1] - Guess who put the User data at the [after login JWT] token
		//		 (who created the JWT token)
		SecurityProviderID providerId = _securityProviderIdFromJWT(afterLoginJWT);

		// [2] - Use a LOCAL end-point that depends on the [third party provider] used to login (google, xlnets, user password, ....)
		//		 this LOCAL end-point (see RESTResourceForSecurityServicesBase#fetchUser)
		//			1) check if the user exists at the local DB; if so, return the data
		//			2) if the user does NOT exists at the local DB, ask the [third party provider]
		//			   for user's data, creates a LOCAL user and returns it
		Url securityProviderGETUrl = _loginPublicConfig.getProvider(providerId)
													   .getUserDataFetchUrl();
		// the url is like: /login/{jwt}?securityProviderId=providerId
		Url url = securityProviderGETUrl.joinWith(UrlPath.from(afterLoginJWT.asString()))
										.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("securityProviderId",providerId)));
		U user = _getUserFromLocalRESTService(url,
											  securityProviderSerializedSession);
		return user;
	}


	/**
	 * Gets the {@link User} data stored at the [after webauth-assertion] JWT token
	 * @param afterLoginJWT
	 * @return
	 */
	public U getUserDataFromJWTOnWebAuthFlow(final JWT afterLoginJWT) {
        log.trace("....get user data from jwt on webauthn flow");
		String webAuthAsString = _loginPublicConfig.getLoginCommonProperties().getProperty("webauthn.urldatafetch");
		Url securityProviderGETUrl = Url.from(webAuthAsString);
		Url url = securityProviderGETUrl.joinWith(UrlPath.from(afterLoginJWT.asString()));
		log.debug(" webauthn.urldatafetch ... :{} ",url);
		U user = _getUserFromLocalRESTService(url,
											  null);
		return user;
	} 

	/**
	 * Gets the {@link User} data stored at the [after login JWT] token
	 *
	 * After a user is logged-in, it's redirected to the [Security REST Services WAR] where an [after login jwt] token is created and then
	 * the [user] is redirected to the [UI] with this [after login jwt] token as param
	 * At the [UI] the request passes through the {@link LoginSessionServletFilter} that tries to create the {@link LoginSession}
	 * ... to do so, the [login session servlet filter] calls back the [Security REST Services WAR] to get the [user data]
	 * 		U user = _userDataFromAfterLoginJWT.getUserDataFromJWT(jwt);  <<<< issues an HTTP request to the [Security REST Services WAR] to get the [user data]
	 * 																 		   ... the HTTP request contains the [after login JWT] token so the [security REST services WAR]
	 * 																	 	       can be sure the request is a legitimate one
	 * In order to get the [user data], the [security REST services WAR] uses some data encoded at the [after login JWT] token
	 * ... BUT sometimes additional data is needed, and that data it's NOT encoded at the [after login JWT] token!!
	 *
	 * A [securityProviderSessionFromRequestProvider] provider gives the filter an opportunity to get this additional data
	 * @param request
	 * @param afterLoginJWT
	 * @param securityProviderSessionFromRequestProvider
	 * @return
	 */
	public LoginSession<U> getLoginSessionFormJWT(final HttpServletRequest request,
					 							  final JWT afterLoginJWT,
					 							  final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider) {
		// a) The [user] has logged-in using a [security provider]'s login page (ie google, xlnets...)
		//	  After being logged-in, the [user] has been redirected from the [security provider]'s login ui to the [security REST services]
		//	  where an [after login JWT] token is created and then redirected again to the [UI] where this filter is configured
		// 	  ... so this is the first time he enters the target...
		//	  here just an [after login JWT] ia available but in order to create the [Login Session], the [user data] is needed
		//	  ... and to get the [user data], the [filter] calls back the [security REST services] with the [after login JWT] token
		//        that "tells" the [security REST services] it's a legitimate request and just returns the [user data]
	    String audience = _audienceFromJWT(afterLoginJWT);
		SecurityProviderID securityProviderId =  ( audience == null || audience.equalsIgnoreCase(JWT_AUDIENCE_FOR_LOGIN))? this.getSecurityProviderFromJWT(afterLoginJWT): null;
		LoginID loginId = this.getLoginIdFromJWT(afterLoginJWT);

		// b) in order to get the [user data], sometimes the [security REST services] needs more data than the contained in the [JWT token]
		// 	  so get the [security provider]'s session (ie the xlnets session)
		// 	  this [security provider]'s-specific session might be serialized and sent to the [security REST war]
		// 	  where it might be needed to get the [user data]
		// 	  ... call the [security REST services] to get the [user data]
		StringBase64 securityProviderSessionBase64Encoded = securityProviderSessionFromRequestProvider != null
																? securityProviderSessionFromRequestProvider.base64SerializeLoginSession(securityProviderSessionFromRequestProvider.getLoginSession(request,
																																																	securityProviderId),
																																		 securityProviderId)
																: null;
		U user = this.getUserDataFromJWT(afterLoginJWT,
										 securityProviderSessionBase64Encoded);
		LoginSession<U> loginSession = new LoginSession<>(WebSessionID.forId(request.getSession(true).getId()),	// get the web session id
														  securityProviderId,loginId,
														  user);
		return loginSession;
	}
/////////////////////////////////////////////////////////////////////////////////////////
	private U _getUserFromLocalRESTService(final Url url,
										   final StringBase64 securityProviderSerializedSession) {
		log.debug("> Get user data using a JWT token from local service at {}",
				 url);
		try {
			HttpResponseResult httpResponseResult = null;
			// see RESTResourceForSecurityServicesBase
			if (securityProviderSerializedSession == null) {
				// GET request
				//
				 log.debug(".[]..using data fetch via [GET] : '{}' ",url.asString());
				 HttpRequest request = HttpRequest.newBuilder()
													  .uri(url.asUri())
													  .GET()
												  .build();
				 HttpResponse<InputStream> response = HttpClient.newBuilder()
																   .build()
																   .send(request,
											                             HttpResponse.BodyHandlers.ofInputStream());

				 httpResponseResult = new HttpResponseResult(response);
			} else {
				// POST request
				log.debug(".[]..using data fetch via [POST] : '{}'",url.asString()); 
				HttpRequest request = HttpRequest.newBuilder()
													  .uri(url.asUri())
													  .POST(HttpRequest.BodyPublishers.ofString(securityProviderSerializedSession.asString()))
												  .build();

				 HttpResponse<InputStream> response = HttpClient.newBuilder()
															   .build()
															   .send(request,
										                             HttpResponse.BodyHandlers.ofInputStream());
				 httpResponseResult = new HttpResponseResult(response);
			}
			if (!httpResponseResult.getCode().isIn(HttpResponseCode.OK)) {
				 log.warn("\t...http response Code : {}",
						  httpResponseResult.getCode());
				 throw new IllegalStateException(Throwables.message("Something was wrong at router calling url {}: responde code={}\n{}",
						 											url,httpResponseResult.getCode(),
						 											httpResponseResult.loadAsString()));
			}
			String userDataJson = httpResponseResult.loadAsString();

			log.trace("\t...json data of type {} loaded from login provider\n{}",
					 _userType,
					 userDataJson);
			U user = _marshaller.forReading()
								.fromJson(userDataJson,
										  _userType);
			return user;
		 }  catch (final IllegalStateException ilExcp) {
			 throw ilExcp;
		 }  catch (final Throwable e) {
			 log.error("\t...error getting user data from local REST service at {}: {}",
					   url,
					   e.getLocalizedMessage(),
					   e);
			 throw new IllegalStateException(e.getLocalizedMessage());
		 }
	}
	private static SecurityProviderID _securityProviderIdFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		CustomClaim securityProviderClaim = jwtParser.parse(jwt)
														.getPayload()
													  .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PROVIDER);

		if ( securityProviderClaim == null ) {
			log.error("\t...received jwt has no [security provider] encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [security provider] encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		String securityProviderStr = securityProviderClaim.getValue();
		SecurityProviderID securityProvider = SecurityProviderID.forId(securityProviderStr);
		log.info("...\t-security provider = {}",
				 securityProvider);
		return securityProvider;
	}
	private static LoginID _loginIdFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		String loginIdStr = jwtParser.parse(jwt)
									 .getPayload()
									 .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_LOGIN_ID)
									 .getValue();
		if (loginIdStr == null) {
			log.error("\t...received jwt has no [loginId] encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [loginId] encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		LoginID loginId = LoginID.forId(loginIdStr);
		log.info("...\t-loginId = {}",
				 loginId);
		return loginId;
	}

	private static String _audienceFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		List<String> audienceStrList = jwtParser.parse(jwt)
												.getPayload()
										       .getAudience();

		if (audienceStrList == null) {
			log.error("\t...received jwt has no [audience] encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [audience] encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		String audience = CollectionUtils.pickOneAndOnlyElement(audienceStrList);
		return audience;
	}

}
