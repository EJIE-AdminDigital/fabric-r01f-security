package r01f.security.saml.api;

import java.io.FileNotFoundException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.KeyDescriptor;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.credential.UsageType;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.keyinfo.KeyInfoSupport;
import org.opensaml.xmlsec.signature.X509Data;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.crypto.CryptoCertificates;
import r01f.crypto.CryptoKeys;
import r01f.types.Path;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLCertificates {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Load a PKCS8 key
	 * @param keyFilePath The path of the key
	 * */
	public static PrivateKey loadPKCS8PrivateKey(final Path keyFilePath) throws SAMLException {
		try {
			return CryptoKeys.loadPKCS8PrivateKey(keyFilePath);
		} catch (FileNotFoundException e) {
			throw new SAMLException("Private key file doesn't exist", e);
		} catch (Exception e) {
			throw new SAMLException("Couldn't load private key", e);
		}
	}
	/**
	 * Load an X.509 certificate
	 * @param filename The path of the certificate
	 * */
	public static X509Certificate loadX509Certificate(final Path keyFilePath) throws SAMLException {
		try {
			return CryptoCertificates.loadX509Certificate(keyFilePath);
		} catch (FileNotFoundException e) {
			throw new SAMLException("Public key file doesn't exist", e);
		} catch (Exception e) {
			throw new SAMLException("Couldn't load public key", e);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static BasicX509Credential generateBasicX509Credential(final Path publicKeyFilePath,
															 	  final Path privateKeyFilePath) throws SAMLException {
		if (publicKeyFilePath == null || privateKeyFilePath == null) throw new SAMLException("No credentials provided");
		
		PrivateKey pk = SAMLCertificates.loadPKCS8PrivateKey(privateKeyFilePath);
		X509Certificate cert = SAMLCertificates.loadX509Certificate(publicKeyFilePath);
		return new BasicX509Credential(cert,pk);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static Credential createtCredentialFrom(final X509Certificate certificate) {
		BasicX509Credential credential = new BasicX509Credential(certificate);
		credential.setCRLs(Collections.emptyList());
		return credential;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static List<X509Certificate> getCertificatesAt(final IDPSSODescriptor idpSSODescriptor) throws SAMLException {
		List<X509Certificate> certificates;
		try {
			certificates = idpSSODescriptor.getKeyDescriptors()
										   .stream()
										   // filter the keys used for signing
										   .filter(keyDescriptor -> keyDescriptor.getUse() == UsageType.SIGNING)
										   // concat all the streams of X509Data from every key descriptor 
										   .flatMap(SAMLCertificates::_x509DataFrom)
										   // get first cerfificate
										   .map(SAMLCertificates::_firstCertificateOf)
										   .collect(Collectors.toList());
		} catch (Exception e) {
			throw new SAMLException("Exception in getCertificates", e);
		}
		return certificates;
	}
	private static Stream<X509Data> _x509DataFrom(final KeyDescriptor descriptor) {
		return descriptor.getKeyInfo()
						 .getX509Datas()
						 .stream()
						 // filter the ones with certificates
						 .filter(data -> data.getX509Certificates().size() > 0);
	}
	private static X509Certificate _firstCertificateOf(final X509Data data) {
		try {
			org.opensaml.xmlsec.signature.X509Certificate cert = data.getX509Certificates().stream().findFirst().orElse(null);
			if (cert != null) return KeyInfoSupport.getCertificate(cert);
		} catch (CertificateException e) {
			log.error("Exception in getFirstCertificate: {}",
					  e.getMessage(),e);
		}
		return null;
	}
}
