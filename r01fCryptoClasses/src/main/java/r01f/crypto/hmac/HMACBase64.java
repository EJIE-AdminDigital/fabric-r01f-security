package r01f.crypto.hmac;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;

@Accessors(prefix="_")
@MarshallType(as="hash")
public final class HMACBase64 { // Used this name HashBase64.
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Raw value of hash as base64
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
	@MarshallField(as="hashAlgorithm")
	@Getter final HMACAlgorithm _hashAlgorithm;

	/**
	 * Timestamp
	 */
	@MarshallField(as="tst")
	@Getter final long _tst;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public HMACBase64(final StringBase64 base64String,final Charset charset,final HMACAlgorithm alg,final long tst ) {
		_value  = base64String;
		_hashAlgorithm = alg;
		_charset = charset;
		_tst = tst;
	}
}