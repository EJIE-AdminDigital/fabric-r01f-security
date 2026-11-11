package r01f.security.login.filter;

import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.httpclient.HttpResponseCode;
import r01f.httpclient.HttpResponseResult;
import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded.CustomClaim;
import r01f.jwt.parser.JWTParser;
import r01f.jwt.parser.JWTParserBuilder;
import r01f.model.security.IsSecurityPrincipal;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.WebSessionID;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.authn.handler.CallBackOnLoginHandlerBase;
import r01f.security.login.LoginSession;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.collections.CollectionUtils;


/**
 * This utility queries a LOCAL endpoint that returns the LOCAL [person] data
 * BEWARE!!
 * 		The LOCAL [principal] data IS NOT the same as the one returned by a [security provider] (ie google, xlnets, etc)
 * 		Usually the LOCAL [principal] data is created FROM the one returned by google, xlnets, etc,
 * 		BUT might have more (or less) properties
 * @param <P>
 */
@Slf4j
public class SecurityPrincipalDataFromAfterLoginJWT<P extends IsSecurityPrincipal> {

	public static final String JWT_AUDIENCE_FOR_LOGIN = "r01.login";
	public static final String JWT_AUDIENCE_FOR_WEBAUTHN = "r01.webauthn";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Class<P> _principalType;
	private final Marshaller _marshaller;
	private final SecurityLoginFilterConfig _loginPublicConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalDataFromAfterLoginJWT(final Class<P> principalType,
									   	  		  final SecurityLoginFilterConfig loginFilterConfig,
									   	  		  final Marshaller marshaller) {
		_principalType = principalType;
		_marshaller = marshaller;
		_loginPublicConfig = loginFilterConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IDENTITY PROVIDER & LOGIN ID
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	public IdentityProvider getIdentityProviderFromJWT(final JWT jwt) {
		IdentityProvider identityProvider = _securityProviderFromJWT(jwt);
		return identityProvider;
	}
	@SuppressWarnings("static-method")
	public AuthenticationMethod getAuthenticationMethodFromJWT(final JWT jwt) {
		AuthenticationMethod authnMethod = _authenticationMethodFromJWT(jwt);
		return authnMethod;
	}
	@SuppressWarnings("static-method")
	public SecurityPrincipalOID getSecurityPrincipalOidFromJWT(final JWT jwt) {
		SecurityPrincipalOID securityPrincipalOid = _securityPrincipalOidFromJWT(jwt);
		return securityPrincipalOid;
	}
	@SuppressWarnings("static-method")
	public SecurityPrincipalID getSecurityPrincipalIdFromJWT(final JWT jwt) {
		SecurityPrincipalID securityPrincipalId = _securityPrincipalIdFromJWT(jwt);
		return securityPrincipalId;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PRINCIPAL DATA
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Gets the {@link IsSecurityPrincipal} data stored at the [after login JWT] token
	 * @param afterLoginJWT
	 * @return
	 */
	public P getSecurityPrincipalDataFromJWT(final JWT afterLoginJWT,
											 final StringBase64 securityProviderSerializedSession) {
		// [0] Checks the audience
        String audience = _audienceFromJWT(afterLoginJWT);
        log.debug(" ..jwt audience {}",
        		  audience);
        if (audience.equalsIgnoreCase(JWT_AUDIENCE_FOR_WEBAUTHN)) return this.getSecurityPrincipalDataFromJWTOnWebAuthFlow(afterLoginJWT);

		// [1] - Guess who put the [identity provider] data at the [after login JWT] token
		//		 (who created the JWT token)
		IdentityProvider identityProvider = this.getIdentityProviderFromJWT(afterLoginJWT);
		AuthenticationMethod authnMethod = this.getAuthenticationMethodFromJWT(afterLoginJWT);
		
		// [2] - Use a LOCAL end-point that depends on the [third party provider] used to login (google, xlnets, user password, ....)
		//		 this LOCAL end-point (see RESTResourceForSecurityServicesBase#fetchUser)
		//			1) check if the [principal] exists at the local DB; if so, return the data
		//			2) if the [principal] does NOT exists at the local DB, ask the [third party provider]
		//			   for [princial]'s data, creates a LOCAL [principal] and returns it
		Url securityProviderGETUrl = _loginPublicConfig.getProviderPublicConfig(identityProvider,authnMethod)
													   .getPrincipalDataFetchUrl();
		// the url is like: /login/{jwt}?identityProviderId=providerId
		//								&authnMethod=authnMethod
		// BEWARE! the url CONTAINS the [after login JWT] token as path param so the [security REST services] can:
		//				- check that the request is a legitimate one
		//				- get the [principal] OID and ID that are encoded at the [after login JWT] token as custom claims
		Url url = securityProviderGETUrl.joinWith(UrlPath.from(afterLoginJWT.asString()))
										.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("identityProvider",identityProvider)))
										.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("authnMethod",authnMethod)));
		P principal = _getSecurityPrincipalFromLocalRESTService(url,
											  					securityProviderSerializedSession);
		return principal;
	}


	/**
	 * Gets the {@link IsSecurityPrincipal} data stored at the [after webauth-assertion] JWT token
	 * @param afterLoginJWT
	 * @return
	 */
	public P getSecurityPrincipalDataFromJWTOnWebAuthFlow(final JWT afterLoginJWT) {
        log.trace("....get [security principal] data from jwt on webauthn flow");
		String webAuthAsString = _loginPublicConfig.getLoginCommonProperties()
												   .getProperty("webauthn.urldatafetch");
		Url securityProviderGETUrl = Url.from(webAuthAsString);
		Url url = securityProviderGETUrl.joinWith(UrlPath.from(afterLoginJWT.asString()));
		log.debug(" webauthn.urldatafetch ... :{} ",url);
		P principal = _getSecurityPrincipalFromLocalRESTService(url,
											  					null);
		return principal;
	} 

	/**
	 * Gets the {@link IsSecurityPrincipal} data stored at the [after login JWT] token
	 *
	 * After a [principal] is logged-in, it's redirected to the [Security REST Services WAR] where an [after login jwt] token is created and then
	 * the [principal] is redirected to the [UI] with this [after login jwt] token as param
	 * At the [UI] the request passes through the {@link LoginSessionServletFilter} that tries to create the {@link LoginSession}
	 * ... to do so, the [login session servlet filter] calls back the [Security REST Services WAR] to get the [principal] data
	 * 
	 * In order to get the [principal] data] the [security REST services WAR] uses some data encoded at the [after login JWT] token
	 * ... BUT sometimes additional data is needed, and that data it's NOT encoded at the [after login JWT] token!!
	 *
	 * A [securityProviderSessionFromRequestProvider] provider gives the filter an opportunity to get this additional data
	 * @param request
	 * @param afterLoginJWT
	 * @param securityProviderSessionFromRequestProvider
	 * @return
	 */
	public LoginSession<P> getLoginSessionFormJWT(final HttpServletRequest request,
					 							  final JWT afterLoginJWT,
					 							  final SecurityProviderSessionFromRequestProvider securityProviderSessionFromRequestProvider) {
		// a) The [principal] has logged-in using a [identity provider]'s login page (ie google, xlnets...)
		//	  After being logged-in, the [principal] has been redirected from the [identity provider]'s login ui to the [security REST services]
		//	  where an [after login JWT] token is created and then redirected again to the [UI] where this filter is configured
		// 	  ... so this is the first time he enters the target...
		//	  here just an [after login JWT] ia available but in order to create the [Login Session], the [principal] data is needed
		//	  ... and to get the [principal] data, the [filter] calls back the [security REST services] with the [after login JWT] token
		//        that "tells" the [security REST services] it's a legitimate request and just returns the [principal] data
	    String audience = _audienceFromJWT(afterLoginJWT);
		IdentityProvider identityProvider = audience == null 
										 || audience.equalsIgnoreCase(JWT_AUDIENCE_FOR_LOGIN)
												? this.getIdentityProviderFromJWT(afterLoginJWT)
												: null;
		AuthenticationMethod authnMethod = audience == null 
									    || audience.equalsIgnoreCase(JWT_AUDIENCE_FOR_LOGIN)
												? this.getAuthenticationMethodFromJWT(afterLoginJWT)
												: null;
		
		SecurityPrincipalOID principalOid = this.getSecurityPrincipalOidFromJWT(afterLoginJWT);
		SecurityPrincipalID principalId = this.getSecurityPrincipalIdFromJWT(afterLoginJWT);

		// b) in order to get the [principal] data, sometimes the [security REST services] needs more data than the contained in the [JWT token]
		// 	  so get the [security provider]'s session (ie the xlnets session)
		// 	  this [identity provider]'s-specific session might be serialized and sent to the [security REST war]
		// 	  where it might be needed to get the [principal] data
		// 	  ... call the [security REST services] to get the [principal] data
		StringBase64 securityProviderSessionBase64Encoded = securityProviderSessionFromRequestProvider != null
																? securityProviderSessionFromRequestProvider.base64SerializeLoginSession(securityProviderSessionFromRequestProvider.getLoginSession(request,
																																																	identityProvider),
																																		 identityProvider)
																: null;
		P principal = this.getSecurityPrincipalDataFromJWT(afterLoginJWT,
										 				   securityProviderSessionBase64Encoded);
		LoginSession<P> loginSession = new LoginSession<>(WebSessionID.forId(request.getSession(true).getId()),	// get the web session id
														  identityProvider,authnMethod,
														  principal,
														  null);	// no sys-wide roles are set at this point (they are set later by the [security REST services] when the [principal] data is fetched)
		return loginSession;
	}
/////////////////////////////////////////////////////////////////////////////////////////
	private P _getSecurityPrincipalFromLocalRESTService(final Url url,
										   				final StringBase64 securityProviderSerializedSession) {
		log.debug("> Get [principal] data using a JWT token from local service at {}",
				 url);
		try {
			HttpResponseResult httpResponseResult = null;
			// see RESTResourceForSecurityServicesBase
			if (securityProviderSerializedSession == null) {
				// GET request
				 log.debug(".[]..using data fetch via [GET]: '{}'",
						   url.asString());
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
				log.debug(".[]..using data fetch via [POST]: '{}'",
						  url.asString()); 
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
			String principalDataJson = httpResponseResult.loadAsString();

			log.trace("\t...json data of type {} loaded from login provider\n{}",
					 _principalType,
					 principalDataJson);
			P principal = _marshaller.forReading()
								     .fromJson(principalDataJson,
										       _principalType);
			return principal;
		 }  catch (final IllegalStateException ilExcp) {
			 throw ilExcp;
		 }  catch (final Throwable e) {
			 log.error("\t...error getting [principal] data from local REST service at {}: {}",
					   url,
					   e.getLocalizedMessage(),
					   e);
			 throw new IllegalStateException(e.getLocalizedMessage());
		 }
	}
////////// --- IDENTITY PROVIDER & AUTHENTICATION METHOD
	private static IdentityProvider _securityProviderFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		CustomClaim identityProviderClaim = jwtParser.parse(jwt)
												     .getPayload()
													 .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_IDENTITY_PROVIDER);

		if ( identityProviderClaim == null ) {
			log.error("\t...received jwt has no [identity provider] encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [identity provider] encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		String securityProviderStr = identityProviderClaim.getValue();
		IdentityProvider securityProvider = IdentityProvider.forId(securityProviderStr);
		log.info("...\t-identity provider = {}",
				 securityProvider);
		return securityProvider;
	}
	private static AuthenticationMethod _authenticationMethodFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		CustomClaim authnMethodClaim = jwtParser.parse(jwt)
										     	.getPayload()
										     	.getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_AUTHN_METHOD);

		if ( authnMethodClaim == null ) {
			log.error("\t...received jwt has no [authentication method] encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [authentication method] encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		String authMethodStr = authnMethodClaim.getValue();
		AuthenticationMethod authenticationMethod = AuthenticationMethod.forId(authMethodStr);
		log.info("...\t-authentication method = {}",
				 authenticationMethod);
		return authenticationMethod;
	}
////////// --- SECURITY PRINCIPAL
	private static SecurityPrincipalOID _securityPrincipalOidFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		String principalOidStr = jwtParser.parse(jwt)
									 	  .getPayload()
									 	  .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_OID)
									 	  .getValue();
		if (principalOidStr == null) {
			log.error("\t...received jwt has no [security principal] OID encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [security principal] OID encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		SecurityPrincipalOID principalOid = SecurityPrincipalOID.forId(principalOidStr);
		log.info("...\t-principalOid = {}",
				 principalOid);
		return principalOid;
	}
	private static SecurityPrincipalID _securityPrincipalIdFromJWT(final JWT jwt) {
		JWTParser jwtParser = JWTParserBuilder.createParser()
											  .build();
		String principalIdStr = jwtParser.parse(jwt)
									 	 .getPayload()
									 	 .getCustomClaim(CallBackOnLoginHandlerBase.JWT_CUSTOM_CLAIM_FOR_PRINCIPAL_ID)
									 	 .getValue();
		if (principalIdStr == null) {
			log.error("\t...received jwt has no [security principal] ID encoded. This is an error of generated JWT  {}!!!!",
		   			  jwt);
			throw new IllegalArgumentException(Throwables.message("Received jwt has no [security principal] ID encoded. This is an error of generated JWT  {}!!!!",
																  jwt));
		}
		SecurityPrincipalID principalId = SecurityPrincipalID.forId(principalIdStr);
		log.info("...\t-principalId = {}",
				 principalId);
		return principalId;
	}
////////// --- JWT AUDIENCE
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
