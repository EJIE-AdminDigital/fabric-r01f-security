package r01f.model.security.keystore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.PersistableModelObjectBase;
import r01f.model.security.keystore.key.PublicKey;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreObjectOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
/**
 * Just KeyStore for a public key.
 * @param <O>
 * @param <SELF_TYPE>
 */
@NoArgsConstructor
@Accessors(prefix="_")
@MarshallType(as="keystore")
public class KeystoreBase<O extends KeystoreObjectOID, SELF_TYPE extends KeystoreBase<O, SELF_TYPE>>
	 extends PersistableModelObjectBase<KeystoreOID, SELF_TYPE>
  implements KeystorePersistableObject {

	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="publicKey")
    @Getter @Setter private PublicKey _publicKey;
/////////////////////////////////////////////////////////////////////////////////////////
//	EXTENSION METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public void setPublicKeyFrom(final byte[] bytes) {
		_publicKey = new PublicKey().withValueAsBytes(bytes);
	}
	public void setPublicKeyFrom(final PublicKey pk) {
		_publicKey = pk;
	}

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	
}
