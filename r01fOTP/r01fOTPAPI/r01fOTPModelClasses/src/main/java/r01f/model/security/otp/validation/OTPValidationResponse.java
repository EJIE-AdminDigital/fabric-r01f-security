package r01f.model.security.otp.validation;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="otpValidationResponse")
@Accessors(prefix="_")
public class OTPValidationResponse
     extends OTPResponseBase {

	private static final long serialVersionUID = -8128342400403464333L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="secretValidated",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final boolean _secretValidated;

	@MarshallField(as="remainingRetryNum",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter public final long _remainingRetryNum;

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPValidationResponse(final OTPTaskOID taskOid,
							   	 final boolean secretValidated,final long remainingRetryNum) {
		super(taskOid);
		_secretValidated = secretValidated;
		_remainingRetryNum = remainingRetryNum;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean wasSecretValidated() {
		return _secretValidated;
	}
}