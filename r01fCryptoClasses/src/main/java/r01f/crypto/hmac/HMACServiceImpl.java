package r01f.crypto.hmac;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import com.google.common.hash.Hashing;

import lombok.experimental.Accessors;
import r01f.crypto.signature.SignerService.SignerServiceException;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.types.StringBase64;

@Accessors(prefix="_")
public  class HMACServiceImpl
		extends HMACServiceBase
	implements HMACService  {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public HMACServiceImpl() {
		super(HMACAlgorithm.HMAC_SHA256, 
			  StandardCharsets.UTF_8);
	}
	public HMACServiceImpl(final HMACAlgorithm hashAlgorithm,
			                  final Charset charSet) {
		super(hashAlgorithm, 
			  charSet);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Generates a hmac for the given data with the key as security token.
	 * @param publicKey
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public HMACBase64 createHMAC(final SecurityToken key,
						         final byte[] data) {
		return createHMAC(key,new String(data));
	}
	/**
	 * Generates a hmac for the given data with the key as security token.
	 * @param publicKey
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public HMACBase64 createHMAC(final SecurityToken key,
						         final CharSequence data) {
		byte[]  textToHash = data.toString().getBytes(_charSet); //ensure charset
		byte[] hashedBytes = null;

		switch (_hashAlgorithm) {

			case HMAC_SHA256 :
							 hashedBytes = Hashing.hmacSha256(key.getBytes()).hashBytes(textToHash).asBytes();
						     break;
			case HMAC_SHA512:
						     hashedBytes = Hashing.hmacSha512(key.getBytes()).hashBytes(textToHash).asBytes();
						     break;

			case HMAC_MD5:
							hashedBytes = Hashing.hmacMd5(key.getBytes()).hashBytes(textToHash).asBytes();
						    break;
			default:
							break;
		}

		String hashedBytesAsBase64 =  new String( Base64.getEncoder().encode(hashedBytes));
		// Return hash as base 64 and meta-data ( algo,tst, etc....
		return new HMACBase64(StringBase64.fromBase64EncodedString(hashedBytesAsBase64),
							  _charSet,
							  _hashAlgorithm,
							  Instant.now().toEpochMilli());
	}
}
