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
import r01f.patterns.Memoized;
import r01f.security.provider.SecurityProvider;
import r01f.security.provider.SecurityProviderPublicConfig;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
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
			 Iterable<SecurityProviderPublicConfig> {

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
	@Getter @Setter private Set<SecurityProviderPublicConfig> _securityProviders;

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
	public Iterator<SecurityProviderPublicConfig> iterator() {
		return _securityProviders != null ? _securityProviders.iterator()
										  : Lists.<SecurityProviderPublicConfig>newArrayList().iterator();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityProviderPublicConfig getProvider(final SecurityProvider provider) {
		return this.getProvider(provider.getCode());
	}
	public SecurityProviderPublicConfig getProvider(final SecurityProviderID provider) {
		Collection<SecurityProviderPublicConfig> cfgs = this.getProvidersMatching(providerConfig -> providerConfig.getId().is(provider));
		return Iterables.getFirst(cfgs,null);
	}
	public Collection<SecurityProviderPublicConfig> getEnabledProviders() {
		return this.getProvidersMatching(SecurityProviderPublicConfig::isEnabled);
	}
	public Collection<SecurityProviderPublicConfig> getProvidersMatching(final Predicate<SecurityProviderPublicConfig> pred) {
		if (CollectionUtils.isNullOrEmpty(_securityProviders)) throw new IllegalStateException("The login [security providers] are null: this is a DEVELOPER FAULT caused by " + SecurityLoginFilterGuiceModule.class.getSimpleName() + " not being installed (see [servlet context listener] constructor)");
		Collection<SecurityProviderPublicConfig> securityProvidersConfig  = _securityProviders.stream()
																						  	  .filter(pred)
																						  	  .collect(Collectors.toList());
		if (CollectionUtils.isNullOrEmpty(securityProvidersConfig)) {
			log.error("Error!! There is[security provider] matching the given predictate");
			throw new IllegalStateException("Error!! There is [security provider] matching the given predicate");
		}
		// Customize vars
		Collection<SecurityProviderPublicConfig> outCfgs = securityProvidersConfig.stream()
																		 		  .map(this::_customizeVarsOf)
																		 		  .collect(Collectors.toList());
		return outCfgs;
	}
	public Collection<SecurityProvider> getEnabledProvidersIds() {
		Collection<SecurityProviderPublicConfig> enabledProvidersConfigs = this.getEnabledProviders();
		return enabledProvidersConfigs.stream()				
							 		  .map(cfg -> SecurityProvider.from(cfg.getId()))
							 		  .collect(Collectors.toList());
	}
	public Properties getPropertiesForProviderWithId(final SecurityProviderID providerId) {
		return this.getProvider(providerId).getProperties();
	} 
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN URL 
/////////////////////////////////////////////////////////////////////////////////////////
	public Url getSecurityRESTServicesLoginEndPointForProviderWithId(final SecurityProviderID providerId) {
		
		if (providerId.is(SecurityProviderID.SAML)) {
			return getSecurityRESTServicesLoginEndPointForSAML();
		}
		if (providerId.is(SecurityProviderID.XLNETS)) {
			return getSecurityRESTServicesLoginEndPointForXLNETS();
		}
		if (providerId.is(SecurityProviderID.GILTZA)) {
			return getSecurityRESTServicesLoginEndPointForGiltza();
		}
		if (providerId.is(SecurityProviderID.GOOGLE)) {
			return getSecurityRESTServicesLoginEndPointForGoogle();
		}
		if (providerId.is(SecurityProviderID.USER_PASSWORD)) {
			return getSecurityRESTServicesLoginEndPointForUserPassword();
		}
		if (providerId.is(SecurityProviderID.OSAKIDETZA_AZURE_AD)) {
			return getSecurityRESTServicesLoginEndPointForOsaAzureAD();
		}
		return null;
	}
	public Url getSecurityRESTServicesLoginEndPointForUserPassword() {
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.USER_PASSWORD);
	}	
	public Url getSecurityRESTServicesLoginEndPointForGoogle() {
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.GOOGLE);
	}
	public Url getSecurityRESTServicesLoginEndPointForGiltza() {
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.GILTZA);
	}
	public Url getSecurityRESTServicesLoginEndPointForXLNETS() {
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.XLNETS);
	}
	public Url getSecurityRESTServicesLoginEndPointForSAML() {
		String samlIssuerId = this.getProvider(SecurityProvider.SAML)
								  .getProperty("issuerId")	
								  .asString();	// the SAML issuerId for the MS ADFS Provider
		
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.SAML)
				   .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("impl",samlIssuerId)));
	}
	
	public Url getSecurityRESTServicesLoginEndPointForOsaAzureAD() {
		String samlIssuerId = this.getProvider(SecurityProvider.OSAKIDETZA_AZURE_AD)
								  .getProperty("issuerId")	
								  .asString(); // the SAML issuerId for the Azure AD Provider
		
		return this._getSecurityRESTServicesLoginEndPointForProviderWithId(SecurityProviderID.OSAKIDETZA_AZURE_AD)
				   .joinWith(UrlQueryString.fromParams(UrlQueryStringParam.of("impl",samlIssuerId)));
	}
	private Url _getSecurityRESTServicesLoginEndPointForProviderWithId(final SecurityProviderID providerId) {
		return this.getProvider(providerId).getLoginUrl();
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
	private SecurityProviderPublicConfig _customizeVarsOf(final SecurityProviderPublicConfig securityProviderConfig) {
		SecurityProviderPublicConfig outCfg = null;
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

