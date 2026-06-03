package r01f.model.security.pairing.operations;

import r01f.enums.EnumExtended;
import r01f.model.services.COREServiceMethod;

/**
 * TK00PairingOperation
 */
public enum PairingOperation
 implements EnumExtended<PairingOperation> {	
	OTP_REQUEST,
	PAIR,
	UNPAIR;
	
	public COREServiceMethod getCOREServiceMethod() {
		return COREServiceMethod.named(this.name());
	}
///////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static PairingOperation from(final COREServiceMethod method) {
		return PairingOperation.fromName(method.asString());
	}
	public static PairingOperation fromName(final String name) {
		return EnumExtended.fromName(name,PairingOperation.class)
						   .orElseThrow(() -> new IllegalArgumentException("no " + PairingOperation.class + " element with name=" + name));
	}
	public static boolean canBe(final String name) {
		return EnumExtended.canBeFromName(name,PairingOperation.class);
	}
}