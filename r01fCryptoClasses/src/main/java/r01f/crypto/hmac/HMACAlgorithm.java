package r01f.crypto.hmac;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum HMACAlgorithm
   implements EnumWithCode<String,HMACAlgorithm>{

	HMAC_SHA256("hmacSha256"),
	HMAC_SHA512("hmacSha512"),
	HMAC_MD5("hmacMD5"),
	MURMUR32("murmur32");

	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;


/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final HMACAlgorithm fromCode(final String code) {
		return EnumWithCode.fromCode(code, HMACAlgorithm.class)
			                .orElse(null);
	}
}
