package r01f.crypto.ecies;

import java.nio.ByteBuffer;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.HexFormat;

import javax.crypto.SecretKey;

import org.bouncycastle.util.Arrays;

import r01f.crypto.CryptoKeyPair;
import r01f.crypto.aes.AESCipher;
import r01f.crypto.aes.AESCipherBuilder;
import r01f.crypto.aes.AESSecretKey;
import r01f.crypto.aes.AESSecretKeyGenerator;
import r01f.crypto.ecc.ECCCipher;
import r01f.crypto.ecc.ECCKeyGenerator;
import r01f.crypto.exceptions.CipherException;
import r01f.crypto.model.ECCEncryptedMessage;
import r01f.crypto.utils.CBCMode;

public class ECIESCipherImpl
  implements ECIESCipher {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final CBCMode _CBCModeForAES;
	private final CBCMode _CBCModeForECC;
	private final int _ivLength;
	private final CryptoKeyPair _keyPair;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOS
/////////////////////////////////////////////////////////////////////////////////////////
	protected ECIESCipherImpl(final CryptoKeyPair keyPair) {
		this(DEFAULT_IV_LENGTH, keyPair);
	}
	protected ECIESCipherImpl(final int ivLength, final CryptoKeyPair keyPair) {
		this(CBCMode.AES_CBC_PKCS5Padding, CBCMode.AES_CBC_NoPadding, ivLength, keyPair);
	}
	protected ECIESCipherImpl(final CBCMode CBCModeForAES, final CBCMode CBCModeForECC,
							  final int ivLength, final CryptoKeyPair keyPair) {
		_CBCModeForAES = CBCModeForAES;
		_CBCModeForECC = CBCModeForECC;
		_ivLength = ivLength;
		_keyPair = keyPair;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public ECCEncryptedMessage encrypt(final byte[] message) {
		try {
			// Generate AES key
			AESSecretKey aesKey = AESSecretKeyGenerator.generateKeyPair();

			// Encrypt message with generated AES key
			AESCipher aesCipher = AESCipherBuilder.createCipher()
												  .usingCBCMode(_CBCModeForAES)
												  .andKey(aesKey)
												  .build();

			byte[] messageEncryptedInAES = aesCipher.encrypt(message);

			String encryptedMessageInHex = HexFormat.of().formatHex(messageEncryptedInAES);
			byte[] encryptedMessageData = encryptedMessageInHex.getBytes();

			// Necessary Key + IV information to reconstruct the key
			int keyInfoLength = aesKey.getKey().getEncoded().length + aesKey.getIV().length;
			byte[] keyInfo = ByteBuffer.allocate(keyInfoLength)
									   .put(aesKey.getKey().getEncoded())
									   .put(aesKey.getIV())
									   .array();

			// Parse ECC keys to encrypt
			PublicKey publicKey = _keyPair.getPublicKey();
			PrivateKey privateKey = _keyPair.getPrivateKey();

			// Create AES secret key to encrypt the message
			SecretKey sharedSecretKey = ECCKeyGenerator.generateSharedSecret(publicKey, privateKey);

			// Generate ECC IV
			ECCCipher eccCipher = new ECCCipher(_CBCModeForECC);
			byte[] iv = eccCipher.generateIv(_ivLength);

			// Encrypt the message using 'aliceSharedSecret'
			byte[] aes256KeyInformationEncryptedInEcc = eccCipher.encrypt(sharedSecretKey, keyInfo, iv);

			// Create B64 String with IV || encrypted AES key information
			String aes256KeyInformationEncryptedInEccInHex = HexFormat.of().formatHex(aes256KeyInformationEncryptedInEcc);
			byte[] eccData = ByteBuffer.allocate(iv.length + aes256KeyInformationEncryptedInEccInHex.getBytes().length)
										.put(iv).put(aes256KeyInformationEncryptedInEccInHex.getBytes()).array();

			return new ECCEncryptedMessage(encryptedMessageData, eccData);
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}

	@Override
	public byte[] decrypt(final ECCEncryptedMessage encryptedMessage) {
		try {

			byte[] eccData = encryptedMessage.getEccData();

			byte[] iv = Arrays.copyOfRange(eccData, 0, _ivLength);
			byte[] keyInfoInHex = Arrays.copyOfRange(eccData, _ivLength, eccData.length);
			byte[] messageDataInHex = encryptedMessage.getMessageData();

			byte[] messageData = HexFormat.of().parseHex(new String(messageDataInHex));
			byte[] eccKeyInfo =  HexFormat.of().parseHex(new String(keyInfoInHex));

			// Parse ECC keys
			PublicKey publicKey = _keyPair.getPublicKey();
			PrivateKey privateKey = _keyPair.getPrivateKey();

			// Create AES secret key to decrypt the message
			SecretKey sharedSecretKey = ECCKeyGenerator.generateSharedSecret(publicKey, privateKey);

			ECCCipher eccCipher = new ECCCipher(_CBCModeForECC);
			byte[] aesKeyInfo = eccCipher.decrypt(sharedSecretKey, eccKeyInfo, iv);

			// Reconstruct AES Key
			AESSecretKey reconstructedKey = new AESSecretKey(aesKeyInfo);

			// Decrypt original message
			AESCipher aesCipher = AESCipherBuilder.createCipher()
													  .usingCBCMode(_CBCModeForAES)
													  .andKey(reconstructedKey)
													 .build();

			byte[] originalMessageDecrypted = aesCipher.decrypt(messageData);
			return originalMessageDecrypted;
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}
}
