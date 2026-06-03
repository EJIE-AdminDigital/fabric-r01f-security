package r01f.model.security.otp.generation;

import lombok.experimental.Accessors;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallType;


@MarshallType(as="otpGenerationResponse")
@Accessors(prefix="_")
public class OTPGenerationResponse
     extends OTPResponseBase {

	private static final long serialVersionUID = 5498390384328599485L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPGenerationResponse(final OTPTaskOID taskOid) {
		super(taskOid);
	}
}