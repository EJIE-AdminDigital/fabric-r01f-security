package r01f.security.saml.api;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;

import org.opensaml.security.credential.Credential;
import org.opensaml.security.x509.BasicX509Credential;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.types.Path;

/**
 * Holds the [service provider]'s credentials
 */
@Accessors(prefix="_")
class SAMLSPCredentials {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private BasicX509Credential _spCredential;
	@Getter private final Collection<Credential> _additionalSpCredentials = new ArrayList<>();
	
/////////////////////////////////////////////////////////////////////////////////////////
//	SERVICE PROVIDER KEYS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Set service provider keys.
	 * @param publicKeyFilePath	the public key
	 * @param privateKeyFilePath the private key
	 * @throws SAMLException if publicKey and privateKey don't form a valid credential
	 */
	public void setSPKeys(final Path publicKeyFilePath,final Path privateKeyFilePath) throws SAMLException {
		_spCredential = SAMLCertificates.generateBasicX509Credential(publicKeyFilePath,privateKeyFilePath);
	}
	/**
	 * Set service provider keys.
	 * @param certificate the certificate
	 * @param privateKey the private key
	 * @throws SAMLException if publicKey and privateKey don't form a valid credential
	 */
	public void setSPKeys(final X509Certificate certificate, 
						  final PrivateKey privateKey) throws SAMLException {
		if (certificate == null || privateKey == null) throw new SAMLException("No credentials provided");
		_spCredential = new BasicX509Credential(certificate,privateKey);
	}
	/**
	 * Add an additional service provider certificate/key pair for decryption.
	 * @param publicKeyFilePath	the public key
	 * @param privateKeyFilePath the private key
	 * @throws SAMLException if publicKey and privateKey don't form a valid credential
	 */
	public void addAdditionalSPKey(final Path publicKeyFilePath,final Path privateKeyFilePath) throws SAMLException {
		_additionalSpCredentials.add(SAMLCertificates.generateBasicX509Credential(publicKeyFilePath,privateKeyFilePath));
	}
	/**
	 * Add an additional service provider certificate/key pair for decryption.
	 * @param certificate the certificate
	 * @param privateKey the private key
	 * @throws SAMLException if publicKey and privateKey don't form a valid credential
	 */
	public void addAdditionalSPKey(final X509Certificate certificate,final PrivateKey privateKey) throws SAMLException {
		_additionalSpCredentials.add(new BasicX509Credential(certificate,privateKey));
	}
	/**
	 * Remove all additional service provider decryption certificate/key pairs.
	 */
	public void clearAdditionalSPKeys() {
		_additionalSpCredentials.clear();
	}
}
