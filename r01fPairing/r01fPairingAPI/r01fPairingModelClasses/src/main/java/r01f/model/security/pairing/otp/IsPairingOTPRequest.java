package r01f.model.security.pairing.otp;

import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface IsPairingOTPRequest
         extends ModelObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean isBySms();
	public boolean isByMail();
	public boolean isMock();
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@SuppressWarnings("unchecked")
	public default <R extends IsPairingOTPRequest> R as(final Class<R> classType) {
		return (R) this;
	}
}
