package r01f.model.security.login.apikey;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import lombok.experimental.Accessors;
import r01f.crypto.CryptoKeyPair;
import r01f.model.security.SecurityObject;


@Accessors(prefix="_")
public class ApiRSAKeyPair
	 extends CryptoKeyPair
  implements SecurityObject {
	
	private static final long serialVersionUID = 3934912904861805359L;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiRSAKeyPair(final RSAPublicKey publicKey,final RSAPrivateKey privateKey,final byte[] publicKeyFingerprint) {
		super(publicKey,privateKey,publicKeyFingerprint);
	}
}

