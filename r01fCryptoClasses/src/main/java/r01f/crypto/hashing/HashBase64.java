package r01f.crypto.hashing;
import java.nio.charset.Charset;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;

@Accessors(prefix="_")
@MarshallType(as="hash")
public final class HashBase64 { // Used this name HashBase64.
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
	@Getter final HashAlgorithm _hashAlgorithm;

	/**
	 * Timestamp
	 */
	@MarshallField(as="tst")
	@Getter final long _tst;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public HashBase64(final StringBase64 base64String,final Charset charset,final HashAlgorithm alg,final long tst ) {
		_value  = base64String;
		_hashAlgorithm = alg;
		_charset = charset;
		_tst = tst;
	}
}