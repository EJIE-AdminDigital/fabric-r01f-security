package r01f.model.security.otp.generation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class OTPGenerationRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPRequestBuilderLengthStep usingSecretOfType(final OTPSecretType secretType) {
		OTPGenerationRequest otpGenRequest = new OTPGenerationRequest();
		otpGenRequest.setSecretType(secretType);
		return new OTPGenerationRequestBuilder() { /* nothing */ }
					.new OTPRequestBuilderLengthStep(otpGenRequest);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPRequestBuilderLengthStep {
		private final OTPGenerationRequest  _otpGenRequest;

		public OTPRequestBuilderTTLStep withLength(final long length) {
			_otpGenRequest.setSecretLength(length);
			return new OTPRequestBuilderTTLStep(_otpGenRequest);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPRequestBuilderTTLStep {
		private final OTPGenerationRequest  _otpGenRequest;

		public OTPRequestBuilderMaxRetriesStep aliveDuringSeconds(final long ttlSecs) {
			_otpGenRequest.setTTLSeconds(ttlSecs);
			return new OTPRequestBuilderMaxRetriesStep(_otpGenRequest);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPRequestBuilderMaxRetriesStep {
		private final OTPGenerationRequest  _otpGenRequest;

		public OTPRequestBuilderBuildStep allowUnlimitedRetryAttempts() {
			_otpGenRequest.setMaxRetries(-1);
			return new OTPRequestBuilderBuildStep(_otpGenRequest);
		}
		public OTPRequestBuilderBuildStep notAllowMoreRetryAttemptsThan(final int numMaxOfRetries) {
			_otpGenRequest.setMaxRetries(numMaxOfRetries);
			return new OTPRequestBuilderBuildStep(_otpGenRequest);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPRequestBuilderBuildStep {
		private final OTPGenerationRequest  _otpGenRequest;

		public OTPGenerationRequest buildFor(final AppCode appCode,final AppComponent appComp) {
			_otpGenRequest.setAppCode(appCode);
			_otpGenRequest.setAppComponent(appComp);
			return _otpGenRequest;
		}
		public OTPGenerationRequest buildFor(final AppCode appCode) {
			return this.buildFor(appCode,null);
		}
	}
}
