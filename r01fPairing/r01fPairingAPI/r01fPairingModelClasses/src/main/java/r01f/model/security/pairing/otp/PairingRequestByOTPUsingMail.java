package r01f.model.security.pairing.otp;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.EMail;

@Accessors(prefix="_")
@MarshallType(as="pairingRequestByOTPMail")
@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface PairingRequestByOTPUsingMail
         extends PairingRequestByOTP  {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getEMail();
}
