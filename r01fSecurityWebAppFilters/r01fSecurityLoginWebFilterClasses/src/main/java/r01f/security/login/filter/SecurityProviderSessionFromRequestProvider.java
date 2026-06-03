package r01f.security.login.filter;

import jakarta.servlet.http.HttpServletRequest;

import r01f.security.login.LoginSession;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.types.StringBase64;

/**
 * After a user is logged-in, it's redirected to the [Security REST Services WAR] where a [JWT Token] is created and then
 * the [user] is redirected to the [UI]
 * At the [UI] the request passes through the {@link LoginSessionServletFilter} that tries to create the {@link LoginSession}
 * ... to do so, the [login session servlet filter] calls back the [Security REST Services WAR] to get the user data
 * <pre class='brush:java'>
 * 		U user = _userDataFromJWT.getUserDataFromJWT(jwt);  <<<< issues an HTTP request to the [Security REST Services WAR] to get the [user data]
 * 																 ... the HTTP request contains the [JWT Token] so the [security REST services WAR]
 * 																	 can be sure the request is a legitimate one
 * </pre>
 * In order to get the [user data], the [security REST services WAR] uses some data encoded at the [JWT token]
 * ... BUT sometimes additional data is needed, and that data it's NOT encoded at the [JWT token]!!
 *
 * This provider gives the filter an opportunity to get this additional data
 */
public interface SecurityProviderSessionFromRequestProvider {
	/**
	 * Returns the [security provider]-specific [login session]
	 * ie, if the [security provider] is XLNets, this method can call the XLNets API and get
	 * 	   the user's session xml document like:
	 * 			Document userSessionDoc = xlnetsApi.getUserSession(request);
	 * @param <S>
	 * @param request
	 * @param providerId
	 * @return
	 */
	public <S> S getLoginSession(final HttpServletRequest request,
								 final SecurityProviderID providerId);
	/**
	 * Serializes the [security provider]-specific [login session] to a [base64] string
	 * that can be sent to the [security REST services war] where it can be de-serialized
	 * and used to query the [security provide]
	 * @param <S>
	 * @param session
	 * @param providerId
	 * @return
	 */
	public <S> StringBase64 base64SerializeLoginSession(final S session,
													 	final SecurityProviderID providerId);
}
