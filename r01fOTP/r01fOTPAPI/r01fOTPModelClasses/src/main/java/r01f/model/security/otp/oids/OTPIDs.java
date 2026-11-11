package r01f.model.security.otp.oids;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.oids.OIDTyped;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static interface IsOTPID
					extends OIDTyped<String> {
		public default String convertToDatabaseColumn(final IsOTPID attribute) {
			return attribute.asString();
		}
	}
}
