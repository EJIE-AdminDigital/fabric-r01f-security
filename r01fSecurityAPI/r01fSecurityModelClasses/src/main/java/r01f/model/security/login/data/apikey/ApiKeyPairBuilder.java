package r01f.model.security.login.data.apikey;

import org.bouncycastle.jcajce.provider.digest.Keccak;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.crypto.CryptoKeyPairBuilder;
import r01f.crypto.CryptoKeyPairSerialized;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class ApiKeyPairBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	private static final int DEF_KEY_SIZE = 2048;
/////////////////////////////////////////////////////////////////////////////////////////
//	BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static ApiKeyPair createRSA() {
		return ApiKeyPairBuilder.createRSA(DEF_KEY_SIZE);
	}
	public static ApiKeyPair createRSA(final int size) {
		CryptoKeyPairSerialized keyPair = CryptoKeyPairBuilder.createRSA(size,
															   // fingerprint provider
															   thePublicKey -> {
														            // Keccak256 based hash fingerprint ( Keccak256 popular and modern hash algorithm provides no very long hash )
																	Keccak.Digest256 digest256 = new Keccak.Digest256();
																	byte[] fingerPrint = digest256.digest(thePublicKey.getEncoded());
																	return fingerPrint;
															   });
		ApiKeyPair outKeyPair = new ApiKeyPair(keyPair);
		return outKeyPair;
	}
}
