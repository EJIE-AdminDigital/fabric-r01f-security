package r01f.security.login;

import jakarta.servlet.http.HttpServletRequest;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryStringParam;

@Slf4j
public class AfterLoginRedirUrlExtraInfoStore {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final String WEB_REQUEST_PARAM_NAME = "toeib64";

/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public static StringBase64 getFromRequest(final HttpServletRequest request) {
		StringBase64 toeib64 = (request.getParameter(WEB_REQUEST_PARAM_NAME) != null) ? StringBase64.fromBase64EncodedString(request.getParameter(WEB_REQUEST_PARAM_NAME))
																				   	  : null;
		log.trace("Get [after login url] extra info (base64) from request param: {}",
				  toeib64 != null ? toeib64 : "nothing found");
		return toeib64;
	}
	public static UrlQueryStringParam asUrlQueryStringParam(final StringBase64 eib64) {
		return UrlQueryStringParam.of(AfterLoginRedirUrlExtraInfoStore.WEB_REQUEST_PARAM_NAME,eib64);
	}
	public static StringBase64 getFromUrl(final Url url) {
		if (url == null) return null;
		return url.getQueryStringParamValue(AfterLoginRedirUrlExtraInfoStore.WEB_REQUEST_PARAM_NAME)
				  .asStringBase64()
				  .orNull();
	}
	public static Url removeAsUrlQueryStringParamFrom(final Url url) {
		return url != null && url.getQueryString() != null ? url.replaceQueryString(// replace the existing query string by a new query string
												    								url.getQueryString()
												    								   .withoutParam(AfterLoginRedirUrlExtraInfoStore.WEB_REQUEST_PARAM_NAME))
														   : url;
	}
	public static AfterLoginRedirUrlExtraInfoStoreAddAsParam addAsUrlQueryStringParam(final StringBase64 extraInffo) {
		return new AfterLoginRedirUrlExtraInfoStoreAddAsParam(extraInffo);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class AfterLoginRedirUrlExtraInfoStoreAddAsParam {
		private final StringBase64 _eib64;

		public Url to(final Url url) {
			return url.joinWith(UrlQueryStringParam.of(AfterLoginRedirUrlExtraInfoStore.WEB_REQUEST_PARAM_NAME,
													   _eib64));
		}
	}
}
