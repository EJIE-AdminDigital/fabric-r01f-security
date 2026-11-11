package r01f.security.authn.provider.config;



import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.httpclient.HttpClientProxySettings;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.patterns.Memoized;
import r01f.types.url.Url;

@MarshallType(as="authenticationProviderSecretConfig")
@Accessors(prefix="_")
public class AuthenticationProviderSecretConfig
	 extends AuthenticationProviderConfigBase
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
////////// --- SECRET key used to create JWT tokens
	@MarshallField(as="keyData")
	@Getter @Setter private SecurityToken _keyData;
	
////////// --- 	Identity provider (ie google, xlnets, etc) urls
	/**
	 * The user login url (the google, xlnets, etc user login url)
	 * BEWARE!!	Do NOT confuse with the APP login url 
	 * 			This is the [identity provider] (google / xlnets...) login url
	 * 			... the APP login url is usually a web page where all
	 * 			    allowed [security provider]s login means are available
	 */
	@MarshallField(as="loginUrl")
	@Setter private String _loginUrl;
	
	protected String getLoginUrlTemplate() {
		return _loginUrl;
	}
	private final transient Memoized<Url> _translatedLoginUrl = new Memoized<>() {
																		@Override
																		public Url supply() {
																			return _translate(_loginUrl);
																		}
																};
	public Url getLoginUrl() {
		return _translatedLoginUrl.get();
	}
////////// --- Proxy settings	
	@MarshallField(as="proxySettings")
	@Getter @Setter private HttpClientProxySettings _proxySettings;	
	public boolean isProxyEnabled() {
		return _proxySettings != null ? _proxySettings.isEnabled()
									  : false;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticationProviderSecretConfig() {
		super();
	}
	public AuthenticationProviderSecretConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		super(identityProvider,authnMethod);
	}
	public AuthenticationProviderSecretConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final Properties urlVars,final Properties properties) {
		super(identityProvider,authnMethod,
			  urlVars,properties);
	}
	public AuthenticationProviderSecretConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final SecurityToken keyData,final String loginUrl,final HttpClientProxySettings proxySettings) {
		super(identityProvider,authnMethod);
		_keyData = keyData;
		_loginUrl = loginUrl;
		_proxySettings = proxySettings;
	}
	public AuthenticationProviderSecretConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final Properties urlVars,final Properties properties,
											  final SecurityToken keyData,final String loginUrl,final HttpClientProxySettings proxySettings) {
		super(identityProvider,authnMethod,
			  urlVars,properties);
		_keyData = keyData;
		_loginUrl = loginUrl;
		_proxySettings = proxySettings;
	}
}
