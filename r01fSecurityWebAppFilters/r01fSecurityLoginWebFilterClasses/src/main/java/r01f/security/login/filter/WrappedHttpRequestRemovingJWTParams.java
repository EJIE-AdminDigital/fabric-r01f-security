package r01f.security.login.filter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.base.Joiner;

import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

/**
 * A HttpServletRequest that removes some URL params like the JWT token
 */
 public class WrappedHttpRequestRemovingJWTParams
 	  extends HttpServletRequestWrapper {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final Map<String,String[]> _parameterMap;
//	private final Url _originalURL;
	private final Url _wrappedURL;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public WrappedHttpRequestRemovingJWTParams(final HttpServletRequest request,
						  					   final String ... paramsToBeRemoved) {
		super(request);
		_parameterMap = new HashMap<String, String[]>();

		Url originalURL = Url.from(request.getRequestURI());
		Url wrappedURL = Url.from(request.getRequestURI());
		for (Iterator<String> it = request.getParameterMap().keySet().iterator(); it.hasNext(); ) {
			String namedParameter = it.next();
			String[] valueParameter = request.getParameterMap().get(namedParameter);

			UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of(namedParameter,
																					 _decode(valueParameter)));
			originalURL = originalURL.joinWith(qryStr);

			// add parameters OTHER THAN the JWT_PARM or the params to be removed
			if (!namedParameter.equalsIgnoreCase(AfterLoginJWTStore.JWT_PARAM)
			 && !ArrayUtils.contains(paramsToBeRemoved,namedParameter)) {
				_parameterMap.put(namedParameter,valueParameter);
				wrappedURL = wrappedURL.joinWith(qryStr);
			}
		}
		// _originalURL = originalURL;
		_wrappedURL = wrappedURL;
	}
	public WrappedHttpRequestRemovingJWTParams(final HttpServletRequest request) {
		this(request,new String[0]);
	}
	public static WrappedHttpRequestRemovingJWTParams from(final HttpServletRequest request) {
		return new WrappedHttpRequestRemovingJWTParams(request);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Map<String, String[]> getParameterMap() {
		return _parameterMap;
	}
	@Override
	public String getRequestURI() {
		return _wrappedURL.toString();
	}
	private static String _decode(final String[] strOr) {
		return Joiner.on(",").skipNulls().join(strOr);
	}
}
