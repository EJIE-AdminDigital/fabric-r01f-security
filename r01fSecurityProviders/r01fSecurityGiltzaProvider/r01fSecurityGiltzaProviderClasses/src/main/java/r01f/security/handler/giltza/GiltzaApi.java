package r01f.security.handler.giltza;

import java.io.InputStream;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Optional;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
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
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.security.authn.handler.AuthenticationProviderException;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.principal.giltza.SecurityPrincipalDataFromGiltza;
import r01f.security.principal.giltza.GiltzaUserDataBuilder;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.Strings;

@Slf4j
 public class GiltzaApi {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final AuthenticationProviderSecretConfig  _securityProviderSecretConfig;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public GiltzaApi(final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	AUTHORIZATION CODE URI
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Get authorization code uri, first step oth Giltza oauth flow..where user will obtain an authorization code,.
	 * @param finalTargetUri
	 * @return
	 */
	public Url getAuthorizationCodeUri(final Url finalTargetUri) {
		String acrValues = _securityProviderSecretConfig.getProperty("acr_values").asString();
		return this.getAuthorizationCodeUri(finalTargetUri,acrValues,null);
	}

	/**
	 * Get authorization code uri, first step oth Giltza oauth flow..where user will obtain an authorization code,.
	 * @param finalTargetUri
	 * @return
	 */
	public Url getAuthorizationCodeUri(final Url finalTargetUri,final String codeChallenge) {
		String acrValues = _securityProviderSecretConfig.getProperty("acr_values").asString();
		return this.getAuthorizationCodeUri(finalTargetUri,acrValues,codeChallenge);
	}
	/**
	 * Get authorization code uri, first step oth Giltza oauth flow..where user will obtain an authorization code,.
	 * @param finalTargetUri
	 * @param acrValues can select each flow, in no one is passed the FLOW SELECTOR will be selected
	 *                     ( f.e for bakq: "urn:safelayer:tws:policies:authentication:flow:bakq")
	 * @return
	 */
	public Url getAuthorizationCodeUri(final Url finalTargetUri,
									   final String acrValues,final String codeChallenge) {
		Url authorizationCodeURI = null;
		try {
			// Load Giltza SecurityProvideConfigData ( key | oauth2callback | authorizationCodeURI)
			String key = _securityProviderSecretConfig.getProperty("key").asString();
			Url oauth2callback =  _securityProviderSecretConfig.getProperty("oauth2callback").asUrl();
			authorizationCodeURI = _securityProviderSecretConfig.getProperty("authorization-code-uri").asUrl();
			String state = StringEncodeUtils.encodeBase64(finalTargetUri.asString()).toString();
			// Compose required params for authorization code uri.
			List<UrlQueryStringParam> queryRequiredParams  = Lists.newArrayList();
			queryRequiredParams.add(UrlQueryStringParam.of("client_id",key));
			queryRequiredParams.add(UrlQueryStringParam.of("redirect_uri",URLEncoder.encode(finalTargetUri.asString(),Charset.defaultCharset())));
			queryRequiredParams.add(UrlQueryStringParam.of("scope",URLEncoder.encode("urn:izenpe:identity:global urn:izenpe:fea:properties urn:safelayer:eidas:authn_details urn:safelayer:eidas:sign:identity:profile",Charsets.ISO_8859_1.name())));
			queryRequiredParams.add(UrlQueryStringParam.of("response_type","code"));
			queryRequiredParams.add(UrlQueryStringParam.of("state",state));
			queryRequiredParams.add(UrlQueryStringParam.of("prompt","login"));
			//ACR Values :
			if (acrValues != null) {
			     //With this params can select each flow, in no one is passed the FLOW SELECTOR will be selected-.
				//,  UrlQueryStringParam.of("acr_values","urn:safelayer:tws:policies:authentication:flow:bakq")
				queryRequiredParams.add(UrlQueryStringParam.of("acr_values", URLEncoder.encode(acrValues,Charset.defaultCharset())));
			}
			// Code Challenge // by default plain , its posible to send a sha , but must be specified..[todo]
			if (codeChallenge != null) {
				queryRequiredParams.add(UrlQueryStringParam.of("code_challenge",codeChallenge));
				queryRequiredParams.add(UrlQueryStringParam.of("code_challenge_method","S256"));
			}

			UrlQueryString qryStr = UrlQueryString.fromParams( queryRequiredParams);
			authorizationCodeURI = authorizationCodeURI.joinWith(qryStr);
			log.info("\n\n[Giltza authorizationCodeURI]: {}",
					 authorizationCodeURI);
			 return authorizationCodeURI;
         } catch (final Throwable th) {
			log.error("Could NOT create giltza's authorization code url: {}",
					  authorizationCodeURI,
					  th.getLocalizedMessage(),th);
			throw new AuthenticationProviderException("Could NOT create Giltza's authorization code url: " + authorizationCodeURI,
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
	public GiltzaJSonFieldAccessor fieldsAccessorFor(final String jsonAsString) {
		return new GiltzaJSonFieldAccessor(jsonAsString);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ACCESS TOKEN
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Obtains the Giltza access token through an authorization code got in the first step of Giltza oauth flow.
	 * This Giltza Access token will be used to obtain user data.
	 * @param authoritationCode
	 * @return
	 */
	public SecurityToken getGiltzaAccessTokenUsing(final String authoritationCode) {
		return getGiltzaAccessTokenUsing(authoritationCode,null);
	}


	/**
	 * Obtains the Giltza access token through an authorization code [with code verifier ]got in the first step of Giltza oauth flow.
	 * This Giltza Access token will be used to obtain user data.
	 * @param authoritationCode
	 * @return
	 */
	public SecurityToken getGiltzaAccessTokenUsing(final String authoritationCode,final String codeVerifier) {
		return getGiltzaAccessTokenUsing(authoritationCode,codeVerifier,null);
	}

	/**
	 * Obtains the Giltza access token through an authorization code [with code verifier ]got in the first step of Giltza oauth flow.
	 * This Giltza Access token will be used to obtain user data.
	 * @param authoritationCode
	 * @param codeVerifier
	 * @param redirectUri the redirect_uri to use for the token exchange; if null the configured
	 *                    'oauth2callback' property is used
	 * @return
	 */
	public SecurityToken getGiltzaAccessTokenUsing(final String authoritationCode,final String codeVerifier,final Url redirectUri) {
		log.warn(" _getGiltzaAccessTokenUsing {}",authoritationCode);
		HttpResponseResult accessTokenResponse = this.getGiltzaAccessTokenHttpResponseUsing(authoritationCode,codeVerifier,redirectUri);
		if (!accessTokenResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = accessTokenResponse.loadAsString();
			log.error("Remote Server Error int Giltza Auth Service: (code={}) > {}",
					  accessTokenResponse.getCode(),errorStream);
			throw new AuthenticationProviderException(Strings.customized("Remote Server error at Giltza Service (code={}): {}",
																   accessTokenResponse.getCode(),errorStream));
		}
		String accessTokenResponseStr = accessTokenResponse.loadAsString();
		log.info("[Giltza access token] http call response {}",
				 accessTokenResponseStr);
		String accessToken = this.fieldsAccessorFor(accessTokenResponseStr)
								 .getValueOfFieldNamed("access_token");
		log.info("[Giltza access token] = {}",
				 accessToken);
		return SecurityToken.forId(accessToken);
	}

	/**
	 * Obtains the Giltza access token through an authorization code got in the first step of Giltza oauth flow.
	 * This Giltza Access token will be used to obtain user data.
	 * @param authorizationCode
	 * @param codeVerifier
	 * @param redirectUri the redirect_uri to use for the token exchange; if null the configured
	 *                    'oauth2callback' property is used. OAuth requires this to be identical to
	 *                    the redirect_uri used when the authorization code was requested.
	 * @return
	 */
	public HttpResponseResult getGiltzaAccessTokenHttpResponseUsing(final String authorizationCode, final String codeVerifier, final Url redirectUri) {
		Url accessTokenURI = _securityProviderSecretConfig.getProperty("access-token-uri").asUrl();
		String key = _securityProviderSecretConfig.getProperty("key").asString();
		String  secret = _securityProviderSecretConfig.getProperty("secret").asString();
		Url oauth2callback =  redirectUri != null ? redirectUri
												  : _securityProviderSecretConfig.getProperty("oauth2callback").asUrl();
		try {

			// .. compose the request
			HttpRequest request = null;
			if (codeVerifier == null ) {
				request  = HttpRequest.newBuilder()
					  						 .uri(accessTokenURI.asUri())
					  						 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
					  						 .header("Authorization", "Basic " + secret)
					  						 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
												  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(key).withName("client_id"),
																	  		    	  HttpRequestFormParameterForText.of(oauth2callback.asString()).withName("redirect_uri"),
																	  		    	  HttpRequestFormParameterForText.of("authorization_code").withName("grant_type"),
																	  		    	  HttpRequestFormParameterForText.of(authorizationCode).withName("code"))

												  		      .build())
					  						 .build();
			} else {
				request  = HttpRequest.newBuilder()
				 						 .uri(accessTokenURI.asUri()) //
				 						 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
				 						 .header("Authorization", "Basic " + secret)
				 						 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
											  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(key).withName("client_id"),
																  		    	  HttpRequestFormParameterForText.of(oauth2callback.asString()).withName("redirect_uri"),
																  		    	  HttpRequestFormParameterForText.of("authorization_code").withName("grant_type"),
																  		    	  HttpRequestFormParameterForText.of(authorizationCode).withName("code"),
																  		    	  HttpRequestFormParameterForText.of(codeVerifier).withName("code_verifier")
											  		    		  )

											  		      .build())
				 						 .build();
			}
	      // .. do the request
			HttpResponse<InputStream> response =  _httpClient()
														   .send(request,
													             HttpResponse.BodyHandlers.ofInputStream());

			return new HttpResponseResult(response);
		} catch (final Throwable th) {
			log.error("Could NOT access Giltza url at {} (using proxy={}): {}",
					  accessTokenURI,
					  _securityProviderSecretConfig.isProxyEnabled(),
					  th.getLocalizedMessage(),th);
			throw new AuthenticationProviderException(Throwables.message("Could NOT access Giltza url at {} (using proxy={}): {}",
					  											accessTokenURI,
					  											_securityProviderSecretConfig.isProxyEnabled(),
					  											th.getLocalizedMessage()),
					  						th);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRINCIPAL
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Using an [access token] returned by giltza gets the user info detailed by scopes.
	 * @param accessToken
	 * @return
	 */
	public HttpResponseResult getGiltzaPrincipalDataHttpResponseUsing(final SecurityToken accessToken) {
		Url userInfoUrl = _securityProviderSecretConfig.getProperty("user-info-uri").asUrl();
		try {
			log.warn("Get [Giltza principal data] at {} (NO PROXY)",
					  userInfoUrl);
			 HttpRequest request = HttpRequest.newBuilder()
												  .uri(userInfoUrl.asUri())
												  .header("Authorization", "Bearer " + accessToken)
												  .GET()
											  .build();
			 HttpResponse<InputStream> response =  _httpClient().send(request,
														      		  HttpResponse.BodyHandlers.ofInputStream());

			return new HttpResponseResult(response);
		} catch (final Throwable th) {
			log.error("Could NOT access Giltza url at {} (using proxy={}): {}",
					  userInfoUrl,
					  _securityProviderSecretConfig.isProxyEnabled(),
					  th.getLocalizedMessage(),th);
			throw new AuthenticationProviderException(Throwables.message("Could NOT access Giltza url at {} (using proxy={}): {}",
					  													 userInfoUrl,
					  													 _securityProviderSecretConfig.isProxyEnabled(),
					  													 th.getLocalizedMessage()),
					  						    	  th);
		}
	}
	/**
	 * Using an [access token] returned by giltza gets the user .
	 * @param accessToken
	 * @return
	 */
	public SecurityPrincipalDataFromGiltza getGiltzaPrincipalDataUsing(final SecurityToken accessToken) {
		HttpResponseResult result = this.getGiltzaPrincipalDataHttpResponseUsing(accessToken);
		if (result.isSuccess()) return _from(result.loadAsString());
		
		throw new AuthenticationProviderException(Throwables.message("Could NOT access Giltza USER info, error/data {}",
					                                           		 result.loadAsString()));
	}
	/**
	 * Using an [access token] returned by goole gets the user email.
	 * @param accessToken
	 * @return
	 */
	public PersonID getGiltzaPersonIdUsing(final SecurityToken accessToken) {
		HttpResponseResult principalDataResponse = this.getGiltzaPrincipalDataHttpResponseUsing(accessToken);
		if (!principalDataResponse.getCode().isIn(HttpResponseCode.OK)) {
			String errorStream = principalDataResponse.loadAsString();
			log.warn("Remote Server Error  Giltza Auth Service  getting [principal] data with token={}: (code={}) > {}",
					 accessToken,
					 principalDataResponse.getCode(),errorStream);
			throw new AuthenticationProviderException(Strings.customized("Remote Server error at Giltza Service using access token={} (code={}): {}",
																		 accessToken,
																		 principalDataResponse.getCode(),errorStream));
		}
		String principalDataResponseStr = principalDataResponse.loadAsString();
		log.info("\n\n Giltza [principal] data http call response {} ",
				 principalDataResponseStr);
		String dniAsString = this.fieldsAccessorFor(principalDataResponseStr)
								 .getValueOfFieldNamed("dni");
		return PersonID.forId(dniAsString);
	}
	private SecurityPrincipalDataFromGiltza _from (final String userInfoResponseAsJson) {

		GiltzaJSonFieldAccessor jsonFieldAccessor = this.fieldsAccessorFor(userInfoResponseAsJson);
		EMail mail = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("email"))
							 .map(EMail::of)
							 .orElse(null);
		String name = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("given_name"))
							  .orElse(null);

		String surname1 = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("surname1"))
								 .orElse(null);
		String surname2 = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("surname2"))
								  .orElse(null);
		PersonID dni = Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("dni"))
							   .map(PersonID::forId)
							   .orElse(null);
		String displayName =  Optional.ofNullable(jsonFieldAccessor.getValueOfFieldNamed("name"))
									  .orElse(null);
		// [N] - Return the giltzaUserData
		SecurityPrincipalDataFromGiltza giltzaPrincipalData = GiltzaUserDataBuilder.forPersonId(dni)
										 							   .name(name)
										 							   .surname1(surname1)
										 							   .surname2(surname2)
										 							   .withMail(mail)
										 							   .displayName(displayName)
										 							   .build();
		return giltzaPrincipalData;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	class GiltzaJSonFieldAccessor {
		private final JsonObject _json;

		private GiltzaJSonFieldAccessor(final String jsonAsString) {
			_json = JsonParser.parseString(jsonAsString)
							  .getAsJsonObject();
		}
		public String getValueOfFieldNamed(final String name) {
			String value = null;
			try {
			  JsonElement jelement = _json.getAsJsonObject().get(name);
			  value = jelement.getAsString();
			  if (value.equals("EMPTY")) {
				  value = null;
			  }
			} catch (final Throwable nulex) {
				log.error(Strings.customized("Field named '{}' does not exist in json:\n {}",
											 name,_json));
			}
			return value;
		}
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
			log.warn(" >>> [Warn! ] Build ssl context for untrusted certs. Just for local environment  ");;
			return HttpClient.newBuilder()
			                 	.sslContext(HttpSSLContextBuilder.buildSSlContextForUnstrustedCerts())
			                 .build();
		} catch (final Throwable e) {
			throw new AuthenticationProviderException(e.getLocalizedMessage());
		}
	}
}
