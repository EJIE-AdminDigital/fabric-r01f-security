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
import r01f.api.interfaces.security.login.entry.CRUDServicesForUserLoginEntry;
import r01f.api.interfaces.security.user.CRUDServicesForUser;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.facets.HasLangIndependentDisplayName;
import r01f.model.security.login.entry.UserLoginEntry;
import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.patterns.Factory;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.security.handler.webauthn.utils.WebAuthnByteStreamUtils;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityIDS.UserLoginEntryID;
import r01f.types.StringBase64;

@Slf4j
public class RESTSubDelegateForWebAuthnAttestationFlowBase<U extends User & HasLangIndependentDisplayName,
											               E extends UserLoginEntry, C extends AuthenticatorCredential>
		   extends RESTSubDelegateForWebAuthnAbstractBase<U,E,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTSubDelegateForWebAuthnAttestationFlowBase(final Provider<SecurityContext> sysSecurityContextProvider,
		                                                 final Marshaller marshaller,
		                                                 final WebAuthnConfig webAuthnConfig,
		                                                 final RelyingParty relyingParty,
		                                                 final CRUDServicesForUser<U> userCrud ,
		                                                 final CRUDServicesForUserLoginEntry<E> loginEntryCrud, final Factory<E> loginEntryFactory,
		                                                 final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,
		                                                 final CredentialFactoryFromRegistrationResponse<U, C> credentialFactoyFromRegistrationResponse) {
		super(sysSecurityContextProvider,
			  marshaller,
			  webAuthnConfig, relyingParty,
			  userCrud,
			  loginEntryCrud,loginEntryFactory,
			  authenticatorCredentialCrud, credentialFactoyFromRegistrationResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  START WEBAUTHN REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthnRegistration(final LoginID  loginId) {
		log.warn("#######################################################################");
	    log.warn(" ////////////////////   START REGISTRATION               /////////////");
	    log.warn("#######################################################################");
		//[1].. Load user from db based on last login of LoginID
		E  entry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
									                     null, // any
										                //  WARNING!! Suppose "username" is LogiID in any IdProvider
										                //  --------------------------------------------------------------
									                     loginId,
										                //  WARNING!! Supposethat with that "username"/LogiID  has logged any moment
										                //  -------------------------------------------------------------------------
										                UserLoginEntryID.forLoginOk()).getOrNull();

		U user = _userCrud.load(_sysSecurityContextProvider.get(),
				                 entry.getUserOid())
					       .getOrThrow();
		//[2].. Create user identity
		UserIdentity userIdentity = UserIdentity.builder()
					         					.name(loginId.asString())
					         					.displayName(user.getDisplayName())
					        					.id(WebAuthnByteStreamUtils.from(user.getOid()))
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
	public Response finishWebAuthnRegistration(final LoginID  loginId,
			                                   final String credNameAsString,
			                                   final String publicKeyCredentialRegistrationResponseAsString,
			                                   final String publicKeyCredentialCreateOptionsAsJSON
			                                   ) {
		try {
			log.warn("#######################################################################");
		    log.warn(" ////////////////////   FINISH REGISTRATION               /////////////");
		    log.warn("#######################################################################");
		    log.warn(" ..\n publicKeyCredentialCreateOptionsAsJSON :\n {}" ,publicKeyCredentialCreateOptionsAsJSON);
		    log.warn(" ..\n publicKeyCreddentialRegistrationResponseAsString :\n {}" ,publicKeyCredentialRegistrationResponseAsString);


			log.warn("\n\n..finish webauth registration for user with login-id {}",loginId.asString());
			//[1].. Load the las logged entry
			E  entry = _loginEntryCrud.loadLastLoginEntryFor(_sysSecurityContextProvider.get(),
										                     null, // any
											                //  WARNING!! Suppose "username" is LogiID in any IdProvider
											                //  --------------------------------------------------------------
										                     loginId,
											                //  WARNING!! Suppose that with that "username"/LoginID  has logged any moment
											                //  -------------------------------------------------------------------------
											                UserLoginEntryID.forLoginOk()).getOrNull();

			SecurityProviderID backMappedSecurityProviderOnRegistration = entry.getSecurityProviderId();

			//[2].. Load the user
			U user = _userCrud.load(_sysSecurityContextProvider.get(),
					                 entry.getUserOid())
						       .getOrThrow();

			//[3].. Build registration request & registration response
		    PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> registrationResponse =
					 PublicKeyCredential.parseRegistrationResponseJson(publicKeyCredentialRegistrationResponseAsString);

	        PublicKeyCredentialCreationOptions requestOptions = PublicKeyCredentialCreationOptions.fromJson(publicKeyCredentialCreateOptionsAsJSON);
			FinishRegistrationOptions options = FinishRegistrationOptions.builder()
																            .request(requestOptions)
																            .response(registrationResponse)
																          .build();
			//[4].. finish registration
	        RegistrationResult result = _relyingParty.finishRegistration(options);

	    	//[5].. store credentials (pk) at db
	        StringBase64 credentialIdAsBAse64 = StringBase64.fromBase64EncodedString(result.getKeyId().getId().getBase64());
	        StringBase64 publicKeyAsBase64 = StringBase64.fromBase64EncodedString(result.getPublicKeyCose().getBase64());;
	       // this.aaguid = attestationData.get().getAaguid();

	        Long count = result.getSignatureCount();
	        C  registredCredentials = _credentialFactoyFromRegistrationResponse.from(user.getOid(),
	        		                                                                 credentialIdAsBAse64,
	        		                                                                 publicKeyAsBase64, count,backMappedSecurityProviderOnRegistration);
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
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
