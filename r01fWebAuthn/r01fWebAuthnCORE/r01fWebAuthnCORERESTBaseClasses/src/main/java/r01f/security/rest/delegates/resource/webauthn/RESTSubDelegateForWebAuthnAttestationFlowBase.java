package r01f.security.rest.delegates.resource.webauthn;

import com.yubico.webauthn.FinishRegistrationOptions;
import com.yubico.webauthn.RegistrationResult;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartRegistrationOptions;
import com.yubico.webauthn.data.AuthenticatorAttestationResponse;
import com.yubico.webauthn.data.ClientRegistrationExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;
import com.yubico.webauthn.data.PublicKeyCredentialCreationOptions;
import com.yubico.webauthn.data.UserIdentity;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.entry.LoginEntry;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.security.handler.webauthn.utils.WebAuthnByteStreamUtils;
import r01f.types.StringBase64;

@Slf4j
public class RESTSubDelegateForWebAuthnAttestationFlowBase<P extends IsSecurityPrincipal,C extends AuthenticatorCredential>
     extends RESTSubDelegateForWebAuthnAbstractBase<P,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTSubDelegateForWebAuthnAttestationFlowBase(final Provider<SecurityContext> sysSecurityContextProvider,
		                                                 final Marshaller marshaller,
		                                                 final WebAuthnConfig webAuthnConfig,
		                                                 final RelyingParty relyingParty,
		                                                 final CRUDServicesForPrincipal<P> principalCrud,
		                                                 final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
		                                                 final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
		                                                 final CredentialFactoryFromRegistrationResponse<P,C> credentialFactoyFromRegistrationResponse) {
		super(sysSecurityContextProvider,
			  marshaller,
			  webAuthnConfig, relyingParty,
			  principalCrud,
			  loginCrud,loginEntryCrud,
			  authenticatorCredentialCrud,credentialFactoyFromRegistrationResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  START WEBAUTHN REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthnRegistration(final LoginID  loginId) {
		log.warn("#######################################################################");
	    log.warn(" ////////////////////   START REGISTRATION               /////////////");
	    log.warn("#######################################################################");
	    
		// [1].. Load user from db based on last login of LoginID
		LoginEntry entry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
											                     null, // any
												                 //  WARNING!! Suppose "username" is LogiID in any IdProvider
												                 //  --------------------------------------------------------------
											                     loginId,
												                 //  WARNING!! Supposethat with that "username"/LogiID  has logged any moment
												                 //  -------------------------------------------------------------------------
												                 LoginEntryEvent.LOGIN_OK)
										   .getOrNull();
		
		P principal = _principalCrud.load(_sysSecurityContextProvider.get(),
				                 		  entry.getSecurityPrincipalOid())
					       .getOrThrow();
		// [2].. Create user identity
		UserIdentity userIdentity = UserIdentity.builder()
					         					.name(loginId.asString())
					         					.displayName(principal.getDisplayName())
					        					.id(WebAuthnByteStreamUtils.from(principal.getOid()))
				        .build();

		StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
																                 .user(userIdentity)
																               .build();

	    PublicKeyCredentialCreationOptions publicKeyCredentialCreateOptions = _relyingParty.startRegistration(registrationOptions);
		String publicKeyCredentialCreateOptionsAsJSON  =  null;
	    try {
	    	publicKeyCredentialCreateOptionsAsJSON  = publicKeyCredentialCreateOptions.toJson();
	    	log.warn(" registration request :\n {}" ,
	    			             publicKeyCredentialCreateOptionsAsJSON);
            log.warn(" registration credential request :\n {}" ,
            		          publicKeyCredentialCreateOptions.toCredentialsCreateJson());
		} catch (final Throwable e) {
			e.printStackTrace();
			return Response.serverError()
								.entity(e.getLocalizedMessage())
							.build();
		}
	    return Response.ok(publicKeyCredentialCreateOptionsAsJSON, MediaType.APPLICATION_JSON)
	    		 	   .build();

	}
/////////////////////////////////////////////////////////////////////////////////////////
// FINISH WEBAUTHN REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response finishWebAuthnRegistration(final LoginID loginId,
			                                   final String credNameAsString,
			                                   final String publicKeyCredentialRegistrationResponseAsString,
			                                   final String publicKeyCredentialCreateOptionsAsJSON) {
		try {
			log.warn("#######################################################################");
		    log.warn(" ////////////////////   FINISH REGISTRATION               /////////////");
		    log.warn("#######################################################################");
		    log.warn(" ..\n publicKeyCredentialCreateOptionsAsJSON :\n {}",
		    		 publicKeyCredentialCreateOptionsAsJSON);
		    log.warn(" ..\n publicKeyCreddentialRegistrationResponseAsString :\n {}",
		    		 publicKeyCredentialRegistrationResponseAsString);


			log.warn("\n\n..finish webauth registration for user with login-id {}",loginId.asString());
			// [1].. Load the las logged entry
			LoginEntry entry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
												                     null, // any identity provider
													                 //  WARNING!! Suppose "username" is LogiID in any [identity provider]
													                 //  --------------------------------------------------------------
												                     loginId,
													                 //  WARNING!! Suppose that with that "username"/LoginID  has logged any moment
													                 //  -------------------------------------------------------------------------
													                 LoginEntryEvent.LOGIN_OK)
									  		  .getOrNull();
			SecurityPrincipalOID principalOid = entry.getSecurityPrincipalOid();

			// [2].. Load the user
			P principal = _principalCrud.load(_sysSecurityContextProvider.get(),
					                 		  principalOid)
						       			.getOrThrow();
			IdentityProvider backMappedSecurityProviderOnRegistration = principal.getSourceIdentityProvider();

			// [3].. Build registration request & registration response
		    PublicKeyCredential<AuthenticatorAttestationResponse,
		    					ClientRegistrationExtensionOutputs> registrationResponse = PublicKeyCredential.parseRegistrationResponseJson(publicKeyCredentialRegistrationResponseAsString);

	        PublicKeyCredentialCreationOptions requestOptions = PublicKeyCredentialCreationOptions.fromJson(publicKeyCredentialCreateOptionsAsJSON);
			FinishRegistrationOptions options = FinishRegistrationOptions.builder()
															             .request(requestOptions)
															             .response(registrationResponse)
																         .build();
			// [4].. finish registration
	        RegistrationResult result = _relyingParty.finishRegistration(options);

	    	// [5].. store credentials (pk) at db
	        StringBase64 credentialIdAsBAse64 = StringBase64.fromBase64EncodedString(result.getKeyId().getId().getBase64());
	        StringBase64 publicKeyAsBase64 = StringBase64.fromBase64EncodedString(result.getPublicKeyCose().getBase64());;
	       // this.aaguid = attestationData.get().getAaguid();

	        Long count = result.getSignatureCount();
	        C registredCredentials = _credentialFactoyFromRegistrationResponse.from(principal.getOid(),
	        		                                                                credentialIdAsBAse64,
	        		                                                                publicKeyAsBase64,
	        		                                                                count,
	        		                                                                backMappedSecurityProviderOnRegistration);
	        _authenticatorCredentialCrud.create(_sysSecurityContextProvider.get(),
	        	                             	registredCredentials);

	        log.warn(" \n [o][o]Finish registration ...........");
	        return Response.ok()
	    		 	       .build();
		} catch (final Throwable e) {
			e.printStackTrace();
			return Response.serverError()
								.entity(e.getLocalizedMessage())
							.build();
		}
	}
}
