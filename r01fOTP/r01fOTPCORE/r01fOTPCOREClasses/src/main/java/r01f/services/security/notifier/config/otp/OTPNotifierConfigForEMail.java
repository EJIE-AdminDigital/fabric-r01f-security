package r01f.services.security.notifier.config.otp;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierAppDependentConfigBase;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.types.Path;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * OTP dependent config for EMail notifier
 */
@Accessors(prefix="_")
public class OTPNotifierConfigForEMail
     extends NotifierAppDependentConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Path _msgTemplatePath;
	@Getter private final Path _msgImagePath;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////

	public OTPNotifierConfigForEMail(final XMLPropertiesForAppComponent props) {
		super(NotifierType.EMAIL,
			  props.getAppCode());
 	     // template path
		 _msgTemplatePath = props.propertyAt(_xPathBase() + "/msgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: fin01/notifier/fin01EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
		_msgImagePath = props.propertyAt(_xPathBase() + "/msgHeaderImage")
						 .asPath(Strings.customized("{}/notifier/{}Logo.gif",
								 					props.getAppCode(),props.getAppCode()));
	}
	public static final OTPNotifierConfigForEMail createFrom(final XMLPropertiesForAppComponent props) {
		return new OTPNotifierConfigForEMail(props);
	}
}
