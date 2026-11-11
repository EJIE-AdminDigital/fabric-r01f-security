package r01f.model.security.pairing.otp;

import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.types.contact.EMail;

@MarshallPolymorphicTypeInfo
public interface IsPairingRequestUsingOTPDeliveredByMail
         extends IsPairingRequestUsingOTP  {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getEMail();
}
