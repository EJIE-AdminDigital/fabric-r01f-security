package r01f.crypto.httpsig;

import java.io.Serializable;
import java.security.Signature;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Base64;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.httpclient.HttpHeader;
import r01f.httpclient.HttpHeaderID;
import r01f.patterns.IsBuilder;
import r01f.types.StringBase64;
import r01f.util.types.Strings;
import r01f.util.types.collections.Lists;

/**
 * HTTP CAVAGE SIGNATURE BUILDER.
 * The IETF Standard : ....
 * https://datatracker.ietf.org/doc/html/draft-cavage-http-signatures-12
 * 
 * Usage:
 *	<pre class='brush:java'>
 *		HttpSignature sig = HTTPSignatureBuilder.create()
 *							                    .forHeaders(headers)
 *							                    .with(certificate)
 *							                    .withPrivateKey(pk)
 *						                    	.build();
 * 	</pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class HttpSignatureBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static HTTPSignatureBuilderHeadersStep create() {
		SignatureInitWrapperData initData = new SignatureInitWrapperData();

		return new HttpSignatureBuilder() { /* nothing */ }
					.new HTTPSignatureBuilderHeadersStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPSignatureBuilderHeadersStep {
		private final SignatureInitWrapperData _initData;

		public HTTPSignatureBuilderX509CertificateStep forHeaders(final HttpHeader...headers) {			;
			return forHeaders(Lists.newArrayList(headers));
		}
		public HTTPSignatureBuilderX509CertificateStep forHeaders(final Collection<HttpHeader> headers) {
			_initData.setHeaders(headers);
			return new HTTPSignatureBuilderX509CertificateStep(_initData);
		}
	}

	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPSignatureBuilderX509CertificateStep {
		private final SignatureInitWrapperData _initData;

		public HTTPSignatureBuilderKeyStep with(final X509Certificate  certificate) {
			_initData.setPublicCertificate(certificate);
			return new HTTPSignatureBuilderKeyStep(_initData);
		}
	}

	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPSignatureBuilderKeyStep {
		private final SignatureInitWrapperData _initData;

		public HTTP2HttpSignatureBuilderBuildStep withPrivateKey(final RSAPrivateKey privateKey) {
			_initData.setPrivateKey(privateKey);
			return new HTTP2HttpSignatureBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// SIGNATURE BUILDER BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTP2HttpSignatureBuilderBuildStep {

		private final SignatureInitWrapperData _initData;

		public HttpSignature build() {
			try {
				//[1] Get Signing String Headers
				HttpSigningStringHeaders signingsStringHeaders =  _initData.headersAsSigningString();
				//[2] Build raw signature
				StringBase64 rawSignature = _buildRawSignatureBC(_initData.getPrivateKey(),
						                                         signingsStringHeaders);
				//[3] Build canonical signature
				String certificateId = _loadPublicCertificateKeyId(_initData.getPublicCertificate());
				HttpSignature signature = _buildCanonicalSignatureForPSD2(certificateId ,
						                                                  rawSignature,
						                                                  _initData.headersIds());
				return signature;
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new HTTPSignatureBuilderImplException(e.getLocalizedMessage());
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * SignatureInitWrapperData
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor
	public static class SignatureInitWrapperData
	         implements Serializable {

		private static final long serialVersionUID = 5787425585729960483L;

		@Getter @Setter  private Collection<HttpHeader>  _headers;
		@Getter @Setter  private RSAPrivateKey   _privateKey;
		@Getter @Setter  private X509Certificate _publicCertificate;

		public HttpSigningStringHeaders headersAsSigningString() {
			HttpSigningStringHeaders  signingHeaders = new HttpSigningStringHeaders();
			Collection<HttpHeader> headers = Lists.newArrayList(_headers);
		    signingHeaders.setHeaders(headers);
			return signingHeaders;
		}
		private Collection<HttpHeaderID> headersIds() {
			Collection<HttpHeaderID> headerIds = _headers.stream()
					                                     .map( (Function<? super HttpHeader, ? extends HttpHeaderID>) HttpHeader::getId)
					                                     .collect(Collectors.toList());
			return headerIds;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// UTILITY METHODS
/////////////////////////////////////////////////////////////////////////////////////////

	protected static StringBase64 _buildRawSignatureBC(final RSAPrivateKey pk ,
			                                           final HttpSigningStringHeaders signingString) {
		try {

			Signature privateSignature = Signature.getInstance("SHA256withRSA");
		    privateSignature.initSign(pk);
		    privateSignature.update(signingString.asString().getBytes());
		    byte[] signature = privateSignature.sign();
		    String signatureAsString = Base64.getEncoder().encodeToString(signature);
		    StringBase64 base64String = new StringBase64(new String(signatureAsString.getBytes()));
		    return base64String;

		} catch (final Throwable th) {
			th.printStackTrace();
			throw  new HTTPSignatureBuilderImplException(th.getLocalizedMessage());
		}

	}

	protected static HttpSignature _buildCanonicalSignatureForPSD2(final String keyId ,
															       final StringBase64 base64Signature,
															       final Collection<HttpHeaderID> idHeaders) {

		HttpSignature signaturePsd2 = new HttpSignature();
		signaturePsd2.setKeyId(keyId);
		// signaturePsd2.setAlgorithm("rsa-sha256");
		signaturePsd2.setAlgorithm("SHA256");
		signaturePsd2.setHeaders(idHeaders);
		signaturePsd2.setSignature(base64Signature);
		return signaturePsd2;

	}

	public static String _loadPublicCertificateKeyId(final X509Certificate certificate)  {
	    //3.1 Recuperamos KeyID (Conjunto de campos recuperados a partir de un certificado): keyId="SN=1,CA=C=ES,ST=Madrid,L=Madrid,O=TPP Name,OU=TPP Unit,CN=TPP Generico"
		try {
		    String dn = certificate.getSubjectX500Principal().getName();
		    LdapName ldapDN = new LdapName(dn);
		    Map<String, Object> attributes =
		    		 ldapDN.getRdns()
		    		       .stream().collect(Collectors.toMap(Rdn::getType,Rdn::getValue));

		    String keyId = Strings.customized( "SN={},"
		    								+  "C={},ST={},L={},O={},OU={},CN={}",
		    										    certificate.getSerialNumber(),
		    										    attributes.get("C"),attributes.get("ST"),attributes.get("L"),attributes.get("O"),attributes.get("OU"),attributes.get("CN"));

			return keyId;
		} catch (final Throwable th) {
			th.printStackTrace();
			throw  new HTTPSignatureBuilderImplException(th.getLocalizedMessage());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// HTTPSignatureBuilderImplException
/////////////////////////////////////////////////////////////////////////////////////////
	public static class HTTPSignatureBuilderImplException
			    extends RuntimeException {
		private static final long serialVersionUID = -8696403720523405755L;

		public HTTPSignatureBuilderImplException(final String msg) {
			super(msg);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// PUBLIC STATIC VOID MAIN
/////////////////////////////////////////////////////////////////////////////////////////
	public static void main(final String[] argv) {
		HttpHeader headers = null;
		X509Certificate certificate = null;
		RSAPrivateKey pk = null;
		HttpSignatureBuilder.create()
			                    .forHeaders(headers)
			                    .with(certificate)
			                    .withPrivateKey(pk)
		                    .build();

	}

}
