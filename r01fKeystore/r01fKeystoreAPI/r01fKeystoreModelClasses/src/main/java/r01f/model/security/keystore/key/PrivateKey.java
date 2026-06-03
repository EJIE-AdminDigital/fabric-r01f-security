package r01f.model.security.keystore.key;

import java.security.spec.PKCS8EncodedKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;
import r01f.util.types.StringEncodeUtils;

@MarshallType(as="privateKey")
@NoArgsConstructor
@Accessors(prefix="_")
public class PrivateKey
     extends KeyBase<PrivateKey> {

	private static final long serialVersionUID = 1304285270347077949L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	@JsonCreator // to-do StringBase64<> test con Marshallfrom
	public PrivateKey(final String privateKeyValueAsBas64) {
		super.setKeyValue(new StringBase64(privateKeyValueAsBas64));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
    public PKCS8EncodedKeySpec  asPKCS8EncodedKeySpec() {
		return new PKCS8EncodedKeySpec (StringEncodeUtils.decodeBase64(this.getKeyValue().asString().getBytes()));
	}
}