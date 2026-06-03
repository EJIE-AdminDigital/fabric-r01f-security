package r01f.model.security.otp.dispatch;

import jakarta.mail.internet.MimeMessage;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class OTPDispatchedMessageFactories {
/////////////////////////////////////////////////////////////////////////////////////////
//	BASE
/////////////////////////////////////////////////////////////////////////////////////////
	public interface OTPDispatchedMessageFactory<F,T,		// from & to
								  	   			 M> {		// message {
		public M createMessageUsing(final F from,final T to,
									final Password password);

	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SMS
/////////////////////////////////////////////////////////////////////////////////////////
	public interface OTPSMSDispatchedMessageFactory
			 extends OTPDispatchedMessageFactory<OwnedContactMean<Phone>,Phone,
						    	       			 String> {
		// just extend
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	EMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	public interface OTPEMailDispatchedMessageFactory
			 extends OTPDispatchedMessageFactory<EMailRFC822Address,EMailRFC822Address,
						    	       			 MimeMessage> {
		// just extend
	}
}
