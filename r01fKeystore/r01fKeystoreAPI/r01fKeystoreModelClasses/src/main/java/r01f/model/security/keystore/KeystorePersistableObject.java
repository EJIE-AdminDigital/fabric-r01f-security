package r01f.model.security.keystore;
import r01f.model.PersistableModelObject;
import r01f.model.security.keystore.key.PublicKey;
import r01f.model.security.keystore.oids.KeystoreOIDs.KeystoreOID;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface KeystorePersistableObject
		 extends PersistableModelObject<KeystoreOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public PublicKey getPublicKey();
	public void setPublicKey(final PublicKey TK00PublicKeyData);
}