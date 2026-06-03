package r01f.crypto.httpsig;

import java.io.Serializable;
import java.security.MessageDigest;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.patterns.IsBuilder;
import r01f.types.StringBase64;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class HttpRequestPayloadDigestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static HTTPSRequestPayloadDigestBuilderSetDataStep create() {
		RequestPayloadInitWrapperData initData = new RequestPayloadInitWrapperData();

		return new HttpRequestPayloadDigestBuilder() { /* nothing */ }
					.new HTTPSRequestPayloadDigestBuilderSetDataStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPSRequestPayloadDigestBuilderSetDataStep {
		private final RequestPayloadInitWrapperData _initData;

		public HTTP2HttpRequestBuilderBuildStep payloadAsString(final String payloadAsString) {
			_initData.setPayloadAsString(payloadAsString);
			return new HTTP2HttpRequestBuilderBuildStep(_initData);
		}
		public HTTP2HttpRequestBuilderBuildStep emptyPayload() {
			_initData.setPayloadAsString("");
			return new HTTP2HttpRequestBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// REQUEST BUILDER BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTP2HttpRequestBuilderBuildStep {

		private final RequestPayloadInitWrapperData _initData;

		public HttpRequestPayloadDigest build() {
			try {
				MessageDigest	mdigest = MessageDigest.getInstance("SHA-256");
				byte[] hash = mdigest.digest(_initData.getPayloadAsString().getBytes());
				StringBase64 digest =  StringBase64.encode(hash);
				HttpRequestPayloadDigest psd2digest = new HttpRequestPayloadDigest(digest);
				return psd2digest;
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new HTTPRequestPayloadBuilderImplException(e.getLocalizedMessage());
			}
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * InitWrapperData
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor
	public static class RequestPayloadInitWrapperData
	         implements Serializable {

		private static final long serialVersionUID = 5787425585729960483L;

		@Getter @Setter  private String _payloadAsString ;
	}
/////////////////////////////////////////////////////////////////////////////////////////
// HTTPRequestPayloadBuilderImplException
/////////////////////////////////////////////////////////////////////////////////////////
	public static class HTTPRequestPayloadBuilderImplException
			    extends RuntimeException {

		private static final long serialVersionUID = -7888592458789620124L;

		public HTTPRequestPayloadBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
