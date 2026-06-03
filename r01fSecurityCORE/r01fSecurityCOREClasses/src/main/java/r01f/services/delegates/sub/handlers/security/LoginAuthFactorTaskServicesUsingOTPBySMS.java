package r01f.services.delegates.sub.handlers.security;

import java.time.Instant;

import lombok.extern.slf4j.Slf4j;
import r01f.api.interfaces.security.otp.OTPService;
import r01f.guids.CommonOIDs.AppCode;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorDispatchResponseOK;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequest;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationRequestUsingOTPBySMS;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseBase;
import r01f.model.security.login.userpassword.factors.LoginAuthFactorValidationResponseOK;
import r01f.model.security.otp.OTPResponseBase;
import r01f.model.security.otp.dispatch.OTPDispatchRequest;
import r01f.model.security.otp.dispatch.OTPDispatchRequestBuilder;
import r01f.model.security.otp.dispatch.OTPDispatchResponse;
import r01f.model.security.otp.dispatch.OTPDispatchedMessageFactories.OTPSMSDispatchedMessageFactory;
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
import r01f.types.contact.OwnedContactMean;
import r01f.types.contact.Phone;
import r01f.util.types.Strings;


@Slf4j
public class LoginAuthFactorTaskServicesUsingOTPBySMS
	implements  LoginAuthFactorTaskServices {
/////////////////////////////////////////////////////////////////////////////////////////
// MEMBERS
/////////////////////////////////////////////////////////////////////////////////////////
	final OTPService _otpService;
	final AppCode _appCode;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorTaskServicesUsingOTPBySMS(final AppCode appCode,
													final OTPService otpService) {
		_otpService = otpService;
		_appCode = appCode;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	public LoginAuthFactorDispatchResponseBase deliver(final SecurityContext securityContext,
													   final Phone phone) {
		if ( phone == null ) throw new IllegalArgumentException(" No phone to deliver the otp");
		// [1] Generate OTP
		log.warn("INFORMATIVE Purpose: Generate OTP");
		log.info("Generate OTP: {}", phone);

		OTPGenerationRequest otpRequest = OTPGenerationRequestBuilder.usingSecretOfType(OTPSecretType.DIGITS_ONLY)
																		 .withLength(6)
																		 .aliveDuringSeconds(10000)
																		 .notAllowMoreRetryAttemptsThan(6)
																	 .buildFor(_appCode);
		// [2] return if creation failed.
		OTPOperationExecResult<OTPGenerationResponse> otpGenerateResult = _otpService.generate( securityContext,
																								otpRequest);
		if (otpGenerateResult.hasFailed()) {
			throw new IllegalStateException(Strings.customized("Cannot generate OTP task {} ",
															   otpGenerateResult.asCOREServiceMethodExecError().
															   					 getError().getLocalizedMessage()));
		}
		// [3] Now, build the OTP Dispatch Request.
		OTPResponseBase otpGenerationResponse = otpGenerateResult.getOrThrow();
		OTPDispatchRequest<?,?,?>	dispatchRequest =  OTPDispatchRequestBuilder.forTaskWithOid(otpGenerationResponse.getTaskOid())
																					.usingSMS()
																					.from(Phone.create("112")) // not used internaly
																					.to(phone)
																					.buildMessageUsing(new OTPSMSDispatchedMessageFactory() {
																												@Override
																												public String createMessageUsing(final OwnedContactMean<Phone> from,
																																				 final Phone to,
																																				 final Password password) {
																													return Strings.customized("Eskatutako kodea / Codigo Solicitado: {}",
																																			  password);
																												}
																											})
																					.buildFor(_appCode);

		OTPOperationExecResult<OTPDispatchResponse> dispatchResponse = _otpService.dispatch(securityContext,
																							dispatchRequest);
		if ( dispatchResponse.hasFailed() ) {
			throw new IllegalStateException(Strings.customized("Cannot dispatch {} ",
															   otpGenerateResult.asCOREServiceMethodExecError()
																				.getError()
																				.getLocalizedMessage()));
		}

		log.warn("INFORMATIVE Purpose: Generated OTP, OK");
		return new LoginAuthFactorDispatchResponseOK(true, Instant.now());
	}


	@Override
	public LoginAuthFactorValidationResponseBase validate(final SecurityContext securityContext,
														  final LoginAuthFactorValidationRequest request) {

		LoginAuthFactorValidationRequestUsingOTPBySMS factorValidationRequest =request.as(LoginAuthFactorValidationRequestUsingOTPBySMS.class);

		log.debug("Validate {}  for phone number {} ",
						factorValidationRequest.getPhone(),
						factorValidationRequest.getSecret());
		OTPValidationRequest<Phone> validReq = OTPValidationRequestBuilder.using(factorValidationRequest.getPhone())
																		  .validate(factorValidationRequest.getSecret())
																		  .buildFor(_appCode);

		OTPOperationExecResult<OTPValidationResponse> result = _otpService.validate(securityContext, validReq);
		if ( result.hasFailed() ) {
			throw new IllegalStateException(Strings.customized("Cannot validate {} ",
															   result.asCOREServiceMethodExecError()
																	 .getError()
																	 .getLocalizedMessage()));
		}

		OTPValidationResponse otpvalidationResponse = result.getOrThrow();
		return new LoginAuthFactorValidationResponseOK(otpvalidationResponse.isSecretValidated(),
													   otpvalidationResponse.getRemainingRetryNum());
	}
}
