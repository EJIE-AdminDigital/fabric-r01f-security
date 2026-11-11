package r01f.security.authn.provider.config;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.config.ContainsConfigData;
import r01f.httpclient.HttpClientProxySettings;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

/**
 * Config XML sample:
 * <pre class="brush:xml">
 * <authenticationProviderConfigSet>
 *		<urlVars>
 *			<frontEndUrlBase>[URL_PROVIDERS_BASE]</frontEndUrlBase>
 *			<restEndPointUrlPath>myAppCORESecurityRESTServicesWar</restEndPointUrlPath>
 *		</urlVars>
 *		<proxySettings enabled="false"
 *					   host="proxy.host"
 *					   port="8080"
 *					   user="user"
 *					   password="pwd"/>
 *		<providers>
 *			<authenticationProviderSecretConfig identityProvider="GOOGLE" authenticationMethod="OAUTH_AUTH_CODE_WITH_PKCE">
 *				<loginUrl>[GOOGLE_OAUTH_AUTH_CODE_WITH_PKCE_LOGIN_URL]</loginUrl>
 *				<keyData>[GOOGLE_OAUTH_AUTH_CODE_WITH_PKCE_KEY]</keyData>
 *	 			<properties>
 *					<key>[KEY]</key>
 *					<secret>[SECRET]</secret>
 *					<authorizationCodeURI>https://accounts.google.com/o/oauth2/auth</authorizationCodeURI>
 *					<accessTokenURI>https://accounts.google.com/o/oauth2/token</accessTokenURI>
 *					<userInfoURI>https://www.googleapis.com/oauth2/v2/userinfo</userInfoURI>
 *					<oauth2callback>$frontEndUrlBase$/$restEndPointUrlPath$/api/google/users/login/oauth2callback</oauth2callback>
 *				</properties>
 *			</authenticationProviderSecretConfig>
 *			...
 *		</providers>
 * </authenticationProviderConfigSet>	
 * </pre>
 */
@MarshallType(as="authenticationProviderConfigSet")
@Slf4j
@Accessors(prefix="_")
public class AuthenticationProviderSecretConfigSet 
  implements ContainsConfigData,
  			 IsAuthenticationProviderSecretConfigSet,
             Iterable<AuthenticationProviderSecretConfig> {	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- PROVIDERS	
	@MarshallField(as="providers")
	@Getter @Setter private Set<AuthenticationProviderSecretConfig> _providers;

////////// --- URL VARS: avoid repeating things in other urls
	@MarshallField(as="urlVars")
	@Getter @Setter protected Properties _urlVars;
	
////////// --- PROXY SETTINGS: avoid repeating things in other urls	
	@MarshallField(as="proxySettings")
	@Getter @Setter private HttpClientProxySettings _proxySettings;
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Iterator<AuthenticationProviderSecretConfig> iterator() {
		return _providers != null ? _providers.iterator()
								  : Collections.emptyIterator();
	}
	public AuthenticationProviderSecretConfig getAuthProviderConfig(final IdentityProvider providerId,final AuthenticationMethod authMethodId) {
		AuthenticationProviderSecretConfig authProviderSecretCfg = _providers.stream()
																			 .filter(providerConfig -> providerConfig.getIdentityProvider().is(providerId)
																					 				&& providerConfig.getAuthenticationMethod().is(authMethodId))
																			 .findFirst().orElse(null);
		if (authProviderSecretCfg == null) {
			String err = Strings.customized("There is no data for [identity provider]={} & [authentication method]={}", 
											providerId,authMethodId);
			log.error(err);
			throw new IllegalStateException(err);
		}
		
		// MIX the url vars & proxy settingsfrom:
		//		- the [identity provider] / [auth method] (if any)
		//		- the [AuthenticationProviderSecretConfigSet] (if any)
		// the ones from the [identity provider] / [auth method] have precedence over the ones from the [AuthenticationProviderSecretConfigSet]
		return  AuthenticationProviderSecretConfigSet.mergeAuthnProviderSecretConfig(authProviderSecretCfg,this);
	}
	public static AuthenticationProviderSecretConfig mergeAuthnProviderSecretConfig(final AuthenticationProviderSecretConfig local,final  IsAuthenticationProviderSecretConfigSet common) {
		// MIX the url vars from:
		//		- the [identity provider] / [auth method] (if any)
		//		- the [AuthenticationProviderSecretConfigSet] (if any)
		// the ones from the [identity provider] / [auth method] have precedence over the ones from the [AuthenticationProviderSecretConfigSet]
		Properties urlVars = _mergeProperties(local.getIdentityProvider(),local.getAuthenticationMethod(),
											  local.getUrlVars(),common.getUrlVars(),
											  "[url vars]");
		// MIX the proxy settings from:
		//	- the [identity provider] / [auth method] (if any)
		//	- the [AuthenticationProviderSecretConfigSet] (if any)
		HttpClientProxySettings proxySettings = local.getProxySettings() != null ? local.getProxySettings()
																				 : common.getProxySettings();	// common proxy settings


		// return a copy of the local config with the mixed url vars and proxy settings
		AuthenticationProviderSecretConfig out = new AuthenticationProviderSecretConfig(local.getIdentityProvider(),local.getAuthenticationMethod(),
																						urlVars,local.getProperties(),
																						local.getKeyData(),local.getLoginUrlTemplate(),proxySettings);
		return local;
	}
	private static Properties _mergeProperties(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod,
											   final Properties local,final Properties common,
											   final String id) {
		// MIX the url vars from:
		//		- the [identity provider] / [auth method] (if any)
		//		- the [AuthenticationProviderSecretConfigSet] (if any)
		// the ones from the [identity provider] / [auth method] have precedence over the ones from the [AuthenticationProviderSecretConfigSet]
		Map<String,String> props = new HashMap<>();
		
		if (CollectionUtils.hasData(local)) {
			// Security provider has custom variables
			log.trace("[identity provider]={} / [auth method]={} HAS {} custom {}",
					  identityProvider,authnMethod,
					  local.size(),
					  id);
			local.forEach((k,v) -> props.put((String)k,(String)v));
		}
		if (CollectionUtils.hasData(common)) {
			// the [identity provider] / [auth method] DOES NOT HAVE custom variables 
			// ... but the [AuthenticationProviderSecretConfigSet] has common custom variables
			log.trace("The {} contains {} COMMON custom {}",
					  AuthenticationProviderSecretConfigSet.class.getSimpleName(),
					  common.size(),
					  id);
			common.entrySet()
				  .stream()
 				  // only add the common url vars that are not already present in the [identity provider] / [auth method] custom vars
				  .filter(e -> !props.containsKey(e.getKey()))
				  .forEach(e -> props.put(e.getKey().toString(),e.getValue().toString()));
		}
		Properties outProps = new Properties();
		if (CollectionUtils.hasData(props)) outProps.putAll(props);
		return outProps;
	}
}

