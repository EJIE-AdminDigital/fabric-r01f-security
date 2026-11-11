package r01f.security.authn.handler;

import com.google.common.reflect.TypeToken;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.jwt.JWTDecoded;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.operation.IsSecurityOperationResult;
import r01f.model.security.principal.SecurityPrincipalAndLogin;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.types.StringBase64;

public abstract class SecurityPrincipalDataFetchHandlerBase<P extends IsSecurityPrincipal> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	protected final Class<P> _principalType;
	protected final Provider<SecurityContext> _securityContextProvider;
	protected final AuthenticationProviderSecretConfig _securityProviderSecretConfig;
	protected final CRUDServicesForPrincipal<P> _principalCrud;
	protected final CRUDServicesForLogin _loginCrud;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalDataFetchHandlerBase(final Class<P> principalType,
												 final Provider<SecurityContext> securityContextProvider,
												 final CRUDServicesForPrincipal<P> principalCrud,final CRUDServicesForLogin loginCrud,
												 final AuthenticationProviderSecretConfig securityProviderSecretConfig) {
		_principalType = principalType;
		_securityContextProvider = securityContextProvider;
		_principalCrud = principalCrud;
		_loginCrud = loginCrud;
		_securityProviderSecretConfig = securityProviderSecretConfig;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Uses the [identity provider]-specific api to get the [security principal] data 
	 * @param jwtDecoded
	 * @param base64SecurityProviderSession
	 * @return
	 */
	public abstract IsSecurityOperationResult<SecurityPrincipalAndLogin<P>> fetchPrincipalAndLogin(final JWTDecoded jwtDecoded,
														   								   		   final StringBase64 base64SecurityProviderSession);

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForPrincipal<P>> C principalCRUDAs(final TypeToken<C> crudType) {
		return (C)_principalCrud;
	}
	@SuppressWarnings("unchecked")
	public <C extends CRUDServicesForPrincipal<P>> C principalCRUDAs(final Class<C> crudType) {
		return (C)_principalCrud;
	}
}
