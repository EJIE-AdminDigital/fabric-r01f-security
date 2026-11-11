package r01f.security.saml.api;

import java.util.Collection;
import java.util.Objects;

import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.saml.saml2.core.LogoutRequest;
import org.opensaml.saml.saml2.core.RequestAbstractType;
import org.opensaml.security.credential.Credential;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.oids.SecurityIDs.LoginID;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;
import r01f.util.types.Strings;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLValidatorsForRequest {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Validate.
	 * @param logoutRequest	the request
	 * @param requestIssuerId the request issuer
	 * @param credentials the credentials
	 * @param loginId
	 * @throws SAMLException a saml exception if the object is not valid
	 */
	public static void validate(final LogoutRequest logoutRequest,
								final SAMLIssuerID requestIssuerId,
								final Collection<Credential> credentials,
								final LoginID loginId) throws SAMLException {
		_validateLogoutRequestId(logoutRequest);
		_validateLogoutRequestVersion(logoutRequest);
		_validateLogoutRequestIssueInstant(logoutRequest);
		_validateLogoutRequestIdentifiers(logoutRequest);		
		_validateRequestIssuer(logoutRequest,
							   requestIssuerId);
		_validateLogoutRequestLoginId(logoutRequest,
									  loginId);
		_validateSignature(logoutRequest, 
						   credentials);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	

	private static void _validateRequestIssuer(final RequestAbstractType request,
									    	   final SAMLIssuerID requestIssuerId) throws SAMLException {
		if (!request.getIssuer().getValue().equals(requestIssuerId.asString())) throw new SAMLException("Request validation failed: The request issuer didn't match the expected value");
	}
	private static void _validateLogoutRequestLoginId(final LogoutRequest request, 
									    			  final LoginID loginId) throws SAMLException {
		if (loginId == null || !loginId.asString().equals(request.getNameID().getValue())) throw new SAMLException("Request validation failed: The nameID (loginId) of the logout request is incorrect");
	}
	private static void _validateLogoutRequestId(final LogoutRequest request) throws SAMLException {
		if (Strings.isNullOrEmpty(request.getID())) throw new SAMLException("Request validation failed: ID attribute must not be empty");
	}
	private static void _validateLogoutRequestVersion(final LogoutRequest request) throws SAMLException {
		if (request.getVersion() == null) throw new SAMLException("Request validation failed: Version attribute must not be null");
		if (!Objects.equals(request.getVersion().toString(), 
							SAMLVersion.VERSION_20.toString())) throw new SAMLException("Wrong SAML Version");
	}
	private static void _validateLogoutRequestIssueInstant(final LogoutRequest request) throws SAMLException {
		if (request.getIssueInstant() == null) throw new SAMLException("Request validation failed: IssueInstant attribute must not be null");
	}
	protected static void _validateLogoutRequestIdentifiers(final LogoutRequest request) throws SAMLException {
		int idCount = 0;
		if (request.getBaseID() != null) idCount++;
		if (request.getNameID() != null) idCount++;
		if (request.getEncryptedID() != null) idCount++;
		if (idCount != 1) throw new SAMLException("Request validation failed: LogoutRequest must contain exactly one of: BaseID, NameID, EncryptedID");
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SIGNATURE
/////////////////////////////////////////////////////////////////////////////////////////
	private static void _validateSignature(final SignableSAMLObject response,
										   final Collection<Credential> credentials) throws SAMLException {
		if (response.getSignature() != null 
		 && !SAMLSignatureOperations.validate(response.getSignature())
		 							.using(credentials)) throw new SAMLException("Request validation failed: The request signature is invalid");
	}
}
