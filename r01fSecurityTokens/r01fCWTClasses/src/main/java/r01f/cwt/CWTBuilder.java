package r01f.cwt;

import java.io.Serializable;
import java.security.PrivateKey;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

import com.authlete.cwt.CWTClaimsSet;
import com.google.common.collect.Lists;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.cwt.model.CWTCOSE;
import r01f.cwt.model.CWTCOSESecurityStrategy;
import r01f.cwt.model.CWTCOSESecurityStrategyBySignature1;
import r01f.cwt.model.CWTCOSESecurityStrategyBySignature1.CWTCOSESecurityStrategyBySignature1InputData;
import r01f.cwt.model.CWTCustomClaim;
import r01f.cwt.model.CWTPayload;
import r01f.cwt.model.CWTProtectedHeader;
import r01f.cwt.model.CWTUnprotectedHeader;
import r01f.cwt.model.transformers.CWTPayloadTransformer;
import r01f.guids.CommonOIDs.AppCode;
import r01f.guids.CommonOIDs.AppComponent;
import r01f.patterns.IsBuilder;

/**
 * CBOR Web Token (CWT) is a compact means of representing claims to be  transferred between two parties.
 * The claims in a CWT are encoded in the Concise Binary Object Representation (CBOR),
 * and CBOR Object Signing and Encryption (COSE) is used for added application-layer security protection

 * The IETF Standard : http://tools.ietf.org/html/rfc7519
 *
 *  CWT Web Token is just a byte token :
 *
 *    f.e a601654b616c6c65041a6669ecd5061a66425fd518636576616c75656239386576616c75656b637573746f6d436c61696d694f6c61626172726961
 *
 * Use this class to wrap it. See alsep : https://cwt.me/
 *
 * ---> To Build a valid CWT:
 * 	          CWT myJWT = CWTBuilder.createJWToken()
 *										 .forIssuer(AppCode.forId("z99"))
 *										 .toAudience("p12.audience")
 *										 .withExpirationSeconds(1)
 *										 .noId()
 *										 .usingCustomClaims(otherFieldsAsClaims)
 *										 .withKey(keyBytes) <==  PKI or HMAC Key.
 *									.build();
 * ----> To Verify a valid CWT .
 *			CWTVerifier verifier = CWTVerifierBuilder.createVerifier()
 *	                                                   .usingKey(keyBytes)
 *	                                                   .checkingExpiration()
 *	                                                   .forAudience("theAudienceForWhoThisTokenWasIssued")
 *	                                                   .buid();
 *	        verifier.verify(myJWT);
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class CWTBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public static CWT cwtFrom(final byte[] signedCWT) {
		return CWT.forValue(signedCWT);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static CWTBuilderIssuerStep createCWToken() {
		CWTInitWrapperData initData = new CWTInitWrapperData();
		initData.getPayloadData().setIssuedAt(Instant.now());
		return new CWTBuilder() { /* nothing */ }
					.new CWTBuilderIssuerStep(initData);
	}
	public static CWTBuilderIssuerStep createCWToken(final Instant instant) {
		CWTInitWrapperData initData = new CWTInitWrapperData();
		initData.getPayloadData().setIssuedAt(instant);
		return new CWTBuilder() { /* nothing */ }
					.new CWTBuilderIssuerStep(initData);
	}
	public static CWTBuilderIssuerStep createCWTokenWithNoIat() { //..tersting purposes
		CWTInitWrapperData initData = new CWTInitWrapperData();
		return new CWTBuilder() { /* nothing */ }
					.new CWTBuilderIssuerStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderIssuerStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderAudienceStep forIssuer(final AppCode appCode) {
			_initData.getPayloadData().setIssuer(appCode.asString());
			return new CWTBuilderAudienceStep(_initData);
		}
		public CWTBuilderAudienceStep forIssuer(final String issuerAsString) {
			_initData.getPayloadData().setIssuer(issuerAsString);
			return new CWTBuilderAudienceStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderAudienceStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderExpirationStep toAudience(final AppComponent...  audience) {
			_initData.getPayloadData().setAudience(Lists.newArrayList(audience)
														.stream()
														.map(AppComponent::asString)
														.collect(Collectors.toList()));
			return new CWTBuilderExpirationStep(_initData);
		}
		public CWTBuilderExpirationStep toAudience(final String...  audience) {
			_initData.getPayloadData().setAudience(Lists.<String>newArrayList(audience));
			return new CWTBuilderExpirationStep(_initData);
		}
		public CWTBuilderExpirationStep noAudience() {
			return new CWTBuilderExpirationStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderExpirationStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderIdStep withExpirationDays(final long expirationDays) {
			 Instant now =  Instant.now();
			_initData.getPayloadData().setExpirationTime(now.plus(expirationDays,ChronoUnit.DAYS));
			return new CWTBuilderIdStep(_initData);
		}
		public CWTBuilderIdStep withExpirationSeconds(final long expirationSeconds) {
			 Instant now =  Instant.now();
			_initData.getPayloadData().setExpirationTime(now.plus(expirationSeconds,ChronoUnit.SECONDS));
			return new CWTBuilderIdStep(_initData);
		}
		public CWTBuilderIdStep expirationAt(final Instant exp) {
			_initData.getPayloadData().setExpirationTime(exp);
			return new CWTBuilderIdStep(_initData);
		}
		public CWTBuilderIdStep withoutExpiration() {
			return new CWTBuilderIdStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderIdStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderCustomClaimsStep withCWTId(final String cwtId) {
			_initData.getPayloadData().setCti(cwtId);
			return new CWTBuilderCustomClaimsStep(_initData);
		}
		public CWTBuilderCustomClaimsStep noId() {
			return new CWTBuilderCustomClaimsStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderCustomClaimsStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderCOSEStep withoutCustomClaims() {
			return new CWTBuilderCOSEStep(_initData);
		}
		public CWTBuilderCustomClaimsStep usingCustomClaim(final String key,final String customClaim) {
			 _initData.getPayloadData().setCustomClaim(CWTCustomClaim.create(key, customClaim));
			return this;
		}
		public CWTBuilderCustomClaimsStep usingCustomClaim(final Integer key,final String customClaim) {
			 _initData.getPayloadData().setCustomClaim(CWTCustomClaim.create(key, customClaim));
			return this;
		}
		public CWTBuilderCustomClaimsStep usingCustomClaim(final String customClaimKey, final  Integer customClaim) {
			 _initData.getPayloadData().setCustomClaim(CWTCustomClaim.create(customClaimKey, customClaim));
			return this;
		}
		public CWTBuilderCOSEStep securized() {
			return new CWTBuilderCOSEStep(_initData);

		}
		public CWTUnsignedBuilderBuildStep unsecurized() {
			return new CWTUnsignedBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  COSE STEP
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderCOSEStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderCOSEAlgoStep signedWithPrivateKey(final PrivateKey privateKey) {
			_initData.setSecurityStrategy(new CWTCOSESecurityStrategyBySignature1(privateKey));
			return new CWTBuilderCOSEAlgoStep(_initData);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderCOSEAlgoStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderCOSEKidStep algo(final int algo) {
			_initData.getProtectedHeader().setAlg(algo);
			return new CWTBuilderCOSEKidStep(_initData);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderCOSEKidStep {
		private final CWTInitWrapperData _initData;

		public CWTBuilderBuildStep kidAsProtectedHeader(final String kid) {
			_initData.getProtectedHeader().setKid(kid);
			return new CWTBuilderBuildStep(_initData);
		}
		public CWTBuilderBuildStep kidAsUnprotectedHeader(final String kid) {
			_initData.getUnprotectedHeader().setKid(kid);
			return new CWTBuilderBuildStep(_initData);
		}
		public CWTBuilderBuildStep noKid() {
			return new CWTBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CWT BUILDER BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTBuilderBuildStep {
		private final CWTInitWrapperData _initData;

		////////// CWT from COSE
		public CWT build() {
			CWTCOSE cose = null;
			if ( _initData.getSecurityStrategy() instanceof  CWTCOSESecurityStrategyBySignature1 ) {
				cose = _buildAsCoseSign1();
			} else {
				throw new IllegalArgumentException(" No implemented exception");
			}
			return CWT.forValue(cose.getAsCWTEncodedBytes());
		}
		////////// BUILD as COSE CWT
		private CWTCOSE _buildAsCoseSign1() {

			//[ 1 ] Cast to custom cose
			CWTCOSESecurityStrategyBySignature1  signature1 = _initData.getSecurityStrategy().as(CWTCOSESecurityStrategyBySignature1.class);

			CWTCOSESecurityStrategyBySignature1InputData data = new CWTCOSESecurityStrategyBySignature1InputData();
			data.setPayload(_initData.getPayloadData());
			data.setProtectedHeader(_initData.getProtectedHeader());
			data.setUnprotectedHeader(_initData.getUnprotectedHeader());
			CWTCOSE cose = signature1.apply(data);

			return cose;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// CWT Unsigned Builder (
//	[] Unsigned or unsecured CWT , that is with no COSE (Signature/Encryption/MAc)
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class CWTUnsignedBuilderBuildStep {

		private final CWTInitWrapperData _initData;

		public CWT build() {
			CWTClaimsSet payload = new CWTPayloadTransformer().fromModelObject(_initData.getPayloadData());
			byte[] unsignedcwt   = payload.encode();
			return CWTBuilder.cwtFrom(unsignedcwt);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * CWTInitWrapperData
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor
	public static class CWTInitWrapperData
	         implements Serializable {

	    private static final long serialVersionUID = -8879489076003313362L;

		@Getter @Setter  private CWTPayload _payloadData = new CWTPayload();
		@Getter @Setter  private CWTProtectedHeader _protectedHeader = new CWTProtectedHeader();
		@Getter @Setter  private CWTUnprotectedHeader _unprotectedHeader =  new CWTUnprotectedHeader();
		@Getter @Setter  private CWTCOSESecurityStrategy _securityStrategy;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	GENERAL PURPOSE EXCEPTION
/////////////////////////////////////////////////////////////////////////////////////////	
	public static class CWTBuilderImplException
			    extends RuntimeException {

		private static final long serialVersionUID = -1934301635865388866L;

		public CWTBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
