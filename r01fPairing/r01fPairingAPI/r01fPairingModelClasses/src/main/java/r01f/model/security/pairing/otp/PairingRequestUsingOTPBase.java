package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.pairing.PairingRequest;
import r01f.model.security.pairing.PairingRequestBase;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.util.types.Strings;

@Accessors(prefix="_")
@MarshallType(as="otpRequestForPairingBase") 
public class PairingRequestUsingOTPBase
     extends PairingRequestBase
  implements PairingRequestByOTP {
	
	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="taskOid",escape=true)
	@Getter @Setter protected OTPTaskOID _taskOid;

	@MarshallField(as="otp",escape=true)
	@Getter @Setter protected Password _otp;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings({ "unchecked", "cast" })
	public <R extends PairingRequest> R as(final Class<R> classType) {
		if (!( this instanceof PairingRequestByOTP )) {
			throw new IllegalArgumentException(Strings.customized("{} is not  TK00PairingRequestUsingOTP", classType)); 
		}
		return (R) this;
	}
	@Override
	public boolean isBySms() {		
		return this instanceof  PairingRequestByOTPUsingSms;
	}
	@Override
	public boolean isByMail() {
		return this instanceof  PairingRequestByOTPUsingMail;
	}
	@Override
	public boolean usingOTP() {
		return true;
	} 
}
