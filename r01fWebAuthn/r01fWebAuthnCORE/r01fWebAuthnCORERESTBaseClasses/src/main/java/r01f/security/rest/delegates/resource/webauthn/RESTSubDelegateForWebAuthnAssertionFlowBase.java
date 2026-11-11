package r01f.security.rest.delegates.resource.webauthn;

import java.net.URI;
import java.util.Map;

import com.yubico.webauthn.AssertionRequest;
import com.yubico.webauthn.AssertionResult;
import com.yubico.webauthn.FinishAssertionOptions;
import com.yubico.webauthn.RelyingParty;
import com.yubico.webauthn.StartAssertionOptions;
import com.yubico.webauthn.data.AuthenticatorAssertionResponse;
import com.yubico.webauthn.data.ClientAssertionExtensionOutputs;
import com.yubico.webauthn.data.PublicKeyCredential;

import jakarta.inject.Provider;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.login.CRUDServicesForLogin;
import r01f.api.interfaces.security.login.entry.CRUDServicesForLoginEntry;
import r01f.api.interfaces.security.principal.CRUDServicesForPrincipal;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.internal.R01FAppCodes;
import r01f.jwt.JWT;
import r01f.jwt.JWTBuilder;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.config.WebAuthnConfig;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.security.handler.webauthn.CredentialFactoryFromRegistrationResponse;
import r01f.security.handler.webauthn.utils.WebAuthnByteStreamUtils;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;

@Slf4j
public class RESTSubDelegateForWebAuthnAssertionFlowBase<U extends IsSecurityPrincipal,C extends AuthenticatorCredential>
		   extends RESTSubDelegateForWebAuthnAbstractBase<U,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public RESTSubDelegateForWebAuthnAssertionFlowBase(final Provider<SecurityContext> sysSecurityContextProvider,
		                                               final Marshaller marshaller,
		                                               final WebAuthnConfig webAuthnConfig,
		                                               final RelyingParty relyingParty,
		                                               final CRUDServicesForPrincipal<U> principalCrud ,
		                                               final CRUDServicesForLogin loginCrud,final CRUDServicesForLoginEntry loginEntryCrud,
		                                               final CRUDServicesForAuthenticatorCredential<C> authenticatorCredentialCrud,final CredentialFactoryFromRegistrationResponse<U,C> credentialFactoyFromRegistrationResponse) {
		super(sysSecurityContextProvider,
			  marshaller,
			  webAuthnConfig,relyingParty,
			  principalCrud,
			  loginCrud,loginEntryCrud,
			  authenticatorCredentialCrud,
			  credentialFactoyFromRegistrationResponse);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//START WEBAUTHN AUTHENTICATION
/////////////////////////////////////////////////////////////////////////////////////////
	public Response startWebAuthAuthentication(final LoginID loginId) {
		log.warn("#######################################################################");
		log.warn(" ////////////////////   START ASSERTION    ///////////////////////////");
		log.warn("#######################################################################");
		String assertionRequesAsJSON  = null;
		try {
			//[1].. Assertion Request
			log.warn("....start assertion");
			AssertionRequest request = _relyingParty.startAssertion(StartAssertionOptions.builder()
														.username(loginId.asString())
													  .build());
			assertionRequesAsJSON  = request.toJson();
			log.warn(" assertion request :\n {}" ,
									assertionRequesAsJSON);
			log.warn(" assertion credential request :\n {}" ,
									request.toCredentialsGetJson());
		} catch (final Throwable e) {
			e.printStackTrace();
			return Response.serverError()
								.entity(e.getLocalizedMessage())
							.build();
		}
		return Response.ok(assertionRequesAsJSON,
							MediaType.APPLICATION_JSON)
						.build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
// FINISH WEBAUTHN
/////////////////////////////////////////////////////////////////////////////////////////
	public Response finishWebAuthAuthentication(final String publicKeyCredentialAssertResponseAsString,
												final String publicKeyCredentialAssertRequestAsJSON,
												final String afterAssertionRedir) {
		try {
			log.warn("#######################################################################");
			log.warn(" ////////////////////   FINISH ASSERTION    //////////////////////////");
			log.warn("#######################################################################");
			log.warn(" afterAssertionRedir :\n {}" , afterAssertionRedir);
			log.warn(" assertion request :\n {}" ,   publicKeyCredentialAssertRequestAsJSON);
			log.warn(" assertion response :\n {}" , publicKeyCredentialAssertResponseAsString);

			PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc;
			pkc = PublicKeyCredential.parseAssertionResponseJson(publicKeyCredentialAssertResponseAsString);
			AssertionRequest request = AssertionRequest.fromJson(publicKeyCredentialAssertRequestAsJSON);

			AssertionResult result =
					_relyingParty.finishAssertion(FinishAssertionOptions.builder()
												    .request(request)
												    .response(pkc)
										    	  .build());


			log.warn( " \n Finish asssertion sucess ? : {}",
													result.isSuccess());

			if (result.isSuccess()) {
				log.warn(" \n\n [o][o] Finish assertion ok!!!");
				// .. build a valid jwt to be used from tthe filter
				JWT jwtAfterAssertion = _createJWT(result, null);
				// ...attach to uri
				URI targetUrl = _afterAssertionURL(jwtAfterAssertion,afterAssertionRedir);
				log.warn(" ..after assertion redir {}", afterAssertionRedir);
			    return Response.seeOther(targetUrl)
			    				.build();
			}
			log.error("Unsucessfull assertion result :{}", result.toString());
			return Response.status(403)
							.build();

		} catch (final Throwable th) {
			th.printStackTrace();
			return Response.serverError()
							.build();
		  }
	}
///////////////////////////////////////////////////////////////////////////////////////////////////////
// PROTECTED METHODS
//////////////////////////////////////////////////////////////////////////////////////////////////////
	protected JWT _createJWT(final AssertionResult result,
 							 final Map<String,String> otherFieldsAsClaims) {
        // ..parse data to r01f model
		SecurityPrincipalOID userOid = SecurityPrincipalOID.fromString(WebAuthnByteStreamUtils.from(result.getCredential().getUserHandle()));
		LoginID loginId = LoginID.forId(result.getUsername());
		CredentialID  credentialId  = CredentialID.fromBase64RawString(result.getCredential().getCredentialId().getBase64());
        // .. build a r01.webauth  token (jwt)
 	 	byte keyBytes[] = _webAuthnConfig.getKeyData().getBytes();
 	 	JWT jwt = JWTBuilder.createJWToken()
	 	 			  			.forIssuer(R01FAppCodes.R01_APP_CODE)
	 	 			  			.toAudience("r01.webauthn")
	 	 			  			.withExpirationSeconds(10L)
	 	 			  			.noId()
	 	 			  			 // ...the required claims to fetch the user at filter
	 	 			  			.usingCustomClaim("userOid", userOid.asString())
	 	 			  		    .usingCustomClaim("loginId", loginId.asString())
	 	 			  		    .usingCustomClaim("credentialId", credentialId.asString())
	 	 			  		     // ... other calims if needed
	 	 			  			.usingCustomClaims(otherFieldsAsClaims)
	 	 			  			.withKey(keyBytes)
 	 			  			.buildWithOptimalAlgorithm();
		log.warn("JWT after authn assertion {}",
				 jwt.asString());
 	 	return jwt;
 	}

	protected static URI _afterAssertionURL( final JWT jwt,
			                                 final String afterAssertionRedir ) {
	 // [1] - Create a url like afterAssertionnRedirToUrl?jwt={the_jwt}
		Url afterLoginRedirToUrl = Url.from(afterAssertionRedir);
		UrlQueryString qryStr = UrlQueryString.fromParams(UrlQueryStringParam.of("jwt",jwt.asString()));
		Url redirUrl = afterLoginRedirToUrl.joinWith(qryStr);
		log.warn("### - Get [_afterAssertionURL] = {}",
				 redirUrl);
		// [N] - URI
		URI targetURI = UriBuilder.fromUri(redirUrl.asString())
									  .build();
		return targetURI;
	}
}
