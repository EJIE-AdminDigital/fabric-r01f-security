package r01f.model.security.otp;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;

@Accessors(prefix="_")
@RequiredArgsConstructor
public abstract class OTPResponseBase
           implements IsOTPObject {

	private static final long serialVersionUID = 7766404476560739303L;
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="taskOid",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter private final OTPTaskOID _taskOid;
}
