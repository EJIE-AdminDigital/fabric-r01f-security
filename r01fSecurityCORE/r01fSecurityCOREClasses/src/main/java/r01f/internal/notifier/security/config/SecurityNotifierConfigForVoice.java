package r01f.internal.notifier.security.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierAppDependentConfigBase;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.types.Path;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * App dependent config for Voice notifier
 */
@Accessors(prefix="_")
public class SecurityNotifierConfigForVoice
     extends NotifierAppDependentConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Path _msgTemplatePath;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityNotifierConfigForVoice(final CoreAppCode coreAppCode,
										  final Path msgTemplatePath) {
		super(NotifierType.VOICE,
			  coreAppCode.asAppCode());
		_msgTemplatePath = msgTemplatePath;
	}
	public SecurityNotifierConfigForVoice(final XMLPropertiesForAppComponent props) {
		super(NotifierType.VOICE,
			  props);
		 _msgTemplatePath = props.propertyAt(_xPathBase() + "/msgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: {appCode}/notifier/{appCode}EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
	}
	public static final SecurityNotifierConfigForVoice createFrom(final XMLPropertiesForAppComponent props) {
		return new SecurityNotifierConfigForVoice(props);
	}
}
