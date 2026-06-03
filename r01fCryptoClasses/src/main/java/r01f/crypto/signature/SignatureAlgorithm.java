package r01f.crypto.signature;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

@Accessors(prefix="_")
@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
public enum SignatureAlgorithm {
	SHA256_WITH_RSA("SHA256withRSA"),
	SHA2456_WITH_ECDSA("SHA256withECDSA");

	@Getter private final String _name;
}
