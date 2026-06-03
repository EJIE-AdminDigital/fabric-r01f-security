package r01f.crypto.aes;

import java.util.Arrays;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
public class AESSecretKey {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final SecretKey _key;
	@Getter private final byte[] _IV;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AESSecretKey(final SecretKey sk,final byte[] ivBytes) {
		_key = sk;
		_IV = ivBytes;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	// This takes in SK || IV for AES256 and creates the SecretKey object and
	// corresponding IV byte array.
	public AESSecretKey(final byte[] skConcatIVBytes) {
		int total_bytes = skConcatIVBytes.length;
		// FOR AES256 the key is 32 bytes and the IV is 16 bytes
		byte[] sk = Arrays.copyOfRange(skConcatIVBytes, 0, 32);
		byte[] iv = Arrays.copyOfRange(skConcatIVBytes, 32, total_bytes);

		_key = new SecretKeySpec(sk, 0, sk.length, "AES");
		_IV = iv;
	}
}
