package r01f.model.security.pairing.otp;

import lombok.experimental.Accessors;

@Accessors(prefix="_")
public abstract class PairingOTPRequestBase
          implements IsPairingOTPRequest {

	private static final long serialVersionUID = 8501664132231161907L;
/////////////////////////////////////////////////////////////////////////////////////
// 	METHODS 
/////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isBySms() {
		return this instanceof PairingOTPRequestBySms;
	}
	@Override
	public boolean isByMail() {
		return this instanceof  PairingOTPRequestByMail;
	}
	@Override
	public boolean isMock() {
		return this instanceof  PairingOTPRequestMock;
	}
}
