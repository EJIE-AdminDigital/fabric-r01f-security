package r01f.security.saml.api;

import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Objects;

import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.Conditions;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.StatusResponseType;
import org.opensaml.security.credential.Credential;
import org.opensaml.xmlsec.signature.Signature;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;
import r01f.util.types.Strings;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLValidatorsForResponse {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Validate.
	 * @param response the response
	 * @param responseIssuerId the response issuer
	 * @param credentials the credentials
	 * @throws SAMLException a saml exception if the object is not valid
	 */
	public static void validate(final StatusResponseType response, 
								final SAMLIssuerID responseIssuerId, 
								final Collection<Credential> credentials) throws SAMLException {
		_validateResponseStatus(response);
		_validateResponseID(response);
		_validateResponseVersion(response);
		_validateResponseIssueInstant(response);
		_validateResponseIssuer(response,
								responseIssuerId);
		_validateSignature(response, 
						   credentials);
	}
	/**
	 * Validate.
	 * @param response the response
	 * @param responseIssuerId the response issuer
	 * @param credentials the credentials
	 * @param now the current date time (for unit test only)
	 * @param notBeforeSkew	the notBeforeSkew
	 * @throws SAMLException a saml exception if the object is not valid
	 */
	public static void validate(final Response response,
								final SAMLIssuerID responseIssuerId,
								final Collection<Credential> credentials,
								final Instant now,final long notBeforeSkew) throws SAMLException {
		SAMLValidatorsForResponse.validate(response,
						  				   responseIssuerId,
						  				   credentials);
		// validate the assertions
		_validateAssertion(response,
						   responseIssuerId, 
						   now,notBeforeSkew);
		_validateAssertionSignature(response,
								    credentials);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _validateResponseStatus(final StatusResponseType response) throws SAMLException {
		if (response.getStatus() == null) throw new SAMLException("Response validation failed: Status is required");
		String statusCode = response.getStatus()
									.getStatusCode()
									.getValue();
		if (!StatusCode.SUCCESS.equals(statusCode)) throw new SAMLException("Invalid status code: " + statusCode);
	}
	private static void _validateResponseIssuer(final StatusResponseType response, 
									    		final SAMLIssuerID responseIssuerId) throws SAMLException {
		if (!response.getIssuer().getValue().equals(responseIssuerId.asString())) throw new SAMLException("Response validation failed: The response issuer didn't match the expected value");
	}
	private static void _validateResponseID(final StatusResponseType response) throws SAMLException {
		if (Strings.isNullOrEmpty(response.getID())) throw new SAMLException("Response validation failed: ID attribute must not be empty");
	}
	private static void _validateResponseVersion(final StatusResponseType response) throws SAMLException {
		if (response.getVersion() == null) throw new SAMLException("Response validation failed: Version attribute must not be null");
		if (!Objects.equals(response.getVersion().toString(), 
							SAMLVersion.VERSION_20.toString())) throw new SAMLException("Response validation failed: Wrong SAML Version");
	}
	private static void _validateResponseIssueInstant(final StatusResponseType response) throws SAMLException {
		if (response.getIssueInstant() == null) throw new SAMLException("Response validation failed: IssueInstant attribute must not be null");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ASSERTIONS
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _validateAssertionSignature(final Response response,
												    final Collection<Credential> credentials) throws SAMLException {
		Signature assertionSignature = response.getAssertions().get(0).getSignature();
		if (response.getSignature() == null 
		 && assertionSignature == null) {
			log.info("Response assertions are NOT signed; ignore assertion signature validation");	// throw new SAMLException("Response validation failed: No signature is present in either response or assertion");
			return;
		}

		if (assertionSignature != null 
		 && !SAMLSignatureOperations.validate(assertionSignature)
				 					.using(credentials)) throw new SAMLException("Response validation failed: The assertion signature is invalid");
	}
	private static void _validateAssertion(final Response response, 
										   final SAMLIssuerID responseIssuerId,
										   final Instant now,final long notBeforeSkew) throws SAMLException {
		if (response.getAssertions().size() != 1) {
			throw new SAMLException("The response doesn't contain exactly 1 assertion");
		}
		Assertion assertion = response.getAssertions().get(0);
		if (!assertion.getIssuer().getValue().equals(responseIssuerId.asString())) throw new SAMLException("Response validation failed: The assertion issuer didn't match the expected value");
		if (assertion.getSubject().getNameID() == null) throw new SAMLException("Response validation failed: The NameID (loginId) value is missing from the SAML response; this is likely an IDP configuration issue");
		_enforceConditions(assertion.getConditions(),
						   now,notBeforeSkew);
	}
	private static void _enforceConditions(final Conditions conditions,
										   final Instant now,final long notBeforeSkew) throws SAMLException {
		Instant theNow = (now != null) ? now : Instant.now();

		Instant notBefore = conditions.getNotBefore();
		Instant skewedNotBefore = notBefore.minus(Duration.ofMillis(notBeforeSkew));
		
		if (theNow.isBefore(skewedNotBefore)) {
			log.error("Response validation failed: The assertion cannot be used before {} ({}) \n [theNow={} ({}) - skewedNotBefore={} ({})] \n notBeforeSkew={}", 
						notBefore.toString(), notBefore.toEpochMilli(), 
						theNow.toString(), theNow.toEpochMilli(), 
						skewedNotBefore.toString(), skewedNotBefore.toEpochMilli(), 
						notBeforeSkew);
			throw new SAMLException("Response validation failed: The assertion cannot be used before " + notBefore.toString());
		}

		Instant notOnOrAfter = conditions.getNotOnOrAfter(); 
		if (theNow.isAfter(notOnOrAfter)) {
			log.error("Response validation failed: The assertion cannot be used after {} \n [theNow={} ({}) - notOnOrAfter={} ({})]", 
						notOnOrAfter.toString(), 
						theNow.toString(), theNow.toEpochMilli(), 
						notOnOrAfter.toString(), notOnOrAfter.toEpochMilli());
			throw new SAMLException("Response validation failed: The assertion cannot be used after	" + notOnOrAfter.toString());
		}
	}
//	private static void _validateAssertionOpenSAML4(final Response response, 
//										   			final SAMLIssuerID responseIssuerId,
//										   			final Instant now,final long notBeforeSkew) throws SAMLException {
//		if (response.getAssertions().size() != 1) {
//			throw new SAMLException("The response doesn't contain exactly 1 assertion");
//		}
//		Assertion assertion = response.getAssertions().get(0);
//		if (!assertion.getIssuer().getValue().equals(responseIssuerId.asString())) throw new SAMLException("Response validation failed: The assertion issuer didn't match the expected value");
//		if (assertion.getSubject().getNameID() == null) throw new SAMLException("Response validation failed: The NameID (loginId) value is missing from the SAML response; this is likely an IDP configuration issue");
//		_enforceConditions(assertion.getConditions(),
//						   now,notBeforeSkew);
//	}
//	private static void _enforceConditionsOpenSAML4(final Conditions conditions,
//										   		    final Instant now,final long notBeforeSkew) throws SAMLException {
//		Instant theNow = now != null ? now : Instant.now();
//
//		Instant notBefore = conditions.getNotBefore();
//		Instant skewedNotBefore = notBefore.minus(notBeforeSkew,ChronoUnit.MILLIS);
//		if (theNow.isBefore(skewedNotBefore)) throw new SAMLException("Response validation failed: The assertion cannot be used before " + notBefore.toString());
//
//		Instant notOnOrAfter = conditions.getNotOnOrAfter();
//		if (theNow.isAfter(notOnOrAfter)) throw new SAMLException("Response validation failed: The assertion cannot be used after	" + notOnOrAfter.toString());
//	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SIGNATURE
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _validateSignature(final SignableSAMLObject response,
										   final Collection<Credential> credentials) throws SAMLException {
		if (response.getSignature() != null 
		 && !SAMLSignatureOperations.validate(response.getSignature())
		 							.using(credentials)) throw new SAMLException("Response validation failed: The response signature is invalid");
	}
}
