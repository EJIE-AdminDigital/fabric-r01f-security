package r01f.model.security.otp.task.validation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.otp.oids.OTPOIDs.OTPTaskOID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.oids.SecurityIDs.Secret;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.Phone;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPValidationRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPValidationRequestBuilderForTaskOidSecretStep using(final OTPTaskOID taskOid) {
		return new OTPValidationRequestBuilder() { /* nothing */ }
					 	.new OTPValidationRequestBuilderForTaskOidSecretStep(taskOid);
	}
	public static OTPValidationRequestBuilderForContactMeanSecretStep<Phone> usingPhone(final OTPTaskOID taskOid,final Phone phone) {
		return OTPValidationRequestBuilder.usingContactMean(taskOid,phone);
	}
	public static OTPValidationRequestBuilderForContactMeanSecretStep<EMail> usingEMail(final OTPTaskOID taskOid,final EMail eMail) {
		return OTPValidationRequestBuilder.usingContactMean(taskOid,eMail);
	}
	public static <C extends IsContactMean> OTPValidationRequestBuilderForContactMeanSecretStep<C> usingContactMean(final OTPTaskOID taskOid,final C contactMean) {
		return new OTPValidationRequestBuilder() { /* nothing */ }
					 	.new OTPValidationRequestBuilderForContactMeanSecretStep<C>(taskOid,contactMean);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderForTaskOidSecretStep {
		private final OTPTaskOID _taskOid;

		public OTPValidationRequestBuilderForTaskOidBuildStep validate(final Secret secret) {
			return new OTPValidationRequestBuilderForTaskOidBuildStep(_taskOid,
																	  secret);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderForTaskOidBuildStep {
		private final OTPTaskOID _taskOid;
		private final Secret _secret;

		public OTPValidationRequestByTaskOID buildFor(final AppCode appCode,final AppComponent appComp) {
			return new OTPValidationRequestByTaskOID(appCode,appComp,
													 _taskOid,_secret);
		}
		public OTPValidationRequestByTaskOID buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderForContactMeanSecretStep<C extends IsContactMean> {
		private final OTPTaskOID _taskOid;
		private final C _key;

		public OTPValidationRequestBuilderForContactMeanBuildStep<C> validate(final Secret secret) {
			return new OTPValidationRequestBuilderForContactMeanBuildStep<C>(_taskOid,
																			 _key,secret);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPValidationRequestBuilderForContactMeanBuildStep<C extends IsContactMean> {
		private final OTPTaskOID _taskOid;
		private final C _key;
		private final Secret _secret;

		public OTPValidationRequestByContactMean<C> buildFor(final AppCode appCode,final AppComponent appComp) {
			return new OTPValidationRequestByContactMean<C>(appCode,appComp,
															_taskOid,
													      	_key,_secret);
		}
		public OTPValidationRequestByContactMean<C> buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
}
