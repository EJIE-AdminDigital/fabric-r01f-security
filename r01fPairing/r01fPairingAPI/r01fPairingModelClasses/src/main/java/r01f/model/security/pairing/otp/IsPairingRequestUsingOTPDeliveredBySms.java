package r01f.model.security.pairing.otp;

import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.types.contact.Phone;

@MarshallPolymorphicTypeInfo
public interface IsPairingRequestUsingOTPDeliveredBySms
         extends IsPairingRequestUsingOTP {	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public Phone getPhone();
}
