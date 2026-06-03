package r01f.model.security.otp.operations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;

/**
 * otp Operations error codes
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPErrorTypes
     		  extends COREServiceErrorTypes {

	// OTP Generation Error.
	public static COREServiceErrorType OTP_GENERATION_ERROR = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.SERVER)
																			  .withName("OTP_GENERATION_ERROR")
																			  .coded(300,1)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	
	// OTP Validation
	public static COREServiceErrorType OTP_VALIDATION_ERROR_OTP_NOT_VALID = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																			  .withName("OTP_VALIDATION_ERROR_OTP_NOT_VALID")
																			  .coded(300,2)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	
	public static COREServiceErrorType OTP_VALIDATION_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																			  .withName("OTP_VALIDATION_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED")
																			  .coded(300,3)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	public static COREServiceErrorType OTP_VALIDATION_ERROR_OTP_NUMBER_OF_ATTEMPTS_EXCEEDED = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																			  .withName("OTP_VALIDATION_ERROR_OTP_NUMBER_OF_ATTEMPTS_EXCEEDED")
																			  .coded(300,4)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	
	// OTP Dispataching
	public static COREServiceErrorType UNKNOWN_METHOD_OF_OTP_DISPATCHING = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																			  .withName("UNKNOWN_METHOD_OF_OTP_DISPATCHING")
																			  .coded(300,5)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	public static COREServiceErrorType OTP_DISPATCHING_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																			  .withName("OTP_DISPATCHING_ERROR_OTP_DOES_NOT_EXISTS_OR_EXPIRED")
																			  .coded(300,6)
																			  .severity(ExceptionSeverity.FATAL)
																			  .build();
	
}