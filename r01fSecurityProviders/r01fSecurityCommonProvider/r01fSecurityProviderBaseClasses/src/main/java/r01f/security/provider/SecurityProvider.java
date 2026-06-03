package r01f.security.provider;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumWithCode;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;

@Accessors(prefix="_")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SecurityProvider
 implements EnumWithCode<SecurityProviderID,SecurityProvider> {

	USER_PASSWORD(SecurityProviderID.USER_PASSWORD),
	GOOGLE(SecurityProviderID.GOOGLE),
	GILTZA(SecurityProviderID.GILTZA),
	SAML(SecurityProviderID.SAML),	
	XLNETS(SecurityProviderID.XLNETS),
	OSAKIDETZA_AZURE_AD(SecurityProviderID.OSAKIDETZA_AZURE_AD),
	JUSTIZIA(SecurityProviderID.SAML),
	PAIRED_PHONE(SecurityProviderID.PAIRED_PHONE),
	REGISTERED_DEVICE(SecurityProviderID.REGISTERED_DEVICE),
	SYSTEM(SecurityProviderID.SYSTEM);	

	@Getter private final SecurityProviderID _code;
	@Getter private final Class<SecurityProviderID> _codeType = SecurityProviderID.class;
	
	private SecurityProvider(final String code) {
		this(SecurityProviderID.forId(code));
	}
	public static SecurityProvider from(final SecurityProviderID code) {
		return EnumWithCode.fromCode(code,SecurityProvider.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + SecurityProvider.class + " element with code=" + code));
	}
	public static SecurityProvider of(final String code) {
		return SecurityProvider.from(SecurityProviderID.forId(code));
	}
}