package r01f.events.security;

import jakarta.inject.Inject;

import com.google.common.eventbus.DeadEvent;
import com.google.common.eventbus.Subscribe;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.annotations.UseEMailNotifier;
import r01f.core.services.notifier.annotations.UseLogNotifier;
import r01f.core.services.notifier.annotations.UseMessagingNotifier;
import r01f.core.services.notifier.annotations.UsePushMessageNotifier;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentMessage;
import r01f.internal.notifier.security.user.pairedphone.PairedPhoneLoginConsentNotifier;
import r01f.internal.notifier.security.user.passwordrecover.UserPasswordRecoverNotifier;
import r01f.internal.notifier.security.user.registration.UserRegistrationNotifier;
import r01f.services.core.events.COREEventBusEventListener;

/**
 * Contains event listeners for the alarm events
 * consumes {@link UserPasswordRecoverMessage} objects and uses a concrete notifier service (email / sms / voice...)
 * to post a notification
 * Multiple event listeners are available
 * <ul>
 * 		<li>SMS based: notifies using SMS services to send a SMS / push notification to the security person mobile</li>
 * 		<li>EMail based: notifies using SMTP services to send an email to the security person mobile</li>
 * 		<li>Voice based: notifies using twilio services to send a voice call to the security person mobile</li>
 * 		<li>Lob based</li>
 * </ul>
 * All them implements {@link UserPasswordRecoverNotifier} that contains a single method: sendNotification(message)
 */
@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class EventListenersForSecurityEvents {
/////////////////////////////////////////////////////////////////////////////////////////
//  Consumes UserPasswordRecoveryMessage objects and uses a concrete XXNotifierServices (email / sms / voice...)
//	to post a notification
//  IMPORTANT!!
//  Guava's {@link EventBus} does not cope with generic events:
//  		There MUST exist specific event handlers associated with CRUD events for each of the indexable model objects
//  		These event handlers are registered at {@link BeanImplementedPersistenceServicesCoreBootstrapGuiceModuleBase} where the @Subscribe annotated method is
//  		used to know the event-type and associate the event type with the event handler
//
//   	The underlying problem is that if a generic event like CRUDOperationEvent<R extends PersistableModelObject<? extends OID>> is used
//  		due to type erasure, ALL event handlers would be associated with the SAME raw event-type: {@link CRUDOperationEvent}
//  		The consequences of this multiple-association is that ALL three event handlers will be called for any event, independently of
//  		the event type
/////////////////////////////////////////////////////////////////////////////////////////
	private static abstract class EventListenerForUserLoginMessageBase<E>
					   implements COREEventBusEventListener {	// DO NOT forget: this automatically registers the listener at the event bus

		public void onMessage(final E pwdRecoverMsg) {
			log.info("[User Login event handler]: received message of type {}",
					 pwdRecoverMsg.getClass());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER PASSWORD RECOVER
/////////////////////////////////////////////////////////////////////////////////////////
	private static abstract class EventListenerForUserPasswordRecoverMessageBase
						  extends EventListenerForUserLoginMessageBase<UserPasswordRecoverMessage> {

		private final UserPasswordRecoverNotifier _notifierServices;

		public EventListenerForUserPasswordRecoverMessageBase(final UserPasswordRecoverNotifier notifierServices) {
			_notifierServices = notifierServices;
		}

		@Subscribe	// subscribes this event listener at the EventBus
		public void onPasswordRecoverMessage(final UserPasswordRecoverMessage pwdRecoverMsg) {
			super.onMessage(pwdRecoverMsg);
			try {
				// [2]-Check if the notifier is enabled
				if (!_notifierServices.isEnabled()) {
					log.warn("[User Password recover event handler]: {} is NOT enabled; the notification will not be sent",
							 _notifierServices.getClass().getSimpleName());
					return;
				}
				// [2]-Use the concrete [notifierServices] (sms/voice/email...) to send the message
				log.info("[User Password recover event handler]: use {} notifier to notify",
						  _notifierServices.getClass().getSimpleName());
				_notifierServices.sendNotification(pwdRecoverMsg);
			} catch (final Throwable th) {
				log.error("Error notifying a password recovery {}",th.getMessage(),th);
			}
		}
	}
	public static class EventListenerForPasswordRecoverMessageLog
			    extends EventListenerForUserPasswordRecoverMessageBase {
		@Inject
		public EventListenerForPasswordRecoverMessageLog(@UseLogNotifier final UserPasswordRecoverNotifier notifierServices) {
			super(notifierServices);
		}
		@Subscribe
		public void onDeadEvent(final DeadEvent deadEvent) {
			// for debugging purposes
		}
	}
	public static class EventListenerForPasswordRecoverMessageNotifyBySMS
			    extends EventListenerForUserPasswordRecoverMessageBase {
		@Inject
		public EventListenerForPasswordRecoverMessageNotifyBySMS(@UseMessagingNotifier final UserPasswordRecoverNotifier notifierServices) {
			super(notifierServices);
		}
	}
	public static class EventListenerForPasswordRecoverMessageNotifyByEMail
			    extends EventListenerForUserPasswordRecoverMessageBase {
		@Inject
		public EventListenerForPasswordRecoverMessageNotifyByEMail(@UseEMailNotifier final UserPasswordRecoverNotifier notifierServices) {
			super(notifierServices);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	USER REGISTRATION
/////////////////////////////////////////////////////////////////////////////////////////
	private static abstract class EventListenerForUserRegistrationMessageBase
						  extends EventListenerForUserLoginMessageBase<UserRegistrationMessage> {

		private final UserRegistrationNotifier  _notifierServices;

		public EventListenerForUserRegistrationMessageBase(final UserRegistrationNotifier notifierServices) {
			_notifierServices = notifierServices;
		}

		@Subscribe	// subscribes this event listener at the EventBus
		public void onUserRegistrationMessage(final UserRegistrationMessage usrRegistrationMsg) {
			super.onMessage(usrRegistrationMsg);
			try {
				// [2]-Check if the notifier is enabled
				if (!_notifierServices.isEnabled()) {
					log.warn("[User registration event handler]: {} is NOT enabled; the notification will not be sent",
							 _notifierServices.getClass().getSimpleName());
					return;
				}
				// [2]-Use the concrete [notifierServices] (sms/voice/email...) to send the message
				log.info("[User registration event handler]: use {} notifier to notify",
						  _notifierServices.getClass().getSimpleName());
				_notifierServices.sendNotification(usrRegistrationMsg);
			} catch (final Throwable th) {
				log.error("Error notifying a user registration {}",th.getMessage(),th);
			}
		}
	}
	public static class EventListenerForUserRegistrationMessageLog
			    extends EventListenerForUserRegistrationMessageBase {
		@Inject
		public EventListenerForUserRegistrationMessageLog(@UseLogNotifier final UserRegistrationNotifier notifierServices) {
			super(notifierServices);
		}
		@Subscribe
		public void onDeadEvent(final DeadEvent deadEvent) {
			// for debugging purposes
		}
	}
	public static class EventListenerForUserRegistrationMessageNotifyBySMS
			    extends EventListenerForUserRegistrationMessageBase {
		@Inject
		public EventListenerForUserRegistrationMessageNotifyBySMS(@UseMessagingNotifier final UserRegistrationNotifier notifierServices) {
			super(notifierServices);
		}
	}
	public static class EventListenerForUserRegistrationMessageNotifyByEMail
			    extends EventListenerForUserRegistrationMessageBase {
		@Inject
		public EventListenerForUserRegistrationMessageNotifyByEMail(@UseEMailNotifier final UserRegistrationNotifier notifierServices) {
			super(notifierServices);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PAIRED PHONE LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	private static abstract class EventListenerForUserPairedPhoneLoginMessageBase
	  extends EventListenerForUserLoginMessageBase<PairedPhoneLoginConsentMessage> {

		private final PairedPhoneLoginConsentNotifier _loginConsentNotifier;

		public EventListenerForUserPairedPhoneLoginMessageBase(final PairedPhoneLoginConsentNotifier notifierServices) {
			_loginConsentNotifier = notifierServices;
		}

		@Subscribe	// subscribes this event listener at the EventBus
		public void onUserPairedPhoneLoginConsent(final PairedPhoneLoginConsentMessage consentNotificationMsg) {
			 log.info("...received on event bus {} ",consentNotificationMsg.getPushMesagge().getTitle());
			 _loginConsentNotifier.sendNotification(consentNotificationMsg);
		}
	 }

	public static class EventListenerForUserPairedPhoneLoginMessagePush
		extends EventListenerForUserPairedPhoneLoginMessageBase {

		@Inject
		public EventListenerForUserPairedPhoneLoginMessagePush(@UsePushMessageNotifier final PairedPhoneLoginConsentNotifier notifierServices) {
			super(notifierServices);
		}

		@Subscribe
		public void onDeadEvent(final DeadEvent deadEvent) {
			// for debugging purposes
		}
	}

}
