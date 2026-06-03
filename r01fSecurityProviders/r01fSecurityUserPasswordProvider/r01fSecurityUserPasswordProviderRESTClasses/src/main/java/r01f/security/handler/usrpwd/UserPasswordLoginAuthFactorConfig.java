package r01f.security.handler.usrpwd;

import java.util.Collection;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorTypeKey;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;
@Accessors(prefix="_")
public class UserPasswordLoginAuthFactorConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////	
	static String AUTH_FACTORS_PROPERTIE = "auth_factors";
	static String AUTH_FACTORS_VALIDATION_URI_PROPERTIE = "auth_factors_validationUrl";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter private Collection<LoginAuthFactorTypeKey> _authFactors;
	@Getter @Setter private Url _authFactorsValidationUri;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public static UserPasswordLoginAuthFactorConfig createFrom(final Properties securityProviderSecretConfig) {
		UserPasswordLoginAuthFactorConfig cfg = new UserPasswordLoginAuthFactorConfig();
		String auth_factors_list = securityProviderSecretConfig.getProperty(AUTH_FACTORS_PROPERTIE);
		if (auth_factors_list == null ) return null;
		// .. the auth factors
		Collection<LoginAuthFactorTypeKey> authFactors = Pattern.compile(";").splitAsStream(auth_factors_list)
																			 .map(  (Function<? super String, ? extends LoginAuthFactorTypeKey>) LoginAuthFactorTypeKey::fromString )
																			.collect(Collectors.toList());
		// .. a enabled uri for validation
		cfg.setAuthFactors(authFactors);
			
		if (CollectionUtils.hasData(cfg.getAuthFactors()) 
		 && securityProviderSecretConfig.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTIE) == null) {
			throw new IllegalStateException(" Not provided any uri for validating user secret known data");
		}
		cfg.setAuthFactorsValidationUri(new Url (securityProviderSecretConfig.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTIE)));
		return cfg;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean hasOTPWithEMail() {
		return _authFactors.stream()
			               .filter( c -> c.is(LoginAuthFactorTypeKey.OTP_BY_EMAIL))
			               .findAny()
			               .orElse(null) == null ? false : true;
		
   }
   public boolean hasOTPWithSMS() {
	  return  _authFactors.stream()
			                .filter( c -> c.is(LoginAuthFactorTypeKey.OTP_BY_SMS))
			                .findAny()
			                .orElse(null) == null ? false : true;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
//	public static void main(final String[] argv) {
//		SecurityProviderSecretConfig sc = new SecurityProviderSecretConfig();
//		Properties properties = new  Properties();
//		properties.put(AUTH_FACTORS_PROPERTIE, "otp_by_email");
//		properties.put(AUTH_FACTORS_VALIDATION_URI_PROPERTIE, "http://theuri.com");
//		sc.setProperties(properties);
//		UserPasswordLoginAuthFactorConfig cfg = UserPasswordLoginAuthFactorConfig.createFrom(properties);
//		System.out.println(cfg.hasOTPWithEMail());
//		System.out.println(cfg.hasOTPWithSMS());
//		System.out.println(cfg.getAuthFactorsValidationUri());
//	}
}
