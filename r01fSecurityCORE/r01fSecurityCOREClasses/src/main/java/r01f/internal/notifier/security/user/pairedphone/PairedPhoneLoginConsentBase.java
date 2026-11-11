package r01f.internal.notifier.security.user.pairedphone;

import lombok.experimental.Accessors;
import r01f.core.notifier.NotifierBase;
import r01f.core.services.notifier.config.NotifierConfig;

@Accessors(prefix="_")
abstract class PairedPhoneLoginConsentNotifierBase<C extends NotifierConfig>
	   extends NotifierBase<C,PairedPhoneLoginConsentMessage>
    implements PairedPhoneLoginConsentNotifier {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTORS
/////////////////////////////////////////////////////////////////////////////////////////
	PairedPhoneLoginConsentNotifierBase(final C config) {
		super(config);
	}
}
