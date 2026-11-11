package r01f.model.security.otp.task.dispatch;

import jakarta.mail.internet.MimeMessage;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class OTPDispatchedMessageFactories {
/////////////////////////////////////////////////////////////////////////////////////////
//	BASE
/////////////////////////////////////////////////////////////////////////////////////////
	public interface IsOTPDispatchedMessageFactory<F,T,		// from & to
								  	   			   M> {		// message {
		public M createMessageUsing(final F from,final T to,
									final Secret secret);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SMS
/////////////////////////////////////////////////////////////////////////////////////////
	public interface IsOTPSMSDispatchedMessageFactory
			 extends IsOTPDispatchedMessageFactory<OwnedContactMean<Phone>,Phone,
						    	       			   String> {
		// just extend
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	public interface IsOTPEMailDispatchedMessageFactory
			 extends IsOTPDispatchedMessageFactory<EMailRFC822Address,EMailRFC822Address,
						    	       			   MimeMessage> {
		// just extend
	}
}
