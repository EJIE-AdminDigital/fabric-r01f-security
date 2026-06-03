package r01f.internal.notifier.security.user.pairedphone;

import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import lombok.extern.slf4j.Slf4j;
import r01f.core.notifier.NotifierResponse;
import r01f.core.notifier.NotifierResponseResult;
import r01f.core.notifier.NotifierResponseResultImpl;
import r01f.core.services.notifier.NotifierPushMessage;
import r01f.core.services.notifier.NotifierPushMessageSubscriber;
import r01f.core.services.notifier.NotifierServiceForPushMessage;
import r01f.core.services.notifier.NotifierServiceResponse;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.internal.R01FAppCodes;
import r01f.patterns.Factory;

/**
 * Paired Phone Login PUSH notifier
 */
@Singleton
@Slf4j
public class PairedPhoneLoginConsentNotifierPushImpl
     extends PairedPhoneLoginConsentNotifierBase<NotifierConfigForPushMessage> {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Push notifier services
	 */
	private final NotifierServiceForPushMessage  _pushNotifier;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	@Inject
	public PairedPhoneLoginConsentNotifierPushImpl(final NotifierConfigForPushMessage config,
			                                       final NotifierServiceForPushMessage pushNotifier) {
		super(config);
		_pushNotifier = pushNotifier;

	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  NotifierResponse sendNotification(final PairedPhoneLoginConsentMessage consentMessage) {
		if (!this.isEnabledConsidering(_pushNotifier)) {
			log.warn("PUSH notifier is DISABLED!");
			return null;
		}
		log.info("[PUSH Notifier ({})]================================================",
				 _pushNotifier.getClass().getSimpleName());
		if (consentMessage.getSecurityTokenForPush() == null) {
			log.warn("... NO Token for identify the PAIRED PHONE ");
			return null;
		}

		NotifierServiceResponse <NotifierPushMessageSubscriber> serviceResponse =
					_pushNotifier.notify(// 1. The Push message FROM
										       R01FAppCodes.APP_CODE,
									     // 2. The Push message TO
										       new NotifierPushMessageSubscriber().forToken(consentMessage.getSecurityTokenForPush()),
										// 3. The Push message ITSELF.
											   new Factory<NotifierPushMessage> () {
																				@Override
																				public NotifierPushMessage create() {
																					return consentMessage.getPushMesagge();
																				}}


									);
		return new NotifierResponse() {
									@Override
									public boolean wasSuccessful() {
										return serviceResponse.wasSuccessful();
									}
									@Override @SuppressWarnings("unchecked")
									public NotifierResponseResult<NotifierPushMessageSubscriber> getResponseResult() {
										return new NotifierResponseResultImpl<NotifierPushMessageSubscriber>(serviceResponse);
									}
								};
	}
}
