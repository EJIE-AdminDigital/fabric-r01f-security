package r01f.crypto.signature;

import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class SignerServiceBase
		   implements SignerService  {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	  @Getter final Provider  _securityProvider;
	  @Getter final KeyFactory _keyFactory;
	  @Getter final SignatureAlgorithm _signatureAlgorithm;
	  @Getter final Charset _charSet;
/////////////////////////////////////////////////////////////////////////////////////////
//	PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////	  
	protected byte[]  _signData(final String algorithm ,
				                final byte[] data,
			                    final PrivateKey privateKey) {
		try {
			Signature signature = getSignatureAlgorithmForProvider(algorithm);//HashBase64.getInstance(algorithm);
			signature.initSign(privateKey);
			signature.update(data);
			return signature.sign();
		} catch (final InvalidKeyException | NoSuchAlgorithmException  | SignatureException e) {
			throw new SignerServiceException(e.getMessage(), e);
		}
	}
	protected  boolean _verifyData(final String algorithm ,
								   final byte[] data, final byte[] sigBytes,
								   final PublicKey publicKey) {
		try {
			Signature signature = getSignatureAlgorithmForProvider(algorithm);//HashBase64.getInstance(algorithm);
			signature.initVerify(publicKey);
			signature.update(data);

			return signature.verify(sigBytes);

		} catch (final InvalidKeyException | NoSuchAlgorithmException  | SignatureException e) {
			throw new SignerServiceException(e.getMessage(), e);
		}
	}
	private Signature getSignatureAlgorithmForProvider(final String algorithm) throws NoSuchAlgorithmException {
		if (_securityProvider != null ) {
			return Signature.getInstance(algorithm, _securityProvider);
		}
		return Signature.getInstance(algorithm); // default platform security provider will be used
	}
}
