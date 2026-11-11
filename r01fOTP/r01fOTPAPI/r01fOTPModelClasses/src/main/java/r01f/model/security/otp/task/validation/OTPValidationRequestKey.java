package r01f.model.security.otp.task.validation;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumExtended;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum OTPValidationRequestKey
 implements EnumExtended<OTPValidationRequestKey> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	TASK_OID,
	CONTACT_MEAN;
}
