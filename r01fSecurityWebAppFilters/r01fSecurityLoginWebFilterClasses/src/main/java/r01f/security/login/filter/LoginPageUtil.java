package r01f.security.login.filter;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.patterns.ProviderFrom;
import r01f.security.provider.SecurityProviderIdCookieStore;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.servlet.HttpServletRequestUtils;
import r01f.types.url.Host;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;
import r01f.types.url.UrlProtocol;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class LoginPageUtil {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static boolean isLogout(final HttpServletRequest request) {
		String webAppContext = request.getContextPath();		// {webApp}
		String requestedUrlPathStr = request.getRequestURI();	// {webApp}/{urlPath}
		return Strings.isNOTNullOrEmpty(requestedUrlPathStr)
			&& requestedUrlPathStr.startsWith(UrlPath.from(webAppContext,"logout").asAbsoluteString());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static LoginPageUtilUrlToStep forSecurityProviderId(final SecurityProviderID securityProviderId) {
		return new LoginPageUtilUrlToStep(req -> securityProviderId);
	}
	public static LoginPageUtilUrlToStep forSecurityProviderIdStoredAt(final SecurityProviderIdCookieStore securityProviderIdStore) {
		return new LoginPageUtilUrlToStep(req -> securityProviderIdStore.getProviderIdFrom(req));
	}
	public static LoginPageUtilUrlToStep forSecurityProviderId(final ProviderFrom<HttpServletRequest,SecurityProviderID> provider) {
		return new LoginPageUtilUrlToStep(provider);
	}

	@RequiredArgsConstructor
	public static class LoginPageUtilUrlToStep {
		private final ProviderFrom<HttpServletRequest,SecurityProviderID> _securityProviderId;

		public LoginPageUtilUrlStep usingDefaultUrlToProvider() {
			return new LoginPageUtilUrlStep(_securityProviderId,
											request -> _defaultUrlToFor(request));
		}
		public LoginPageUtilUrlStep usingUrlToProvider(final ProviderFrom<HttpServletRequest,Url> urlToProvider) {
			return new LoginPageUtilUrlStep(_securityProviderId,
											urlToProvider);
		}
	}
	@RequiredArgsConstructor
	public static class LoginPageUtilUrlStep {
		private final ProviderFrom<HttpServletRequest,SecurityProviderID> _securityProviderId;
		private final ProviderFrom<HttpServletRequest,Url> _urlToFromRequestProvider;

		public Url loginPageUrl(final SecurityLoginFilterConfig loginFilterConfig,
								final HttpServletRequest request) {
			Url urlToRedir = null;

			// b1) The [user] is NOT authenticated (there's no [login session]), BUT there exists a [service provider cookie] that
			//	   indicates that the [user] might be authenticated in another [webApp]
			//	   ... so the [user] is REDIRECTED to the /login [security REST service] endpoint
			boolean isLogout = LoginPageUtil.isLogout(request);

			SecurityProviderID securityProviderId = _securityProviderId.from(request);
			if (!isLogout
			 && securityProviderId != null
			 && securityProviderId.isNOT(SecurityProviderID.USER_PASSWORD)) {
				log.trace("[login filter] > user already logged-in using provider={} > redir the user to the [security provider]'s login page",
										  securityProviderId);

				// compose the [security REST services] login endpoint url
				// BEWARE! This url is a [security REST services] endpoint url like: https://host/xxxCORESecurityRESTServicesWar/api/google/users/login?to={urlTo}
				//		   This is NOT the [security provider]'s login url (ie google login url is like: http://site/app/google/users/login?to={urlTo})
				//		   ... the [security REST services] login endpoint will in-turn redir to the [security provider]'s login url (ie the google or xlnets one)
				Url securityRESTServicesLoginEndPointForProvider = loginFilterConfig.getSecurityRESTServicesLoginEndPointForProviderWithId(securityProviderId);

				// the target url (the url to which the user will be redirected to after being logged-in)
				Url urlTo = _defaultUrlToFor(request);

				// the final url
				urlToRedir = securityRESTServicesLoginEndPointForProvider.joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("to",urlTo)));
			}
			// b2) the user has NOT been authenticated anywhere
			//	   (there is neither an [login session] stored at the [http session], either a [JWT token] at the [http request])
			else  {
				// the login page (ui)
				Url loginUIUrl = _loginPageUrl(loginFilterConfig,
											   _urlToFromRequestProvider,
											   request);
				urlToRedir = loginUIUrl;
			}
			return urlToRedir;
		}
	}
	private static Url _loginPageUrl(final SecurityLoginFilterConfig loginFilterConfig,
									 final ProviderFrom<HttpServletRequest,Url> urlToProvider,
								   	 final HttpServletRequest req) {
		boolean logEnabled = log.isDebugEnabled();

		Url finalLoginUrl = null;

		// try to get the login url from the config
		// the login url might contain variables
		Url loginUrl = loginFilterConfig.getAppLoginUrl();
		if (logEnabled) log.debug("\t - Configured [login url]: {} " ,
				 				  loginUrl);

		if (loginUrl == null) {
			if (logEnabled) log.debug("\t * NO [login url] configured > try to guess it" ,
					  				  loginUrl);
			// ... if the config does NOT contains a login url,
			//	   try to guess it > something like {servletContext}/login
			UrlProtocol protocol = UrlProtocol.from(req.getProtocol());
			Host host = HttpServletRequestUtils.clientRequestedHost(req);
			int port = req.getLocalPort();
			UrlPath webAppPath = new UrlPath(true,		// preserve trailing slash
											 req.getContextPath());
			loginUrl = Url.from(protocol,host,port,
								webAppPath.joinedWith("login"));
			if (logEnabled) log.debug("\t...the [login url] will be: {} ",
					  				  loginUrl);
		}
		// add a query string like: to={url being accessed}
		Url urlTo = urlToProvider.from(req);

		Map<String,String> params = new HashMap<>();
		params.put("to",urlTo.asStringUrlEncodingQueryStringParamsValues());

		UrlQueryString qryStr = UrlQueryString.from(params);
		finalLoginUrl = loginUrl.joinWith(qryStr);

		if (logEnabled) log.debug("\t - Final [login url]={}",
				  				  urlTo);
		return finalLoginUrl;
	}
	private static Url _defaultUrlToFor(final HttpServletRequest req) {
		if (log.isDebugEnabled()) log.debug(HttpServletRequestUtils.printHeaders(req)); // print headers

		String reqUrlStr = HttpServletRequestUtils.clientRequestedHost(req)	// BEWARE!! this method takes proxies into account in order to get the REAL client url
												  .asUrl()
												  .joinWith(UrlPath.from(req.getRequestURI()))		// {webApp}/{urlPath}	(does NOT include query string)
												  .toString();
		reqUrlStr = reqUrlStr.endsWith("/logout/") ? reqUrlStr.replaceAll("/logout/","/") : reqUrlStr;
		reqUrlStr = reqUrlStr.endsWith("/logout") ? reqUrlStr.replaceAll("/logout","/") : reqUrlStr;

		Url urlTo = Url.from(reqUrlStr);

// TODO maybe POST form params should be "translated" to GET query string params??? (if so use the commented code below)
		// add the query params
		if (Strings.isNOTNullOrEmpty(req.getQueryString())) {
			UrlQueryString qryStrUrlTo = UrlQueryString.fromParamsString(req.getQueryString());
			urlTo = urlTo.joinWith(qryStrUrlTo);
		}
		// add both the query string params & post-form params as query params
		// (post params are "transformed" to query string params)
// 		Map<String,String[]> params = req.getParameterMap();	// <-- this returns both the post params and query string params
//		Collection<UrlQueryStringParam> qryParams = CollectionUtils.hasData(params)
//														? params.entrySet()
//															 	.stream()
//															 	// do not allow multiple param values
//															 	.filter(me -> me.getValue() != null
//															 		       && me.getValue().length == 1)
//															 	.map(me -> UrlQueryStringParam.of(me.getKey(),me.getValue()[0]))
//															 	.collect(Collectors.toList())
//														: null;
//		UrlQueryString qryStrUrlTo = UrlQueryString.fromParams(qryParams);
//		urlTo = urlTo.joinWith(qryStrUrlTo);
		log.info("Final urlTo: {}",
				 urlTo.asString());
		return urlTo;
	}
}
