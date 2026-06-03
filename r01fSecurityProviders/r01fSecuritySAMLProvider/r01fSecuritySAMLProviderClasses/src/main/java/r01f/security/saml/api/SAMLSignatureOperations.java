package r01f.security.saml.api;

import java.util.Collection;

import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.saml.common.SignableSAMLObject;
import org.opensaml.security.SecurityException;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.SignatureSigningParameters;
import org.opensaml.xmlsec.keyinfo.impl.X509KeyInfoGeneratorFactory;
import org.opensaml.xmlsec.signature.Signature;
import org.opensaml.xmlsec.signature.impl.SignatureBuilder;
import org.opensaml.xmlsec.signature.support.SignatureConstants;
import org.opensaml.xmlsec.signature.support.SignatureException;
import org.opensaml.xmlsec.signature.support.SignatureSupport;
import org.opensaml.xmlsec.signature.support.SignatureValidator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLSignatureOperations {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/** 
	 * Signs a SamlObject with default settings.
	 * if the credential is null this method do not do anything 
	 * @param credential the credential used to sign
	 */
	public static SAMLSignatureOperationsSignStep using(final BasicX509Credential credential)  {
		return new SAMLSignatureOperationsSignStep(credential);
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class SAMLSignatureOperationsSignStep {
		final BasicX509Credential _credential;
		
		public void signSAMLObject(final SignableSAMLObject samlObject) throws SAMLException {
			if (_credential == null) return;
			try {
				// Describe how we're going to sign the request
				SignatureBuilder signer = new SignatureBuilder();
				Signature signature = signer.buildObject(Signature.DEFAULT_ELEMENT_NAME);
				signature.setCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
				signature.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
				signature.setKeyInfo(new X509KeyInfoGeneratorFactory().newInstance()
																	  .generate(_credential));
				signature.setSigningCredential(_credential);
				samlObject.setSignature(signature);
	
				// Actually sign the request
				SignatureSigningParameters signingParameters = new SignatureSigningParameters();
				signingParameters.setSigningCredential(_credential);
				signingParameters.setSignatureCanonicalizationAlgorithm(SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
				signingParameters.setSignatureAlgorithm(SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
				signingParameters.setKeyInfoGenerator(new X509KeyInfoGeneratorFactory().newInstance());
				SignatureSupport.signObject(samlObject, 
											signingParameters);
			} catch (SecurityException | MarshallingException | SignatureException e) {
				throw new SAMLException("Failed to sign request", e);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Validates a signature
	 * @param signature
	 * @return
	 */
	public static SAMLSignatureOperationsValidateStep validate(final Signature signature) {
		return new SAMLSignatureOperationsValidateStep(signature);
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class SAMLSignatureOperationsValidateStep {
		private final Signature _signature;
		
		public boolean using(final Collection<Credential> credentials) {
			if (_signature == null) return false;
			
			// It's fine if any of the credentials match the signature
			return credentials.stream()
							  .anyMatch(credential -> {
											try {
												SignatureValidator.validate(_signature,credential);
												return true;
											} catch (SignatureException ex) {
												log.error("Error validating signature: {}",
														  ex.getMessage(),ex);
												return false;
											}
										});
		}
	}
}
