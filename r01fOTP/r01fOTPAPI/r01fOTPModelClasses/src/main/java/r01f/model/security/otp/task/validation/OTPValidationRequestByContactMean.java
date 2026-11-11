package r01f.model.security.otp.task.validation;

import lombok.experimental.Accessors;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.IsContactMean;



@Accessors(prefix="_")
public class OTPValidationRequestByContactMean<C extends IsContactMean>
     extends OTPValidationRequestBase<C> {

	private static final long serialVersionUID = 764373603014902357L;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPValidationRequestByContactMean(final AppCode appCode,final AppComponent appComp,
									   		 final OTPTaskOID taskOid,
									   		 final C key,final Secret secret) {
		super(appCode,appComp,
			  taskOid,
			  key,secret);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public C getContactMean() {
		return _key;
	}
	@Override
	public OTPValidationRequestKey getKeyType() {
		return OTPValidationRequestKey.CONTACT_MEAN;
	}
}