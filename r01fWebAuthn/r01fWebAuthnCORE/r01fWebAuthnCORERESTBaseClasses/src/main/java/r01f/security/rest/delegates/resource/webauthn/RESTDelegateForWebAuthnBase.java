package r01f.security.rest.delegates.resource.webauthn;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.facets.HasLangIndependentDisplayName;
import r01f.jwt.JWT;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;

@RequiredArgsConstructor
public abstract class RESTDelegateForWebAuthnBase<U extends User & HasLangIndependentDisplayName,
											      E extends UserLoginEntry, C extends AuthenticatorCredential>
		   implements RESTDelegate {
/////////////////////////////////////////////////////////////////////////////////////////
//	SUB-DELEGATE
/////////////////////////////////////////////////////////////////////////////////////////
	final RESTSubDelegateForWebAuthnAssertionFlowBase<U,E,C> _subDelegateForAssertionFlow;
	final RESTSubDelegateForWebAuthnAttestationFlowBase<U,E,C> _subDelegateForAttestationFlow;
	final RESTSubDelegateForWebAuthnLoginFlowBase<U,E,C> _subDelegateForLoginFlow;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTDelegateForWebAuthnBase(final Provider<SecurityContext> sysSecurityContextProvider,
						               final Marshaller marshaller,
						               final WebAuthnConfig webAuthnConfig,
						               final RelyingParty relyingParty,
						               final CRUDServicesForUser<U> userCrud ,
						               final CRUDServicesForUserLoginEntry<E> loginEntryCrud, final Factory<E> loginEntryFactory,
						               final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
						               final CredentialFactoryFromRegistrationResponse<U,C> credentialFactoyFromRegistrationResponse) {

		_subDelegateForAssertionFlow =    new RESTSubDelegateForWebAuthnAssertionFlowBase<>(sysSecurityContextProvider, 
																							marshaller, 
																							webAuthnConfig,
																							relyingParty, 
																							userCrud, 
																							loginEntryCrud,loginEntryFactory, 
																							authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
		_subDelegateForAttestationFlow  = new  RESTSubDelegateForWebAuthnAttestationFlowBase<>(sysSecurityContextProvider, 
																							   marshaller,  
																							   webAuthnConfig,
																							   relyingParty, 
																							   userCrud, 
																							   loginEntryCrud,loginEntryFactory,
																							   authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
		_subDelegateForLoginFlow  = new  RESTSubDelegateForWebAuthnLoginFlowBase<>(sysSecurityContextProvider,
																				   marshaller, 
																				   webAuthnConfig,
																				   relyingParty, 
																				   userCrud, 
																				   loginEntryCrud,loginEntryFactory,
																				   authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//   WEBAUTHN REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthnRegistration(final LoginID  loginId) {
		return _subDelegateForAttestationFlow.startWebAuthnRegistration(loginId);

	}

	public Response finishWebAuthnRegistration(final LoginID  loginId,
			                                   final String credNameAsString,
			                                   final String publicKeyCredentialRegistrationResponseAsString,
			                                   final String publicKeyCredentialCreateOptionsAsJSON) {
		return _subDelegateForAttestationFlow.finishWebAuthnRegistration(loginId,
				                                                         credNameAsString,
				                                                         publicKeyCredentialRegistrationResponseAsString,publicKeyCredentialCreateOptionsAsJSON);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//  WEBAUTHN AUTHENTICATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthAuthentication(final LoginID loginId) {
		return _subDelegateForAssertionFlow.startWebAuthAuthentication(loginId);
	}

	public Response finishWebAuthAuthentication(final String publicKeyCredentialAssertResponseAsString,
                                                final String publicKeyCredentialAssertRequestAsJSON,
                                                final String afterAssertionRedir) {
		return _subDelegateForAssertionFlow.finishWebAuthAuthentication(publicKeyCredentialAssertResponseAsString,
				                                                        publicKeyCredentialAssertRequestAsJSON,
				                                                        afterAssertionRedir);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  WEBAUTHN LOGIN POST-ASSERTION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response fetchUser(final JWT jwt) {
		// TODO Auto-generated method stub
		return _subDelegateForLoginFlow.fetchUser(jwt);
	}

}
