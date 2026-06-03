package r01f.crypto.signature;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;

@Accessors(prefix="_")
@MarshallType(as="signature")
public  final class SignatureBase64 { // Used this name HashBase64, becase of "Signature" name is used by java.security.Signature
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Raw value of signature as base64
	 */
	@MarshallField(as="value")
	@Getter final StringBase64 _value;
	/**
	 * Charset
	 */
	@MarshallField(as="charset")
	@Getter final Charset _charset;
	/**
	 * HashAlgorithm
	 */
	@MarshallField(as="signatureAlgorithm")
	@Getter final SignatureAlgorithm _signatureAlgorithm;
	/**
	 * Timestamp
	 */
	@MarshallField(as="tst")
	@Getter final long _tst;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////	
	public SignatureBase64(final StringBase64 base64String,final Charset charset,final SignatureAlgorithm alg,final long tst ) {
		_value  = base64String;
		_signatureAlgorithm = alg;
		_charset = charset;
		_tst = tst;
	}
}