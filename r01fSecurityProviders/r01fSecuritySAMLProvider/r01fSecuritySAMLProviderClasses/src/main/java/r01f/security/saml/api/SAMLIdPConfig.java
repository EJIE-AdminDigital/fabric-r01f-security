package r01f.security.saml.api;

import java.io.Reader;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import org.opensaml.saml.metadata.resolver.impl.DOMMetadataResolver;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.opensaml.security.credential.Credential;

import lombok.Getter;
import lombok.experimental.Accessors;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.security.saml.api.SAMLIDs.SAMLIssuerID;
import r01f.security.saml.api.SAMLIDs.SAMLRelyingPartyID;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;

@Accessors(prefix="_")
public class SAMLIdPConfig {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter private final SAMLRelyingPartyID _relyingPartyId;
	@Getter private final IdentityProvider _IdPNick;
	@Getter private final SAMLIssuerID _IdPIssuerId;
	@Getter private final Url _IdPUrl;
	@Getter private final Collection<Credential> _IdPCredentials;	
	@Getter private final Url _assertionConsumerServiceUrl;		// ACS = Assertion Consumer Service > the url that the [idp] will POST / GET with the saml response containing the assertions
												  				// beware!! usually this url is NOT used by the idp!!
												  				//			the idp will use the acs configured at the idp console
	@Getter private final SAMLIdPBinding _IdPBinding;
	
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Constructs an SAML config using explicit parameters.
	 * @param relyingPartyId the identifier of the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param idpIssuerId the expected issuer ID for SAML request & responses sent by the idp (usually an url)
	 * @param idpUrl the url where the SAML request will be submitted.
	 * @param idpCertificates the list of base-64 encoded certificates to use to validate responses.
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @param samlBinding what type of SAML binding should the client use.
	 * @throws SAMLException thrown if any error occur while loading the provider information.
	 */
	public SAMLIdPConfig(final SAMLRelyingPartyID relyingPartyId,
						 final IdentityProvider idpNick,final SAMLIssuerID idpIssuerId,
				   		 final Url idpUrl,
				   		 final Collection<X509Certificate> idpCertificates,
				   		 final Url assertionConsumerServiceUrl,
				   		 final SAMLIdPBinding samlBinding) throws SAMLException {
		if (relyingPartyId == null) throw new IllegalArgumentException("relyingPartyIdentifier");
		if (idpUrl == null) throw new IllegalArgumentException("identityProviderUrl");
		if (idpIssuerId == null) throw new IllegalArgumentException("responseIssuer");
		if (CollectionUtils.isNullOrEmpty(idpCertificates)) throw new IllegalArgumentException("certificates");

		_relyingPartyId = relyingPartyId;
		
		_IdPNick = idpNick;
		_IdPIssuerId = idpIssuerId;
		_IdPUrl = idpUrl;
		_IdPCredentials = idpCertificates.stream()
								   		 .map(SAMLCertificates::createtCredentialFrom)
								   		 .collect(Collectors.toList());
		_assertionConsumerServiceUrl = assertionConsumerServiceUrl;
		_IdPBinding = samlBinding;
	}
	/**
	 * Constructs an SAML config using explicit parameters.
	 * @param relyingPartyId the identifier of the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param idpIssuerId the expected issuer ID for SAML request & responses sent by the idp (usually an url)
	 * @param idpUrl the url where the SAML request will be submitted.
	 * @param idpCertificates the list of base-64 encoded certificates to use to validate responses.
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @throws SAMLException thrown if any error occur while loading the provider information.
	 */
	public SAMLIdPConfig(final SAMLRelyingPartyID relyingPartyId,
				   		 final IdentityProvider idpNick,final SAMLIssuerID idpIssuerId,
				   		 final Url idpUrl,
				   		 final Collection<X509Certificate> idpCertificates,
				   		 final Url assertionConsumerServiceUrl) throws SAMLException {

		this(relyingPartyId,
			 idpNick,idpIssuerId,
			 idpUrl,
			 idpCertificates,
			 assertionConsumerServiceUrl,
			 SAMLIdPBinding.POST);
	}
	/**
	 * Constructs an SAML config using explicit parameters.
	 * @param relyingPartyId the identifier of the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param idpIssuerId the expected issuer ID for SAML request & responses sent by the idp (usually an url)
	 * @param idpUrl the url where the SAML request will be submitted.
	 * @param idpCertificates the base-64 encoded certificate to use to validate responses.
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @throws SAMLException thrown if any error occur while loading the provider information.
	 */
	public SAMLIdPConfig(final SAMLRelyingPartyID relyingPartyId,
						 final IdentityProvider idpNick,final SAMLIssuerID idpIssuerId,
				   		 final Url idpUrl,
				   		 final Url assertionConsumerServiceUrl,
				   		 final X509Certificate idpCertificates) throws SAMLException {

		this(relyingPartyId,
			 idpNick,idpIssuerId,
			 idpUrl,
			 Collections.singletonList(idpCertificates),
			 assertionConsumerServiceUrl,
			 SAMLIdPBinding.POST);
	}
	/**
	 * Constructs an SAML config using XML metadata obtained from the identity provider.
	 * When using Okta as an identity provider, it is possible to pass null to relyingPartyIdentifier and
	 * assertionConsumerServiceUrl; they will be inferred from the metadata provider XML.
	 * @param relyingPartyId the identifier for the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param idpMetaData the XML metadata obtained from the identity provider.
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @return The created {@link SAMLAPI}.
	 * @throws SAMLException thrown if any error occur while loading the metadata information.
	 */
	public static SAMLIdPConfig fromMetadata(final SAMLRelyingPartyID relyingPartyId,  
									   		 final IdentityProvider idpNick,final Reader idpMetaData,
									   		 final Url assertionConsumerServiceUrl) {
		return SAMLIdPConfig.fromMetadata(relyingPartyId, 
										  idpNick,idpMetaData, 
										  assertionConsumerServiceUrl,
										  SAMLIdPBinding.POST);
	}
	/**
	 * Constructs an SAML config using XML metadata obtained from the identity provider. 
	 * When using Okta as an identity provider, it is possible to pass null to relyingPartyIdentifier and
	 * assertionConsumerServiceUrl; they will be inferred from the metadata provider XML.
	 * @param relyingPartyId the identifier for the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @param idpMetaData the XML metadata obtained from the identity provider.
	 * @param samlBinding  the HTTP method to use for binding to the IdP.
	 * @return The created {@link SAMLAPI}.
	 * @throws SAMLException thrown if any error occur while loading the metadata information.
	 */
	public static SAMLIdPConfig fromMetadata(final SAMLRelyingPartyID relyingPartyId,
									   		 final IdentityProvider idpNick,final Reader idpMetaData,
									   		 final Url assertionConsumerServiceUrl,
									   		 final SAMLIdPBinding samlBinding) {
		SAMLAPI.ensureOpenSAMLIsInitialized();
		return SAMLIdPConfig.fromMetadata(relyingPartyId,  
										  idpNick,idpMetaData, 
										  assertionConsumerServiceUrl,
										  samlBinding, 
										  null);	// idp no certificates
	}
	/**
	 * Constructs an SAML config using XML metadata obtained from the identity provider. 
	 * When using Okta as an identity provider, it is possible to pass null to relyingPartyIdentifier and
	 * assertionConsumerServiceUrl; they will be inferred from the metadata provider XML.
	 * @param relyingPartyId the identifier for the relying party.
	 * @param idpNick a nick name for the idp (ie okta)
	 * @param idpMetaData the XML metadata obtained from the identity provider
	 * @param assertionConsumerServiceUrl the url where the identity provider will post back the SAML response.
	 * @param samlBinding  the HTTP method to use for binding to the IdP.
	 * @param idpCertificates list of certificates.
	 * @return The created {@link SAMLAPI}.
	 * @throws SAMLException thrown if any error occur while loading the metadata information.
	 */
	public static SAMLIdPConfig fromMetadata(final SAMLRelyingPartyID relyingPartyId,
									   		 final IdentityProvider idpNick,final Reader idpMetaData,
									   		 final Url assertionConsumerServiceUrl,
									   		 final SAMLIdPBinding samlBinding,
									   		 final Collection<X509Certificate> idpCertificates) {
		DOMMetadataResolver metadataResolver = SAMLMarshaller.createMetadataResolverSkippingBom(idpMetaData);
		EntityDescriptor entityDescriptor = SAMLMarshaller.getEntityDescriptorUsing(metadataResolver);

		// IdP SSO Descriptor parsing
		IDPSSODescriptor idpSSODescriptor = SAMLMarshaller.getIdPSSODescriptorFrom(entityDescriptor);
		SingleSignOnService idpBinding = null;
		if (idpSSODescriptor.getSingleSignOnServices() != null
		 && !idpSSODescriptor.getSingleSignOnServices().isEmpty()) {
			idpBinding = SAMLMarshaller.getIdPBindingFrom(idpSSODescriptor, 
														  samlBinding);
		}
		Collection<X509Certificate> idpX509Certificates = SAMLCertificates.getCertificatesAt(idpSSODescriptor);
		boolean isOkta = entityDescriptor.getEntityID().contains(".okta.com");

		SAMLRelyingPartyID theRelyingPartyId = relyingPartyId;
		if (relyingPartyId == null) {
			// Okta's own toolkit uses the entity ID as a relying party identifier, so if we
			// detect that the IDP is Okta let's tolerate a null value for this parameter.
			if (isOkta) {
				theRelyingPartyId = SAMLRelyingPartyID.forId(entityDescriptor.getEntityID());
			} else {
				throw new IllegalArgumentException("relyingPartyIdentifier");
			}
		}

		Url theAssertionConsumerServiceUrl = assertionConsumerServiceUrl;
		if (idpBinding != null 
		 && assertionConsumerServiceUrl == null 
		 && isOkta) {
			// Again, Okta's own toolkit uses this value for the assertion consumer url, which
			// kinda makes no sense since this is supposed to be a url pointing to a server
			// outside Okta, but it probably just straight ignores this and use the one from
			// it's own config anyway.
			theAssertionConsumerServiceUrl = Url.from(idpBinding.getLocation());
		}

		if (idpCertificates != null) {
			// Adding certificates given to this method
			// because some idp metadata file does not embedded signing certificate
			idpX509Certificates.addAll(idpCertificates);
		}

		Url idpUrl;
		if (idpBinding != null) {
			idpUrl = Url.from(idpBinding.getLocation());
		} else {
			idpUrl = theAssertionConsumerServiceUrl;
		}
		SAMLIssuerID idpIssuerId = SAMLIssuerID.forId(entityDescriptor.getEntityID());

		return new SAMLIdPConfig(theRelyingPartyId,
								 // idp data
						   		 idpNick,idpIssuerId,
						   		 idpUrl,
						   		 idpX509Certificates,
						   		 // where the idp will send the responses and how
						   		 theAssertionConsumerServiceUrl,
						   		 samlBinding);
	}
}
