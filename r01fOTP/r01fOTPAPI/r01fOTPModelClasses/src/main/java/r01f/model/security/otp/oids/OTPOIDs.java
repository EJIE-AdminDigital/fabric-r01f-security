package r01f.model.security.otp.oids;

import lombok.NoArgsConstructor;
import r01f.annotations.Immutable;
import r01f.guids.OID;
import r01f.guids.OIDBaseMutable;
import r01f.guids.OIDs;
import r01f.guids.PersistableObjectOID;

public abstract class OTPOIDs {
/////////////////////////////////////////////////////////////////////////////////////////
//	BASE
/////////////////////////////////////////////////////////////////////////////////////////
	public static interface IsOTPOID
	                extends PersistableObjectOID {
		// just a marker interface

	}
	/**
	 * Base for every otp oid objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class OTPOIDBase
	              		 extends OIDBaseMutable<String>
			   		  implements IsOTPOID {
		private static final long serialVersionUID = 7778880230670929915L;

		public OTPOIDBase(final String id) {
			super(id);
		}
	}
	/**
	 * Base for every  OTP oid objects
	 */
	@Immutable
	@NoArgsConstructor
	public static abstract class OTPObjectOIDBase
	                     extends OIDBaseMutable<String> // normally this should extend OIDBaseImmutable BUT it MUST have a default no-args constructor to be serializable
			          implements IsOTPOID {
		private static final long serialVersionUID = 8996483673956096810L;
		public OTPObjectOIDBase(final String id) {
			super(id);
		}
		public static String supplyOidAsString() {
			return OIDs.supplyOid();
		}
		public String convertToDatabaseColumn(final OID attribute) {
			return attribute.asString();
		}
		
		/**
		 * Generates an oid
		 * @return the generated oid
		 */
		public static String supplyId() {
			return OTPGUIDDispenser.generateGUID();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@NoArgsConstructor
	public static class OTPTaskOID
    			extends OTPObjectOIDBase
    	     implements IsOTPOID  {
		private static final long serialVersionUID = -5379507184826775226L;

		public OTPTaskOID(final String oidAsString) {
			super(oidAsString);
		}
		public static OTPTaskOID supplyOid() {
	        return new OTPTaskOID(OTPObjectOIDBase.supplyOidAsString());
		}
		public static OTPTaskOID forId(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID valueOf(final String otpOid) {
			return new OTPTaskOID(otpOid);
		}
		public static OTPTaskOID supply() {
			return OTPTaskOID.forId(OTPObjectOIDBase.supplyId());
		}
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@Immutable
	@NoArgsConstructor
	public static class OTPNotifierTaskOID
		extends OTPObjectOIDBase
	implements IsOTPOID  {
	
		private static final long serialVersionUID = -7763641095379769789L;
	
		public OTPNotifierTaskOID(final String oidAsString) {
			super(oidAsString);
		}
		public static OTPNotifierTaskOID supplyOid() {
			return new OTPNotifierTaskOID(OTPObjectOIDBase.supplyOidAsString());
		}
		public static OTPNotifierTaskOID forId(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID valueOf(final String otpOid) {
			return new OTPNotifierTaskOID(otpOid);
		}
		public static OTPNotifierTaskOID supply() {
			return OTPNotifierTaskOID.forId(OTPObjectOIDBase.supplyId());
		}
	}
}
