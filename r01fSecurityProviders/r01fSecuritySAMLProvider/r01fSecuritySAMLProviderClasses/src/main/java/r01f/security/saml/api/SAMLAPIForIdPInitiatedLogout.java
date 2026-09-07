package r01f.security.saml.api;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.saml.saml2.core.LogoutResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.securitycontext.SecurityIDS.LoginID;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public class SAMLAPIForIdPInitiatedLogout {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLIdPConfig _IdPConfig;
	private final SAMLSPCredentials _spCredentials;
/////////////////////////////////////////////////////////////////////////////////////////
//	IdP INITIATED LOGOUT
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Redirect to identity provider logout.
	 * @param response	 the response
	 * @param statusCode the status code
	 * @param statMsg the stat msg
	 * @throws IOException the io exception
	 * @throws SAMLException the saml exception
	 */
	public void redirectToIdPLogout(final HttpServletResponse response,
								    final String statusCode,final String statMsg) throws IOException,
																			  			 SAMLException {
		Map<String, String> values = new HashMap<>();
		values.put(SAMLAPI.HTTP_RESP_SAML_PARAM,this.createSAMLLogoutResponse(statusCode,
															   	   	  		  statMsg));
		SAMLAPI.redirectToIdp(response,
							  _IdPConfig.getIdPUrl(),
							  values,
							  _IdPConfig.getIdPBinding());
	}
	/**
	 * Gets saml logout response.
	 * @param status the status code @See StatusCode.java
	 * @return saml logout response
	 * @throws SAMLException the saml exception
	 */
	public String createSAMLLogoutResponse(final String status) throws SAMLException {
		return this.createSAMLLogoutResponse(status,null);
	}
	/**
	 * Gets saml logout response.
	 * @param status the status code @See StatusCode.java
	 * @param statMsg the status message
	 * @return saml logout response
	 * @throws SAMLException the saml exception
	 */
	public String createSAMLLogoutResponse(final String status,final String statMsg) throws SAMLException {
		LogoutResponse response = SAMLMarshaller.forRelyingPartyWithId(_IdPConfig.getRelyingPartyId())
												.createLogoutResponseWith(status,statMsg);
		// sign the response using the SP credential
		// (if the credential is null the method does NOT sign the request)
		SAMLSignatureOperations.using(_spCredentials.getSpCredential())
				  		  	   .signSAMLObject(response);
		// marshall and encode
		return SAMLMarshaller.marshallAndEncodeSAMLObject(response,
														  _IdPConfig.getIdPBinding());
	}
	/**
	 * Processes a POST containing the SAML logout request.
	 * @param request the {@link HttpServletRequest}.
	 * @param loginId the user to log out.
	 * @throws SAMLException thrown is an unexpected error occurs.
	 */
	public void processLogoutRequestFromIdP(final HttpServletRequest request,
											final LoginID loginId) throws SAMLException {
		String encodedResponse = request.getParameter(SAMLAPI.HTTP_REQ_SAML_PARAM);
		this.decodeAndValidateLogoutRequestFromIdP(encodedResponse,
												   loginId,
												   SAMLIdPBinding.fromHttpMethod(request));
	}
	/**
	 * Decodes and validates an SAML logout request send by an identity provider.
	 * @param encodedRequest the encoded request send by the identity provider.
	 * @param loginId The user to logout
	 * @param idpBinding The HTTP method used by the request
	 * @throws SAMLException if the signature is invalid, or if any other error occurs.
	 */
	public void decodeAndValidateLogoutRequestFromIdP(final String encodedRequest,
												   	  final LoginID loginId,
												   	  final SAMLIdPBinding idpBinding) throws SAMLException {
		LogoutRequest logoutRequest = SAMLMarshaller.parseResponse(encodedRequest,
																   idpBinding);
		SAMLValidatorsForRequest.validate(logoutRequest,
										  _IdPConfig.getIdPIssuerId(),		// the request issuer is the idp
										  _IdPConfig.getIdPCredentials(),
										  loginId);
	}
}
