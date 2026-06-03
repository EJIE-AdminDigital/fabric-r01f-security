package r01f.model.security.keystore.key;

import java.security.spec.X509EncodedKeySpec;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;
import r01f.util.types.StringEncodeUtils;

@MarshallType(as="publicKey")
@NoArgsConstructor
@Accessors(prefix="_")
public class PublicKey
   	 extends KeyBase<PublicKey> {

	private static final long serialVersionUID = -1037770988991342899L;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@JsonCreator // to-do StringBase64<> test con Marshallfrom
	public PublicKey(final String publicKeyValueAsBas64) {
		super.setKeyValue(new StringBase64(publicKeyValueAsBas64));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean  hasSpecAlgorithm() {
		 return this.getAlgorithm() != null;
    }
	public X509EncodedKeySpec asX509EncodedKeySpec() {
		return new X509EncodedKeySpec(StringEncodeUtils.decodeBase64(this.getKeyValue().asString().getBytes()));
	}
}