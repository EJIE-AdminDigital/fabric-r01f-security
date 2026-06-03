package r01f.security.login.filter;

import r01f.model.security.login.Login;
import r01f.model.security.user.User;

public interface ApiKeyJWTAuthenticationSubscriber<U extends User,L extends Login> {

	public void onSuccess(final U user,final L login);
	public void onError(final ApiKeyJWTAuthenticationError errorType,
						final String errDetail);
}