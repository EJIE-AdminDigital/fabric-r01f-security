package r01f.model.security.pairing.otp;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.types.contact.Phone;

@Accessors(prefix="_")
@MarshallType(as="pairingRequestUsingOTPUsingSms")
public interface PairingRequestByOTPUsingSms
         extends PairingRequestByOTP {	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
   /* @MarshallField(as="phone")
	@Getter @Setter protected Phone _phone;*/
	public Phone getPhone();
}
