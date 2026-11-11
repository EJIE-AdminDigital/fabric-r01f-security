package r01f.security.login.filter;

import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.bootstrap.security.SecurityLoginFilterGuiceModule;
import r01f.config.ContainsConfigData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallField.MarshallFieldAsXml;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.patterns.Memoized;
import r01f.security.authn.provider.config.AuthenticationProviderPublicConfig;
import r01f.types.url.Url;
import r01f.types.url.UrlQueryString;
import r01f.types.url.UrlQueryStringParam;
import r01f.util.types.StringCustomizeUtils;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="securityLoginFilterConfig")
@Slf4j
@Accessors(prefix="_")
public class SecurityLoginFilterConfig
  implements ContainsConfigData,
			 Iterable<AuthenticationProviderPublicConfig> {

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Test local development environment?
	 */
	@MarshallField(as="test",
				   whenXml=@MarshallFieldAsXml(attr=true))
	@Getter @Setter private boolean _test;
	/**
	 * The MULTI login url  (uri of a APPLICATION containing multiple logins)
	 * BEWARE!!	Do NOT confuse with the one of the [security provider] (ie google, xlnets, etc)
	 * 			This url is the APPLICATION one where all the [security provider]s login options are listed
	 * 			(google, xlnets, user / password, etc)
	 */
	@MarshallField(as="appLoginUrl")
	@Setter private Url _appLoginUrl;
	private final transient Memoized<Url> _translatedAppLoginUrl = new Memoized<>() {
																			@Override
																			public Url supply() {
																				return _appLoginUrl != null ? _translate(_appLoginUrl) : null;
																			}
																	};
	public Url getAppLoginUrl() {
		return _translatedAppLoginUrl.get();
	}

	@MarshallField(as="providers")
	@Getter @Setter private Set<AuthenticationProviderPublicConfig> _securityProviders;

	@MarshallField(as="urlVars")
	@Getter @Setter private Properties _urlVars;

	@MarshallField(as="loginCommonProperties")
	@Getter @Setter private Properties _loginCommonProperties;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityLoginFilterConfig() {
		// default no-args constructor
		// this config is built from the security.login.filter.properties.xml using the
		// marshaller
		// (see SecurityLoginFilterGuiceModule.java)
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	ITERATOR
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Iterator<AuthenticationProviderPublicConfig> iterator() {
		return _securityProviders != null ? _securityProviders.iterator()
										  : Lists.<AuthenticationProviderPublicConfig>newArrayList().iterator();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public AuthenticationProviderPublicConfig getProviderPublicConfig(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		Collection<AuthenticationProviderPublicConfig> cfgs = this.getProvidersMatching(providerConfig -> providerConfig.getIdentityProvider().is(identityProvider)
																									   && providerConfig.getAuthenticationMethod().is(authnMethod));
		return Iterables.getFirst(cfgs,null);
	}
	public Collection<AuthenticationProviderPublicConfig> getEnabledProviders() {
		return this.getProvidersMatching(AuthenticationProviderPublicConfig::isEnabled);
	}
	public Collection<AuthenticationProviderPublicConfig> getProvidersMatching(final Predicate<AuthenticationProviderPublicConfig> pred) {
		if (CollectionUtils.isNullOrEmpty(_securityProviders)) throw new IllegalStateException("The login [security providers] are null: this is a DEVELOPER FAULT caused by " + SecurityLoginFilterGuiceModule.class.getSimpleName() + " not being installed (see [servlet context listener] constructor)");
		Collection<AuthenticationProviderPublicConfig> securityProvidersConfig  = _securityProviders.stream()
																						  	  .filter(pred)
																						  	  .collect(Collectors.toList());
		if (CollectionUtils.isNullOrEmpty(securityProvidersConfig)) {
			log.error("Error!! There is[security provider] matching the given predictate");
			throw new IllegalStateException("Error!! There is [security provider] matching the given predicate");
		}
		// Customize vars
		Collection<AuthenticationProviderPublicConfig> outCfgs = securityProvidersConfig.stream()
																		 		  .map(this::_customizeVarsOf)
																		 		  .collect(Collectors.toList());
		return outCfgs;
	}
	public Properties getPropertiesFor(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		return this.getProviderPublicConfig(identityProvider,authnMethod)
				   .getProperties();
	} 
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN URL 
/////////////////////////////////////////////////////////////////////////////////////////
	public Url getSecurityRESTServicesLoginEndPointForProviderWithId(final IdentityProvider identityProvider,final AuthenticationMethod authnMethod) {
		Url outUrl = null;
		if (authnMethod == AuthenticationMethod.SAML) {
			String samlIssuerId = this.getProviderPublicConfig(identityProvider,AuthenticationMethod.SAML)
									  .getProperty("issuerId")	
									  .asString();	// the SAML issuerId for the MS ADFS Provider
			
			outUrl = this.getProviderPublicConfig(identityProvider,AuthenticationMethod.SAML)
					     .getLoginUrl()
					     .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("impl",samlIssuerId)));
		} else {
			outUrl = this.getProviderPublicConfig(identityProvider,authnMethod)
					     .getLoginUrl();
		}
		return outUrl;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	private Url _translate(final Url url) {
		if (CollectionUtils.isNullOrEmpty(_urlVars)) return url;

		// replace variables
		String urlAsString = StringCustomizeUtils.replaceDollarDelimitedVariableValues(url.asString(),
																					   _urlVars);
		// debug
		if (urlAsString.contains("$")) {
			// debug... 
			log.warn("Could NOT translate url {} with vars:\n" + 
	 				 "{}",
	 				 url,
	 				 StringCustomizeUtils.createVariableVarsDebugInfo(_urlVars));
		}
		return Url.from(urlAsString);
	}
	private AuthenticationProviderPublicConfig _customizeVarsOf(final AuthenticationProviderPublicConfig securityProviderConfig) {
		AuthenticationProviderPublicConfig outCfg = null;
		if (CollectionUtils.hasData(securityProviderConfig.getUrlVars())) {
			// Security provider has custom variables
			log.debug("Security Provider has custom urls vars ");
			return securityProviderConfig;
		} else if (CollectionUtils.hasData(this.getUrlVars())) {
			log.debug("Security Provider has no urls vars, but the parent SecurityProviderPublicConfig does {} !!!! ");
			// Security provider has NO custom variables, but the parent has.
			securityProviderConfig.setUrlVars(this.getUrlVars());
			outCfg = securityProviderConfig;
		} else {
			outCfg = securityProviderConfig;
		}
		return outCfg;
	}
}

