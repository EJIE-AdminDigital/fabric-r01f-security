package r01f.services.security.notifier.config.otp;


import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierAppDependentConfigBase;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.types.Path;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * OTP dependent config for SMS notifier
 */
@Accessors(prefix="_")
public class OTPNotifierConfigForSMS
     extends NotifierAppDependentConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Path _msgTemplatePath;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public OTPNotifierConfigForSMS(final XMLPropertiesForAppComponent props) {
		super(NotifierType.SMS,
			  props);
		 _msgTemplatePath = props.propertyAt(_xPathBase() + "/msgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: fin01/notifier/fin01EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
	}
	public static final OTPNotifierConfigForSMS createFrom(final XMLPropertiesForAppComponent props) {
		return new OTPNotifierConfigForSMS(props);
	}
}
