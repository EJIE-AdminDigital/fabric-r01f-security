package r01f.model.security.otp.oids;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OIDBaseMutable;
import r01f.guids.OIDTyped;
import r01f.objectstreamer.annotations.MarshallType;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static interface IsOTPID
					extends OIDTyped<String> {
		// just a marker interface
	}
	/**
	 * Base for every OTP oid objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class OTPIDBase
						 extends OIDBaseMutable<String>
					  implements IsOTPID {
		private static final long serialVersionUID = 4162366466990455545L;

		public OTPIDBase(final String id) {
			super(id);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  OTPDIDs
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Base for every  OTP oid objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class OTPObjectIDBase
						 extends OIDBaseMutable<String> // normally this should extend OIDBaseImmutable BUT it MUST have a default no-args constructor to be serializable
					  implements IsOTPID {//, AttributeConverter<OTPOID, String> {
		private static final long serialVersionUID = 8996483673956096810L;
		public OTPObjectIDBase(final String id) {
			super(id);
		}
		@SuppressWarnings("static-method")
		public String convertToDatabaseColumn(final IsOTPID attribute) {
			return attribute.asString();
		}
	}
	/**
	 * OTP ID
	 */
	@Immutable
	@MarshallType(as="otpId")
	@NoArgsConstructor
	public static class OTPID
				extends OTPIDBase {
		private static final long serialVersionUID = -5818330851015602242L;
		public OTPID(final String oid) {
			super(oid);
		}
		public static OTPID forId(final String id) {
			return new OTPID(id);
		}
	}
}
