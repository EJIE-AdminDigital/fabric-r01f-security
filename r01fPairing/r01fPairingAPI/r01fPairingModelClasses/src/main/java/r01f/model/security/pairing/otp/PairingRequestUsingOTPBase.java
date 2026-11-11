package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.model.security.pairing.PairingRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.Secret;
import r01f.util.types.Strings;

@Accessors(prefix="_")
@MarshallType(as="otpRequestForPairingBase") 
public class PairingRequestUsingOTPBase
     extends PairingRequestBase
  implements IsPairingRequestUsingOTP {
	
	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="taskOid",escape=true)
	@Getter @Setter protected OTPTaskOID _taskOid;

	@MarshallField(as="otp",escape=true)
	@Getter @Setter protected Secret _otp;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isBySms() {		
		return this instanceof  IsPairingRequestUsingOTPDeliveredBySms;
	}
	@Override
	public boolean isByMail() {
		return this instanceof  IsPairingRequestUsingOTPDeliveredByMail;
	}
	@Override
	public boolean usingOTP() {
		return true;
	} 
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings({ "unchecked" })
	public <R extends IsPairingRequest> R as(final Class<R> classType) {
		if (!( this instanceof IsPairingRequestUsingOTP)) throw new IllegalArgumentException(Strings.customized("{} is not {}",
																												classType,IsPairingRequestUsingOTP.class)); 
		return (R) this;
	}
}
