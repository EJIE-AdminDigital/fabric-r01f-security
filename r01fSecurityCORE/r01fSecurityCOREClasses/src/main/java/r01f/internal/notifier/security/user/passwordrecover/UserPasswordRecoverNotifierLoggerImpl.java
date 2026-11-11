package r01f.internal.notifier.security.user.passwordrecover;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.events.security.UserPasswordRecoverMessage;

/**
 * A notifier that just logs the message
 */
@Singleton
@Slf4j
public class UserPasswordRecoverNotifierLoggerImpl
     extends UserPasswordRecoverNotifierBase<NotifierConfigForLog> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public UserPasswordRecoverNotifierLoggerImpl(final NotifierConfigForLog config) {
		super(config,
			  null);	// no velocity engine needed for logging
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse sendNotification(final UserPasswordRecoverMessage pwdRecoverMsg) {
		boolean enabled = _config.isEnabled();
		if (!enabled) return null;

		log.info("[LogNotifier]: PASSWORD RECOVER EVENT: principalOid={} using={}",
				 pwdRecoverMsg.getPrincipalOid(),pwdRecoverMsg.getMedium());
		return null;
	}
}
