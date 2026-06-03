package r01f.crypto.ecc;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import r01f.crypto.utils.CBCMode;

public class ECCCipher {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private CBCMode _cbcMode;
	private String _provider;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public ECCCipher() {
		this(CBCMode.AES_CBC_NoPadding, "BC");
	}
	public ECCCipher(final CBCMode cbcMode) {
		this(cbcMode, "BC");
	}
	public ECCCipher(final CBCMode cbcMode, final String providerName) {
		_init(cbcMode, providerName);
	}
	private void _init(final CBCMode cbcMode, final String providerName) {
		_cbcMode = cbcMode != null ? cbcMode : CBCMode.AES_CBC_NoPadding;
		_provider = providerName != null && !providerName.isEmpty() ? providerName : "BC";
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public byte[] encrypt(final SecretKey key, final byte[] plainTextBytes, final byte[] iv) throws NoSuchPaddingException, 
																									NoSuchAlgorithmException, 
																									NoSuchProviderException,
																									InvalidAlgorithmParameterException, 
																									InvalidKeyException, 
																									ShortBufferException, 
																									BadPaddingException,
																									IllegalBlockSizeException {
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(_cbcMode.getName(), _provider);
		byte[] cipherText;

		cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec);
		cipherText = new byte[cipher.getOutputSize(plainTextBytes.length)];
		int encryptLength = cipher.update(plainTextBytes, 0, plainTextBytes.length, cipherText, 0);
		encryptLength += cipher.doFinal(cipherText, encryptLength);

		return cipherText;
	}
	public byte[] decrypt(final SecretKey key, final byte[] cipherTextBytes, final byte[] iv) throws NoSuchPaddingException, 
																									 NoSuchAlgorithmException, 
																									 NoSuchProviderException,
																									 InvalidAlgorithmParameterException, 
																									 InvalidKeyException, 
																									 ShortBufferException, 
																									 BadPaddingException,
																									 IllegalBlockSizeException {
		Key decryptionKey = new SecretKeySpec(key.getEncoded(), key.getAlgorithm());
		IvParameterSpec ivSpec = new IvParameterSpec(iv);
		Cipher cipher = Cipher.getInstance(_cbcMode.getName(), _provider);
		byte[] plainText;

		cipher.init(Cipher.DECRYPT_MODE, decryptionKey, ivSpec);
		plainText = new byte[cipher.getOutputSize(cipherTextBytes.length)];
		int decryptLength = cipher.update(cipherTextBytes, 0, cipherTextBytes.length, plainText, 0);
		decryptLength += cipher.doFinal(plainText, decryptLength);

		return plainText;
	}
	public byte[] generateIv(final int ivLength) {
		return new SecureRandom().generateSeed(ivLength);
	}
}
