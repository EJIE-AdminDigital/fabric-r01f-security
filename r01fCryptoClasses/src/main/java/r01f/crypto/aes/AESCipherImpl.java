package r01f.crypto.aes;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import r01f.crypto.exceptions.CipherException;
import r01f.crypto.utils.CBCMode;

public class AESCipherImpl
  implements AESCipher {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	private final CBCMode _cbcMode;
	private final AESSecretKey _key;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	protected AESCipherImpl(final AESSecretKey key) {
		this(CBCMode.AES_CBC_NoPadding, key);
	}	
	protected AESCipherImpl(final CBCMode cbcMode, final AESSecretKey key) {
		_cbcMode = cbcMode;
		_key = key;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public byte[] encrypt(final byte[] message) {
		try {
			Cipher cipher = Cipher.getInstance(_cbcMode.getName());
			SecretKeySpec keySpec = new SecretKeySpec(_key.getKey().getEncoded(),"AES");
			IvParameterSpec ivParameterSpec = new IvParameterSpec(_key.getIV());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);
			byte[] cipherText = cipher.doFinal(message);
			return cipherText;
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}
	@Override
	public byte[] decrypt(final byte[] cipherText)  {
		try {
			Cipher cipher = Cipher.getInstance(_cbcMode.getName());
			SecretKeySpec keySpec = new SecretKeySpec(_key.getKey().getEncoded(), "AES");
			IvParameterSpec ivSpec = new IvParameterSpec(_key.getIV());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			byte[] decryptedText = cipher.doFinal(cipherText);
			return decryptedText;
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}
}
