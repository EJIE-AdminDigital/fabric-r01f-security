package r01f.services.delegates.security.userpassword.secondfactor;






import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.core.services.mail.EMailMimeMessages;
import r01f.core.services.mail.model.EMailMessage;
import r01f.core.services.mail.model.EMailMessageBuilder;
import r01f.core.services.mail.model.EMailRFC822Address;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.authn.secondfactor.messages.dispatch.LoginAuthnSecondFactorDispatchResponse;
import r01f.model.security.login.authn.secondfactor.messages.dispatch.LoginAuthnSecondFactorDispatchResult;
import r01f.model.security.login.authn.secondfactor.messages.validation.IsLoginAuthnSecondFactorValidationRequest;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationRequestUsingOTP;
import r01f.model.security.login.authn.secondfactor.messages.validation.LoginAuthnSecondFactorValidationResponse;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.security.otp.task.dispatch.IsOTPDispatchRequest;
import r01f.model.security.otp.task.dispatch.OTPDispatchRequestBuilder;
import r01f.model.security.otp.task.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPEMailDispatchedMessageFactory;
import r01f.model.security.otp.task.dispatch.OTPDispatchedMessageFactories.IsOTPSMSDispatchedMessageFactory;
import r01f.model.security.otp.task.generation.OTPGenerationRequest;
import r01f.model.security.otp.task.generation.OTPGenerationRequestBuilder;
import r01f.model.security.otp.task.generation.OTPGenerationResponse;
import r01f.model.security.otp.task.generation.OTPSecretType;
import r01f.model.security.otp.task.validation.IsOTPValidationRequest;
import r01f.model.security.otp.task.validation.OTPValidationRequestBuilder;
import r01f.model.security.otp.task.validation.OTPValidationResponse;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.SecurityIDs.Secret;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;


@Slf4j
public class LoginAuthnSecondFactorTaskServicesUsingOTP
  implements LoginAuthnSecondFactorTaskServices {
/////////////////////////////////////////////////////////////////////////////////////////
// MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	final OTPService _otpService;
	final AppCode _appCode;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthnSecondFactorTaskServicesUsingOTP(final AppCode appCode,
													  final OTPService otpService) {
		_otpService = otpService;
		_appCode = appCode;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  DELIVER
/////////////////////////////////////////////////////////////////////////////////////////
	public <M extends IsContactMean> LoginAuthnSecondFactorDispatchResponse deliver(final SecurityContext securityContext,
													   	  							final M contactMean) {
		if (contactMean == null ) throw new IllegalArgumentException(" No [contact mean] to deliver the otp");
		// [1] Generate OTP
		log.info("Generate OTP to be delivered to {}",
				 contactMean);

		OTPGenerationRequest otpRequest = OTPGenerationRequestBuilder.usingSecretOfType(OTPSecretType.DIGITS_ONLY)
																		 .withLength(6)
																		 .aliveDuringSeconds(10000)
																		 .notAllowMoreRetryAttemptsThan(6)
																	 .buildFor(_appCode);
		// [2] return if creation failed.
		OTPOperationExecResult<OTPGenerationResponse> otpGenerateResult = _otpService.generate(securityContext,
																							   otpRequest);
		if (otpGenerateResult.hasFailed()) {
			throw new IllegalStateException(Strings.customized("Cannot generate OTP task {}",
															   otpGenerateResult.asCOREServiceMethodExecError().
															   					 getError().getLocalizedMessage()));
		}
		// [3] Now, build the OTP Dispatch Request.
		OTPResponseBase otpGenerationResponse = otpGenerateResult.getOrThrow();
		IsOTPDispatchRequest<?,?,?>	dispatchRequest = null;
		if (contactMean instanceof Phone) {
			Phone phone = contactMean.asPhone();
			dispatchRequest = OTPDispatchRequestBuilder.forTaskWithOid(otpGenerationResponse.getTaskOid())
													   .usingSMS()
													   .from(Phone.create("112")) // not used internaly
													   .to(phone)
													   .buildMessageUsing(new IsOTPSMSDispatchedMessageFactory() {
																					@Override
																					public String createMessageUsing(final OwnedContactMean<Phone> from,final Phone to,
																													 final Secret secret) {
																						return Strings.customized("Eskatutako kodea / Codigo Solicitado: {}",
																												  secret);
																					}
																				})
													   .buildFor(_appCode);
		} else if (contactMean instanceof EMail) {
			EMail email = contactMean.asEMail();
			dispatchRequest = OTPDispatchRequestBuilder.forTaskWithOid(otpGenerationResponse.getTaskOid())
													   .usingEMail()
													   .from(EMail.create("no-reply@euskadi.eus")) // not used internaly
													   .to(email)
													   .buildMessageUsing(new IsOTPEMailDispatchedMessageFactory() {
																				@Override
																				public MimeMessage createMessageUsing(final EMailRFC822Address from,final EMailRFC822Address to,
																						                              final Secret password) {
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
																					} catch (MessagingException mesEx) {
																						log.error("Error building message {}",
																								  mesEx.getLocalizedMessage(),
																								  mesEx);
																					}
																					return null;
																				}
																			})
													.buildFor(_appCode);
		}

		OTPOperationExecResult<OTPDispatchResponse> dispatchResponse = _otpService.dispatch(securityContext,
																							dispatchRequest);
		if (dispatchResponse.hasFailed()) throw new IllegalStateException(Strings.customized("Cannot dispatch {} ",
															   				 				 otpGenerateResult.asCOREServiceMethodExecError()
															   				 				 				  .getError().getLocalizedMessage(),
															   				 				 otpGenerateResult.asCOREServiceMethodExecError()
															   				 				 				  .getError()));
		return new LoginAuthnSecondFactorDispatchResponse(LoginAuthnSecondFactorDispatchResult.DISPATCHED);	// dispathed
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	VALIDATE
/////////////////////////////////////////////////////////////////////////////////////////
	@Override 
	public LoginAuthnSecondFactorValidationResponse validate(final SecurityContext securityContext,
														  	 final IsLoginAuthnSecondFactorValidationRequest request) {

		LoginAuthnSecondFactorValidationRequestUsingOTP validationReq = request.asLoginAuthnSecondFactorValidationRequestUsingOTP();

		log.debug("Validate {} for [contact mean] {} ",
				  validationReq.getSecret(),
				  validationReq.getFactor()
				  			   .getContactMean());
		IsOTPValidationRequest<?> validReq = OTPValidationRequestBuilder.usingContactMean(OTPTaskOID.from(validationReq.getOtpJobOid()),
																						  validationReq.getFactor()
																							  		   .getContactMean())
																		    .validate(validationReq.getSecret())
																		    .buildFor(_appCode);

		OTPOperationExecResult<OTPValidationResponse> result = _otpService.validate(securityContext,
																					validReq);
		if (result.hasFailed()) throw new IllegalStateException(Strings.customized("Cannot validate {} ",
															   					   result.asCOREServiceMethodExecError()
															   					   		 .getError().getLocalizedMessage()),
																				   result.asCOREServiceMethodExecError()
															   					   		 .getError());

		OTPValidationResponse otpvalidationResponse = result.getOrThrow();
		return new LoginAuthnSecondFactorValidationResponse(otpvalidationResponse.isSecretValidated(),
													   		otpvalidationResponse.getRemainingRetryNum());
	}
}
