package r01f.crypto.hashing;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;

@Accessors(prefix="_")
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum HashAlgorithm
   implements EnumWithCode<String,HashAlgorithm>{

	HMAC_SHA256("hmacSha256"),
	HMAC_SHA512("hmacSha512"),
	HMAC_MD5("hmacMD5"),
	MURMUR32("murmur32");

	@Getter private final String _code;
	@Getter private final Class<String> _codeType = String.class;


/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static final HashAlgorithm fromCode(final String code) {
		return EnumWithCode.fromCode(code, HashAlgorithm.class)
			                .orElse(null);
	}
}
