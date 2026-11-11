	package r01f.security.login.filter;

import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.login.Login;

public interface ApiKeyJWTAuthenticationSubscriber<P extends IsSecurityPrincipal> {

	public void onSuccess(final P principal,final Login login);
	public void onError(final ApiKeyJWTAuthenticationError errorType,
						final String errDetail);
}