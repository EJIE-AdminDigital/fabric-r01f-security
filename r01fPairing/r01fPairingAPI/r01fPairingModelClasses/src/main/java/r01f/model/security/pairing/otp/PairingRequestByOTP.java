package r01f.model.security.pairing.otp;

import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.pairing.PairingRequest;
import r01f.securitycontext.SecurityIDS.Password;

public interface PairingRequestByOTP
         extends PairingRequest {
	
	public boolean isBySms();
	
	public boolean isByMail();
	
	public OTPTaskOID getTaskOid();
	
	public Password getOtp();
	
}
