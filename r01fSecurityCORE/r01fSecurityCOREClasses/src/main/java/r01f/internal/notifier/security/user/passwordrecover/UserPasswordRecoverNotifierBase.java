package r01f.internal.notifier.security.user.passwordrecover;


import java.time.Instant;

import org.apache.velocity.app.VelocityEngine;

import lombok.Getter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierBase;
import r01f.core.services.notifier.NotifierService;
import r01f.core.services.notifier.config.NotifierConfig;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.NotificationMedium;
import r01f.util.types.datetime.DateTimeMarshaller;

/**
 * Base notifier service
 */
@Slf4j
@Accessors(prefix="_")
abstract class UserPasswordRecoverNotifierBase<C extends NotifierConfig>
	   extends NotifierBase<C,UserPasswordRecoverMessage>
    implements UserPasswordRecoverNotifier {
/////////////////////////////////////////////////////////////////////////////////////////
// CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final VelocityEngine _velocityEngine;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	UserPasswordRecoverNotifierBase(final C config,
						 		    final VelocityEngine velocityEngine) {
		super(config);
		_velocityEngine = velocityEngine;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  STATIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static String _alarmDateFormated() {
		return DateTimeMarshaller.forMarshalling(Instant.now())
								 .asStringUsingPattern(DATE_FORMAT,null);
	}
	protected <C extends IsContactMean> C _getContactMean(final UserPasswordRecoverMessage pwdRecoverMsg,
						  							      final NotificationMedium medium,
						  							      final NotifierService<?,?,?> notifier) {
		if (pwdRecoverMsg == null) throw new IllegalArgumentException("The password recover message MUST be NOT null");
		if (pwdRecoverMsg.getMedium().isNOT(NotificationMedium.SMS)) throw new IllegalArgumentException("The password recover message MUST be an PHONE message");
		
		if (!this.isEnabledConsidering(notifier)) {
			log.warn("SMS notifier is DISABLED!");
			return null;
		}
		log.info("[{} Notifier ({})]================================================",
				 medium,notifier.getClass().getSimpleName());
		if (pwdRecoverMsg.getContactMean() == null) {
			log.warn("... NO phones to notify to");
			return null;
		}
		return pwdRecoverMsg.getContactMean() != null ? (C)pwdRecoverMsg.getContactMean() : null;
	}
}
