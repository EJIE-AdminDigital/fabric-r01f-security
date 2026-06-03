package r01f.internal.notifier.security.user.registration;


import java.time.Instant;

import org.apache.velocity.app.VelocityEngine;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.notifier.NotifierBase;
import r01f.core.services.notifier.config.NotifierConfig;
import r01f.events.security.UserRegistrationMessage;
import r01f.util.types.datetime.DateTimeMarshaller;

/**
 * Base notifier service
 */
@Accessors(prefix="_")
abstract class UserRegistrationNotifierBase<C extends NotifierConfig>
	   extends NotifierBase<C,UserRegistrationMessage>
    implements UserRegistrationNotifier {
/////////////////////////////////////////////////////////////////////////////////////////
// 	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter protected final VelocityEngine _velocityEngine;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	UserRegistrationNotifierBase(final C config,
						 		 final VelocityEngine velocityEngine) {
		super(config);
		_velocityEngine = velocityEngine;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static String _alarmDateFormated() {
		return DateTimeMarshaller.forMarshalling(Instant.now())
								 .asStringUsingPattern(DATE_FORMAT,null);
	}
}
