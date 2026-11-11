package r01f.core.bootstrap.guice.security.authn.providers;

import java.nio.charset.Charset;

import org.w3c.dom.Node;

import com.google.inject.Binder;
import com.google.inject.Module;
import com.google.inject.Provides;

import jakarta.inject.Singleton;
import r01f.model.annotations.ModelObjectsMarshaller;
import r01f.objectstreamer.Marshaller;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.AuthenticationMethod;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.authn.provider.annotations.GiltzaAuthnProviderConfig;
import r01f.security.authn.provider.annotations.GoogleAuthnProviderConfig;
import r01f.security.authn.provider.annotations.PairedPhoneAuthnProviderConfig;
import r01f.security.authn.provider.annotations.SAMLAuthnProviderConfig;
import r01f.security.authn.provider.annotations.UserPasswordAuthnProviderConfig;
import r01f.security.authn.provider.annotations.XlnetsAuthnProviderConfig;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfigSet;
import r01f.security.authn.provider.config.IsAuthenticationProviderSecretConfigSet;
import r01f.services.ids.ServiceIDs.CoreAppCode;
import r01f.services.ids.ServiceIDs.CoreModule;
import r01f.xml.XMLStringSerializer;
import r01f.xmlproperties.XMLPropertiesBuilder;
import r01f.xmlproperties.XMLPropertiesForApp;
import r01f.xmlproperties.XMLPropertiesForAppComponent;


public abstract class AuthnProvidersSecretConfigGuiceModuleBase
		   implements Module {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private final CoreAppCode _coreAppCode;
	private final CoreModule _coreModule;
/////////////////////////////////////////////////////////////////////////////////////////
//	 CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	protected AuthnProvidersSecretConfigGuiceModuleBase(final CoreAppCode coreAppCode,final CoreModule coreModule) {
		_coreAppCode = coreAppCode;
		_coreModule = coreModule;
	}
	protected AuthnProvidersSecretConfigGuiceModuleBase(final CoreAppCode coreAppCode) {
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
	IsAuthenticationProviderSecretConfigSet _provideSecretConfigSet(@ModelObjectsMarshaller final Marshaller marshaller) {
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
		IsAuthenticationProviderSecretConfigSet sc = marshaller.forReading()
													   .fromXml(propertiesXml,
																AuthenticationProviderSecretConfigSet.class);
		return sc;
	}
	@Provides @UserPasswordAuthnProviderConfig @Singleton
	@SuppressWarnings("static-method")
	AuthenticationProviderSecretConfig  _provideUserPasswordLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
		return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.FABRIC,AuthenticationMethod.USER_PASSWORD);
	}
	@Provides @GoogleAuthnProviderConfig @Singleton
	@SuppressWarnings("static-method")
	AuthenticationProviderSecretConfig  _provideGoogleLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.GOOGLE,AuthenticationMethod.OAUTH_AUTH_CODE_FLOW);
	}
	@Provides @SAMLAuthnProviderConfig @Singleton
	@SuppressWarnings("static-method")
	AuthenticationProviderSecretConfig  _provideSAMLLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.ELKARLAN_AZURE_AD,AuthenticationMethod.SAML);
	}
	@Provides @XlnetsAuthnProviderConfig @Singleton
	@SuppressWarnings("static-method")
	AuthenticationProviderSecretConfig  _provideXLNetsLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.XLNETS,AuthenticationMethod.UNKNOWN);
	}
	@Provides @GiltzaAuthnProviderConfig @Singleton
	@SuppressWarnings("static-method")
	AuthenticationProviderSecretConfig  _provideGiltzaLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
		 return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.GILTZA,AuthenticationMethod.OAUTH_AUTH_CODE_FLOW);
	}
   	@Provides @PairedPhoneAuthnProviderConfig @Singleton
   	@SuppressWarnings("static-method")
   	AuthenticationProviderSecretConfig  _providePairedPhoneLoginProviderConfig(final AuthenticationProviderSecretConfigSet securityProviderSecretConfigSet) {
	   	 return securityProviderSecretConfigSet.getAuthProviderConfig(IdentityProvider.XLNETS,AuthenticationMethod.PAIRED_PHONE);
   	}
}
