package r01f.model.security.pairing;


import r01f.model.ModelObject;
import r01f.model.security.keystore.key.PublicKey;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;


@MarshallPolymorphicTypeInfo
public interface IsPairingResult
         extends ModelObject {
	
	public PublicKey getPublicKey();
}
