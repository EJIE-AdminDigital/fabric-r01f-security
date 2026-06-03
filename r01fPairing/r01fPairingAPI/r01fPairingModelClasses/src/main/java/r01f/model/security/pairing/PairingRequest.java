package r01f.model.security.pairing;

import r01f.model.ModelObject;
import r01f.model.security.keystore.key.PublicKey;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface PairingRequest
         extends ModelObject {
	
	public PublicKey getPublicKey();
	
	public <R extends PairingRequest> R as(final Class<R> classType);

	public boolean usingOTP();
}
