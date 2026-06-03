package r01f.client.api.sub.security.login.userpassword;

import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.login.userpassword.UserPasswordLoginServices;
import r01f.httpclient.HttpRequestBodyPublisherForURLEncodedForm;
import r01f.httpclient.HttpRequestFormParameterForText;
import r01f.mime.MimeTypes;
import r01f.model.security.login.userpassword.UserPasswordLogin;
import r01f.model.security.login.userpassword.UserPasswordLoginErrorType;
import r01f.model.security.login.userpassword.UserPasswordLoginRequest;
import r01f.model.security.login.userpassword.UserPasswordLoginRequestBuilder;
import r01f.model.security.login.userpassword.UserPasswordLoginResponse;
import r01f.model.security.login.userpassword.UserPasswordLoginResponseForWebFlow;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationRequest;
import r01f.model.security.login.userpassword.confirmation.UserPasswordLoginConfirmationResponse;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateRequest;
import r01f.model.security.login.userpassword.create.UserPasswordLoginCreateResponse;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorUsingOTPBySMS;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryRequest;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryRequestBuilder;
import r01f.model.security.login.userpassword.recovery.UserPasswordLoginRecoveryResponse;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetRequest;
import r01f.model.security.login.userpassword.reset.UserPasswordLoginResetResponse;
import r01f.model.security.operation.SecurityOperationResult;
import r01f.model.security.user.User;
import r01f.objectstreamer.Marshaller;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.services.api.delegates.ClientAPIServiceDelegateBase;
import r01f.types.contact.EMail;
import r01f.types.contact.HasMailAddress;
import r01f.types.contact.HasPhoneNumber;
import r01f.types.contact.Phone;
import r01f.types.url.Url;
import r01f.util.types.Strings;

public class ClientAPIDelegateForUserPasswordLoginServices<U extends User & HasMailAddress & HasPhoneNumber,
														   L extends UserPasswordLogin>
	 extends ClientAPIServiceDelegateBase<UserPasswordLoginServices<U,L>> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForUserPasswordLoginServices(final Provider<SecurityContext> securityContextProvider,
												 		 final Marshaller marshaller,
												 		 final UserPasswordLoginServices<U,L> loginServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  loginServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	REGISTER & CONFIRM
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("unchecked")
	public UserPasswordLoginCreateResponse createLogin(final UserPasswordLoginCreateRequest<?> request) {
		SecurityOperationResult<UserPasswordLoginCreateResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
																			    .createLogin(this.getSecurityContext(),
																					  		 request);
		UserPasswordLoginCreateResponse registerResponse = opResult.getOrThrow();
		return registerResponse;
	}
	@SuppressWarnings("unchecked")
	public UserPasswordLoginConfirmationResponse confirmLogin(final UserPasswordLoginConfirmationRequest request) {
		SecurityOperationResult<UserPasswordLoginConfirmationResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
																					  .confirmLogin(this.getSecurityContext(),
																							  	    request);
		UserPasswordLoginConfirmationResponse confirmResponse = opResult.getOrThrow();
		return confirmResponse;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////
	public boolean login(final LoginID loginId,final Password password) {
		UserPasswordLoginRequest req = UserPasswordLoginRequestBuilder.using(loginId)
																	  .with(password)
																	  .build();
		UserPasswordLoginResponse response = this.login(req);
		return response.hasSucceeded();
	}
	@SuppressWarnings("unchecked")
	public UserPasswordLoginResponse login(final UserPasswordLoginRequest loginRequest) {
		SecurityOperationResult<UserPasswordLoginResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
																		  .login(this.getSecurityContext(),
																				 loginRequest);
		UserPasswordLoginResponse loginResponse = opResult.getOrThrow();
		return loginResponse;
	}

	@SuppressWarnings("unchecked")
	public  UserPasswordLoginResponse validate(final LoginAuthFactorValidationRequest loginAuthFactorValidationRequest) {
		SecurityOperationResult<UserPasswordLoginResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
				                                                          	.validate(getSecurityContext(),
				                                                        		      loginAuthFactorValidationRequest);

		UserPasswordLoginResponse loginResponse = opResult.getOrThrow();
		return loginResponse;
	}


	/**
	 * Uses a web flow to login: post the user / password to the [security rest services login] endpoint
	 * The [security rest services login] method returns the location where the user might be redirected
	 * @param securityRESTServicesEndPointUrl
	 * @param login
	 * @param password
	 * @param toUrl
	 * @return
	 */
	public UserPasswordLoginResponseForWebFlow webFlowLogin(final Url securityRESTServicesEndPointUrl,
							 								final LoginID login,final Password password,
							 								final Url toUrl) {
		HttpRequest request = null;
		try {
			request = HttpRequest.newBuilder()
										 .uri(securityRESTServicesEndPointUrl.asUri())
										 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
										 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
										  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(login.asString()).withName("userCode"),
															  		    	  HttpRequestFormParameterForText.of(password.asString()).withName("pass"),
															  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

										  		    		  )

										  		      .build())
										 .build();
		} catch ( final  URISyntaxException urix) {
			throw new IllegalArgumentException(urix.getLocalizedMessage());
		}
		return  _webFlowLogin(request);
	}

	public UserPasswordLoginResponseForWebFlow webFlowLogin(final Url securityRESTServicesEndPointUrl,
							 								final LoginID login,final Password password,
							 								final LoginAuthFactor authFactor,
							 								final Url toUrl) {
		HttpRequest postReq = null;
		try {
			if ( authFactor.isByOTP()) {
				LoginAuthFactorUsingOTPBase authFactorByOTP = authFactor.as(LoginAuthFactorUsingOTPBase.class);
				if ( authFactorByOTP.isByMail()) {
					postReq = HttpRequest.newBuilder()
										 .uri(securityRESTServicesEndPointUrl.asUri())
										 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
										 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
										  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(login.asString()).withName("userCode"),
															  		    	  HttpRequestFormParameterForText.of(password.asString()).withName("pass"),
															  		    	  HttpRequestFormParameterForText.of(authFactorByOTP.as(LoginAuthFactorUsingOTPByEMail.class).getMail().asString()).withName("email"),
															  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

										  		    		  )

										  		      .build())
										 .build();

				} else if (authFactorByOTP.isBySms()) {
					postReq = HttpRequest.newBuilder()
										 .uri(securityRESTServicesEndPointUrl.asUri())
										 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
										 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
										  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(login.asString()).withName("userCode"),
															  		    	  HttpRequestFormParameterForText.of(password.asString()).withName("pass"),
															  		    	  HttpRequestFormParameterForText.of(authFactorByOTP.as(LoginAuthFactorUsingOTPBySMS.class).getPhone().asString()).withName("phone"),
															  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

										  		    		  )

										  		      .build())
										 .build();
				}
			} else {
				throw new IllegalArgumentException( Strings.customized(" '{}' not supported."
																		+ "In order to supply another auth factor method extend this method and the login REST Service",authFactor.getClass()));
			};

		} catch ( final  URISyntaxException urix) {
			throw new IllegalArgumentException(urix.getLocalizedMessage());
		}
		return  _webFlowLogin(postReq);
	}

	public UserPasswordLoginResponseForWebFlow webFlowLoginValidateAuthFactor(final Url securityRESTServicesEndPointUrl,
																			  final LoginAuthFactorValidationRequest authFactorValidationRequest,
																			  final Url toUrl) {
		HttpRequest postReq = null;
		try {
			if ( authFactorValidationRequest.getFactor().isByOTP()) {
				LoginAuthFactorUsingOTPBase authFactorByOTP = authFactorValidationRequest.getFactor().as(LoginAuthFactorUsingOTPBase.class);
				if ( authFactorByOTP.isByMail()) {
						postReq = HttpRequest.newBuilder()
											 .uri(securityRESTServicesEndPointUrl.asUri())
											 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
											 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
											  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(authFactorValidationRequest.getLoginId().asString()).withName("userCode"),
																  		    	  HttpRequestFormParameterForText.of(authFactorValidationRequest.getSecret().asString()).withName("secret"),
																  		    	  HttpRequestFormParameterForText.of(authFactorByOTP.as(LoginAuthFactorUsingOTPByEMail.class).getMail().asString()).withName("email"),
																  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

											  		    		  )

											  		      .build())
											 .build();


				} else if (authFactorByOTP.isBySms()) {
						postReq = HttpRequest.newBuilder()
								 .uri(securityRESTServicesEndPointUrl.asUri())
								 .header("Content-Type", MimeTypes.FORM_URL_ENCODED.getName())
								 .POST(new HttpRequestBodyPublisherForURLEncodedForm()
								  		      .withPOSTFormParameters(HttpRequestFormParameterForText.of(authFactorValidationRequest.getLoginId().asString()).withName("userCode"),
													  		    	  HttpRequestFormParameterForText.of(authFactorValidationRequest.getSecret().asString()).withName("secret"),
													  		    	  HttpRequestFormParameterForText.of(authFactorByOTP.as(LoginAuthFactorUsingOTPBySMS.class).getPhone().asString()).withName("phone"),
													  		    	  HttpRequestFormParameterForText.of(toUrl.asString()).withName("to")

								  		    		  )

								  		      .build())
								 .build();
				}
			} else {
				throw new IllegalArgumentException( Strings.customized(" '{}' not supported."
													+ "In order to supply another auth factor method extend this method and the login REST Service",authFactorValidationRequest.getClass()));
			};

		} catch ( final  URISyntaxException urix) {
			throw new IllegalArgumentException(urix.getLocalizedMessage());
		}

		return  _webFlowLogin(postReq);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	RESET & RECOVER
/////////////////////////////////////////////////////////////////////////////////////////
	public UserPasswordLoginRecoveryResponse recoverPassword(final LoginID login,final EMail email) {
		UserPasswordLoginRecoveryRequest req = UserPasswordLoginRecoveryRequestBuilder.forLoginId(login)
																					  .sendNotificationTo(email)
																					  .build();
		return this.recoverPassword(req);
	}
	public UserPasswordLoginRecoveryResponse recoverPassword(final LoginID login,final Phone phone) {
		UserPasswordLoginRecoveryRequest req = UserPasswordLoginRecoveryRequestBuilder.forLoginId(login)
																					  .sendNotificationTo(phone)
																					  .build();
		return this.recoverPassword(req);
	}
	@SuppressWarnings("unchecked")
	public UserPasswordLoginRecoveryResponse recoverPassword(final UserPasswordLoginRecoveryRequest request) {
		SecurityOperationResult<UserPasswordLoginRecoveryResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
																						.recoverPassword(this.getSecurityContext(),
																										 request);
		UserPasswordLoginRecoveryResponse requestOk = opResult.getOrThrow();
		return requestOk;
	}
	@SuppressWarnings("unchecked")
	public UserPasswordLoginResetResponse resetPassword(final UserPasswordLoginResetRequest request) {
		SecurityOperationResult<UserPasswordLoginResetResponse> opResult = this.getServiceProxyAs(UserPasswordLoginServices.class)
																					 .resetPassword(this.getSecurityContext(),
																									request);
		UserPasswordLoginResetResponse requestOk = opResult.getOrThrow();
		return requestOk;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	WEB FLOW PROTECTED METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	protected UserPasswordLoginResponseForWebFlow _webFlowLogin(final HttpRequest postReq) {
		try {
			HttpResponse<InputStream> response = HttpClient.newBuilder()
														   .build()
														   .send(postReq,
																 HttpResponse.BodyHandlers.ofInputStream());
			// the REST service should return a 303 (moved permanently response code) to:
			//		- if the login was successfull > the [toUrl] with a JWT token
			//		- if the login was NOT successfull > the login page (in this case a x-r01-userpwd-error header is returned with the error cause
			int statusCode = response.statusCode();
			if (statusCode == 303) {
				// .. the uri target
				String locationHeader = response.headers().firstValue("Location").get();
				// .. some functional headers
				String xErrorHeader = response.headers().firstValue("x-r01-userpwd-error").orElse(null);
				String xAuthFactorPendingHeader = response.headers().firstValue("x-r01-userpwd-pending-on-auth-factor").orElse(null);

				UserPasswordLoginResponseForWebFlow outResponse = null;
				if (xErrorHeader != null) {
					outResponse = UserPasswordLoginResponseForWebFlow.failed(Url.from(locationHeader),
																		  	 UserPasswordLoginErrorType.valueOf(xErrorHeader));
				} else if (xAuthFactorPendingHeader != null) {
					outResponse = UserPasswordLoginResponseForWebFlow.pendingOnAuthFactor(Url.from(locationHeader));
				} else {
					outResponse = UserPasswordLoginResponseForWebFlow.success(Url.from(locationHeader));
				}
				return outResponse;
			}
			throw new IllegalStateException(Strings.customized("Security REST '{}' service returned an illegal response " +
															   "303 code was expected but was {} ",
															   postReq.uri(),statusCode));
		} catch (final Throwable th) {
			throw new IllegalStateException("There is some kind of problem with the [security rest services]: " + th.getMessage(),th);
		}
	}

	protected UserPasswordLoginResponseForWebFlow _webFlowLoginValidateAuthFactor(final HttpRequest postReq) {
		// ..currently for validating a auth factor has the same flow as the first step when ( usr / pwd / phone/ is sent )
		 return _webFlowLogin(postReq);
	}
}
