package r01f.cwt.verifier;

import java.io.IOException;
import java.security.PublicKey;

import com.authlete.cbor.CBORDecoder;
import com.authlete.cose.COSEException;
import com.authlete.cose.COSEMessage;
import com.authlete.cose.COSESign1;
import com.authlete.cose.COSEVerifier;

import r01f.cwt.CWT;

/**
 * See : https://github.com/authlete/cbor
 */
public class CWTVerifierForCOSESign1
	 extends CWTVerifierForCOSEBase
  implements CWTVerifierForCOSE {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	final PublicKey _publicKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CWTVerifierForCOSESign1( final PublicKey pk) {
		 super();
		_publicKey = pk;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS TO IMPLEMENT (verify|sign|encrypt|mac) depending on CWTCOSESecurityStrategy
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean verify(final CWT cwtAsModelObject) {
		com.authlete.cwt.CWT cwtAuthLete;
		try {
			cwtAuthLete = (com.authlete.cwt.CWT)new CBORDecoder(cwtAsModelObject.getValue().decodeAsBytes()).next();
		    COSEMessage messageTo = cwtAuthLete.getMessage();
			COSESign1 sign1DecToVerify = (COSESign1)messageTo;

			COSEVerifier verifier = new COSEVerifier(_publicKey);
			boolean verify  = verifier.verify(sign1DecToVerify);
			return verify;
		} catch (final IOException | COSEException e) {
			throw new IllegalStateException(e.getLocalizedMessage());
		}
	}
}