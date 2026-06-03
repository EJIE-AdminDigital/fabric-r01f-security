package r01f.internal.notifier.security.user.pairedphone;

import java.time.Instant;

import lombok.experimental.Accessors;
import r01f.core.notifier.NotifierBase;
import r01f.core.services.notifier.config.NotifierConfig;
import r01f.util.types.datetime.DateTimeMarshaller;

/**
 * Base notifier service
 */
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
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

	protected static String _alarmDateFormated() {
		return DateTimeMarshaller.forMarshalling(Instant.now())
								 .asStringUsingPattern(DATE_FORMAT,null);
	}
}
