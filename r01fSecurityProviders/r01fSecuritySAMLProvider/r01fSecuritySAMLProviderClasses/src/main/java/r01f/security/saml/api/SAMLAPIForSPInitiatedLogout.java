package r01f.security.saml.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.NameID;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.url.Url;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public class SAMLAPIForSPInitiatedLogout {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLIdPConfig _IdPConfig;
	private final SAMLSPCredentials _spCredentials;
/////////////////////////////////////////////////////////////////////////////////////////
//	SP INITIATED LOGOUUT
/////////////////////////////////////////////////////////////////////////////////////////
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Redirects an {@link HttpServletResponse} to the configured identity provider.
	 * @param response The {@link HttpServletResponse}.
	 * @param loginId the user to log out.
	 * @param afterIdpLogout the url to which redirect to afcter successful login
	 * 						 this url is just put as saml [relayState] and it's value will return to the ACS (assertion consumer service) unchanged
	 * 						 ... the ACS will the redir to the url
	 * @throws IOException thrown if an IO error occurs.
	 * @throws SAMLException thrown is an unexpected error occurs.
	 */
	public void redirectToIdPLogout(final HttpServletResponse response,
									final LoginID loginId,
									final Url afterIdpLogout) throws IOException,
																   	 SAMLException {
		Map<String,String> values = new HashMap<>();
		values.put(SAMLAPI.HTTP_REQ_SAML_PARAM,this.createSAMLLogoutRequest(loginId));
		if (afterIdpLogout != null) values.put(SAMLAPI.HTTP_RELAY_STATE_PARAM,afterIdpLogout.asString());

		SAMLAPI.redirectToIdp(response,
							  _IdPConfig.getIdPUrl(),
							  values,
							  _IdPConfig.getIdPBinding());
	}
	/**
	 * Gets the encoded logout request.
	 * @param loginId the user to logout
	 * @return the logout request
	 * @throws SAMLException the saml exception
	 */
	public String createSAMLLogoutRequest(final LoginID loginId) throws SAMLException {
		LogoutRequest request = SAMLMarshaller.forRelyingPartyWithId(_IdPConfig.getRelyingPartyId())
											  .createBasicSAMLRequest(LogoutRequest.DEFAULT_ELEMENT_NAME);

		NameID nid = SAMLMarshaller.buildSAMLObject(NameID.DEFAULT_ELEMENT_NAME);
		nid.setValue(loginId.asString());
		request.setNameID(nid);

		// sign the request using the SP credential
		// (if the credential is null the method does NOT sign the request)
		SAMLSignatureOperations.using(_spCredentials.getSpCredential())
				  		  	   .signSAMLObject(request);
		// marshall and encode
		return SAMLMarshaller.marshallAndEncodeSAMLObject(request,
														  _IdPConfig.getIdPBinding());
	}
	/**
	 * Processes a request containing the SAML response.
	 * @param request the {@link HttpServletRequest}.
	 * @return An {@link SAMLResponse} object containing information decoded from the SAML response.
	 * @throws SAMLException thrown is an unexpected error occurs.
	 */
	public SAMLLogoutResponse processLogoutResponseFromIdP(final HttpServletRequest request) throws SAMLException {
		String encodedResponse = request.getParameter(SAMLAPI.HTTP_RESP_SAML_PARAM);
		return this.decodeAndValidateSAMLLogoutResponse(encodedResponse,
														SAMLIdPBinding.fromHttpMethod(request));
	}
	/**
	 * Decodes and validates an SAML response returned by an identity provider.
	 * @param encodedResponse the encoded response returned by the identity provider.
	 * @param idpBinding The HTTP method used by the request
	 * @return An {@link SAMLResponse} object containing information decoded from the SAML response.
	 * @throws SAMLException if the signature is invalid, or if any other error occurs.
	 */
	public SAMLLogoutResponse decodeAndValidateSAMLLogoutResponse(final String encodedResponse,
																  final SAMLIdPBinding idpBinding) throws SAMLException {
		LogoutResponse logoutResponse = SAMLMarshaller.parseResponse(encodedResponse,
																	 idpBinding);
		SAMLValidatorsForResponse.validate(logoutResponse,
										   _IdPConfig.getIdPIssuerId(),
										   _IdPConfig.getIdPCredentials());
		return new SAMLLogoutResponse(logoutResponse.getStatus());
	}
}
