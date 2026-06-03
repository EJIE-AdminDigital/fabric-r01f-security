package r01f.model.security.pairing.otp;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.ModelObject;
import r01f.model.security.otp.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.oids.OTPOIDs.OTPNotifierTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;

@Accessors(prefix="_")
@MarshallType(as="otpResponse")
public class OTPRequestResult 
  implements ModelObject {

	private static final long serialVersionUID = 8563205652723617669L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="otpDispatchResponse")
	@Getter @Setter private OTPDispatchResponse _otpDispatchResponse;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPRequestResult(final OTPDispatchResponse otpDispatchResponse) {
		_otpDispatchResponse = otpDispatchResponse;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public OTPNotifierTaskOID getNotifierTaskOid() {					
		return  _otpDispatchResponse.getNotifierTaskOid();
	}
}
