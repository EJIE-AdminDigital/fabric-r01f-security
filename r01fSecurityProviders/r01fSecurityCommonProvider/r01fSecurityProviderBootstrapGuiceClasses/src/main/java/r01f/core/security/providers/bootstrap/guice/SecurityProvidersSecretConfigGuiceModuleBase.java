package r01f.core.security.providers.bootstrap.guice;

import java.nio.charset.Charset;

import org.w3c.dom.Node;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import jakarta.inject.Singleton;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.security.annotations.GiltzaSecurityProviderConfig;
import r01f.security.annotations.GoogleSecurityProviderConfig;
import r01f.security.annotations.PairedPhoneSecurityProviderConfig;
import r01f.security.annotations.SAMLSecurityProviderConfig;
import r01f.security.annotations.UserPasswordSecurityProviderConfig;
import r01f.security.annotations.XlnetsSecurityProviderConfig;
import r01f.security.provider.SecurityProvider;
import r01f.security.provider.SecurityProviderSecretConfig;
import r01f.security.provider.SecurityProviderSecretConfigSet;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;
import r01f.xml.XMLStringSerializer;
import r01f.xmlproperties.XMLPropertiesBuilder;
import r01f.xmlproperties.XMLPropertiesForApp;
import r01f.xmlproperties.XMLPropertiesForAppComponent;


public abstract class SecurityProvidersSecretConfigGuiceModuleBase
		   implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final CoreAppCode _coreAppCode;
	private final CoreModule _coreModule;
/////////////////////////////////////////////////////////////////////////////////////////
//	 CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected SecurityProvidersSecretConfigGuiceModuleBase(final CoreAppCode coreAppCode,final CoreModule coreModule) {
		_coreAppCode = coreAppCode;
		_coreModule = coreModule;
	}
	protected SecurityProvidersSecretConfigGuiceModuleBase(final CoreAppCode coreAppCode) {
		this(coreAppCode,
			 null);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public void configure(final Binder binder) {
		//.. nothing! just providing security providers secrets config
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Provides @Singleton
	SecurityProviderSecretConfigSet _provideSecretConfigSet( @ModelObjectsMarshaller final Marshaller marshaller) {
		XMLPropertiesForApp props = XMLPropertiesBuilder.create()
															.notUsingCache()
														 .forApp(_coreAppCode);
		AppComponent appComp = _coreModule != null ? AppComponent.compose(_coreModule,
																		  "security.login.providers")
												   : AppComponent.forId("security.login.providers");
		XMLPropertiesForAppComponent providerProps = props.forComponent(appComp);

		// get the properties as an XML
		Node node = providerProps.propertyAt("/*")
					 			 .node();
		String propertiesXml = XMLStringSerializer.writeNodeNoFail(node,
															 	   Charset.defaultCharset());
		// marshall to a business object
		SecurityProviderSecretConfigSet sc = marshaller.forReading()
													   .fromXml(propertiesXml,
																SecurityProviderSecretConfigSet.class);
		return sc;
	}
	@Provides @UserPasswordSecurityProviderConfig @Singleton
	@SuppressWarnings("static-method")
	SecurityProviderSecretConfig  _provideUserPasswordLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
		return securityProviderSecretConfigSet.getProvider(SecurityProvider.USER_PASSWORD);
	}
	@Provides @GoogleSecurityProviderConfig @Singleton
	@SuppressWarnings("static-method")
	SecurityProviderSecretConfig  _provideGoogleLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getProvider(SecurityProvider.GOOGLE);
	}
	@Provides @SAMLSecurityProviderConfig @Singleton
	@SuppressWarnings("static-method")
	SecurityProviderSecretConfig  _provideSAMLLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getProvider(SecurityProvider.SAML);
	}
	@Provides @XlnetsSecurityProviderConfig @Singleton
	@SuppressWarnings("static-method")
	SecurityProviderSecretConfig  _provideXLNetsLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getProvider(SecurityProvider.XLNETS.getCode());
	}
	@Provides @GiltzaSecurityProviderConfig @Singleton
	@SuppressWarnings("static-method")
	SecurityProviderSecretConfig  _provideGiltzaLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getProvider(SecurityProvider.GILTZA);
	}
   	@Provides @PairedPhoneSecurityProviderConfig @Singleton
   	@SuppressWarnings("static-method")
   	SecurityProviderSecretConfig  _providePairedPhoneLoginProviderConfig(final SecurityProviderSecretConfigSet securityProviderSecretConfigSet) {
	   	 return securityProviderSecretConfigSet.getProvider(SecurityProvider.PAIRED_PHONE.getCode());
   	}
}
