package r01f.internal.notifier.security.user.passwordrecover;


import java.time.Instant;

import org.apache.velocity.app.VelocityEngine;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.notifier.NotifierBase;
import r01f.core.services.notifier.config.NotifierConfig;
import r01f.events.security.UserPasswordRecoverMessage;
import r01f.util.types.datetime.DateTimeMarshaller;

/**
 * Base notifier service
 */
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
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////

	protected static String _alarmDateFormated() {
		return DateTimeMarshaller.forMarshalling(Instant.now())
								 .asStringUsingPattern(DATE_FORMAT,null);
	}
}
