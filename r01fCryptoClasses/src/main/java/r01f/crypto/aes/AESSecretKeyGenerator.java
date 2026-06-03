package r01f.crypto.aes;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class AESSecretKeyGenerator {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static AESSecretKey generateKeyPair() throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(256);

		SecretKey key = keyGenerator.generateKey();
		byte[] IV = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(IV);

		AESSecretKey response = new AESSecretKey(key, IV);
		return response;
	}
}
