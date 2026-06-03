package r01f.model.security.keystore;
import r01f.model.security.keystore.key.PrivateKey;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface KeystoreWithPKPersistableObject
		 extends KeystorePersistableObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public PrivateKey getPrivateKey();
	public void setPrivateKey(final PrivateKey TK00PublicKeyData);
	
}