package r01f.model.security.otp.task.dispatch;

import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPSMSDispatchedMessageFactory;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;

public class OTPDispatchRequestBySMS
	 extends OTPDispatchRequestBase<OwnedContactMean<Phone>,Phone,		// from & to
						     		String> {							// message


	private static final long serialVersionUID = -135121288082168940L;

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPDispatchRequestBySMS(final OTPTaskOID taskOid,
								   final AppCode appCode,final AppComponent appComponent,
						    	   final OwnedContactMean<Phone> from,final Phone to,
						    	   final IsOTPSMSDispatchedMessageFactory messageFactory) {
		super(taskOid,
			  appCode,appComponent,
			  from,to,
			  messageFactory);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public OTPDispatchMedium getMedium() {
		return OTPDispatchMedium.SMS;
	}
}
