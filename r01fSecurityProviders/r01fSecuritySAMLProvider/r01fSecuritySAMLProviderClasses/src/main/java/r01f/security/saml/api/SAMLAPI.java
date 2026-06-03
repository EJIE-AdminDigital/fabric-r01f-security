package r01f.security.saml.api;

import java.io.IOException;
import java.security.Security;
import java.util.Collection;
import java.util.Map;
import java.util.function.Predicate;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.opensaml.core.config.InitializationService;

import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.security.saml.api.SAMLIDs.SAMLIdPID;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;
import r01f.webbrowser.WebBrowserUtils;

/**
 * see
 *      How SAML works
 *          https://auth0.com/blog/how-saml-authentication-works/
 *          https://goteleport.com/blog/how-saml-authentication-works/
 *          https://www.pingidentity.com/en/resources/client-library/articles/saml.html
 *          https://riptutorial.com/saml-2-0/example/23445/the-saml2-0-authentication-flow
 *
 * 		Sample implementations:
 * 			https://github.com/coveooss/saml-client
 * 			https://github.com/lastpass/saml-sdk-java
 *
 * 		Documentation
 *          https://wiki.shibboleth.net/confluence/display/SP3/JavaHowTo
 *          https://stackoverflow.com/questions/22908548/single-sign-on-in-spring-by-using-saml-extension-and-shibboleth/22934522#22934522
 *          https://stackoverflow.com/questions/28240458/how-do-i-talk-to-adfs-from-java
 *
 *      Do not use SAML for Mobile Apps or JS SPA apps
 *      	https://www.identityserver.com/articles/why-you-wouldn-t-use-saml-in-a-spa-and-mobile-app
 *
 *      Apache module
 *          https://github.com/latchset/mod_auth_mellonl
 *          https://bgstack15.wordpress.com/2016/03/24/adding-adfs-integration-to-apache/
 *
 *      Libraries
 *          https://nzpcmad.blogspot.com/2013/06/saml-saml-connectivity-toolkit.html
 *
 *      Create [service provider] metadata
 *          https://www.samltool.com/sp_metadata.php
 *
 * Usage
 *
 * [0] - Get the [IdP] metadata
 * 				For example, for an ADFS [idP] the metadata url can be downloaded from https://{idpUlr (adfs url)}/FederationMetadata/2007-06/FederationMetadata.xml
 * [1] - Create a SAMLAPI instance
 * 		 <pre class='brush:java'>
 *	         Path oktaMetaDataPath = Path.from("saml-metadata/okta-federationmetadata.xml");
 *	         SAMLIdPConfig oktaIdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("r01-platea"),
 *	         													 	 // the saml IdP metadata (for ms adfs download it from https://{idpUlr (adfs url)}/FederationMetadata/2007-06/FederationMetadata.xml)
 *	         													 	 ResourcesLoaderBuilder.createDefaultResourcesLoader()
 *	         										  								   	   .getReader(oktaMetaDataPath),
 *	         										  				 // the asertion consumer end-point > this url will be called by the [IdP]
 *	         										  				 Url.from("http://localhost:8080/r01PLATEAWebCORESecurityRESTServicesWar/api/saml/users/login/callback/"),
 *	         										  				 // how the [idp] will contact the assertion consumer
 *	         										  				 SAMLIdPBinding.REDIRECT);
 *	         // Elkarlan ADFS
 *	         Path adfsMetaDataPath = Path.from("saml-metadata/elkarlan-adfs-federationmetadata.xml");
 *	         SAMLIdPConfig elkarlanIdPConfig = SAMLIdPConfig.fromMetadata(SAMLRelyingPartyID.forId("https://src.alm02.itbatera.euskadi.eus"),
 *	         													 	 	 // the saml IdP metadata (for ms adfs download it from https://{idpUlr (adfs url)}/FederationMetadata/2007-06/FederationMetadata.xml)
 *	         													 	 	 ResourcesLoaderBuilder.createDefaultResourcesLoader()
 *	         										  								   	       .getReader(adfsMetaDataPath),
 *	         										  					 // the asertion consumer end-point > this url will be called by the [IdP]
 *	         										  					 Url.from("http://localhost:8080/r01PLATEAWebCORESecurityRESTServicesWar/api/saml/users/login/callback/"),
 *	         										  					 // how the [idp] will contact the assertion consumer
 *	         										  					 SAMLIdPBinding.REDIRECT);
 *	         SAMLAPI samlAPI = new SAMLAPI(oktaIdPConfig,
 *	         							  elkarlanIdPConfig);
 * 		 </pre>
 *
 * [2] - Generate a SAML request and redir to the [IdS]
 * 		 <pre class='brush:java'>
 *		    SAMLAPIForAuthn authnAPI = samlAPI.usingIdP(currentIssuerId)
 *		    								  .getAuthAPI();
 *		    if (samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() == SAMLIdPBinding.POST) {
 *		        Url idpRedirUrl = samlAuthnAPI.composeIdPClientRedirectAuthnUrl(to);
 *		        return Response.seeOther(URI.create(idpRedirUrl.asStringUrlEncodingQueryStringParamsValues()))	// BEWARE!! encode url params
 *		        			   .build();
 *		    } else if (samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() == SAMLIdPBinding.REDIRECT) {
 *		        String autoSubmmitFormHtml = samlAuthnAPI.composeIdpAuthnAutoSubmitHtmlForm(to);
 *		        return Response.ok(autoSubmmitFormHtml)
 *		        			   .build();
 *		    } else {
 *		    	throw new IllegalStateException("saml idp binding " +  samlAPI.getIdPConfigFor(currentIssuerId).getIdPBinding() + " is NOT supported!");
 *		    }
 *		 </pre>
 *
 * [3] - Consume the SAML response at http://my-site/some/url/that/processes/assertions
 * 		 <pre class='brush:java'>
 *		        SAMLAPIForIdPResponse responseAPI = samlAPI.usingIdP(currentIssuerId)
 *		        										   .getIdPResponseAPI();
 *
 *		        SAMLResponse resp = responseAPI.decodeAndValidateAuthnResponse(samlResponse,
 *		        									  					   	   SAMLIdPBinding.POST);
 *		        Map<String,String> attrs = SAMLAPIForIdPResponse.getAttributesFromResponse(resp);
 * 		 <pre>
 */
@Accessors(prefix="_")
public class SAMLAPI {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTANTS
/////////////////////////////////////////////////////////////////////////////////////////
	public static final String HTTP_REQ_SAML_PARAM = "SAMLRequest";
	public static final String HTTP_RESP_SAML_PARAM = "SAMLResponse";
	public static final String HTTP_RELAY_STATE_PARAM = "RelayState";
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static boolean _SAML_INITIALIZED = false;

/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Getter private final Collection<SAMLIdPConfig> _IdPConfigs = Lists.newArrayList();
	@Getter private final SAMLSPCredentials _spCredentials = new SAMLSPCredentials();

/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Creates a SAML client .
	 * @throws SAMLException thrown if any error occur while loading the provider information.
	 */
	public SAMLAPI() throws SAMLException {
		SAMLAPI.ensureOpenSAMLIsInitialized();
	}
	/**
	 * Creates a SAML client for the given IdP configs
	 * @param idpConfigs
	 */
	public SAMLAPI(final SAMLIdPConfig... idpConfigs) {
		this();
		if (CollectionUtils.hasData(idpConfigs)) {
			for (SAMLIdPConfig cfg : idpConfigs) {
				this.addIdPConfig(cfg);
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	INITIALIZE
/////////////////////////////////////////////////////////////////////////////////////////
	public static void initializeSAML() {
		try {
			// Initialize saml
			InitializationService.initialize();
			// avoid certain errors related with the absence of certain cyphers at the jdk
			// (beware to add BouncyCastle dep)
			Security.addProvider(new BouncyCastleProvider());
			_SAML_INITIALIZED = true;
		} catch (Throwable ex) {
			throw new SAMLException("Error while initializing the Open SAML library",ex);
		}
	}
	public synchronized static void ensureOpenSAMLIsInitialized() throws SAMLException {
		if (_SAML_INITIALIZED) return;	// nothing to do
		SAMLAPI.initializeSAML();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	IdP configs
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLIdPConfig getIdPConfigFor(final SAMLIdPID idpId) {
		if (CollectionUtils.isNullOrEmpty(_IdPConfigs)) throw new IllegalStateException("NO idp config for " + idpId);
		return _IdPConfigs.stream()
						  .filter(idpConfig -> idpConfig.getIdPNick().is(idpId))
						  .findFirst().orElseThrow(() -> new IllegalStateException("NO idp config for " + idpId));
	}
	public SAMLIdPConfig getIdPConfigFor(final SAMLIssuerID idpId) {
		if (CollectionUtils.isNullOrEmpty(_IdPConfigs)) throw new IllegalStateException("NO idp config for " + idpId);
		return _IdPConfigs.stream()
						  .filter(idpConfig -> idpConfig.getIdPIssuerId().is(idpId))
						  .findFirst().orElseThrow(() -> new IllegalStateException("NO idp config for " + idpId));
	}
	public SAMLIdPConfig getIdPConfigFor(final Predicate<SAMLIdPConfig> filter) {
		if (CollectionUtils.isNullOrEmpty(_IdPConfigs)) throw new IllegalStateException("NO idps defined!");
		return _IdPConfigs.stream()
						  .filter(filter)
						  .findFirst().orElseThrow(() -> new IllegalStateException("Could NOT find the required IdP!"));
	}
	public SAMLAPI addIdPConfig(final SAMLIdPConfig idpConfig) {
		_IdPConfigs.add(idpConfig);
		return this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SUB APIS
/////////////////////////////////////////////////////////////////////////////////////////
	public SAMLAPIBuilderSubAPIStep usingIdP(final SAMLIdPID idpId) {
		SAMLIdPConfig idpConfig = this.getIdPConfigFor(idpId);
		return new SAMLAPIBuilderSubAPIStep(idpConfig);
	}
	public SAMLAPIBuilderSubAPIStep usingIdP(final SAMLIssuerID idpId) {
		SAMLIdPConfig idpConfig = this.getIdPConfigFor(idpId);
		return new SAMLAPIBuilderSubAPIStep(idpConfig);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLAPIBuilderSubAPIStep {
		private final SAMLIdPConfig _config;

		public SAMLAPIForAuthn getAuthAPI() {
			return new SAMLAPIForAuthn(_config,
									   _spCredentials);
		}
		public SAMLAPIForIdPResponse getIdPResponseAPI() {
			return new SAMLAPIForIdPResponse(_config,
											 _spCredentials);
		}
		public SAMLAPIForSPInitiatedLogout getSPInitiatedLogoutAPI() {
			return new SAMLAPIForSPInitiatedLogout(_config,
												   _spCredentials);
		}
		public SAMLAPIForIdPInitiatedLogout getIdPInitiatedLogoutAPI() {
			return new SAMLAPIForIdPInitiatedLogout(_config,
													_spCredentials);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	static void redirectToIdp(final HttpServletResponse response,
							  final Url idpUrl,
						   	  final Map<String,String> params,
						   	  final SAMLIdPBinding idPBinding) throws IOException {
		if (idPBinding == SAMLIdPBinding.POST) {
			WebBrowserUtils.postUsingBrowser(response,
											 idpUrl,
											 params);
		} else if (idPBinding == SAMLIdPBinding.REDIRECT) {
			WebBrowserUtils.redirectWithParams(response,
											   idpUrl,
											   params);
		} else {
			throw new IllegalArgumentException(idPBinding + " is NOT a supported idp binding!");
		}
	}
}
