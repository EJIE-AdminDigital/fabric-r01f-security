package r01f.security.handler.saml;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.resources.ResourcesLoaderBuilder;
import r01f.security.authn.provider.config.AuthenticationProviderSecretConfig;
import r01f.security.saml.api.SAMLAPI;
import r01f.security.saml.api.SAMLIDs.SAMLRelyingPartyID;
import r01f.security.saml.api.SAMLIdPBinding;
import r01f.security.saml.api.SAMLIdPConfig;
import r01f.types.Path;
import r01f.types.url.Url;
import r01f.types.url.UrlPath;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLAPIFactory {
/////////////////////////////////////////////////////////////////////////////////////////
//	FROM CONFIG
/////////////////////////////////////////////////////////////////////////////////////////
	public static SAMLAPI createSAMLAPIFrom(final AuthenticationProviderSecretConfig cfg) {
		return SAMLAPIFactory.createSAMLAPIWithIdps(cfg);	// TODO use the config to create the saml api
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	FOR TESTING POURPOSES: DO NOT USE!!!!!
/////////////////////////////////////////////////////////////////////////////////////////
	@SneakyThrows  @SuppressWarnings("resource")
	public static SAMLAPI createSAMLAPIWithIdps(final AuthenticationProviderSecretConfig cfg) {
		Url frontEndUrl = cfg.getUrlVar("frontEndUrlBase")
							 .asUrl();
		// Elkarlan ADFS
		Path adfsMetaDataPath = Path.from("saml-metadata/elkarlan-adfs-federationmetadata.xml");
		SAMLIdPConfig elkarlanIdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("https://platea.cms.euskadi.eus"),
															 	 	 // the saml IdP metadata (for ms adfs download it from https://{idpUlr (adfs url)}/FederationMetadata/2007-06/FederationMetadata.xml)
																	 IdentityProvider.ELKARLAN_AZURE_AD,	
															 	 	 ResourcesLoaderBuilder.createDefaultResourcesLoader()
												  								   	       .getReader(adfsMetaDataPath),
												  					 // the asertion consumer service end-point (acs) > this url will be called by the [IdP]
												  					 // beware!! usually this url is NOT used by the idp!!
												  					 //			 the idp will use the acs configured at the idp console
												  					 Url.from(frontEndUrl).joinWith(UrlPath.from("/api/saml/users/login/callback/elkarlan")), 
												  					 // how the [idp] will contact the assertion consumer
												  					 SAMLIdPBinding.REDIRECT);
		// Elkarlan ADFS2
		Path adfs2MetaDataPath = Path.from("saml-metadata/elkarlan-adfs2-federationmetadata.xml");
		SAMLIdPConfig elkarlan2IdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("https://platea.cms.euskadi.eus"),
															 	 	  // the saml IdP metadata (for ms adfs download it from https://{idpUlr (adfs url)}/FederationMetadata/2007-06/FederationMetadata.xml)
																	  IdentityProvider.ELKARLAN2_AZURE_AD,	
															 	 	  ResourcesLoaderBuilder.createDefaultResourcesLoader()
												  								   	        .getReader(adfs2MetaDataPath),
												  					  // the asertion consumer service end-point (acs) > this url will be called by the [IdP]
												  					  // beware!! usually this url is NOT used by the idp!!
												  					  //		  the idp will use the acs configured at the idp console
												  					  Url.from(frontEndUrl).joinWith(UrlPath.from("/api/saml/users/login/callback/elkarlan2")), 
												  					  // how the [idp] will contact the assertion consumer
												  					  SAMLIdPBinding.REDIRECT);
		
		// Osakidetza Azure AD
		Path osaAzureADMetaDataPath = Path.from("saml-metadata/osakidetza-azure-ad-federationmetadata.xml");
		SAMLIdPConfig osaAzureADIdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("PLATEAWeb"), // Identifier / Entity ID
															 	 	  // the saml IdP metadata (for AzureAD download it from identity provider url)
																	  IdentityProvider.OSAKIDETZA_AZURE_AD,	
															 	 	  ResourcesLoaderBuilder.createDefaultResourcesLoader()
												  								   	        .getReader(osaAzureADMetaDataPath),
												  					  // the asertion consumer service end-point (acs) > this url will be called by the [IdP]
												  					  // beware!! usually this url is NOT used by the idp!!
												  					  //		  the idp will use the acs configured at the idp console  
												  					  Url.from(frontEndUrl).joinWith(UrlPath.from("/api/saml/users/login/callback/osaazuread")),
												  					  // localhost test
												  					  //Url.from("https://platea.cms.canary.euskadi.eus/r01PLATEAWebCORESecurityRESTServicesWar/api/saml/users/login/callback/osaazuread"),
												  					  // how the [idp] will contact the assertion consumer
												  					  SAMLIdPBinding.POST);
		
		// Osakidetza Azure AD Prod
		Path osaAzureADPDMetaDataPath = Path.from("saml-metadata/osakidetza-azure-ad-prod-federationmetadata.xml");
		SAMLIdPConfig osaAzureADPDIdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("PLATEAWeb"), // Identifier / Entity ID
															 	 	  // the saml IdP metadata (for AzureAD download it from identity provider url)
																	  IdentityProvider.OSAKIDETZA_PD_AZURE_AD,	
															 	 	  ResourcesLoaderBuilder.createDefaultResourcesLoader()
												  								   	        .getReader(osaAzureADPDMetaDataPath),
												  					  // the asertion consumer service end-point (acs) > this url will be called by the [IdP]
												  					  // beware!! usually this url is NOT used by the idp!!
												  					  //		  the idp will use the acs configured at the idp console  
												  					  Url.from(frontEndUrl).joinWith(UrlPath.from("/api/saml/users/login/callback/osaazureadpd")),												  					  		   	     
												  					  // how the [idp] will contact the assertion consumer
												  					  SAMLIdPBinding.POST);				
		
		
		SAMLAPI samlAPI = new SAMLAPI(elkarlanIdPConfig,
									  elkarlan2IdPConfig,
									  osaAzureADIdPConfig,
									  osaAzureADPDIdPConfig);
		return samlAPI;
	}
}
