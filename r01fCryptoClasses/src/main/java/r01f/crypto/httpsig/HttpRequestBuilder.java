package r01f.crypto.httpsig;

import java.io.Serializable;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.util.Collection;
import java.util.stream.Stream;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.httpclient.HttpHeader;
import r01f.httpclient.HttpHeaderID;
import r01f.patterns.IsBuilder;
import r01f.types.url.Url;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;


/**
 * HTTP REQUEST BUILDER.
 * 
 * The IETF Standard for a http request with Cavage Signature:
 * Usage:
 *	<pre class='brush:java'>
 *		HttpRequest req = HttpRequestBuilder.create()
 *				                    .forHeaders(headers)
 *				                    .with(certificate)
 *				                    .withPrivateKey(pk)
 *		                			.build();
 *	</pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class HttpRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static HTTPSignatureBuilderHeadersStep create() {
		RequestInitWrapperData initData = new RequestInitWrapperData();

		return new HttpRequestBuilder() { /* nothing */ }
					.new HTTPSignatureBuilderHeadersStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPSignatureBuilderHeadersStep {
		protected final RequestInitWrapperData _initData;

		public HTTPRequestBuilderUrlOrMoreHeadersStep headers(final HttpHeader...headers) {			;
			return headers(Lists.newArrayList(headers));
		}
		public HTTPRequestBuilderUrlOrMoreHeadersStep headers(final Collection<HttpHeader> headers) {
			_initData.addHeaders(headers);
			return new HTTPRequestBuilderUrlOrMoreHeadersStep(_initData);
		}
		public HTTPRequestBuilderUrlOrMoreHeadersStep header(final String idHeader, final String vale) {
			_initData.addHeader(idHeader,vale);
			return new HTTPRequestBuilderUrlOrMoreHeadersStep(_initData);
		}
	}
	public class HTTPRequestBuilderUrlOrMoreHeadersStep
			extends HTTPSignatureBuilderHeadersStep {

		public HTTPRequestBuilderUrlOrMoreHeadersStep(final RequestInitWrapperData initData) {
			super(initData);
		}
		public HTTPRequestBuilderMethodStep url(final Url url) {
			_initData.setUrl(url);
			return new HTTPRequestBuilderMethodStep(_initData);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTPRequestBuilderMethodStep {
		private final RequestInitWrapperData _initData;

		public HTTP2HttpRequestBuilderBuildForGETStep get() {
			return new HTTP2HttpRequestBuilderBuildForGETStep(_initData);
		}
		public HTTP2HttpRequestBuilderBuildForPOSTStep post() {
			_initData.setBodyPublisher(BodyPublishers.noBody());
			return new HTTP2HttpRequestBuilderBuildForPOSTStep(_initData);
		}
		public HTTP2HttpRequestBuilderBuildForPOSTStep post(final BodyPublisher pb) {
			_initData.setBodyPublisher(pb);
			return new HTTP2HttpRequestBuilderBuildForPOSTStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// REQUEST BUILDER BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTP2HttpRequestBuilderBuildForGETStep {

		private final RequestInitWrapperData _initData;

		public HttpRequest build() {
			try {
				 HttpRequest.Builder request = HttpRequest.newBuilder()
														 .uri(new URI(_initData.getUrl().getUrl()));
			   /* _initData.getHeaders()
							         .forEach( h -> {
							        	 request.header(h.getId().asString(), h.getValue());

							         });*/
				request.headers(_initData.asIdValueStringArray());
				return request.GET()
								.build();
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new HTTPRequestBuilderImplException(e.getLocalizedMessage());
			}
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class HTTP2HttpRequestBuilderBuildForPOSTStep {

		private final RequestInitWrapperData _initData;

		public HttpRequest build() {
			try {
				HttpRequest.Builder request = HttpRequest.newBuilder()
														 .uri(new URI(_initData.getUrl().getUrl()));
			   /* _initData.getHeaders()
				         .forEach( h -> {
				        	              request.header(h.getId().asString(), h.getValue());
				         				});*/
				request.headers(_initData.asIdValueStringArray());
				return request.POST(_initData.getBodyPublisher())
								.build();
			} catch (final Throwable e) {
				e.printStackTrace();
				throw new HTTPRequestBuilderImplException(e.getLocalizedMessage());
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
	public static class RequestInitWrapperData
	         implements Serializable {

		private static final long serialVersionUID = 5787425585729960483L;

		@Getter @Setter  private Collection<HttpHeader>  _headers;
		@Getter @Setter  private Url   _url;
		@Getter @Setter  private BodyPublisher _bodyPublisher;

		public void addHeaders( final Collection<HttpHeader>  headers) {
			if ( CollectionUtils.isNullOrEmpty(_headers)) {
				_headers = Lists.newArrayList();
			}
			_headers.addAll(headers);
		}
		public void addHeaders( final HttpHeader...  headers) {
			addHeaders(Lists.newArrayList(headers));
		}
		public void addHeader(final String idHeader, final String vale) {
			addHeaders(new HttpHeader(HttpHeaderID.forId(idHeader),vale));
		}

		public String[]  asIdValueStringArray() {
			String[] values =  _headers.stream()
					                   .flatMap( h -> Stream.of( h.getId().asString(),h.getValue()))
					                   .toArray(String[]::new);
			return values;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	HTTPSignatureBuilderImplException
/////////////////////////////////////////////////////////////////////////////////////////
	public static class HTTPRequestBuilderImplException
			    extends RuntimeException {

		private static final long serialVersionUID = -7888592458789620124L;

		public HTTPRequestBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
