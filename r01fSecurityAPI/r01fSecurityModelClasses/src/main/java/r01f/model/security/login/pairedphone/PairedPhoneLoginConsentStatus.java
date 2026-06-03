package r01f.model.security.login.pairedphone;

import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumExtended;

@Accessors(prefix="_")
@RequiredArgsConstructor
public enum PairedPhoneLoginConsentStatus
 implements EnumExtended<PairedPhoneLoginConsentStatus> {
	REQUESTED,
	ACCEPTED,
	DENIED,
	ERROR;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	public static final boolean canBe(final String name) {
		return EnumExtended.canBeFromName(name,PairedPhoneLoginConsentStatus.class);
	}
}
