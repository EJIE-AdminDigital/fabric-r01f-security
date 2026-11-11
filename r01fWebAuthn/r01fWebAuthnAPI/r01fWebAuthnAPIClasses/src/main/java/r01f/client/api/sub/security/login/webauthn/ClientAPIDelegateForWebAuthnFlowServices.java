package r01f.client.api.sub.security.login.webauthn;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.webauthn.CRUDServicesForAuthenticatorCredential;
import r01f.httpclient.HttpRequestBodyPublisherForURLEncodedForm;
import r01f.httpclient.HttpRequestFormParameterForText;
import r01f.io.IOStreams;
import r01f.mime.MimeTypes;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityIDs.LoginID;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;
import r01f.types.StringBase64;
import r01f.types.url.Url;
import r01f.util.types.Strings;

public class ClientAPIDelegateForWebAuthnFlowServices< C extends AuthenticatorCredential>
	 extends ClientAPIServiceDelegateBase<CRUDServicesForAuthenticatorCredential<C>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForWebAuthnFlowServices(final Provider<SecurityContext> securityContextProvider,
												    final Marshaller marshaller,
												    final CRUDServicesForAuthenticatorCredential<C> service) {
		super(securityContextProvider,
			  marshaller,
			  service);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
///

	/**
	 * Uses a web flow to login: post the user / password to the [security rest services login] endpoint
	 * The [security rest services login] method returns the location where the user might be redirected
	 * @param securityRESTServicesEndPointUrlForStartAttestion
	 * @param login
	 * @param password
	 * @param toUrl
	 * @return
	 */
	public ResponseForWebAuthAttestationStartFlow webFlowForStartAttestation(final Url securityRESTServicesEndPointUrlForStartAttestion,
							 								                 final LoginID loginId,
							 								                 final Url toUrl) {
		HttpRequest request = null;
		try {
			request = HttpRequest.newBuilder()
										 .uri(securityRESTServicesEndPointUrlForStartAttestion.asUri())
										 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
										 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
										  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(loginId.asString()).withName("loginId"),
															  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

										  		    		  )

										  		      .build())
										 .build();
		} catch ( final  URISyntaxException urix) {
			throw new IllegalArgumentException(urix.getLocalizedMessage());
		}
		return  _webFlowForStartAttestation(request);
	}



/////////////////////////////////////////////////////////////////////////////////////////
//	WEB FLOW PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected ResponseForWebAuthAttestationStartFlow _webFlowForStartAttestation(final HttpRequest postReq) {
		try {
			HttpResponse<InputStream> response = HttpClient.newBuilder()
														   .build()
														   .send(postReq,
																 HttpResponse.BodyHandlers.ofInputStream());
			// the REST service should return a 303 (moved permanently response code) to:
			//		- if the login was successfull > the [toUrl] with a JWT token
			//		- if the login was NOT successfull > the login page (in this case a x-r01-userpwd-error header is returned with the error cause
			int statusCode = response.statusCode();
			if (statusCode == 200) {
				ResponseForWebAuthAttestationStartFlow outResponse = new ResponseForWebAuthAttestationStartFlow();
				InputStream is = response.body();
				outResponse.setPublicKeyCredentialCreateOptionAsBas64JSON(StringBase64.encode(IOStreams.inputStreamBytes(is)));

				return outResponse;
			}
			throw new IllegalStateException(Strings.customized("Security REST '{}' service returned an illegal response " +
															   "200 code was expected but was {} ",
															   postReq.uri(),statusCode));
		} catch (final Throwable th) {
			throw new IllegalStateException("There is some kind of problem with the [security rest services]: " + th.getMessage(),th);
		}
	}

}
