package r01f.model.security.pairing.otp;

import r01f.model.ModelObject;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface OTPRequest
         extends ModelObject {

	public <R extends OTPRequest> R as(final Class<R> classType);
	public boolean isBySms();
	public boolean isByMail();
	public boolean isMock();
}
