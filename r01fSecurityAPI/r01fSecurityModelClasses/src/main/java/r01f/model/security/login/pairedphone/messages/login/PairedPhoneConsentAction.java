package r01f.model.security.login.pairedphone.messages.login;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;
import r01f.enums.EnumExtended;

@Accessors(prefix="_")
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum PairedPhoneConsentAction
 implements EnumExtended<PairedPhoneConsentAction> {
	ACCEPTED,
	DENIED;
}
