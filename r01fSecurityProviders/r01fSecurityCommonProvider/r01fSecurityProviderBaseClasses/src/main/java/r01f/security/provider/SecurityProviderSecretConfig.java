package r01f.security.provider;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.httpclient.HttpClientProxySettings;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.Memoized;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.types.url.Url;

@MarshallType(as="securityProviderSecretConfig")
@Accessors(prefix="_")
@NoArgsConstructor
public class SecurityProviderSecretConfig
	 extends SecurityProviderConfigBase
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	SECRET key used to create JWT tokens
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="keyData")
	@Getter @Setter private SecurityToken _keyData;
/////////////////////////////////////////////////////////////////////////////////////////
//	Security provider (ie google, xlnets, etc) urls                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * The user login url (the google, xlnets, etc user login url)
	 * BEWARE!!	Do NOT confuse with the APP login url 
	 * 			This is the google / xlnets... login url
	 * 			... the APP login url is usually a web page where all
	 * 			    allowed [security provider]s login means are available
	 */
	@MarshallField(as="loginUrl")
	@Setter private String _loginUrl;
	private final transient Memoized<Url> _translatedLoginUrl = new Memoized<>() {
																		@Override
																		public Url supply() {
																			return _translate(_loginUrl);
																		}
																};
	public Url getLoginUrl() {
		return _translatedLoginUrl.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@MarshallField(as="proxySettings")
	@Getter @Setter private HttpClientProxySettings _proxySettings;	
	public boolean isProxyEnabled() {
		return _proxySettings != null ? _proxySettings.isEnabled()
									  : false;
	}

}
