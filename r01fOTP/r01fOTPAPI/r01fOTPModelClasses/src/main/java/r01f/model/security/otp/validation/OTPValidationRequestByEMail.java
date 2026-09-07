package r01f.model.security.otp.validation;

import lombok.experimental.Accessors;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;



@Accessors(prefix="_")
public class OTPValidationRequestByEMail
     extends OTPValidationRequestBase<EMail> {

	private static final long serialVersionUID = 764373603014902357L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPValidationRequestByEMail(final AppCode appCode,final AppComponent appComp,final OTPTaskOID taskOid,
									   final EMail key,final Password secret) {
		super(appCode,appComp,taskOid,
			  key,secret);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public EMail getEMail() {
		return _key;
	}
	@Override
	public OTPValidationRequestKey getKeyType() {
		return OTPValidationRequestKey.EMAIL;
	}
}