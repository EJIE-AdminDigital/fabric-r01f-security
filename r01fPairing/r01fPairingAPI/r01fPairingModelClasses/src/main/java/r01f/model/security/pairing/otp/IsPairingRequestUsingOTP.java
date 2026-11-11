package r01f.model.security.pairing.otp;

import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.pairing.IsPairingRequest;
import r01f.oids.SecurityIDs.Secret;

public interface IsPairingRequestUsingOTP
         extends IsPairingRequest {
	
	public boolean isBySms();
	
	public boolean isByMail();
	
	public OTPTaskOID getTaskOid();
	
	public Secret getOtp();
	
}
