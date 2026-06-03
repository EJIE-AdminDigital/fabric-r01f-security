package r01f.crypto.model;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.annotations.Immutable;

@Immutable
@Accessors(prefix="_")
public class CryptoKeyPair
  implements Serializable {

	private static final long serialVersionUID = -6332522682883964647L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final PublicKey _publicKey;
	@Getter private final PrivateKey _privateKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CryptoKeyPair(final PublicKey publicKey, final PrivateKey privateKey) {
		_publicKey = publicKey;
		_privateKey = privateKey;
	}
}
