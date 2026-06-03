package r01f.crypto.httpsig;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;


@MarshallType(as="payloadDigest")
@Accessors(prefix="_")
@RequiredArgsConstructor
public class HttpRequestPayloadDigest {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="digest")
    @Getter private final StringBase64 _digest;

	@MarshallField(as="_alg")
    @Getter private final String _alg;

	public HttpRequestPayloadDigest(final StringBase64 digest) {
		_digest = digest;
		_alg = "SHA-256";
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Sample:
// 		"SHA-256=" + _digest.asString()
/////////////////////////////////////////////////////////////////////////////////////////
	public String asString() {
		StringBuffer normalizedStr = new StringBuffer();
		normalizedStr.append(_alg + "=" + _digest.asString() );
		return normalizedStr.toString();
	}
}
