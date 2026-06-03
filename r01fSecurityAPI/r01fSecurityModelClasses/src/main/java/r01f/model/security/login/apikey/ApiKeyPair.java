package r01f.model.security.login.apikey;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import lombok.experimental.Accessors;
import r01f.crypto.CryptoKeyPairSerialized;
import r01f.model.security.SecurityObject;
import r01f.objectstreamer.annotations.MarshallFrom;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityToken;


@MarshallType(as="keyPair")
@Accessors(prefix="_")
public class ApiKeyPair
	 extends CryptoKeyPairSerialized
  implements SecurityObject {
	private static final long serialVersionUID = 3934912904861805359L;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ApiKeyPair(@MarshallFrom("publicKey")   final SecurityToken publicKey,
			          @MarshallFrom("privateKey")  final SecurityToken privateKey,
			          @MarshallFrom("fingerPrint") final SecurityToken fingerPrint) {

		super(publicKey,privateKey,fingerPrint);
	}
	public ApiKeyPair(final SecurityToken publicKey) {
		super(publicKey);
	}
	public <P extends CryptoKeyPairSerialized> ApiKeyPair(final P other) {
		super(other);
	}
	public ApiKeyPair(final RSAPublicKey publicKey,final RSAPrivateKey privateKey,final byte[] fingerPrint) {
		super(publicKey,privateKey,fingerPrint);
	}
}

