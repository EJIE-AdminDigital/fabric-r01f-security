package r01f.model.security.pairing.otp;

import lombok.experimental.Accessors;
import r01f.objectstreamer.annotations.MarshallType;

@Accessors(prefix="_")
@MarshallType(as="otpRequestForPairingBase")
public abstract class OTPRequestBase
  implements OTPRequest {

	private static final long serialVersionUID = 8501664132231161907L;
//////////////////////////////////////////////////////////////////////////////////////
// METHODS TO IMPLEMENT
/////////////////////////////////////////////////////////////////////////////////////
	@Override @SuppressWarnings("unchecked")
	public <R extends OTPRequest> R as(final Class<R> classType) {
		return (R) this;
	}
	@Override
	public boolean isBySms() {
		return this instanceof OTPRequestBySms;
	}
	@Override
	public boolean isByMail() {
		return this instanceof  OTPRequestByMail;
	}
	@Override
	public boolean isMock() {
		return this instanceof  OTPRequestMock;
	}
}
