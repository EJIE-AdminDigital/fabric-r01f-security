package r01f.security.saml.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.opensaml.saml.saml2.core.AuthnRequest;
import org.opensaml.saml.saml2.core.NameIDPolicy;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.util.types.StringEncodeUtils;
import r01f.util.types.collections.CollectionUtils;
import r01f.webbrowser.WebBrowserUtils;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public class SAMLAPIForAuthn {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLIdPConfig _IdPConfig;
	private final SAMLSPCredentials _spCredentials;
/////////////////////////////////////////////////////////////////////////////////////////
//	AUTHN REQUEST
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Builds an encoded SAML request.
	 * @return The base-64 encoded SAML request.
	 * @throws SAMLException thrown if an unexpected error occurs.
	 */
	public String createSAMLAuthnRequest() throws SAMLException {
		AuthnRequest request = SAMLMarshaller.forRelyingPartyWithId(_IdPConfig.getRelyingPartyId())
											 .createBasicSAMLRequest(AuthnRequest.DEFAULT_ELEMENT_NAME);

		request.setProtocolBinding("urn:oasis:names:tc:SAML:2.0:bindings:" + _IdPConfig.getIdPBinding().getCode());		// HTTP-POST or HTTP-Redirect
		request.setDestination(_IdPConfig.getIdPUrl().asString());
		request.setAssertionConsumerServiceURL(_IdPConfig.getAssertionConsumerServiceUrl().asString());

		NameIDPolicy nameIDPolicy = SAMLMarshaller.buildSAMLObject(NameIDPolicy.DEFAULT_ELEMENT_NAME);
		nameIDPolicy.setFormat("urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified");
		request.setNameIDPolicy(nameIDPolicy);

		// sign the request using the SP credential
		// (if the credential is null the method does NOT sign the request)
		SAMLSignatureOperations.using(_spCredentials.getSpCredential())
				  		  	   .signSAMLObject(request);

		return SAMLMarshaller.marshallAndEncodeSAMLObject(request,
														  _IdPConfig.getIdPBinding());
	}
	/**
	 * If using Redirect (GET) idp binding, this method returns the target url
	 * BEWARE!!! When using this url ENCODE the url params
	 * @param afterAuthnUrl the url to which redirect to afcter successful login
	 * 						this url is just put as saml [relayState] and it's value will return to the ACS (assertion consumer service) unchanged
	 * 						... the ACS will the redir to the url
	 * @return
	 */
	public Url composeIdPClientRedirectAuthnUrl(final Url afterAuthnUrl) {
		if (_IdPConfig.getIdPBinding() == SAMLIdPBinding.POST) throw new IllegalStateException("IdP binding is " + SAMLIdPBinding.POST + " for client redirect use IdP binding = " + SAMLIdPBinding.REDIRECT);

		Map<String,String> values = new HashMap<>();
		values.put(SAMLAPI.HTTP_REQ_SAML_PARAM,this.createSAMLAuthnRequest());
		if (afterAuthnUrl != null) {
			String afterAuthUrl_urlencoded = StringEncodeUtils.urlEncode(afterAuthnUrl.asString()).toString();	// BEWARE!! url encode!
			values.put(SAMLAPI.HTTP_RELAY_STATE_PARAM,afterAuthUrl_urlencoded);
		}

		Url theUrl = CollectionUtils.hasData(values) ? _IdPConfig.getIdPUrl().joinWith(new UrlQueryString(values))
													 : _IdPConfig.getIdPUrl();
		return theUrl;
	}
	/**
	 * If using POST idn binding, this method returns an html with an auto-submit by POST form
	 * @param afterAuthnUrl the url to which redirect to afcter successful login
	 * 						this url is just put as saml [relayState] and it's value will return to the ACS (assertion consumer service) unchanged
	 * 						... the ACS will the redir to the url
	 * @return
	 */
	public String composeIdpAuthnAutoSubmitHtmlForm(final Url afterAuthnUrl) {
		Map<String,String> values = new HashMap<>();
		if (afterAuthnUrl != null) values.put(SAMLAPI.HTTP_RELAY_STATE_PARAM,afterAuthnUrl.asString());
		values.put(SAMLAPI.HTTP_REQ_SAML_PARAM,this.createSAMLAuthnRequest());
		String html = WebBrowserUtils.composeAutoSubmitHtmlForm(_IdPConfig.getIdPUrl(),
														  		values);
		return html;
	}
	/**
	 * Redirects an {@link HttpServletResponse} to the configured identity provider.
	 * @param response	The {@link HttpServletResponse}.
	 * @param afterAuthnUrl the url to which redirect to afcter successful login
	 * 						this url is just put as saml [relayState] and it's value will return to the ACS (assertion consumer service) unchanged
	 * 						... the ACS will the redir to the url
	 * @throws IOException	thrown if an IO error occurs.
	 * @throws SAMLException thrown is an unexpected error occurs.
	 */
	public void redirectToIdPAuthnUrl(final HttpServletResponse response,
							  		  final Url afterAuthnUrl) throws IOException,
														      		  SAMLException {
		Map<String,String> values = new HashMap<>();
		values.put(SAMLAPI.HTTP_REQ_SAML_PARAM,this.createSAMLAuthnRequest());
		if (afterAuthnUrl != null) values.put(SAMLAPI.HTTP_RELAY_STATE_PARAM,afterAuthnUrl.asString());

		SAMLAPI.redirectToIdp(response,
							  _IdPConfig.getIdPUrl(),
							  values,
							  _IdPConfig.getIdPBinding());
	}
}
