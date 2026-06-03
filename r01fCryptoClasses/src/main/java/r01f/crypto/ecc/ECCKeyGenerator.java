package r01f.crypto.ecc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;

import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class ECCKeyGenerator {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static KeyPair generateKeyPair(final String curveName) throws InvalidAlgorithmParameterException, 
																		 NoSuchProviderException, 
																		 NoSuchAlgorithmException {
		ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec(curveName);
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("ECDH", "BC");

		keyPairGenerator.initialize(parameterSpec);

		return keyPairGenerator.generateKeyPair();
	}
	public static SecretKey generateSharedSecret(final PublicKey publicKey, final PrivateKey privateKey) throws NoSuchProviderException, 
																												NoSuchAlgorithmException, 
																												InvalidKeyException {
		KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
		keyAgreement.init(privateKey);
		keyAgreement.doPhase(publicKey, true);

		return keyAgreement.generateSecret("AES");
	}
}
