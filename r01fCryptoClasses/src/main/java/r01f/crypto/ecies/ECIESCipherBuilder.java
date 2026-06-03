package r01f.crypto.ecies;

import java.io.IOException;
import java.io.Serializable;
import java.security.PrivateKey;
import java.security.PublicKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.crypto.model.CryptoKeyPair;
import r01f.crypto.utils.CBCMode;
import r01f.crypto.utils.PEMParser;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ECIESCipherBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static ECIESCipherBuilderPaddingStep createCipher() {
		ECIESCipherBuilderInitWrapperData initData = new ECIESCipherBuilderInitWrapperData();

		return new ECIESCipherBuilder() {/* nothing */}
					.new ECIESCipherBuilderPaddingStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class ECIESCipherBuilderPaddingStep {
		private final ECIESCipherBuilderInitWrapperData _initData;

		public ECIESCipherBuilderIvStep withCBCModes(final CBCMode CBCModeForAES,
													 final CBCMode CBCModeForECC) {
			_initData.setCBCModeForAES(CBCModeForAES);
			_initData.setCBCModeForECC(CBCModeForECC);
			return new ECIESCipherBuilderIvStep(_initData);
		}
		public ECIESCipherBuilderIvStep withCBCModeForAES(final CBCMode CBCModeForAES) {
			_initData.setCBCModeForAES(CBCModeForAES);
			return new ECIESCipherBuilderIvStep(_initData);
		}
		public ECIESCipherBuilderIvStep withCBCModeForECC(final CBCMode CBCModeForECC) {
			_initData.setCBCModeForECC(CBCModeForECC);
			return new ECIESCipherBuilderIvStep(_initData);
		}
		public ECIESCipherBuilderIvStep withDefaultCBCModes() {
			_initData.setCBCModeForAES(CBCMode.AES_CBC_PKCS5Padding);
			_initData.setCBCModeForECC(CBCMode.AES_CBC_NoPadding);
			return new ECIESCipherBuilderIvStep(_initData);
		}
		// TODO remove when nobody use they
		@Deprecated
		public ECIESCipherBuilderKeyPairStep withIvLength(final int ivLength) {
			_initData.setIvLength(ivLength);
			return new ECIESCipherBuilderKeyPairStep(_initData);
		}
		@Deprecated
		public ECIESCipherBuilderKeyPairStep withDefaultIvLength() {
			_initData.setIvLength(ECIESCipher.DEFAULT_IV_LENGTH);
			return new ECIESCipherBuilderKeyPairStep(_initData);
		}
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class ECIESCipherBuilderIvStep {
		private final ECIESCipherBuilderInitWrapperData _initData;

		public ECIESCipherBuilderKeyPairStep withIvLength(final int ivLength) {
			_initData.setIvLength(ivLength);
			return new ECIESCipherBuilderKeyPairStep(_initData);
		}

		public ECIESCipherBuilderKeyPairStep withDefaultIvLength() {
			_initData.setIvLength(ECIESCipher.DEFAULT_IV_LENGTH);
			return new ECIESCipherBuilderKeyPairStep(_initData);
		}
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class ECIESCipherBuilderKeyPairStep {
		private final ECIESCipherBuilderInitWrapperData _initData;

		public ECIESCipherBuilderBuildStep usingKeyPair(final CryptoKeyPair keyPair) {
			_initData.setKeyPair(keyPair);
			return new ECIESCipherBuilderBuildStep(_initData);
		}
		
		public ECIESCipherBuilderPrivateKeyStep usingPublicKey(final byte[] publicKeyInPEMFormat) {
			_initData.setPublicKeyPEM(publicKeyInPEMFormat);
			return new ECIESCipherBuilderPrivateKeyStep(_initData);
		}
		
		public ECIESCipherBuilderPrivateKeyStep usingPublicKey(final PublicKey publicKey) {
			_initData.setPublicKey(publicKey);
			return new ECIESCipherBuilderPrivateKeyStep(_initData);
		}
	}
	@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
	public class ECIESCipherBuilderPrivateKeyStep {
		private final ECIESCipherBuilderInitWrapperData _initData;
		public ECIESCipherBuilderBuildStep andPrivateKey(final byte[] privateKeyInPEMFormat) {
			_initData.setPrivateKeyPEM(privateKeyInPEMFormat);
			return new ECIESCipherBuilderBuildStep(_initData);
		}
		
		public ECIESCipherBuilderBuildStep andPrivateKey(final PrivateKey privateKey) {
			_initData.setPrivateKey(privateKey);
			return new ECIESCipherBuilderBuildStep(_initData);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class ECIESCipherBuilderBuildStep {
		private final ECIESCipherBuilderInitWrapperData _initData;

		public ECIESCipher build() throws ECIESCipherBuilderImplException {
			//Build KeyPair
			CryptoKeyPair keyPair = null;
			
			if (_initData.getKeyPair() != null) {
				keyPair = _initData._keyPair;
			} else {
				PublicKey publicKey = null;
				PrivateKey privateKey = null;
				
				//Public Key
				if (_initData.getPublicKeyPEM() != null) {
					try {
						publicKey = PEMParser.publicKeyFromPEM(_initData.getPublicKeyPEM());
					} catch (IOException ioex) {
						throw new ECIESCipherBuilderImplException("Public key is not in a valid PEM format!!");
					}
				} else if (_initData.getPublicKey() != null) {
					publicKey = _initData.getPublicKey();
				} else {
					throw new ECIESCipherBuilderImplException("Public Key must not be null!!");
				}
				
				//Private Key
				if (_initData.getPrivateKeyPEM() != null) {
					try {
						privateKey = PEMParser.privateKeyFromPEM(_initData.getPrivateKeyPEM());
					} catch (IOException ioex) {
						throw new ECIESCipherBuilderImplException("Private key is not in a valid PEM format!!");
					}
				} else if (_initData.getPrivateKey() != null) {
					privateKey = _initData.getPrivateKey();
				} else {
					throw new ECIESCipherBuilderImplException("Private Key must not be null!!");
				}
				
				keyPair = new CryptoKeyPair(publicKey, privateKey);
			}
			
			return new ECIESCipherImpl(_initData.getIvLength(), keyPair);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * JWTInitVerifierWrapperData
	 */
	@Accessors(prefix = "_")
	private static class ECIESCipherBuilderInitWrapperData 
			  implements Serializable {

		private static final long serialVersionUID = 5787425585729960483L;

		@Getter	@Setter private int _ivLength;
		@Getter	@Setter private CryptoKeyPair _keyPair;
		@Getter @Setter private byte[] _publicKeyPEM;
		@Getter @Setter private PublicKey _publicKey;
		@Getter @Setter private byte[] _privateKeyPEM;
		@Getter @Setter private PrivateKey _privateKey;
		@Getter @Setter private CBCMode _CBCModeForAES;
		@Getter @Setter private CBCMode _CBCModeForECC;
		
	}
	public static class ECIESCipherBuilderImplException 
				extends RuntimeException {

		private static final long serialVersionUID = -1402518636879911975L;

		public ECIESCipherBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
