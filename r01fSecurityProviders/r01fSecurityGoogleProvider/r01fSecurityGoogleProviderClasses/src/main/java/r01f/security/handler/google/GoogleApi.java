package r01f.security.handler.google;

import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import lombok.extern.slf4j.Slf4j;
import r01f.exceptions.Throwables;
import r01f.httpclient.HttpRequestBodyPublisherForURLEncodedForm;
import r01f.httpclient.HttpRequestFormParameterForText;
import r01f.httpclient.HttpResponseCode;
import r01f.httpclient.HttpResponseResult;
import r01f.httpclient.HttpSSLContextBuilder;
import r01f.internal.Env;
import r01f.mime.MimeTypes;
import r01f.security.provider.SecurityProviderException;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.user.google.GoogleUserData;
import r01f.security.user.google.GoogleUserDataBuilder;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.contact.EMail;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;

@Slf4j
public class GoogleApi {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SecurityProviderSecretConfig  _securityProviderSecretConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GoogleApi(final SecurityProviderSecretConfig securityProviderSecretConfig) {
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Get authorization code uri, first step oth google oauth flow..where user will obtain an authorization code,.
	 * @param finalTargetUri
	 * @return
	 */
	public Url getAuthorizationCodeUri(final Url finalTargetUri) {
		Url authorizationCodeURI = null;
		try {
			String key = _securityProviderSecretConfig.getProperty("key").asString();
			Url oauth2callback =  _securityProviderSecretConfig.getProperty("oauth2callback").asUrl();
			authorizationCodeURI = _securityProviderSecretConfig.getProperty("authorization-code-uri").asUrl();

			String state = StringEncodeUtils.encodeBase64(finalTargetUri.asString()).toString();
			UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("client_id",key), // The CallBack uri mus be registred!
					                                          UrlQueryStringParam.of("redirect_uri",oauth2callback.asString()),
															  UrlQueryStringParam.of("scope",URLEncoder.encode("openid profile email https://www.googleapis.com/auth/plus.login","UTF-8")),
															  UrlQueryStringParam.of("response_type","code"),
															  UrlQueryStringParam.of("state",state));
			authorizationCodeURI = authorizationCodeURI.joinWith(qryStr);
			log.warn("[Google authorizationCodeURI]: {}",
					 authorizationCodeURI);
			 return authorizationCodeURI;
		} catch (final Throwable th) {
			log.error("Could NOT create google's authorization code url: {}. Exception: {}",
					  authorizationCodeURI,
					  th.getLocalizedMessage(),
					  th);
			throw new SecurityProviderException("Could NOT create google's authorization code url: " + authorizationCodeURI,
					  							th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Provides access to a json's field values
	 * @param jsonAsString
	 * @return
	 */
	public GoogleJSonFieldAccessor fieldsAccessorFor(final String jsonAsString) {
		return new GoogleJSonFieldAccessor(jsonAsString);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Obtains the google access token through an authorization code got in the first step of google oauth flow.
	 * This Google Access token will be used to obtain user data.
	 * @param authoritationCode
	 * @return
	 */
	public SecurityToken getGoogleAccessTokenUsing(final String authoritationCode) {
		log.info(" _getGoogleAccessTokenUsing {}",authoritationCode);
		HttpResponseResult accessTokenResponse = this.getGoogleAccessTokenHttpResponseUsing(authoritationCode);
		if (!accessTokenResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = accessTokenResponse.loadAsString();
			log.error("Remote Server Error int Google Auth Service: (code={}) > {}",
					  accessTokenResponse.getCode(),errorStream);
			throw new SecurityProviderException(Strings.customized("Remote Server error at Google Service (code={}): {}",
																   accessTokenResponse.getCode(),errorStream));
		}
		String accessTokenResponseStr = accessTokenResponse.loadAsString();
		log.info("[google access token] http call response {}",
				 accessTokenResponseStr);
		String accessToken = this.fieldsAccessorFor(accessTokenResponseStr)
								 .getValueOfFieldNamed("access_token");
		log.info("[google access token] = {}",
				 accessToken);
		return SecurityToken.forId(accessToken);
	}
	/**
	 * Obtains the google access token through an authorization code got in the first step of google oauth flow.
	 * This Google Access token will be used to obtain user data.
	 * @param authorizationCode
	 * @return
	 */
	public HttpResponseResult getGoogleAccessTokenHttpResponseUsing(final String authorizationCode) {
		Url accessTokenURI = _securityProviderSecretConfig.getProperty("access-token-uri").asUrl();
		String key = _securityProviderSecretConfig.getProperty("key").asString();
		String  secret = _securityProviderSecretConfig.getProperty("secret").asString();

		Url oauth2callback =  _securityProviderSecretConfig.getProperty("oauth2callback").asUrl();
		try {
			// .. compose the request
			HttpRequest request = HttpRequest.newBuilder()
					  						 .uri(accessTokenURI.asUri())
					  						 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
					  						 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
												  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(key).withName("client_id"),
																	  		    	  HttpRequestFormParameterForText.of(secret).withName("client_secret"),
																	  		    	  HttpRequestFormParameterForText.of(oauth2callback.asString()).withName("redirect_uri"),
																	  		    	  HttpRequestFormParameterForText.of("authorization_code").withName("grant_type"),
																	  		    	  HttpRequestFormParameterForText.of(authorizationCode).withName("code"))

												  		   .build())
					  						 .build();
	      // .. do the request
			HttpResponse<InputStream> response =  _httpClient()
														   .send(request,
													             HttpResponse.BodyHandlers.ofInputStream());

			return new HttpResponseResult(response);
		} catch (final Throwable th) {
			th.printStackTrace();
			log.error("Could NOT access google url at {} (using proxy={}): {}",
					  accessTokenURI,
					  _securityProviderSecretConfig.isProxyEnabled(),
					  th.getLocalizedMessage(),th);
			throw new SecurityProviderException(Throwables.message("Could NOT access google url at {} (using proxy={}): {}",
					  											accessTokenURI,
					  											_securityProviderSecretConfig.isProxyEnabled(),
					  											th.getLocalizedMessage()),
					  						th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER INFO
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Using an [access token] returned by goole gets the user info detailed by scopes.
	 * @param accessToken
	 * @return
	 */
	public HttpResponseResult getGoogleUserInfoHttpResponseUsing(final SecurityToken accessToken) {
		Url userInfoBaseUrl = _securityProviderSecretConfig.getProperty("user-info-uri").asUrl();
		UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("access_token",
																				 accessToken));
		Url userInfoUrl = userInfoBaseUrl.joinWith(qryStr);
		try {
			log.info("Get [google user info] at {} (NO PROXY)",
					 userInfoUrl);
			// .. do the request
			HttpResponse<InputStream> response = _httpClient()
											   				.send(HttpRequest.newBuilder()
																				 .uri(userInfoUrl.asUri())
																				 .GET()
																			 .build(),
											   					  HttpResponse.BodyHandlers.ofInputStream());
			return new HttpResponseResult(response);
		} catch (final Throwable th) {
			log.error("Could NOT access google url at {} (using proxy={}): {}",
					  userInfoUrl,
					  _securityProviderSecretConfig.isProxyEnabled(),
					  th.getLocalizedMessage(),th);
			throw new SecurityProviderException(Throwables.message("Could NOT access google url at {} (using proxy={}): {}",
					  											userInfoUrl,
					  											_securityProviderSecretConfig.isProxyEnabled(),
					  											th.getLocalizedMessage()),
					  						    th);
		}
	}
	
	public GoogleUserData getGoogleUserInfoUsing(final SecurityToken accessToken) {
		HttpResponseResult result =  getGoogleUserInfoHttpResponseUsing( accessToken);
		if (result.isSuccess()) {
			return _from(result.loadAsString());
		} 
		throw new SecurityProviderException(Throwables.message("Could NOT access Google USER info, error/data {}",
					                                               result.loadAsString()));
		
	}
	/**
	 * Using an [access token] returned by goole gets the user email.
	 * @param accessToken	 * @return
	 */
	public EMail getGoogleUserEMailUsing(final SecurityToken accessToken) {
		HttpResponseResult userInfoResponse = this.getGoogleUserInfoHttpResponseUsing(accessToken);
		if (!userInfoResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = userInfoResponse.loadAsString();
			log.info("Remote Server Error  Google Auth Service  getting user info with token={}: (code={}) > {}",
					 accessToken,
					 userInfoResponse.getCode(),errorStream);
			throw new SecurityProviderException(Strings.customized("Remote Server error at Google Service using access token={} (code={}): {}",
																accessToken,
																userInfoResponse.getCode(),errorStream));
		}
		String userInfoResponseStr = userInfoResponse.loadAsString();
		log.info("Google user info http call response {} ",
				 userInfoResponseStr);
		String emailAsString = this.fieldsAccessorFor(userInfoResponseStr)
								   .getValueOfFieldNamed("email");
		return EMail.of(emailAsString);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HTTP CLIENT
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	private  HttpClient _httpClient() {
		Env env = Env.guessFromSystemProps();
		if (env.isNOT(Env.LOC)) {
			return HttpClient.newBuilder()
							 .build();
		}
		try {
			log.warn(" >>> [Warn! ] Build ssl context for untrusted cets. Just for local environment  ");;
			return HttpClient.newBuilder()
			                 	.sslContext(HttpSSLContextBuilder.buildSSlContextForUnstrustedCerts())
			                 .build();
		} catch (final Throwable e) {
			throw new SecurityProviderException(e.getLocalizedMessage());
		}
	}
	
	private GoogleUserData _from (final String userInfoResponseAsJson) {

		GoogleJSonFieldAccessor jsonFieldAccessor = fieldsAccessorFor(userInfoResponseAsJson);
		EMail googleMail = EMail.of(jsonFieldAccessor.getValueOfFieldNamed("email"));
		String givenName = jsonFieldAccessor.getValueOfFieldNamed("given_name");
		String familyName = jsonFieldAccessor.getValueOfFieldNamed("family_name");
		Url uri = Url.from(jsonFieldAccessor.getValueOfFieldNamed("picture"));
		String displayName = Strings.customized("{} {}",
												givenName,familyName);

		// [3] - Return the third-party user data
		GoogleUserData googleUserData = 
				GoogleUserDataBuilder.createForMail(googleMail)
										 .givenName(givenName)
										 .familiyName(familyName)
										 .andDisplayName(displayName)
										 .andProfile(uri)
									 .build();
		return googleUserData;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GSON
/////////////////////////////////////////////////////////////////////////////////////////
	private static final Gson GSON = new Gson();

	class GoogleJSonFieldAccessor {
		private final JsonObject _json;

		private GoogleJSonFieldAccessor(final String jsonAsString) {
			_json = JsonParser.parseString(jsonAsString)
							   .getAsJsonObject();
		}
		public String getValueOfFieldNamed(final String name) {
			String value = null;
			try {
				JsonElement jelement = _json.getAsJsonObject().get(name);
				value = jelement.getAsString();
			} catch (final NullPointerException nulex) {
				log.error(Strings.customized("Field named '{}' does not exist in json:\n {}",
											 name,GSON.toJson(_json)));
			}
			return value;
		}
	}

	
}
