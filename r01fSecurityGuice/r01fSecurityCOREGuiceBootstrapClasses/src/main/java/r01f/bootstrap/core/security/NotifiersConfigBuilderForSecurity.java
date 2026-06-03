package r01f.bootstrap.core.security;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.core.services.notifier.config.NotifierConfigForEMail;
import r01f.core.services.notifier.config.NotifierConfigForLog;
import r01f.core.services.notifier.config.NotifierConfigForPushMessage;
import r01f.core.services.notifier.config.NotifierConfigForSMS;
import r01f.core.services.notifier.config.NotifierConfigForVoice;
import r01f.core.services.notifier.config.NotifiersConfigs;
import r01f.core.services.notifier.spi.NotifierSPIUtil;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForEMail;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForPush;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForSMS;
import r01f.internal.notifier.security.config.SecurityNotifierConfigForVoice;
import r01f.patterns.IsBuilder;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class NotifiersConfigBuilderForSecurity
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static NotifiersConfigsBuilderMailStep loadNotifiersConfigFrom(final XMLPropertiesForAppComponent props) {

		return new NotifiersConfigBuilderForSecurity() { /* nothing */ }
						.new NotifiersConfigsBuilderMailStep(props);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderMailStep {

		@Getter private final XMLPropertiesForAppComponent props;

		public NotifiersConfigsBuilderSMSStep forEMail() {
			NotifierConfigForEMail forEMail = NotifierSPIUtil.emailNotifierConfigFrom(props,
																					  // config provider
																					  (impl,properties) -> new SecurityNotifierConfigForEMail(properties));
			return new NotifiersConfigsBuilderSMSStep(props,
													  forEMail);
		}
		public NotifiersConfigsBuilderSMSStep noEMail() {
			return new NotifiersConfigsBuilderSMSStep(props,
													  null);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderSMSStep {

		@Getter private final XMLPropertiesForAppComponent props;
		@Getter private final NotifierConfigForEMail _forEMail;

		public NotifiersConfigsBuilderVoiceStep forSMS() {
			NotifierConfigForSMS forSMS = NotifierSPIUtil.smsNotifierConfigFrom(props,
																				// config provider
																				(impl,properties) -> new SecurityNotifierConfigForSMS(properties));
			return new NotifiersConfigsBuilderVoiceStep(props,_forEMail,forSMS);
		}

		public NotifiersConfigsBuilderVoiceStep noSMS() {
			return new NotifiersConfigsBuilderVoiceStep(props,_forEMail,null);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderVoiceStep {

		@Getter private final XMLPropertiesForAppComponent props;
		@Getter private final NotifierConfigForEMail _forEMail;
		@Getter private final NotifierConfigForSMS _forSMS;

		public NotifiersConfigsBuilderLogStep forVoice() {
			NotifierConfigForVoice forVoice = NotifierSPIUtil.voiceNotifierConfigFrom(props,
																					  // config provider
																					  (impl,properties) -> new SecurityNotifierConfigForVoice(properties));
			return new NotifiersConfigsBuilderLogStep(props,
													  _forEMail,
													  _forSMS,
													  forVoice);
		}

		public NotifiersConfigsBuilderLogStep noVoice() {
			return new NotifiersConfigsBuilderLogStep(props,
													  _forEMail,
													  _forSMS,
													  null);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderLogStep {

		@Getter private final XMLPropertiesForAppComponent props;
		@Getter private final NotifierConfigForEMail _forEMail;
		@Getter private final NotifierConfigForSMS _forSMS;
		@Getter private final NotifierConfigForVoice _forVoice;

		public NotifiersConfigsBuilderPushStep forLog() {
			return new NotifiersConfigsBuilderPushStep(props,
													   _forEMail,
													   _forSMS,
													   _forVoice,
													   new NotifierConfigForLog(props));
		}

		public NotifiersConfigsBuilderPushStep noLog() {
			return new NotifiersConfigsBuilderPushStep(props,
													   _forEMail,
													   _forSMS,
													   _forVoice,
													   null);
		}
	}

	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderPushStep {

		@Getter private final XMLPropertiesForAppComponent props;
		@Getter private final NotifierConfigForEMail _forEMail;
		@Getter private final NotifierConfigForSMS _forSMS;
		@Getter private final NotifierConfigForVoice _forVoice;
		@Getter private final NotifierConfigForLog _forLog;


		public NotifiersConfigsBuilderBuildStep forPush() {
			NotifierConfigForPushMessage forPUSH = NotifierSPIUtil.pushMessageNotifierConfigFrom(props,
																								 // config provider
																								 (impl,properties) -> new SecurityNotifierConfigForPush(properties));
			return new NotifiersConfigsBuilderBuildStep(_forEMail,
														_forSMS,
														_forVoice,
														_forLog,
														forPUSH);
		}
		public NotifiersConfigsBuilderBuildStep noPush() {
			return new NotifiersConfigsBuilderBuildStep(_forEMail,
														_forSMS,
														_forVoice,
														_forLog,
														null);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class NotifiersConfigsBuilderBuildStep {

		@Getter private final NotifierConfigForEMail _forEMail;
		@Getter private final NotifierConfigForSMS _forSMS;
		@Getter private final NotifierConfigForVoice _forVoice;
		@Getter private final NotifierConfigForLog _forLog;
		@Getter private final NotifierConfigForPushMessage _forPushMessage;


		public NotifiersConfigs build() {
			// Assemble all notifier configs
			NotifiersConfigs outCfg = new NotifiersConfigs(_forEMail,
														   _forSMS,
														   _forVoice,
														   _forLog,
														   _forPushMessage);
			log.trace("\n Loading Notifier Configs :\n {}",
					 outCfg.debugInfo());
			return outCfg;
		}
	}
}
