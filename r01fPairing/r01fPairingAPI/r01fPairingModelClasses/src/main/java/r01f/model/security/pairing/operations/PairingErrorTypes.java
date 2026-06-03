package r01f.model.security.pairing.operations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.exceptions.ExceptionSeverity;
import r01f.model.services.COREServiceErrorOrigin;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;

/**
 * Pairing Operations common error codes
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class PairingErrorTypes
     		  extends COREServiceErrorTypes {

	//	PAIR_ERROR_FOR_NOT_VALID_OTP
	public static COREServiceErrorType PAIR_ERROR_FOR_NOT_VALID_OTP = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																							  .withName("PAIR_ERROR_FOR_NOT_VALID_OTP")
																							  .coded(300,1)
																							  .severity(ExceptionSeverity.FATAL)
																						  .build();
	
//	PAIR_ERROR_FOR_NOT_VALID_OTP
	public static COREServiceErrorType PAIR_ERROR_ON_OTP_DISTPATCH = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																							  .withName("PAIR_ERROR_FOR_NOT_VALID_OTP")
																							  .coded(300,1)
																							  .severity(ExceptionSeverity.FATAL)
																						  .build();
	
	//	PAIR_ERROR_FOR_UNCHECKED_USER_DATA
	public static COREServiceErrorType PAIR_ERROR_FOR_UNCHECKED_USER_DATA = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.CLIENT)
																								  .withName("PAIR_ERROR_FOR_UNCHECKED_USER_DATA")
																								  .coded(300,4)
																								  .severity(ExceptionSeverity.FATAL)
																							  .build();
	
	// UNPAIR_ERROR
	public static COREServiceErrorType UNPAIR_ERROR = COREServiceErrorType.originatedAt(COREServiceErrorOrigin.SERVER)
																							  .withName("UNPAIR_ERROR")
																							  .coded(300,5)
																							  .severity(ExceptionSeverity.FATAL)
																							  .build();
	
}