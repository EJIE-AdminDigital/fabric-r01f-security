package r01f.cwt.verifier;

import java.io.Serializable;
import java.security.PublicKey;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.patterns.IsBuilder;

/**
 * CWT TOKEN VERIFIER .
 * Usage:
 * <pre class='brush:java'>
 *				CWTVerifier verifier = JWTVerifierBuilder.createVerifier()
 *	                                                         .usingKey("cortomaltese".getBytes())
 *	                                                         .checkingExpiration()
 *	                                                         .forAudience("theAudienceForWhoThisTokenWasIssued")
 *                                                        .buid();
 *		        verifier.verify(myJWT);
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class CWTVerifierBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static CWTVerifierBuilderKeyStep createVerifier() {
		CWTVerifierBuilderInitWrapperData initData = new CWTVerifierBuilderInitWrapperData();

		return new CWTVerifierBuilder() { /* nothing */ }
					.new CWTVerifierBuilderKeyStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTVerifierBuilderKeyStep {
		private final CWTVerifierBuilderInitWrapperData _initData;

		public CWTVerifierBuilderExpirationStep  usingKey(final byte[] key) {
			_initData.setKey(key);
			return new CWTVerifierBuilderExpirationStep(_initData);
		}

		public CWTVerifierBuilderExpirationStep usingPublicKey(final PublicKey pk) {
			_initData.setPublicKey(pk);
			return new CWTVerifierBuilderExpirationStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTVerifierBuilderExpirationStep {
		private final CWTVerifierBuilderInitWrapperData _initData;

		public CWTVerifierBuilderAudienceStep checkingExpiration(final boolean checkExpiration) {
			if (checkExpiration) {
				return checkingExpiration();
			} else {
				return ignoreExpiration();
			}
		}
		public CWTVerifierBuilderAudienceStep checkingExpirationAndIssue() {
			_initData.setCheckExpiration(true);
			_initData.setCheckExpirationAndIssue(true);
			return new CWTVerifierBuilderAudienceStep(_initData);
		}
		public CWTVerifierBuilderAudienceStep checkingExpiration() {
			_initData.setCheckExpiration(true);
			_initData.setCheckExpirationAndIssue(false);
			return new CWTVerifierBuilderAudienceStep(_initData);
		}
		public CWTVerifierBuilderAudienceStep ignoreExpiration() {
			_initData.setCheckExpiration(false);
			return new CWTVerifierBuilderAudienceStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTVerifierBuilderAudienceStep {
		private final CWTVerifierBuilderInitWrapperData _initData;
		public CWTVerifierBuilderBuildStep forAudience(final boolean checkAudience,
													   final String audience ) {
			if (checkAudience) {
				return forAudience(audience);
			} else {
				return ignoreAudience();
			}
		}
		public CWTVerifierBuilderBuildStep ignoreAudience() {
			return new CWTVerifierBuilderBuildStep(_initData);
		}
		public CWTVerifierBuilderBuildStep forAudience(final String audience ) {
			_initData.setAudienceToCheck(audience);
			return new CWTVerifierBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTVerifierBuilderBuildStep {
		private final CWTVerifierBuilderInitWrapperData _initData;

		public CWTVerifier build() {
			CWTVerifierForCOSE coseVerifer  = null;
			try {
				if (_initData.getKey() != null) {
					//verifierProvider = new MACVerifier(_initData.getKey());
					throw new UnsupportedOperationException(" Key for MAC set , but not implemented CWTCOSESMac0");
				} else {
					/*if (_initData.getPublicKey() instanceof RSAPublicKey) {
						verifierProvider = new RSASSAVerifier((RSAPublicKey)_initData.getPublicKey());
					} else if (_initData.getPublicKey() instanceof ECPublicKey) {
						verifierProvider = new ECDSAVerifier((ECPublicKey)_initData.getPublicKey());
					} else {
						throw new CWTVerifierBuilderImplException("Invalid Public Key type : "+_initData.getPublicKey().getAlgorithm());
					}*/
					coseVerifer = new CWTVerifierForCOSESign1(_initData.getPublicKey());
				}
				return new CWTVerifierImpl(coseVerifer,
						                   _initData._audienceToCheck,
								           _initData._checkExpiration,
								           _initData._checkExpirationAndIssue);

			} catch (final Throwable e) {
				e.printStackTrace();
				throw new CWTVerifierBuilderImplException(e.getLocalizedMessage());
			}

		}
	}

/////////////////////////////////////////////////////////////////////////////////////////
// INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 *  CWTVerifierBuilderInitWrapperData
	 */
	@Accessors(prefix="_")
	public static class CWTVerifierBuilderInitWrapperData
			 implements Serializable {

	     private static final long serialVersionUID = -7245273964007573069L;

		@Getter @Setter private byte[] _key ;
		@Getter @Setter private PublicKey _publicKey;
		@Getter @Setter boolean _checkExpiration;
		@Getter @Setter boolean _checkExpirationAndIssue;
		@Getter @Setter String _audienceToCheck;

	}
	public static class CWTVerifierBuilderImplException
				extends RuntimeException {

		private static final long serialVersionUID = 4421413297928251907L;

		public CWTVerifierBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
