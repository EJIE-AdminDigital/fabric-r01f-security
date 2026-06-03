package r01f.crypto.aes;

import java.io.Serializable;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.crypto.utils.CBCMode;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class AESCipherBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static AESCipherBuilderCBCModeStep createCipher() {
		AESCipherBuilderInitWrapperData initData = new AESCipherBuilderInitWrapperData();

		return new AESCipherBuilder() {
			/* nothing */}.new AESCipherBuilderCBCModeStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class AESCipherBuilderCBCModeStep {
		private final AESCipherBuilderInitWrapperData _initData;
		
		public AESCipherBuilderKeyStep usingCBCMode(final CBCMode cbcMode) {
			_initData.setCbcMode(cbcMode);
			return new AESCipherBuilderKeyStep(_initData);
		}

		public AESCipherBuilderKeyStep usingDefaultCBCmode() {
			_initData.setCbcMode(AESCipher.DEFAULT_CBC_MODE);
			return new AESCipherBuilderKeyStep(_initData);
		}
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class AESCipherBuilderKeyStep {
		private final AESCipherBuilderInitWrapperData _initData;
		
		public AESCipherBuilderBuildStep andKey(final AESSecretKey key) {
			_initData.setKey(key);
			return new AESCipherBuilderBuildStep(_initData);
		}
	}	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class AESCipherBuilderBuildStep {
		private final AESCipherBuilderInitWrapperData _initData;

		public AESCipher build() throws AESCipherBuilderImplException {
			if (_initData.getKey() == null) {
				throw new AESCipherBuilderImplException("AES Key must not be null!!");
			} 
			return new AESCipherImpl(_initData.getCbcMode(), _initData.getKey());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * JWTInitVerifierWrapperData
	 */
	@Accessors(prefix = "_")
	private static class AESCipherBuilderInitWrapperData implements Serializable {

		private static final long serialVersionUID = 5787425585729960483L;

		@Getter	@Setter
		private CBCMode _cbcMode;

		@Getter	@Setter
		private AESSecretKey _key;
	}
	public static class AESCipherBuilderImplException extends RuntimeException {

		private static final long serialVersionUID = -1402518636879911975L;

		public AESCipherBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
