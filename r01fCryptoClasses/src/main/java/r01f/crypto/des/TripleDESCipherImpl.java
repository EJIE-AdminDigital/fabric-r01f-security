package r01f.crypto.des;

import java.nio.charset.Charset;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import r01f.crypto.exceptions.CipherException;
import r01f.crypto.utils.CBCMode;
import r01f.types.StringBase64;
import r01f.types.StringHex;

public class TripleDESCipherImpl
  implements TripleDESCipher {
/////////////////////////////////////////////////////////////////////////////////////////
// 	DEFAULT _charSet
/////////////////////////////////////////////////////////////////////////////////////////	
	public static final String DEFAULT_ENCODING = "CP500";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	private final CBCMode _cbcMode;
	private final TripleDESKey _key;
	private final Charset _charset;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public TripleDESCipherImpl(final TripleDESKey key) {
		this(CBCMode.DESede_CBC_NoPadding, key);
	}	
	protected TripleDESCipherImpl(final CBCMode cbcMode, final TripleDESKey key) {
		_cbcMode = cbcMode;
		_key = key;
		_charset = Charset.forName(DEFAULT_ENCODING);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	ENCRYPT
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public byte[] encrypt(final byte[] messageAsBytes) {
		try {
			Cipher cipher = Cipher.getInstance(_cbcMode.getName());
			SecretKeySpec keySpec = new SecretKeySpec(_key.getKey().getEncoded(),TripleDESKey.ALGORITHM_3DES);
			IvParameterSpec ivParameterSpec = new IvParameterSpec(_key.getIV());
			cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivParameterSpec);			
			// Encoded
			byte[] encodedMessage = new String(messageAsBytes).getBytes(_charset);
			byte[] cipherText = cipher.doFinal(encodedMessage);
			return cipherText;
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}	
	@Override
	public StringHex encryptAsHex(final byte[] messageAsBytes) {
		return StringHex.encode(encrypt(messageAsBytes));		
	}
	@Override
	public StringBase64 encryptAsBase64(final byte[] messageAsBytes) {		
		return StringBase64.encode(encrypt(messageAsBytes));
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	DECRYPT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public byte[] decrypt(final byte[] cipherBytes)  {
		try {
			Cipher cipher = Cipher.getInstance(_cbcMode.getName());
			SecretKeySpec keySpec = new SecretKeySpec(_key.getKey().getEncoded(), TripleDESKey.ALGORITHM_3DES);
			IvParameterSpec ivSpec = new IvParameterSpec(_key.getIV());
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
			String decodedStr = new String( cipher.doFinal(cipherBytes),_charset);
			return  decodedStr.getBytes();		
			
		} catch ( final Throwable th) {
			throw new CipherException(th.getLocalizedMessage(),th);
		}
	}
	@Override
	public byte[] decryptFromHex(final StringHex encrypted) throws CipherException {	
		return decrypt(encrypted.decodeAsBytes());
	}
	@Override
	public byte[] decryptFromBase64(final StringBase64 encrypted) throws CipherException {	
		return decrypt(encrypted.decodeAsBytes());
	}	
}
