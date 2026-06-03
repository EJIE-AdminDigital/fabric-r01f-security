package r01f.services.delegates.sub.handlers.security;

import java.time.Instant;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.core.services.mail.EMailMimeMessages;
import r01f.core.services.mail.model.EMailMessage;
import r01f.core.services.mail.model.EMailMessageBuilder;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.guids.CommonOIDs.AppCode;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseOK;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPByEMail;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseOK;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.dispatch.OTPDispatchRequest;
import r01f.model.security.otp.dispatch.OTPDispatchRequestBuilder;
import r01f.model.security.otp.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPEMailDispatchedMessageFactory;
import r01f.model.security.otp.generation.OTPGenerationRequest;
import r01f.model.security.otp.generation.OTPGenerationRequestBuilder;
import r01f.model.security.otp.generation.OTPGenerationResponse;
import r01f.model.security.otp.generation.OTPSecretType;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.validation.OTPValidationRequest;
import r01f.model.security.otp.validation.OTPValidationRequestBuilder;
import r01f.model.security.otp.validation.OTPValidationResponse;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;
import r01f.util.types.Strings;


@Slf4j
public class LoginAuthFactorTaskServicesUsingOTPByEMail
	implements  LoginAuthFactorTaskServices {
/////////////////////////////////////////////////////////////////////////////////////////
// MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	final OTPService _otpService;
	final AppCode _appCode;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorTaskServicesUsingOTPByEMail( final AppCode appCode,
			                                           final OTPService otpService) {
		_otpService = otpService;
		_appCode = appCode;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorDispatchResponseBase deliver(final SecurityContext securityContext,
			                                           final EMail mail) {
		if ( mail == null ) throw new IllegalArgumentException(" No mail to deliver the otp");
		// [1] Generate OTP
		log.info("Generate OTP");
		OTPGenerationRequest otpRequest = OTPGenerationRequestBuilder.usingSecretOfType(OTPSecretType.DIGITS_ONLY)
																		 .withLength(6)
																		 .aliveDuringSeconds(10000)
																		 .notAllowMoreRetryAttemptsThan(6)
																	 .buildFor(_appCode);
		// [2] return if creation failed.
		OTPOperationExecResult<OTPGenerationResponse> otpGenerateResult = _otpService.generate( securityContext,
																								otpRequest);
		if (otpGenerateResult.hasFailed()) {
			throw new IllegalStateException(Strings.customized("Cannot generate OTP task {} " ,
															                        otpGenerateResult.asCOREServiceMethodExecError().getError().getLocalizedMessage()));
		}
		// [3] Now, build the OTP Dispatch Request.
		OTPResponseBase otpGenerationResponse = otpGenerateResult.getOrThrow();
		OTPDispatchRequest<?,?,?>	dispatchRequest =  OTPDispatchRequestBuilder.forTaskWithOid(otpGenerationResponse.getTaskOid())
																					.usingEMail()
																					.from(EMail.create("no-reply@euskadi.eus")) // not used internaly
																					.to(mail)
																					.buildMessageUsing(new OTPEMailDispatchedMessageFactory() {
																												@Override
																												public MimeMessage createMessageUsing(final EMailRFC822Address from,
																														                              final EMailRFC822Address to,
																														                              final Password password) {

																													try {
																															EMailMessage message = EMailMessageBuilder.from(from)
																																									  .to(to)
																																									  .noCC().noBCC()
																																									  .withSubject("[Euskadi.eus ] OTP Dispatch")
																																									  .withPlainTextBody(password.asString())
																																								  .build();
																															return EMailMimeMessages.createMimeMessageFor(message)
																																					.withDefaultCharset()
																																					.usingDefaultSession()
																																					.noAttachments()
																																					.build();
																													} catch (final MessagingException mesEx) {
																														log.error("Error building message {}",
																																  mesEx.getLocalizedMessage(),
																																  mesEx);
																													}
																													return null;
																												}
																											})
																					.buildFor(_appCode);

		OTPOperationExecResult<OTPDispatchResponse> dispatchResponse = _otpService.dispatch(securityContext,
																							dispatchRequest);
		if ( dispatchResponse.hasFailed() ) {
			throw new IllegalStateException(Strings.customized("Cannot dispatch {} " ,
                                                    otpGenerateResult.asCOREServiceMethodExecError().getError().getLocalizedMessage()));
		}
		return new LoginAuthFactorDispatchResponseOK(true,Instant.now());
	}


	@Override
	public LoginAuthFactorValidationResponseBase validate(final SecurityContext securityContext,
												          final LoginAuthFactorValidationRequest request) {

		LoginAuthFactorValidationRequestUsingOTPByEMail factorValidationRequest =request.as(LoginAuthFactorValidationRequestUsingOTPByEMail.class);

		log.info("Validate '{} ' for secret '{}' ",
											factorValidationRequest.getEMail(),
											factorValidationRequest.getSecret());
		OTPValidationRequest<EMail> validReq = OTPValidationRequestBuilder.using(factorValidationRequest.getEMail())
																		  .validate(factorValidationRequest.getSecret())
																		  .buildFor(_appCode);

		OTPOperationExecResult<OTPValidationResponse> result = _otpService.validate(securityContext, validReq);
		if ( result.hasFailed() ) {
			throw new IllegalStateException(Strings.customized("Cannot validate {} " ,
					                        result.asCOREServiceMethodExecError().getError().getLocalizedMessage()));
		}
		OTPValidationResponse otpvalidationResponse = result.getOrThrow();
		return new LoginAuthFactorValidationResponseOK(otpvalidationResponse.isSecretValidated(),otpvalidationResponse.getRemainingRetryNum());
	}
}
