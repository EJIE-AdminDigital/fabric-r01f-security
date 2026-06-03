package r01f.security.saml.api;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.opensaml.saml.saml2.core.Assertion;
import org.opensaml.saml.saml2.core.EncryptedAssertion;
import org.opensaml.saml.saml2.core.Response;
import org.opensaml.saml.saml2.encryption.Decrypter;
import org.opensaml.security.credential.Credential;
import org.opensaml.security.x509.BasicX509Credential;
import org.opensaml.xmlsec.encryption.support.DecryptionException;
import org.opensaml.xmlsec.encryption.support.InlineEncryptedKeyResolver;
import org.opensaml.xmlsec.keyinfo.KeyInfoCredentialResolver;
import org.opensaml.xmlsec.keyinfo.impl.ChainingKeyInfoCredentialResolver;
import org.opensaml.xmlsec.keyinfo.impl.CollectionKeyInfoCredentialResolver;
import org.opensaml.xmlsec.keyinfo.impl.StaticKeyInfoCredentialResolver;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.util.types.collections.CollectionUtils;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLDercrypter {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Decode the encrypted assertion.
	 * @param response the response
	 * @throws DecryptionException the decryption exception
	 */
	public static SAMLSignerSAMLObjectStep using(final BasicX509Credential credential,
												 final Collection<Credential> additionalSpCredentials)  {
		return new SAMLSignerSAMLObjectStep(credential,
											additionalSpCredentials);
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	static class SAMLSignerSAMLObjectStep {
		private final BasicX509Credential _spCredential;
		private final Collection<Credential> _additionalSpCredentials;
		
		public void decodeEncryptedAssertion(final Response response) throws DecryptionException {
			if (response.getEncryptedAssertions().size() == 0) return;	// no assertions
			
			for (EncryptedAssertion encryptedAssertion : response.getEncryptedAssertions()) {
				// Create a decrypter
				List<KeyInfoCredentialResolver> resolverChain = new ArrayList<>();
	
				if (_spCredential != null) resolverChain.add(new StaticKeyInfoCredentialResolver(_spCredential));
				if (CollectionUtils.hasData(_additionalSpCredentials)) resolverChain.add(new CollectionKeyInfoCredentialResolver(_additionalSpCredentials));
				Decrypter decrypter = new Decrypter(null,
													new ChainingKeyInfoCredentialResolver(resolverChain),
													new InlineEncryptedKeyResolver());
				decrypter.setRootInNewDocument(true);
				// Decrypt the assertion
				Assertion decryptedAssertion = decrypter.decrypt(encryptedAssertion);
				// Add the assertion
				response.getAssertions().add(decryptedAssertion);
			}
		}
	}
}
