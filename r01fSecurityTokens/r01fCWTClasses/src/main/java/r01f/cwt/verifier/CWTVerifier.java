package r01f.cwt.verifier;

import r01f.cwt.CWT;


/**
 * CWT verifies interface
 */
public interface CWTVerifier {

	CWTVerificationResult verify(final CWT jwt);
}
