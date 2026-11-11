package r01f.internal.notifier.security.user.registration;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.events.security.UserRegistrationMessage;

/**
 * A notifier that just logs the message
 */
@Singleton
@Slf4j
public class UserRegistrationNotifierLoggerImpl
     extends UserRegistrationNotifierBase<NotifierConfigForLog> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public UserRegistrationNotifierLoggerImpl(final NotifierConfigForLog config) {
		super(config,
			  null);	// no velocity engine needed for logging
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public NotifierResponse sendNotification(final UserRegistrationMessage usrRegistrationMsg) {
		boolean enabled = _config.isEnabled();
		if (!enabled) {
			return null;
		}

		log.info("[LogNotifier]: USER REGISTRATION EVENT: userOid={} using={}",
				 usrRegistrationMsg.getUserOid(),usrRegistrationMsg.getMedium());
		log.info("\t>Phone: {}",usrRegistrationMsg.getPhone());
		log.info("\t>EMail: {}",usrRegistrationMsg.getEmail());
		return null;
	}
}
