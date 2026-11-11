package r01f.model.security.otp.oids;

import lombok.Getter;
import r01f.annotations.Immutable;
import r01f.oids.JavaOIDDispenser;
import r01f.oids.OID;
import r01f.oids.OIDs;
import r01f.oids.PersistableObjectOID;
import r01f.types.jobs.JobOID;

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
		public static OTPTaskOID supply() {
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
		public JobOID asJobOID() {
			return JobOID.forId(this.id);
		}
		public static OTPTaskOID from(final JobOID jobOid) {
			return OTPTaskOID.forId(jobOid.asString());
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
		public static OTPNotifierTaskOID supply() {
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
