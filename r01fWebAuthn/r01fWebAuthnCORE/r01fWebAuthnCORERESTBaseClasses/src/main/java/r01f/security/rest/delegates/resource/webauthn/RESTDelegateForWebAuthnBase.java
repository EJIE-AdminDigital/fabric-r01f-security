package r01f.security.rest.delegates.resource.webauthn;

import com.yubico.webauthn.RelyingParty;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.jwt.JWT;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.rest.resources.delegates.RESTDelegate;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;

@RequiredArgsConstructor
public abstract class RESTDelegateForWebAuthnBase<P extends IsSecurityPrincipal,C extends AuthenticatorCredential>
		   implements RESTDelegate {
/////////////////////////////////////////////////////////////////////////////////////////
//	SUB-DELEGATE
/////////////////////////////////////////////////////////////////////////////////////////
	final RESTSubDelegateForWebAuthnAssertionFlowBase<P,C> _subDelegateForAssertionFlow;
	final RESTSubDelegateForWebAuthnAttestationFlowBase<P,C> _subDelegateForAttestationFlow;
	final RESTSubDelegateForWebAuthnLoginFlowBase<P,C> _subDelegateForLoginFlow;
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTDelegateForWebAuthnBase(final Provider<SecurityContext> sysSecurityContextProvider,
						               final Marshaller marshaller,
						               final WebAuthnConfig webAuthnConfig,
						               final RelyingParty relyingParty,
						               final CRUDServicesForPrincipal<P> principalCrud ,
						               final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
						               final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
						               final CredentialFactoryFromRegistrationResponse<P,C> credentialFactoyFromRegistrationResponse) {

		_subDelegateForAssertionFlow =    new RESTSubDelegateForWebAuthnAssertionFlowBase<>(sysSecurityContextProvider, 
																							marshaller, 
																							webAuthnConfig,
																							relyingParty, 
																							principalCrud, 
																							loginCrud,loginEntryCrud, 
																							authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
		_subDelegateForAttestationFlow  = new  RESTSubDelegateForWebAuthnAttestationFlowBase<>(sysSecurityContextProvider, 
																							   marshaller,  
																							   webAuthnConfig,
																							   relyingParty, 
																							   principalCrud, 
																							   loginCrud,loginEntryCrud,
																							   authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
		_subDelegateForLoginFlow  = new  RESTSubDelegateForWebAuthnLoginFlowBase<>(sysSecurityContextProvider,
																				   marshaller, 
																				   webAuthnConfig,
																				   relyingParty, 
																				   principalCrud, 
																				   loginCrud,loginEntryCrud,
																				   authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//   WEBAUTHN REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthnRegistration(final LoginID loginId) {
		return _subDelegateForAttestationFlow.startWebAuthnRegistration(loginId);

	}

	public Response finishWebAuthnRegistration(final LoginID loginId,
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
