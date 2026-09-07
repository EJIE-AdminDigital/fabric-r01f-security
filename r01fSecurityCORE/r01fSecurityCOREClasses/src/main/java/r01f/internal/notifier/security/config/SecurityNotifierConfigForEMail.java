package r01f.internal.notifier.security.config;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.core.services.notifier.config.NotifierAppDependentConfigBase;
import r01f.core.services.notifier.config.NotifierEnums.NotifierType;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.util.types.Strings;
import r01f.xmlproperties.XMLPropertiesForAppComponent;

/**
 * App dependent config for EMail notifier
 */
@Accessors(prefix="_")
public class SecurityNotifierConfigForEMail
     extends NotifierAppDependentConfigBase {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Path _msgTemplatePath;
	@Getter private final Path _usrRegMsgTemplatePath;
	@Getter private final Path _msgImagePath;
	@Getter private final Url _pwdRstRedirectUrl;
	@Getter private final Url _usrRegRedirectUrl;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityNotifierConfigForEMail(final CoreAppCode coreAppCode,
										  final Path msgTemplatePath,
										  final Path usrRegMsgPath,
							 	   	   	  final Path msgImagePath,
							 	   	   	  final Url pwdRstRedirectUrl,
							 	   	   	  final Url usrRegRedirectUrl) {
		super(NotifierType.EMAIL,
			  coreAppCode.asAppCode());
		_msgTemplatePath = msgTemplatePath;
		_usrRegMsgTemplatePath = usrRegMsgPath;
		_msgImagePath = msgImagePath;
		_pwdRstRedirectUrl = pwdRstRedirectUrl;
		_usrRegRedirectUrl = usrRegRedirectUrl;
	}
	public SecurityNotifierConfigForEMail(final XMLPropertiesForAppComponent props) {
		super(NotifierType.EMAIL,
			  props.getAppCode());
 	     // template path
		 _msgTemplatePath = props.propertyAt(_xPathBase() + "/msgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: {appCode}/notifier/{appCode}EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
		  _usrRegMsgTemplatePath = props.propertyAt(_xPathBase() + "/usrRegMsgTemplate")
								 .asPath(Strings.customized("{}/notifier/{}{}MessageTemplate.vm",	// ie: {appCode}/notifier/{appCode}EMAILMessageTemplate.vm
										  					_appCode,
															_appCode,_type));
		 _msgImagePath = props.propertyAt(_xPathBase() + "/msgHeaderImage")
						 	   .asPath(Strings.customized("{}/notifier/{}Logo.gif",
								 						  props.getAppCode(),props.getAppCode()));
		 _pwdRstRedirectUrl = props.propertyAt(_xPathBase() + "/pwdRstRedirectUrl").asUrl();
		 _usrRegRedirectUrl = props.propertyAt(_xPathBase() + "/usrRegRedirectUrl").asUrl();
	}
	public static final SecurityNotifierConfigForEMail createFrom(final XMLPropertiesForAppComponent props) {
		return new SecurityNotifierConfigForEMail(props);
	}
}
