package r01f.security.rest.delegates.resource.webauthn;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import lombok.RequiredArgsConstructor;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.securitycontext.SecurityContext;


@RequiredArgsConstructor
public abstract class RESTSubDelegateForWebAuthnAbstractBase<U extends User,
											                 E extends UserLoginEntry, C extends AuthenticatorCredential> {
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

	// user  CRUD
	protected final CRUDServicesForUser<U> _userCrud;

	// user login CRUD & factory
	protected final CRUDServicesForUserLoginEntry<E> _loginEntryCrud;
	protected final Factory<E> _loginEntryFactory;

	// credentials CRUD & factory
	protected final CRUDServicesForAuthenticatorCredential<C>        _authenticatorCredentialCrud;
	protected final CredentialFactoryFromRegistrationResponse<U,C>   _credentialFactoyFromRegistrationResponse;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
