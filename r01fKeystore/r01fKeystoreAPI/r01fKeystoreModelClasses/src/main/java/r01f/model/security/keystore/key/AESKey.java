package r01f.model.security.keystore.key;

import com.fasterxml.jackson.annotation.JsonCreator;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.StringBase64;

@MarshallType(as="aesKey") //a modelo oject to store shared -aes- key between two parties
@NoArgsConstructor
@Accessors(prefix="_")
public class AESKey
   	 extends KeyBase<AESKey> {

	private static final long serialVersionUID = -1037770988991342899L;
/////////////////////////////////////////////////////////////////////////////////////////
//FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="ivValue", escape=true)
	@Getter @Setter private StringBase64 _ivValue;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@JsonCreator // to-do StringBase64<>
	public AESKey(final String aesKeyValueAsBas64) {
		super.setKeyValue(new StringBase64(aesKeyValueAsBas64));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean  hasSpecAlgorithm() {
		 return this.getAlgorithm() != null;
    }
	public boolean  hasSecretWithIV() {
		 return super.getKeyValue()!= null  && _ivValue != null;
   }


}