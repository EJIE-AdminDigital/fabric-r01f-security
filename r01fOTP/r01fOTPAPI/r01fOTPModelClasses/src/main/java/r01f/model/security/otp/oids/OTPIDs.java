package r01f.model.security.otp.oids;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDTyped;
import r01f.objectstreamer.annotations.MarshallType;

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
/////////////////////////////////////////////////////////////////////////////////////////
//  OTPDIDs
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * OTP ID
	 */
	@Immutable
	@MarshallType(as="otpId")
	public record OTPID(@Getter String id)
	   implements IsOTPID {
		public static OTPID from(final String id) {
			return new OTPID(id);
		}
		public static OTPID forId(final String id) {
			return new OTPID(id);
		}
		public static OTPID valueOf(final String id) {
			return new OTPID(id);
		}
		public static OTPID fromString(final String id) {
			return new OTPID(id);
		}
		
		@Override
		public String asString() {
			return this.id;
		}
		@Override
		public String toString() {
			return this.id;
		}
	}
}
