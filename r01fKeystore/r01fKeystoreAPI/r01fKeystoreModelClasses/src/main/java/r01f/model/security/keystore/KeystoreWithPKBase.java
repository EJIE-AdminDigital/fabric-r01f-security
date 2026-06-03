package r01f.model.security.keystore;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.keystore.key.PrivateKey;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreObjectOID;
import r01f.objectstreamer.annotations.MarshallField;

@Accessors(prefix="_")
public abstract class KeystoreWithPKBase<O extends KeystoreObjectOID, SELF_TYPE extends KeystoreWithPKBase<O, SELF_TYPE>>
	 extends KeystoreBase<O, SELF_TYPE>
  implements KeystoreWithPKPersistableObject {

	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="privateKey")
    @Getter @Setter private PrivateKey _privateKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setPrivateKeyFrom(final byte[] bytes) {
		_privateKey = new PrivateKey().withValueAsBytes(bytes);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
}
