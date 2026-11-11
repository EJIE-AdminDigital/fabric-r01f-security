package r01f.core.bootstrap.spring.security.authn.providers;

import java.util.Collections;
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
import r01f.model.security.IdentityProviderAndAuthnMethod;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfigSet;
import r01f.security.authn.provider.config.IsAuthenticationProviderSecretConfigSet;


@Slf4j
@Accessors(prefix="_")
public class AuthnProvidersSecretSpringBootstrapConfiguration 
  implements ContainsConfigData,
  			 IsAuthenticationProviderSecretConfigSet,
             Iterable<AuthenticationProviderSecretConfig> {	
/////////////////////////////////////////////////////////////////////////////////////////
//  PROPERTIES PREFIX (yml)
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String PROPERTIES_PREFIX = "security-provider-config-set";
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
////////// --- PROVIDERS
	// The key is a combination of the [identity provider] and the [authentication method]
	// as [identityProvider]/[authenticationMethod]
	@Getter @Setter private Map<IdentityProviderAndAuthnMethod,AuthenticationProviderSecretConfig> _providers;
	
////////// --- URL VARS: avoid repeating things in other urls
	@Getter @Setter protected Properties _urlVars;
	
////////// --- PROXY SETTINGS: avoid repeating things in other urls	
	@Getter @Setter private HttpClientProxySettings _proxySettings;
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Iterator<AuthenticationProviderSecretConfig> iterator() {
		return _providers != null ? this.asSet().iterator()
								  : Collections.emptyIterator();
	}
	public Set<AuthenticationProviderSecretConfig> asSet() {     
        return Set.copyOf(_providers.values());
    }	
	
	public AuthenticationProviderSecretConfig getProvider(final IdentityProvider identityProvider, final AuthenticationMethod authnMethod) { 
	    IdentityProviderAndAuthnMethod key = new IdentityProviderAndAuthnMethod(identityProvider, authnMethod);	
	    AuthenticationProviderSecretConfig authnProviderConfig = _providers != null ? _providers.get(key) : null;	 
	    return AuthenticationProviderSecretConfigSet.mergeAuthnProviderSecretConfig(authnProviderConfig, this);
	}
}

