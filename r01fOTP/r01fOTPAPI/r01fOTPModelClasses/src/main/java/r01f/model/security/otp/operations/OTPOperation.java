package r01f.model.security.otp.operations;

import r01f.enums.EnumExtended;
import r01f.model.services.COREServiceMethod;

/**
 * OTPRequestedOperation
 */
public enum OTPOperation
 implements EnumExtended<OTPOperation> {
	GENERATE,
	VALIDATION,
	DISPATCH,
	OTHER;
	
	public COREServiceMethod getCOREServiceMethod() {
		return COREServiceMethod.named(this.name());
	}
///////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPOperation from(final COREServiceMethod method) {
		return OTPOperation.fromName(method.asString());
	}
	public static OTPOperation fromName(final String name) {
		return EnumExtended.fromName(name,OTPOperation.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + OTPOperation.class + " element with name=" + name));
	}
	public static boolean canBe(final String name) {
		return EnumExtended.canBeFromName(name,OTPOperation.class);
	}
}