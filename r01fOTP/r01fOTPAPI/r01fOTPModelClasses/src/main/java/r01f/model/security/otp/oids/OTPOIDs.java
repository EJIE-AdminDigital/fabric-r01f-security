package r01f.model.security.otp.oids;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.guids.JavaOIDDispenser;
import r01f.guids.OID;
import r01f.guids.OIDs;
import r01f.guids.PersistableObjectOID;

public abstract class OTPOIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	BASE
/////////////////////////////////////////////////////////////////////////////////////////
	public static interface IsOTPOID
	                extends PersistableObjectOID {
		public static String supplyOidAsString() {
			return OIDs.supplyOid();
		}
		public default String convertToDatabaseColumn(final OID attribute) {
			return attribute.asString();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	public record OTPTaskOID(@Getter String id)
       implements IsOTPOID  {

		public static OTPTaskOID from(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID forId(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID valueOf(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID fromString(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID supplyOid() {
			return OTPTaskOID.forId(JavaOIDDispenser.generateGUID());
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
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	public record OTPNotifierTaskOID(@Getter String id)
	   implements IsOTPOID  {
	
		public static OTPNotifierTaskOID from(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID forId(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID valueOf(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID fromString(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID supplyOid() {
			return OTPNotifierTaskOID.forId(JavaOIDDispenser.generateGUID());
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
