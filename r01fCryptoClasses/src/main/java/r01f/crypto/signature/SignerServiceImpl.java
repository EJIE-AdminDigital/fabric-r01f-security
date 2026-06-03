package r01f.crypto.signature;

import java.nio.charset.Charset;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.time.Instant;
import java.util.Base64;

import lombok.experimental.Accessors;
import r01f.types.StringBase64;

@Accessors(prefix="_")
public  class SignerServiceImpl
		extends SignerServiceBase
     implements SignerService  {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public SignerServiceImpl(final Provider securityProvider,
			                 final KeyFactory keyFactory,
			                 final SignatureAlgorithm signatureAlgorithm,final Charset charSet) {
		super(securityProvider, keyFactory, signatureAlgorithm, charSet);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Generates a sign for the given data
	 * @param publicKey
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public SignatureBase64 sign(final KeySpec publicKey,final KeySpec privateKey,
							    final byte[] data)  {
		return sign(publicKey,privateKey, new String(data));
	}
	/**
	 * Generates a sign for the given data
	 * @param publicKey
	 * @param privateKey
	 * @param data
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public SignatureBase64 sign(final KeySpec publicKey,final KeySpec privateKey,
							    final CharSequence data)  {
		try {
			PrivateKey privateKeyRSA = getKeyFactory().generatePrivate(privateKey);
			byte[]  textToSignAsUT8Bytes = data.toString().getBytes(_charSet);
			byte[] signedData = _signData(_signatureAlgorithm.getName(),textToSignAsUT8Bytes,privateKeyRSA);
			String signedDataAsBase64String = new String( Base64.getEncoder().encode(signedData));

			// Return signature as base 64 and meta-data ( algo,tst, etc....
			return new SignatureBase64(StringBase64.fromBase64EncodedString(signedDataAsBase64String),
									    _charSet,
									    _signatureAlgorithm,
									    Instant.now().toEpochMilli());
		} catch (final  InvalidKeySpecException e) {
			e.printStackTrace();
			throw new SignerServiceException(e.getMessage(), e);
		}
	}
	/**
	 * Verify the sign for the given data
	 * @param publicKey
	 * @param ticket
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public 	boolean verify(final KeySpec publicKeyRSA,
			               final byte[] data, final SignatureBase64 sign) {
		return verify(publicKeyRSA, new String(data), sign);
	}
	/**
	 * Verify the sign for the given data
	 * @param publicKey
	 * @param ticket
	 * @return
	 * @throws SignerServiceException
	 */
	@Override
	public 	boolean verify(final KeySpec publicKeyRSA,
			               final CharSequence data, final SignatureBase64 sign) {

		try {
			PublicKey publicKey = getKeyFactory().generatePublic(publicKeyRSA);
			byte[]  textToVerifyAsUT8Bytes = data.toString().getBytes(_charSet);
			byte[]  signBytes = sign.getValue().decodeAsBytes();
			return _verifyData(_signatureAlgorithm.getName(),textToVerifyAsUT8Bytes,signBytes, publicKey);

		} catch (final InvalidKeySpecException e) {
			e.printStackTrace();
			throw new SignerServiceException(e.getMessage(), e);
		}
	}
}
