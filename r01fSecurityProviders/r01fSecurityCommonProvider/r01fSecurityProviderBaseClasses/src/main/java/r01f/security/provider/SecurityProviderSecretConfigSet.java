package r01f.security.provider;

import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import r01f.config.ContainsConfigData;
import r01f.objectstreamer.annotations.MarshallField;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;

@MarshallType(as="securityProviderConfigSet")
@Slf4j
@Accessors(prefix="_")
public class SecurityProviderSecretConfigSet 
  implements ContainsConfigData,
             Iterable<SecurityProviderSecretConfig> {	
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="providers")
	@Getter @Setter private Set<SecurityProviderSecretConfig> _providers;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	URL VARS: avoid repeating things in other urls                                           
/////////////////////////////////////////////////////////////////////////////////////////
	@MarshallField(as="urlVars")
	@Getter @Setter protected Properties _urlVars;
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public Iterator<SecurityProviderSecretConfig> iterator() {
		return _providers != null ? _providers.iterator()
							: Lists.<SecurityProviderSecretConfig>newArrayList().iterator();
	}
	public SecurityProviderSecretConfig getProvider(final SecurityProvider provider) {
		return this.getProvider(provider.getCode());
	}
	public SecurityProviderSecretConfig getProvider(final SecurityProviderID providerId) {
		SecurityProviderSecretConfig securityProviderConfig = _providers.stream()
																		.filter(providerConfig -> providerConfig.getId().equals(providerId))
																		.findFirst().orElse(null);
		if (securityProviderConfig == null) {
			log.error(" Error!! There is no data for provider id '{}'", 
																	providerId);
			throw new IllegalStateException(Strings.customized(" Error!! There is no data provider of id '{}'", providerId.getId()));
		} 
		if (CollectionUtils.hasData(securityProviderConfig.getUrlVars())) {
			// Security provider has custom variables
			log.trace("Security Provider has custom urls vars ");
			return securityProviderConfig;				
		} else if (CollectionUtils.hasData(this.getUrlVars())) {
			log.trace("Security Provider has not custom urls vars, but SecurityProviderSecretConfigSet the parent has");
			//Security provider has NO custom variables, but the parent has.
			securityProviderConfig.setUrlVars(this.getUrlVars());
			return securityProviderConfig;
		}  else {
			return securityProviderConfig;
		}			
	}
}

