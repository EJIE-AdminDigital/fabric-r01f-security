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
 * App dependent config for SMS notifier
 */
@Accessors(prefix="_")
public class SecurityNotifierConfigForSMS
     extends NotifierAppDependentConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Path _msgTemplatePath;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityNotifierConfigForSMS(final CoreAppCode coreAppCode,
										final Path msgTemplatePath) {
		super(NotifierType.SMS,
			  coreAppCode.asAppCode());
		_msgTemplatePath = msgTemplatePath;
	}
	public SecurityNotifierConfigForSMS(final XMLPropertiesForAppComponent props) {
		super(NotifierType.SMS,
			  props);
		 _msgTemplatePath = props.propertyAt(_xPathBase() + "/msgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: {appCode}/notifier/{appCode}EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
	}
	public static final SecurityNotifierConfigForSMS createFrom(final XMLPropertiesForAppComponent props) {
		return new SecurityNotifierConfigForSMS(props);
	}
}
