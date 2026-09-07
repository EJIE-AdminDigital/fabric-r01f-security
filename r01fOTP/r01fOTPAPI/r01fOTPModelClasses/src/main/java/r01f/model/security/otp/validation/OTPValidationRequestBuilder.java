package r01f.model.security.otp.validation;




import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPValidationRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPValidationRequestBuilderSecretStep<OTPTaskOID> using(final OTPTaskOID taskOid) {
		return new OTPValidationRequestBuilder() { /* nothing */ }
					 	.new OTPValidationRequestBuilderSecretStep<OTPTaskOID>(taskOid, taskOid);
	}
	public static OTPValidationRequestBuilderSecretStep<Phone> using(final OTPTaskOID taskOid, final Phone phone) {
		return new OTPValidationRequestBuilder() { /* nothing */ }
					 	.new OTPValidationRequestBuilderSecretStep<Phone>(taskOid, phone);
	}
	public static OTPValidationRequestBuilderSecretStep<EMail> using(final OTPTaskOID taskOid, final EMail eMail) {
		return new OTPValidationRequestBuilder() { /* nothing */ }
					 	.new OTPValidationRequestBuilderSecretStep<EMail>(taskOid, eMail);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderSecretStep<K> {
		private final OTPTaskOID _taskOid;
		private final K _key;

		public OTPValidationRequestBuilderBuildStep<K> validate(final Password secret) {
			return new OTPValidationRequestBuilderBuildStep<K>(_taskOid,_key,secret);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderBuildStep<K> {
		private final OTPTaskOID _taskOid;
		private final K _key;
		private final Password _secret;

		@SuppressWarnings("unchecked")
		public <R extends OTPValidationRequest<K>> R buildFor(final AppCode appCode,final AppComponent appComp) {
			if (_key instanceof OTPTaskOID) {
				return (R)new OTPValidationRequestByTaskOID(appCode,appComp,
													        (OTPTaskOID)_key,_secret);
			} else if (_key instanceof EMail) {
				return (R)new OTPValidationRequestByEMail(appCode,appComp,_taskOid,
													      (EMail)_key,_secret);
			} else if (_key instanceof Phone) {
				return (R)new OTPValidationRequestByPhone(appCode,appComp,_taskOid,
													      (Phone)_key,_secret);
			}
			throw new IllegalArgumentException(_key.getClass() + " is NOT a supported validation key type: " +
											   OTPTaskOID.class + ", " +
											   EMail.class + ", " +
											   Phone.class);
		}
		public <R extends OTPValidationRequest<K>> R buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
}
