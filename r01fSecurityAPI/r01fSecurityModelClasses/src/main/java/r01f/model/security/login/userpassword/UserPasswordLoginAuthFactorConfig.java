package r01f.model.security.login.userpassword;

import java.util.Collection;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorTypeKey;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;
@Slf4j
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
   	public static UserPasswordLoginAuthFactorConfig createEmpty() {		
		return new UserPasswordLoginAuthFactorConfig();
	}
	public static UserPasswordLoginAuthFactorConfig createFrom(final Properties properties) {
		// ..check if properties are null
		if ( properties == null ) {
			return createEmpty();
		}
		UserPasswordLoginAuthFactorConfig cfg = new UserPasswordLoginAuthFactorConfig();
		String auth_factors_list = properties.getProperty(AUTH_FACTORS_PROPERTIE);
		if (auth_factors_list == null ) return createEmpty();
		// .. the auth factors
		Collection<LoginAuthFactorTypeKey> authFactors = Pattern.compile(";").splitAsStream(auth_factors_list)
																			 .map(  (Function<? super String, ? extends LoginAuthFactorTypeKey>) LoginAuthFactorTypeKey::fromString )
																			.collect(Collectors.toList());
		// .. a enabled uri for validation
		cfg.setAuthFactors(authFactors);
			
		if  (CollectionUtils.hasData(cfg.getAuthFactors()) 
		  && properties.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTIE) == null) {
			log.warn(" Not provided any uri for validating user secret known data.A default one will be created...");
		}
		cfg.setAuthFactorsValidationUri(new Url(properties.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTIE)));
		return cfg;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean hasOTPWithEMail() {
		if (CollectionUtils.isNullOrEmpty(_authFactors)) return false;			
		return _authFactors.stream()
			               .filter( c -> c.is(LoginAuthFactorTypeKey.OTP_BY_EMAIL))
			               .findAny()
			               .orElse(null) == null ? false : true;
		
   }
   public boolean hasOTPWithSMS() {
	  if (CollectionUtils.isNullOrEmpty(_authFactors)) return false;	
	  return  _authFactors.stream()
			              .filter( c -> c.is(LoginAuthFactorTypeKey.OTP_BY_SMS))
			              .findAny()
			              .orElse(null) == null ? false : true;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
//	public static void main(final String[] argv) {
//		Properties properties = new  Properties();
//		properties.put(AUTH_FACTORS_PROPERTIE, "otp_by_email");
//		properties.put(AUTH_FACTORS_VALIDATION_URI_PROPERTIE, "http://theuri.com");
//		
//		UserPasswordLoginAuthFactorConfig cfg = UserPasswordLoginAuthFactorConfig.createFrom(properties);
//		System.out.println(cfg.hasOTPWithEMail());
//		System.out.println(cfg.hasOTPWithSMS());
//		System.out.println(cfg.getAuthFactorsValidationUri());
//	}
}
