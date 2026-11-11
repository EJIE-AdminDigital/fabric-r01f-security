package r01f.model.security.otp.task.dispatch;

import jakarta.mail.internet.MimeMessage;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPEMailDispatchedMessageFactory;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;

public class OTPDispatchRequestByEMail
	 extends OTPDispatchRequestBase<EMailRFC822Address,EMailRFC822Address,	// TO & FROM
						     		MimeMessage> {							// message

	private static final long serialVersionUID = 2534444906491261226L;
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPDispatchRequestByEMail(final OTPTaskOID taskOid,
									 final AppCode appCode,final AppComponent appComponent,
							  		 final EMailRFC822Address from,final EMailRFC822Address to,
							  		 final IsOTPEMailDispatchedMessageFactory messageFactory) {
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
		return OTPDispatchMedium.EMAIL;
	}
}
