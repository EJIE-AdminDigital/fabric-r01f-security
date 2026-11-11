package r01f.security.rest.delegates.resource.webauthn;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;


@RequiredArgsConstructor
public abstract class RESTSubDelegateForWebAuthnAbstractBase<U extends IsSecurityPrincipal,C extends AuthenticatorCredential> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	// system-internal security context provider (used to query the CRUD services)
	protected final Provider<SecurityContext> _sysSecurityContextProvider;

	// marshaller
	protected final Marshaller _marshaller;

	protected final WebAuthnConfig _webAuthnConfig;

	// relyingParty  ( build at bootstraping)
	protected final RelyingParty _relyingParty;

	// principal  CRUD
	protected final CRUDServicesForPrincipal<U> _principalCrud;

	// login CRUD & factory
	protected final CRUDServicesForLogin _loginCrud;
	protected final CRUDServicesForLoginEntry _loginEntryCrud;

	// credentials CRUD & factory
	protected final CRUDServicesForAuthenticatorCredential<C> _authenticatorCredentialCrud;
	protected final CredentialFactoryFromRegistrationResponse<U,C> _credentialFactoyFromRegistrationResponse;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
