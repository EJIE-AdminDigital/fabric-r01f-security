package r01f.security.saml.api;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.schema.impl.XSAnyImpl;
import org.opensaml.core.xml.schema.impl.XSStringImpl;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Attribute;
import org.opensaml.saml.saml2.core.AttributeStatement;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.xmlsec.encryption.support.DecryptionException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;

@RequiredArgsConstructor(access=AccessLevel.PACKAGE)
public class SAMLAPIForIdPResponse {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final SAMLIdPConfig _samlIdPConfig;
	private final SAMLSPCredentials _spCredentials;

	private Instant _now; // used for testing only	(opensaml 4 > use: Instant _now)
	private long _notBeforeSkew = 0L;
/////////////////////////////////////////////////////////////////////////////////////////
//	RESPONSE
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Processes a request containing the SAML response
	 * @param request the {@link HttpServletRequest}.
	 * @return An {@link SAMLResponse} object containing information decoded from the SAML response.
	 * @throws SAMLException thrown is an unexpected error occurs.
	 */
	public SAMLResponse processAuthnResponseFromIdp(final HttpServletRequest request) throws SAMLException {
		String encodedResponse = request.getParameter(SAMLAPI.HTTP_RESP_SAML_PARAM);
		return this.decodeAndValidateAuthnResponse(encodedResponse,
											 	   SAMLIdPBinding.fromHttpMethod(request));
	}
	/**
	 * Decodes and validates an SAML response returned by an identity provider.
	 * @param encodedResponse the encoded response returned by the identity provider.
	 * @param idpBinding The HTTP method used by the request
	 * @return An {@link SAMLResponse} object containing information decoded from the SAML response.
	 * @throws SAMLException if the signature is invalid, or if any other error occurs.
	 */
	public SAMLResponse decodeAndValidateAuthnResponse(final String encodedResponse,
													   final SAMLIdPBinding idpBinding) throws SAMLException {
		// Decode and parse the response
		Response response = SAMLMarshaller.parseResponse(encodedResponse,
													 	 idpBinding);
		// Decode the assertions
		try {
			SAMLDercrypter.using(_spCredentials.getSpCredential(),
								 _spCredentials.getAdditionalSpCredentials())
						  .decodeEncryptedAssertion(response);
		} catch (DecryptionException e) {
			throw new SAMLException("Cannot decrypt the assertion", e);
		}
		// Validate	the response (Assertion / HashBase64 / Schema)
		SAMLValidatorsForResponse.validate(response,
							    		   _samlIdPConfig.getIdPIssuerId(),
							    		   _samlIdPConfig.getIdPCredentials(),
							    		   _now,_notBeforeSkew);
		// get the assertions
		Assertion assertion = response.getAssertions()
									  .get(0);
		return new SAMLResponse(SAMLIssuerID.forId(response.getIssuer().getValue()),
								assertion);
	}
	/**
	 * Gets attributes from the IDP Response
	 * @param response the response
	 * @return the attributes
	 */
	public static Map<String,String> getAttributesFromResponse(final SAMLResponse response) {
		HashMap<String, String> map = new HashMap<>();
		if (response == null) return map;

		List<AttributeStatement> attributeStatements = response.getAssertion()
															   .getAttributeStatements();
		if (attributeStatements == null) return map;

		for (AttributeStatement statement : attributeStatements) {
			for (Attribute attribute : statement.getAttributes()) {
				XMLObject xmlObject = attribute.getAttributeValues().get(0);
				if (xmlObject instanceof XSStringImpl) {
					map.put(attribute.getName(), ((XSStringImpl) xmlObject).getValue());
				} else {
					map.put(attribute.getName(), ((XSAnyImpl) xmlObject).getTextContent());
				}
			}
		}
		return map;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DATE TIME
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Sets the date that will be considered as now. This is only useful for testing.
	 * @param now the date to use for now.
	 */
	public void setDateTimeNow(final Instant now) {
		_now = now;
	}
	/**
	 * Sets by how much the current time can be before the assertion's notBefore.
	 * Used to mitigate clock differences between the identity provider and relying party.
	 * @param notBeforeSkew non-negative amount of skew (in milliseconds) to allow between the
	 *						current time and the assertion's notBefore date. Default: 0
	 */
	public void setNotBeforeSkew(final long notBeforeSkew) {
		if (notBeforeSkew < 0) throw new IllegalArgumentException("Skew must be non-negative");
		_notBeforeSkew = notBeforeSkew;
	}
}
