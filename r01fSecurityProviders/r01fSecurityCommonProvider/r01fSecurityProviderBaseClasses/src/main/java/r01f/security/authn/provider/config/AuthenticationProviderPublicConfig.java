package r01f.security.authn.provider.config;

import java.util.Properties;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.config.ContainsConfigData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.patterns.Memoized;
import r01f.types.url.Url;

@MarshallType(as="authenticationProviderPublicConfig")
@Accessors(prefix="_")
public class AuthenticationProviderPublicConfig
     extends AuthenticationProviderConfigBase
  implements ContainsConfigData {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- ENABLED	
	@MarshallField(as="enabled",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private boolean _enabled = true;
	
////////// --- LOGIN URL	
	/**
	 * The application login url
	 * BEWARE!!	Do NOT confuse with the one of the [identity provider] (ie google, xlnets, etc)
	 * 			This url is the APPLICATION one where all the [identity provider]s login options are listed
	 * 			(google, xlnets, user / password, etc)
	 */
	@MarshallField(as="loginUrl")
	@Setter String _loginUrl;
	private final transient Memoized<Url> _translatedLoginUrl = new Memoized<>() {
																		@Override
																		public Url supply() {
																			return _translate(_loginUrl);
																		}
																};
	public Url getLoginUrl() {
		return _translatedLoginUrl.get();
	}
////////// --- PRINCIPAL DATA FECTH URL	
	/**
	 * The LOCAL endpoint where the [principal data] is exposed
	 * The LOCAL [principal data] might have been created from the [security provider]'s 
	 * user data, BUT it's NOT the same! 
	 */
	@MarshallField(as="principalDataFetchUrl")
	@Setter private String _principalDataFetchUrl;
	private final transient Memoized<Url> _translatedUserDataFetchUrl = new Memoized<>() {
																				@Override
																				public Url supply() {
																					return _translate(_principalDataFetchUrl);
																				}
																		};
	public Url getPrincipalDataFetchUrl() {
		return _translatedUserDataFetchUrl.get();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public AuthenticationProviderPublicConfig() {
		super();
	}
	public AuthenticationProviderPublicConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		super(identityProvider,authnMethod);
	}
	public AuthenticationProviderPublicConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final boolean enabled,final String loginUrl,final String principalDataFetchUrl) {
		super(identityProvider,authnMethod);
		_enabled = enabled;
		_loginUrl = loginUrl;
		_principalDataFetchUrl = principalDataFetchUrl;
	}
	public AuthenticationProviderPublicConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final Properties urlVars,final Properties properties) {
		super(identityProvider,authnMethod,
			  urlVars,properties);
	}
	public AuthenticationProviderPublicConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											  final Properties urlVars,final Properties properties,
											  final boolean enabled,final String loginUrl,final String principalDataFetchUrl) {
		super(identityProvider,authnMethod,
			  urlVars,properties);
		_enabled = enabled;
		_loginUrl = loginUrl;
		_principalDataFetchUrl = principalDataFetchUrl;
	}
	
}
