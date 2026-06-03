package r01f.security.saml.api;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.zip.Deflater;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

import javax.xml.namespace.QName;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.input.BOMInputStream;
import org.opensaml.core.xml.XMLObject;
import org.opensaml.core.xml.config.XMLObjectProviderRegistrySupport;
import org.opensaml.core.xml.io.Marshaller;
import org.opensaml.core.xml.io.MarshallingException;
import org.opensaml.core.xml.io.UnmarshallingException;
import org.opensaml.saml.common.SAMLObject;
import org.opensaml.saml.common.SAMLVersion;
import org.opensaml.saml.metadata.resolver.impl.DOMMetadataResolver;
import org.opensaml.saml.saml2.core.Issuer;
import org.opensaml.saml.saml2.core.LogoutResponse;
import org.opensaml.saml.saml2.core.RequestAbstractType;
import org.opensaml.saml.saml2.core.Status;
import org.opensaml.saml.saml2.core.StatusCode;
import org.opensaml.saml.saml2.core.StatusMessage;
import org.opensaml.saml.saml2.core.impl.StatusCodeBuilder;
import org.opensaml.saml.saml2.core.impl.StatusMessageBuilder;
import org.opensaml.saml.saml2.metadata.EntityDescriptor;
import org.opensaml.saml.saml2.metadata.IDPSSODescriptor;
import org.opensaml.saml.saml2.metadata.SingleSignOnService;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.shibboleth.utilities.java.support.component.ComponentInitializationException;
import net.shibboleth.utilities.java.support.xml.BasicParserPool;
import net.shibboleth.utilities.java.support.xml.XMLParserException;
import r01f.security.saml.api.SAMLIDs.SAMLRelyingPartyID;
import r01f.util.types.StringEncodeUtils;

@Slf4j
@NoArgsConstructor(access=AccessLevel.PRIVATE)
abstract class SAMLMarshaller {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static String createRandomId() {
		return "r01-" + UUID.randomUUID().toString();	// ADFS needs IDs to start with a letter
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SAML OBJECT FACTORY
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public static <O extends XMLObject> O buildSAMLObject(final QName qname) {
		return (O)XMLObjectProviderRegistrySupport.getBuilderFactory()
													 .getBuilder(qname)
													 .buildObject(qname);
	}
	/**
	 * Create a minimal SAML request
	 * @param defaultElementName The SomeClass.DEFAULT_ELEMENT_NAME we'll be casting this object into
	 */
	public static SAMLMarshallerRequestCreateStep forRelyingPartyWithId(final SAMLRelyingPartyID relyingPartyId) {
		return new SAMLMarshallerRequestCreateStep(relyingPartyId);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public static class SAMLMarshallerRequestCreateStep {
		private final SAMLRelyingPartyID _relyingPartyId;

		@SuppressWarnings("unchecked")
		public <R extends RequestAbstractType> R createBasicSAMLRequest(final QName defaultElementName) {
			RequestAbstractType request = SAMLMarshaller.buildSAMLObject(defaultElementName);
			request.setID(SAMLMarshaller.createRandomId());

			request.setVersion(SAMLVersion.VERSION_20);
			request.setIssueInstant(Instant.now());	// opensaml 4 > use: request.setIssueInstant(Instant.now());
														// opensaml < 4 use: request.setIssueInstant(new DateTime());

			Issuer issuer = SAMLMarshaller.buildSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
			issuer.setValue(_relyingPartyId.asString());
			request.setIssuer(issuer);

			return (R)request;
		}
		public LogoutResponse createLogoutResponseWith(final String status,final String statMsg) {
			LogoutResponse response = SAMLMarshaller.buildSAMLObject(LogoutResponse.DEFAULT_ELEMENT_NAME);
			response.setID(SAMLMarshaller.createRandomId());

			response.setVersion(SAMLVersion.VERSION_20);
			response.setIssueInstant(Instant.now());	// opensaml 4 > use: response.setIssueInstant(Instant.now());
														// opensaml < 4 use: response.setIssueInstant(new DateTime());

			Issuer issuer = SAMLMarshaller.buildSAMLObject(Issuer.DEFAULT_ELEMENT_NAME);
			issuer.setValue(_relyingPartyId.asString());
			response.setIssuer(issuer);

			// Status
			Status stat = SAMLMarshaller.buildSAMLObject(Status.DEFAULT_ELEMENT_NAME);
			StatusCode statCode = new StatusCodeBuilder().buildObject();
			statCode.setValue(status);
			stat.setStatusCode(statCode);
			if (statMsg != null) {
				StatusMessage statMessage = new StatusMessageBuilder().buildObject();
				statMessage.setValue(statMsg);	// opensaml 4 > use: statMessage.setValue(statMsg);
												// opensaml < 4 use: statMessage.setMessage(statMsg);
				stat.setStatusMessage(statMessage);
			}
			response.setStatus(stat);

			return response;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static final BasicParserPool _domParser = SAMLMarshaller._createDOMParser();

	@SneakyThrows
	private static BasicParserPool _createDOMParser() {
		BasicParserPool basicParserPool = new BasicParserPool();
		basicParserPool.initialize();
		return basicParserPool;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings({ "unchecked" })
	public static <O extends SAMLObject> O parseResponse(final String encodedResponse,
										   				 final SAMLIdPBinding idpBindig) throws SAMLException {
		log.info("[SAML] Validating response {}",encodedResponse);
		try (Reader responseReader = _decodeAndInflate(encodedResponse,
													   idpBindig)) {
			Document responseDocument = _domParser.parse(responseReader);
			return (O)XMLObjectProviderRegistrySupport.getUnmarshallerFactory()
													  .getUnmarshaller(responseDocument.getDocumentElement())
													  .unmarshall(responseDocument.getDocumentElement());
		} catch (UnmarshallingException | XMLParserException | IOException ex) {
			throw new SAMLException("Cannot decode xml encoded response",ex);
		}
	}
	private static Reader _decodeAndInflate(final String encodedResponse,
											final SAMLIdPBinding idpBinding) {
		// decode base64
		ByteArrayInputStream afterB64Decode = new ByteArrayInputStream(Base64.decodeBase64(encodedResponse));

		// inflate if necessary
		Reader outReader = null;
		if (idpBinding == SAMLIdPBinding.REDIRECT) {
			InputStream afterInflate = _inflate(afterB64Decode);
			outReader = new InputStreamReader(afterInflate,StandardCharsets.UTF_8);
		} else if (idpBinding == SAMLIdPBinding.POST) {
			outReader = new InputStreamReader(afterB64Decode,StandardCharsets.UTF_8);
		} else {
			throw new IllegalArgumentException("IdP binding " + idpBinding + " is NOT supported!");
		}
		return outReader;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Convert a SAML request to a base64-encoded and after url-encoded String
	 * @param request The request to encode
	 * @param idpBinding
	 * @throws SAMLException if marshalling the request fails
	 * */
	public static String marshallAndEncodeSAMLObject(final XMLObject request,
													 final SAMLIdPBinding idpBinding) throws SAMLException {
		StringWriter stringWriter;
		try {
			stringWriter = SAMLMarshaller.marshallXmlObject(request);
		} catch (MarshallingException me) {
			throw new SAMLException("Error while marshalling SAML object to XML: " + me.getMessage(),me);
		}
		String samlObjStr = stringWriter.toString();		
		log.trace("[SAML] object xml: {}",samlObjStr);
		
		byte[] samlObjBytes = samlObjStr.getBytes();
		if (idpBinding == SAMLIdPBinding.REDIRECT) {
			try {
				samlObjBytes = _deflate(samlObjStr.getBytes());
			} catch (IOException ioEx) {
				throw new SAMLException("Error while deflating saml object: " + ioEx.getMessage(),ioEx);
			}
		}
		
		String samlObjBase64 = Base64.encodeBase64String(samlObjBytes);		
		
		if (idpBinding == SAMLIdPBinding.REDIRECT) {
			return  StringEncodeUtils.urlEncode(samlObjBase64).toString();	// BEWARE!! url encode!
		}
		
		return samlObjBase64;
	}
	public static StringWriter marshallXmlObject(final XMLObject object) throws MarshallingException {
		StringWriter stringWriter = new StringWriter();
		Marshaller marshaller = XMLObjectProviderRegistrySupport.getMarshallerFactory()
																.getMarshaller(object);
		Element dom = marshaller.marshall(object);
		SAMLXMLHelper.writeNode(dom,stringWriter);

		return stringWriter;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	DEFLATE & INFLATE
/////////////////////////////////////////////////////////////////////////////////////////
	private static InputStream _inflate(final InputStream inflatedInputStream) {
		InputStream afterInflate = new InflaterInputStream(inflatedInputStream,
														   new Inflater(true));	// true = support GZIP
		return afterInflate;
	}
	private static byte[] _deflate(final byte[] input) throws IOException {
		// deflate and base-64 encode it
		Deflater deflater = new Deflater(Deflater.DEFAULT_COMPRESSION,
										 true);		// true = support GZIP
		deflater.setInput(input);
		deflater.finish();

		ByteArrayOutputStream bos = new ByteArrayOutputStream();

		byte[] tmp = new byte[8192];
		int count;
		while (!deflater.finished()) {
			count = deflater.deflate(tmp);
			bos.write(tmp,0,count);
		}
		bos.flush();
		bos.close();
		deflater.end();
		return bos.toByteArray();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METADATA
/////////////////////////////////////////////////////////////////////////////////////////
	public static DOMMetadataResolver createMetadataResolver(final InputStream metadata) throws SAMLException {
		try {
			Document metadataDocument = _domParser.parse(metadata);
			DOMMetadataResolver resolver = new DOMMetadataResolver(metadataDocument.getDocumentElement());
			resolver.setId("componentId"); // The resolver needs an ID for the initialization to go through.
			resolver.initialize();
			return resolver;
		} catch (ComponentInitializationException | XMLParserException ex) {
			throw new SAMLException("Cannot load identity provider metadata", ex);
		}
	}
	public static DOMMetadataResolver createMetadataResolverSkippingBom(final Reader metadata) throws SAMLException {
		try (InputStream metaDataSkippingBom = _skipBom(metadata)) {
			return SAMLMarshaller.createMetadataResolver(metaDataSkippingBom);
		} catch (IOException e) {
			throw new SAMLException("Couldn't read metadata",e);
		}
	}
	/**
	 * Wrap a {@link java.io.Reader Reader} to skip a BOM if it is present.
	 * OpenSaml won't accept a metadata file if it starts with a BOM.
	 * @param metadata The metadata with optional BOM
	 * @return A {@link Reader} which will never return a BOM
	 */
	@SuppressWarnings("resource")
	private static InputStream _skipBom(final Reader metadata) throws SAMLException {
		try {
			InputStream metadataInputStream = IOUtils.toInputStream(IOUtils.toString(metadata),
																	StandardCharsets.UTF_8);
			BOMInputStream bomin = BOMInputStream.builder()
													.setInclude(false)
													.setInputStream(metadataInputStream)
												.get();
			return bomin;

		} catch (final IOException e) {
			throw new SAMLException("Couldn't read metadata", e);
		}
	}
	public static EntityDescriptor getEntityDescriptorUsing(final DOMMetadataResolver metadata) throws SAMLException {
		List<EntityDescriptor> entityDescriptors = new ArrayList<>();
		metadata.forEach(entityDescriptors::add);
		if (entityDescriptors.size() != 1) throw new SAMLException("Bad entity descriptor count: " + entityDescriptors.size());
		return entityDescriptors.get(0);
	}
	public static IDPSSODescriptor getIdPSSODescriptorFrom(final EntityDescriptor entityDescriptor) throws SAMLException {
		IDPSSODescriptor idpSSODescriptor = entityDescriptor.getIDPSSODescriptor("urn:oasis:names:tc:SAML:2.0:protocol");
		if (idpSSODescriptor == null) throw new SAMLException("Cannot retrieve IdP SSO descriptor");

		return idpSSODescriptor;
	}
	public static SingleSignOnService getIdPBindingFrom(final IDPSSODescriptor idpSSODescriptor,
														final SAMLIdPBinding samlBinding) throws SAMLException {
		return idpSSODescriptor.getSingleSignOnServices()
							   .stream()
							   .filter(ssoService -> ssoService.getBinding().equals("urn:oasis:names:tc:SAML:2.0:bindings:" + samlBinding.getCode()))
							   .findAny()
							   .orElseThrow(() -> new SAMLException("Cannot find " + samlBinding.getCode() + " SSO binding in metadata"));
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
//	public static void main(final String[] args) {
//		_test(BASE_64);
//	}
//	private static void _test(final String base64) {
//		try {
//			System.out.println("====>BASE64\n" + base64);
//			Reader decodedAndInflated = _decodeAndInflate(base64,
//														  SAMLIdPBinding.REDIRECT);
//			String xml = StringPersistenceUtils.load(decodedAndInflated);
//			System.out.println("====>XML\n--->" + xml + "<---");
//		} catch (Throwable th) {
//			log.error("ERROR!!: {}",th.getMessage(),th);
//		}
//	}
//	private static final String BASE_64 = "xVZtb9s2EP4rgj5Poixbli04Qt3EK7w1L4jdFsiXgSLPDheJFEiqyfbre/RbLER1s3XIAAEST8e75x7eQ3JiaFXW2S2YWkkD3vzizP+DR5CyfsyDtEijYBANi4DyOA3idBiNVzBmfNTzvc+gjVDyzI/DyPfmxjQwl8ZSadEUxb0gSoJ4uOz1s2Sc9ZMwSeI737sAY4WkdjPz3traZITUJbVAQ1aZ0FDJC/UUQmMeKBfuTXTUu/k4Xc6mX6A4v76dLYA1Wti/8Gu5AP1VMDBfqCa0FsTVQxqD2Eip1kISRsuyoOyBQPlAdUll7HvnrlaHs9EyU9QIk0lagcksyxbTy48ZlpSxrVPWSFMDEysBHMuUe6qW6sxHYEFvyJI0YmkQjUZFMEgSFoxpvApWSdovRgXv98eF7z1VpTTZhuzTWWutrGKq9PPJhlK9nXp6EjVYsKPUzx2lyCjlKxOH+5JbbLpfxOxoI1Y3xk7INlc+2bbDwlLbmPboXHHwPtOygdNYzMY7WzQMwxvfI/mEtKNO92i33Vb0hkOAggUxpTQY8ME4GMOABlHBk3HB03GSxq/vr3ZX/jPudoz/LIeLpvgTmM0nV5hsfuH9qnRFf9BsziJ4sNq4ZrUrwljsPj9vJBXhWlXwd0G1Fo1R77ogTcg22yE99vhKuGiO6Euw94r/oN+rrACqQfudMS6opf+6/a+UvZbXerqyoNurN8iiwfPq3aLQarGR5v+1N7h+7Sj/2ZpP0M6FMxpX2HvAVYPv9OQgvTtd/ZEfKqPhWDwDJNlqwbZ598a8g5FWAxwcnz9bgcgzboxq8UfRWHCqhAopP7J5rpe2S4D5DLuHipoQxWQUrUOl1+TRkDiKEoKP4DgZGSespKIyBH1FSTnXTv5HQTd7R/7pavrh+nJ2965eQ6e2EHx7xpHhv4K4Fl9BOgV04EO5vQUEg1LsBPDBSd17v9P6W0DpxvH70gu8NphfPMcOmme/zWdvgayp5Utgr94QfwJfJZhWRq1syFS1Qzgi0fAFwkchuXo0lDHVSNvN5EvAp7GRTm029l4ext5m+P3jME7C4chtPQsUIQp+Ljk8vfas3SXD3cLCk22Pzks8L29hlZ88UlnGnB+ab/D1qDS/wWsN7p3Al5rifUpp6zapjrht8354RAQ5XB8OF4v9mZR/Aw==";
}
