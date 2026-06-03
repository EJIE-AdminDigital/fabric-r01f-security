package r01f.model.security.otp.dispatch;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPNotifierTaskOID;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="otpDispatchResponse")
@Accessors(prefix="_")
public class OTPDispatchResponse
     extends OTPResponseBase {

	private static final long serialVersionUID = -4805574268683253492L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="notifierTaskOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final OTPNotifierTaskOID _notifierTaskOid;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPDispatchResponse(final OTPTaskOID taskOid,
							   final OTPNotifierTaskOID notifierTaskOid) {
		super(taskOid);
		_notifierTaskOid = notifierTaskOid;
	}
}