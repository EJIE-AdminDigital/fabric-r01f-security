package r01f.security.handler.saml;

import java.net.URI;

import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import r01f.security.authn.handler.LoginEntryPointHandlerBase;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLAPIForAuthn;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.types.StringBase64;
import r01f.types.url.Url;

@Slf4j
public class SAMLLoginEntryPointHandler
     extends LoginEntryPointHandlerBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	private final SAMLAPI _samlAPI;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public SAMLLoginEntryPointHandler(final AuthenticationProviderSecretConfig securityProviderSecretConfig,
									  final SAMLAPI samlAPI) {
		super(securityProviderSecretConfig);
		_samlAPI = samlAPI;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Response redirToSecurityProviderLoginEntryPointWith(final Url to,
															   final String impl) {
		return this.redirToSecurityProviderLoginEntryPointWith(to,null,	// no after login [to url] extra info
															   impl);
	}
	@Override
	public Response redirToSecurityProviderLoginEntryPointWith(final Url to,final StringBase64 afterLoginToUrlExtaInfoBase64,
															   final String impl) {

		// [0] - Get the saml api depending on the impl (ie okta)
		//		 Beware that the concrete SAMLAPI impl MUST have been initialized beforehand!
		SAMLIssuerID currentIssuerId = SAMLIssuerID.forId(impl);			// ie SAMLEuskadiConstants.OKTA_ISSUERID;

		// [1] - Depending on the binding mode issue a CLIENT REDIR to the IdP or POST an HTML form to the IdP
		if (_samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() == SAMLIdPBinding.POST) {
			// POST the [saml authn request] to the  [IdP]
			return _createAutoSubmitHTMLFormResponse(to,
													 impl);
		} else if (_samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() == SAMLIdPBinding.REDIRECT) {
			// REDIRECT to the [IdP] with the [saml authn request] as url param
			return _createClientRedir(to,
									  impl);
		} else {
			throw new IllegalStateException("saml idp binding " +  _samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() + " is NOT supported!");
		}
	}
	private Response _createClientRedir(final Url afterLoginRedirToUrl,
										final String impl) {
		SAMLIssuerID currentIssuerId = SAMLIssuerID.forId(impl);			// ie SAMLAPI.OKTA_ISSUERID;
		SAMLAPIForAuthn authnAPI = _samlAPI.usingIdP(currentIssuerId)
										   .getAuthAPI();
		Url idpRedirUrl = authnAPI.composeIdPClientRedirectAuthnUrl(afterLoginRedirToUrl);
		log.debug("[SAML] > REDIR (GET) to IdP at {}",
				 idpRedirUrl);
		return Response.seeOther(URI.create(idpRedirUrl.asString()))	// BEWARE!! url params
					   .build();
	}
	private Response _createAutoSubmitHTMLFormResponse(final Url afterLoginRedirToUrl,
													   final String impl) {
		SAMLIssuerID currentIssuerId = SAMLIssuerID.forId(impl);			// ie SAMLEuskadiConstants.OKTA_ISSUERID;
		SAMLAPIForAuthn authnAPI = _samlAPI.usingIdP(currentIssuerId)
										   .getAuthAPI();

		String autoSubmmitFormHtml = authnAPI.composeIdpAuthnAutoSubmitHtmlForm(afterLoginRedirToUrl);
		log.debug("[SAML] > REDIR (POST) to IdP with\n{}",autoSubmmitFormHtml);
		return Response.ok(autoSubmmitFormHtml)
					   .build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public Url getLoginEntryPointUrl(final Url afterLoginRedirToUrl, final String impl) {
		throw new UnsupportedOperationException("this method is NOT used in SAML impl");
	}
}
