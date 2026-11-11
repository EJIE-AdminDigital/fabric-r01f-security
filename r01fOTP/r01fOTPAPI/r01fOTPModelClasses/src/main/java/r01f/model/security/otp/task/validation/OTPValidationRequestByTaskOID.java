package r01f.model.security.otp.task.validation;

import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;



@Accessors(prefix="_")
public class OTPValidationRequestByTaskOID
     extends OTPValidationRequestBase<OTPTaskOID> {

	private static final long serialVersionUID = -7830666454831685338L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPValidationRequestByTaskOID(final AppCode appCode,final AppComponent appComp,
									   	 final OTPTaskOID key,final Secret secret) {
		super(appCode,appComp,
			  key,
			  key,secret);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public OTPTaskOID getTaskOid() {
		return _key;
	}
	@Override
	public OTPValidationRequestKey getKeyType() {
		return OTPValidationRequestKey.TASK_OID;
	}
}