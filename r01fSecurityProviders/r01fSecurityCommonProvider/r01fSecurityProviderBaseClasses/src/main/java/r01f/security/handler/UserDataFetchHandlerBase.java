package r01f.security.handler;

import jakarta.inject.Provider;

import com.google.common.reflect.TypeToken;

import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.jwt.JWTDecoded;
import r01f.model.security.login.Login;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.securitycontext.SecurityContext;
import r01f.types.StringBase64;

public abstract class UserDataFetchHandlerBase<U extends User,L extends Login> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Class<U> _userType;
	protected final Class<L> _loginType;
	protected final Provider<SecurityContext> _securityContextProvider;
	protected final SecurityProviderSecretConfig _securityProviderSecretConfig;
	protected final CRUDServicesForUser<U> _userCrud;
	protected final CRUDServicesForLogin<L> _loginCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public UserDataFetchHandlerBase(final Class<U> userType,final Class<L> loginType,
									final Provider<SecurityContext> securityContextProvider,
									final CRUDServicesForUser<U> userCrud,final CRUDServicesForLogin<L> loginCrud,
									final SecurityProviderSecretConfig securityProviderSecretConfig) {
		_userType = userType;
		_loginType = loginType;
		_securityContextProvider = securityContextProvider;
		_userCrud = userCrud;
		_loginCrud = loginCrud;
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Uses the [security provider]-specific api to get the [user data]
	 * @param jwtDecoded
	 * @param base64SecurityProviderSession
	 * @return
	 */
	public abstract SecurityOperationResult<U> fetchUser(final JWTDecoded jwtDecoded,
														 final StringBase64 base64SecurityProviderSession);

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForUser<U>> C userCRUDAs(final TypeToken<C> crudType) {
		return (C)_userCrud;
	}
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForUser<U>> C userCRUDAs(final Class<C> crudType) {
		return (C)_userCrud;
	}
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForLogin<L>> C loginCRUDAs(final TypeToken<C> crudType) {
		return (C)_loginCrud;
	}
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForLogin<L>> C loginCRUDAs(final Class<C> crudType) {
		return (C)_loginCrud;
	}
}
