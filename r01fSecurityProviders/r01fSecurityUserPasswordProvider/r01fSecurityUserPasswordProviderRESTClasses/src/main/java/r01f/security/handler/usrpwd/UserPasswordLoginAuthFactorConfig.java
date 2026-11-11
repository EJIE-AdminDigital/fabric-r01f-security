package r01f.security.handler.usrpwd;

import java.util.Collection;
import java.util.Properties;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;


@Accessors(prefix="_")
public class UserPasswordLoginAuthFactorConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////	
	static String AUTH_FACTORS_PROPERTY = "auth_factors";
	static String AUTH_FACTORS_VALIDATION_URI_PROPERTY = "auth_factors_validationUrl";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter private Collection<LoginAuthSecondFactorTypeKey> _authFactors;
	@Getter @Setter private Url _authFactorsValidationUri;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////	
	public static UserPasswordLoginAuthFactorConfig createFrom(final Properties securityProviderSecretConfig) {
		UserPasswordLoginAuthFactorConfig cfg = new UserPasswordLoginAuthFactorConfig();
		String auth_factors_list = securityProviderSecretConfig.getProperty(AUTH_FACTORS_PROPERTY);
		if (auth_factors_list == null ) return null;
		
		// .. the auth factors
		Collection<LoginAuthSecondFactorTypeKey> authFactors = null;
		authFactors = Pattern.compile(";")
							 .splitAsStream(auth_factors_list)
							 .map((Function<? super String,? extends LoginAuthSecondFactorTypeKey>)LoginAuthSecondFactorTypeKey::fromString )
							 .collect(Collectors.toList());
		// .. a enabled uri for validation
		cfg.setAuthFactors(authFactors);
			
		if (CollectionUtils.hasData(cfg.getAuthFactors()) 
		 && securityProviderSecretConfig.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTY) == null) {
			throw new IllegalStateException(" Not provided any uri for validating user secret known data");
		}
		cfg.setAuthFactorsValidationUri(new Url (securityProviderSecretConfig.getProperty(AUTH_FACTORS_VALIDATION_URI_PROPERTY)));
		return cfg;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public boolean hasOTPWithEMail() {
		return _authFactors.stream()
			               .filter( c -> c.is(LoginAuthSecondFactorTypeKey.OTP_BY_EMAIL))
			               .findAny()
			               .orElse(null) == null ? false : true;
   }
   public boolean hasOTPWithSMS() {
	  return  _authFactors.stream()
			              .filter( c -> c.is(LoginAuthSecondFactorTypeKey.OTP_BY_SMS))
			              .findAny()
			              .orElse(null) == null ? false : true;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////   
	@Accessors(prefix="_")
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
   	public enum LoginAuthSecondFactorTypeKey
	 implements EnumWithCode<String,LoginAuthSecondFactorTypeKey> { // todo String x LoginAuthFactorTypeKeyID
		OTP_BY_EMAIL("otp_by_email"),
		OTP_BY_SMS("otp_by_sms");
		
		@Getter private final String _code;
		@Getter private final Class<String> _codeType = String.class;
		
		public static final boolean canBe(final String code) {
			return EnumWithCode.canBeFromCode(code,LoginAuthSecondFactorTypeKey.class);
		}
		public static final LoginAuthSecondFactorTypeKey fromString(final String code) {
			return EnumWithCode.fromCode(code,LoginAuthSecondFactorTypeKey.class)
							   .orElseThrow(() -> new IllegalArgumentException("no " + LoginAuthSecondFactorTypeKey.class + " element with code=" + code));
		}
	}
}
