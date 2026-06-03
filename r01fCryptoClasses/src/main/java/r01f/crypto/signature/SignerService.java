package r01f.crypto.signature;

import java.security.spec.KeySpec;



/**
 * Use the SignerServiceBuilder to build instance :
 * -------------------------------------------------
 * 		 SignerServiceBuilder.create()
 *							      .defaultSecurityProviders()
 *							      .andRSAKeyfactory()
 *							      .algorithm_SHA256_WITH_RSA()
 *								  .defaultCharset()
 *							  .build();
 *
 */
public interface SignerService {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public 	SignatureBase64 sign (final KeySpec publicKey, final KeySpec privateKey,
			                      final CharSequence data);

	public 	SignatureBase64 sign (final KeySpec publicKey, final KeySpec privateKey,
                                  final byte[] data);

	public boolean verify (final KeySpec publicKeyRSA,
			               final CharSequence data, final SignatureBase64 sign) ;

	public boolean verify (final KeySpec publicKeyRSA,
                           final byte[] data, final SignatureBase64 sign) ;
/////////////////////////////////////////////////////////////////////////////////////////
//	EXCEPTION
/////////////////////////////////////////////////////////////////////////////////////////
	public static class SignerServiceException 
				extends RuntimeException {

		private static final long serialVersionUID = -3418590032771709185L;

		public SignerServiceException(final String msg,final Throwable th) {
			super(msg,th);
		}
	}
}
