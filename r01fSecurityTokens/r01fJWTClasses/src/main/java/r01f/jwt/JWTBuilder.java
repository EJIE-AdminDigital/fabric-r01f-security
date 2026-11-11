package r01f.jwt;

import java.io.Serializable;
import java.security.PrivateKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;
import com.nimbusds.jose.CompressionAlgorithm;
import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.PlainHeader;
import com.nimbusds.jose.PlainObject;
import com.nimbusds.jose.crypto.ECDSASigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.RSASSASigner;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.ByteUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.jwt.JWTDecoded.CustomClaim;
import r01f.jwt.JWTDecoded.Payload;
import r01f.oids.OID;
import r01f.oids.CommonOIDs.AppCode;
import r01f.oids.CommonOIDs.AppComponent;
import r01f.patterns.IsBuilder;
import r01f.types.StringBase64;
import r01f.util.types.Strings;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.datetime.DateTimeUtils;

/**
 * JWT TOKEN BUILDER.
 * The IETF Standard : http://tools.ietf.org/html/rfc7519
 * See: https://developer.atlassian.com/static/connect/docs/latest/concepts/understanding-jwt.html
 * See http://connect2id.com/products/nimbus-jose-jwt/examples/jwt-with-hmac
 *
 * In this use are specially relevant this claims:
 *		- ISSUER:  : The APP CLient Code
 *		- AUDIENCE : in this case the service invoked
 *		- EXPIRATION  :   When jwt expirates
 *
 * Use  web https://jwt.io/  to parse public claims of a JWT string.
 *
 * To use this BUILDER:
 * <pre class='brush:java'>
 *		JWT jwt = JWTBuilder.createJWToken()
 *									.forIssuer(AppCode.forId("z99"))
 *									 .toAudience("p12.audience")
 *									 .withExpirationSeconds(1)
 *									 .noId()
 *									 .usingCustomClaims(otherFieldsAsClaims)
 *									 .withKey(keyBytes) <==  PKI or HMAC Key.
 *								.build();
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class JWTBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static JWTBuilderIssuerStep createJWToken() {
		JWTInitWrapperData initData = new JWTInitWrapperData();
		initData.getPayloadData().setIssuedAt(Instant.now());
		return new JWTBuilder() { /* nothing */ }
					.new JWTBuilderIssuerStep(initData);
	}

	public static JWTBuilderIssuerStep createJWToken(final long iatTst) {
		JWTInitWrapperData initData = new JWTInitWrapperData();
		initData.getPayloadData().setIssuedAt(Instant.ofEpochMilli(iatTst));
		return new JWTBuilder() { /* nothing */ }
					.new JWTBuilderIssuerStep(initData);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderIssuerStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderAudienceStep forIssuer(final AppCode appCode) {		
			_initData.getPayloadData().setIssuer(appCode.asString());
			return new JWTBuilderAudienceStep(_initData);
		}

		public JWTBuilderAudienceStep forIssuer(final OID oid) {
			_initData.getPayloadData().setIssuer(oid.asString());
			return new JWTBuilderAudienceStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderAudienceStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderExpirationStep toAudience(final AppComponent...  audience) {
			_initData.getPayloadData().setAudience(Lists.newArrayList(audience)
														.stream()
														.map(AppComponent::asString)
														.collect(Collectors.toList()));
			return new JWTBuilderExpirationStep(_initData);
		}
		public JWTBuilderExpirationStep toAudience(final String...  audience) {
			_initData.getPayloadData().setAudience(Lists.<String>newArrayList(audience));
			return new JWTBuilderExpirationStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderExpirationStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderIdStep withExpirationDays(final long expirationDays) {
			_initData.getPayloadData().setExpirationTime(Instant.now().plus(Duration.ofDays(expirationDays)));
			return new JWTBuilderIdStep(_initData);
		}

		public JWTBuilderIdStep withExpirationSeconds(final long expirationSeconds) {
			_initData.getPayloadData().setExpirationTime(Instant.now().plus(Duration.ofSeconds(expirationSeconds)));
			return new JWTBuilderIdStep(_initData);
		}
		public JWTBuilderIdStep noExpiration() {
			return new JWTBuilderIdStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderIdStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderCustomClaimsStep withJWTId(final String jwtId) {
			_initData.getPayloadData().setJti(jwtId);
			return new JWTBuilderCustomClaimsStep(_initData);
		}
		public JWTBuilderCustomClaimsStep noId() {
			return new JWTBuilderCustomClaimsStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderCustomClaimsStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderKeyStep withoutCustomClaims() {
			return new JWTBuilderKeyStep(_initData);
		}
		public JWTBuilderCustomClaimsStep usingCustomClaim(final String name,final String customClaim) {
			if (Strings.isNullOrEmpty(customClaim)) return this;

			if (CollectionUtils.isNullOrEmpty(_initData.getPayloadData().getCustomClaims())) {
				_initData.getPayloadData().setCustomClaim(name,customClaim);
			} else {
				  Collection<CustomClaim> claims =  Lists.newArrayList(_initData.getPayloadData().getCustomClaims());
				  CustomClaim cc = new CustomClaim();
				  cc.setName(name);
				  cc.setValue(customClaim);
				  claims.add(cc);
				  _initData.getPayloadData().setCustomClaims(claims);
			}
			return this;
		}
		public JWTBuilderCustomClaimsStep usingCustomClaims(final Map<String,String> customClaimsAsStringValueMap) {
			if (CollectionUtils.isNullOrEmpty(customClaimsAsStringValueMap)) {
				return this;
			}
			if (CollectionUtils.isNullOrEmpty(_initData.getPayloadData().getCustomClaims())) {
				for (Map.Entry<String, String> entry : customClaimsAsStringValueMap.entrySet()) {
				  String claimName = entry.getKey();
				  String claimValue = entry.getValue();
				  if (Strings.isNullOrEmpty(claimValue)) continue;

					_initData.getPayloadData()
							 .setCustomClaim(claimName,claimValue);
				}
			} else {
			  Collection<CustomClaim> claims =  Lists.newArrayList(_initData.getPayloadData().getCustomClaims());
			  for (Map.Entry<String, String> entry : customClaimsAsStringValueMap.entrySet()) {
				  String claimName = entry.getKey();
				  String claimValue = entry.getValue();
				  if (Strings.isNullOrEmpty(claimValue)) continue;

				  CustomClaim cc = new CustomClaim();
				  cc.setName(claimName);
				  cc.setValue(claimValue);
				  claims.add(cc);
			   }
			  _initData.getPayloadData().setCustomClaims(claims);

			}
			return this;
		}
		public JWTBuilderBuildStep withKey(final byte[] key) {
			return new JWTBuilderKeyStep(_initData)
									.withKey(key);
		}
		public JWTBuilderBuildStep withPrivateKey(final PrivateKey privateKey) {
			return new JWTBuilderKeyStep(_initData)
					        .withPrivateKey(privateKey);
		}
		public JWTUnsignedBuilderBuildStep unsigned() {
			return new JWTBuilderKeyStep(_initData)
					        .unsigned();
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderKeyStep {
		private final JWTInitWrapperData _initData;

		public JWTBuilderBuildStep  withKey(final byte[] key) {
			_initData.setKey(key);
			return new JWTBuilderBuildStep(_initData);
		}
		public JWTBuilderBuildStep withPrivateKey(final PrivateKey privateKey) {
			_initData.setPrivateKey(privateKey);
			return new JWTBuilderBuildStep(_initData);
		}
		public JWTUnsignedBuilderBuildStep unsigned() {		;
			return new JWTUnsignedBuilderBuildStep(_initData);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static JWT jwtFrom(final SignedJWT signedJWT) {
		String serialized = signedJWT.serialize();
		return JWT.forValue(serialized);
	}
	public static JWT jwtFrom(final PlainObject unsignedJWT) {
		String serialized = unsignedJWT.serialize();
		return JWT.forValue(serialized);
	}
	public static JWT jwtFrom(final JWSObject jws) {
		String serialized = jws.serialize();
		return JWT.forValue(serialized);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// JWT BUILDER BUILD
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTBuilderBuildStep {
		private final JWTInitWrapperData _initData;

		////////// JWT
		public JWT buildWithOptimalAlgorithm() {
			return _build(true,null);
		}

		public JWT buildCompressed() {
			return _build(true,null,true);
		}
		public JWT buildCompressed(JWSAlgorithm alg) {
			return _build(false,alg,true);
		}
		public JWT build() {
			return _build(false, null);
		}
		public JWT build(final JWSAlgorithm defaultAlg) {
			return _build(false,defaultAlg);
		}
		private JWT _build(final boolean usingOptimal,final JWSAlgorithm defaultAlg) {
			SignedJWT signedJWT = _buildSignedJWT(usingOptimal,defaultAlg);
			return JWTBuilder.jwtFrom(signedJWT);
		}

		private JWT _build(final boolean usingOptimal,final JWSAlgorithm defaultAlg, final boolean compressed) {
			JWSObject signedJWT = _buildSignedJWS(usingOptimal,defaultAlg,CompressionAlgorithm.DEF);
			return JWTBuilder.jwtFrom(signedJWT);
		}


		////////// SignedJWT
		public SignedJWT buildSignedJWTWithOptimalAlgorithm() {
			return _buildSignedJWT(true,null);
		}
		public SignedJWT buildSignedJWT() {
			return _buildSignedJWT(false, null);
		}
		public SignedJWT buildSignedJWT(final JWSAlgorithm defaultAlg) {
			return _buildSignedJWT(false,defaultAlg);
		}
		@SuppressWarnings("null")
		private SignedJWT _buildSignedJWT(final boolean usingOptimal,final JWSAlgorithm defaultAlg) {
			SignedJWT signedJWT = null;
			JWSSigner signer = null;
			try {
				JWTClaimsSet claimsSet = _initData.payloadAsClaimSet();
				///////////////// HMAC BASED
				if (_initData.hmacBased()) {
					if (usingOptimal) {
						JWSAlgorithm jwAlgorithm = _determineOptimalAlgorithm(_initData.getKey());
						signedJWT = new SignedJWT(_buildJWSHeader(jwAlgorithm,JWSAlgorithm.HS256,false), claimsSet);
					} else {
						signedJWT = new SignedJWT(_buildJWSHeader(defaultAlg,JWSAlgorithm.HS256,false), claimsSet);
					}
					signer =  new MACSigner(_initData.getKey());
					//////////// PRIVATE/PUBLIC KEY BASED
				} else if (_initData.pkiBased()  ) {
					if (_initData.rsaBased()) {
						signedJWT = new SignedJWT(_buildJWSHeader(defaultAlg,JWSAlgorithm.RS256,false), claimsSet);
					   signer = new RSASSASigner( _initData.getPrivateKey());
				   } else if (_initData.ecBased()) {
					   		signedJWT = new SignedJWT(_buildJWSHeader(defaultAlg,JWSAlgorithm.ES256,false), claimsSet);
							signer = new ECDSASigner( (ECPrivateKey) _initData.getPrivateKey());
				   } else {
					   	throw new JWTBuilderImplException("Unknow type of private key :" +_initData.getPrivateKey().getClass());
				   }
				}
				signedJWT.sign(signer);
				return signedJWT;
			} catch (final JOSEException e) {
				e.printStackTrace();
				throw new JWTBuilderImplException(e.getLocalizedMessage());
			}
		}

		@SuppressWarnings("null")
		private JWSObject _buildSignedJWS(final boolean usingOptimal,final JWSAlgorithm defaultAlg, final CompressionAlgorithm alg) {
			JWSObject jwsObject = null;
			JWSSigner signer = null;
			try {
				String claimsetAsString = _initData.payloadAsClaimSetAsString();
				boolean compressed = ( alg != null ) && alg.equals(CompressionAlgorithm.DEF);
				com.nimbusds.jose.Payload  payload  = null;
				if (compressed) {
					 StringBase64 base64Deflated = StringBase64.deflateAndEncodeURLSafe(claimsetAsString.getBytes());
					 payload = new com.nimbusds.jose.Payload(Base64URL.from(base64Deflated.asString()));
				} else {
					 StringBase64 base64 = StringBase64.encodeURLSafe(claimsetAsString);
					 payload = new com.nimbusds.jose.Payload(Base64URL.from(base64.asString()));
				}
				///////////////// HMAC BASED
				if (_initData.hmacBased() ){
					if (usingOptimal) {
						JWSAlgorithm jwAlgorithm = _determineOptimalAlgorithm(_initData.getKey());
						jwsObject = new JWSObject(_buildJWSHeader(jwAlgorithm,JWSAlgorithm.HS256,compressed), payload);
					} else {
						jwsObject = new JWSObject(_buildJWSHeader(defaultAlg,JWSAlgorithm.HS256,compressed), payload);
					}
					signer =  new MACSigner(_initData.getKey());
					//////////// PRIVATE/PUBLIC KEY BASED
				} else if (_initData.pkiBased() ) {
							if (_initData.rsaBased()) {
								 jwsObject = new JWSObject(_buildJWSHeader(defaultAlg,JWSAlgorithm.RS256,compressed), payload);
								 signer = new RSASSASigner( _initData.getPrivateKey());
						   } else if (_initData.ecBased()) {
							     jwsObject = new JWSObject(_buildJWSHeader(defaultAlg,JWSAlgorithm.ES256,compressed), payload);
								 signer = new ECDSASigner( (ECPrivateKey) _initData.getPrivateKey());
				   } else {
					   	throw new JWTBuilderImplException("Unknow type of private key :" +_initData.getPrivateKey().getClass());
				   }
				}
				jwsObject.sign(signer);
				return jwsObject;
			} catch (final JOSEException e) {
				e.printStackTrace();
				throw new JWTBuilderImplException(e.getLocalizedMessage());
			}
		}

		private JWSHeader _buildJWSHeader(final JWSAlgorithm alg, final JWSAlgorithm defaultIfNull, boolean useCompresion) {
			JWSHeader header = null;
			if ( useCompresion) {
				header  = new JWSHeader.Builder( alg != null ?  alg :defaultIfNull )
				                		.customParam("zip", CompressionAlgorithm.DEF.getName())
				                	  .build();
			} else {
				header  = new JWSHeader( alg != null ?  alg :defaultIfNull );
			}
			return header;
		}

		private JWSAlgorithm _determineOptimalAlgorithm(final byte[] secret) throws KeyLengthException {
			JWSAlgorithm result = null;

			Set<JWSAlgorithm> algorithms = MACSigner.getCompatibleAlgorithms(ByteUtils.bitLength(secret));
			if (algorithms.contains(JWSAlgorithm.HS512)) {
				result = JWSAlgorithm.HS512;
			}
			if (result == null && algorithms.contains(JWSAlgorithm.HS384)) {
				result = JWSAlgorithm.HS384;
			}
			if (result == null && algorithms.contains(JWSAlgorithm.HS256)) {
				result = JWSAlgorithm.HS256;
			}
			if (result == null) {
				throw new KeyLengthException("Secret key is too short for any JWS HMAC algorithm.");
			}
			return result;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// JWT Unsigned Builder (
//	Unsigned JWT, According to RFC 7519 ( https://www.rfc-editor.org/rfc/rfc7519#section-6 )
//		To support use cases in which the JWT content is secured by a means other than a signature and/or encryption
//		contained within the JWT (such as a signature on a data structure containing the JWT),
//		JWTs MAY also be created without a signature or encryption.
//		An Unsecured JWT is a JWS using the "alg" Header Parameter value "none" and with the empty string for its JWS Signature value,
//		as defined in the JWA specification [JWA]; it is an Unsecured JWS with the JWT Claims Set as its JWS Payload.
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class JWTUnsignedBuilderBuildStep {
		private final JWTInitWrapperData _initData;

		public JWT build() {
			PlainObject unsignedJwt = new PlainObject(_initData.payloadAsClaimSet().toPayload());
			return JWTBuilder.jwtFrom(unsignedJwt);
		}

		public JWT buildCompressed() {
			PlainObject unsignedJwt = new PlainObject(new PlainHeader.Builder()
					                                                   .customParam("zip", CompressionAlgorithm.DEF.getName())
					                                                 .build(),
					                                  new com.nimbusds.jose.Payload(Base64URL.from(StringBase64.deflateAndEncodeURLSafe(_initData.payloadAsClaimSetAsString().getBytes()).asString())));

			return JWTBuilder.jwtFrom(unsignedJwt);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	INNER WRAPPER HELPER CLASSES
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * JWTInitWrapperData
	 */
	@Accessors(prefix="_")
	@NoArgsConstructor
	public static class JWTInitWrapperData
	         implements Serializable {
		private static final long serialVersionUID = 5787425585729960483L;

		@Getter @Setter  private Payload _payloadData = new Payload();
		@Getter @Setter  private byte[]  _key ;
		@Getter @Setter  private PrivateKey _privateKey;

		public boolean  hmacBased() {
			return _key != null;
		}
		public boolean  pkiBased() {
			return _privateKey != null;
		}
		public boolean  rsaBased() {
			return _privateKey instanceof RSAPrivateKey;
		}
		public boolean  ecBased() {
			return _privateKey instanceof ECPrivateKey;
		}

		public JWTClaimsSet payloadAsClaimSet() {
			JWTClaimsSet claimsSet = null;
			if (CollectionUtils.hasData(_payloadData.getCustomClaims())) {
				claimsSet = new JWTClaimsSet.Builder(_getCustomClaims(_payloadData.getCustomClaims()))
												.subject(_payloadData.getSubject())
												.audience(_payloadData.getAudience())
												.expirationTime(DateTimeUtils.dateFromInstant(_payloadData.getExpirationTime()))
												.issuer(_payloadData.getIssuer())
												.issueTime(DateTimeUtils.dateFromInstant(_payloadData.getIssuedAt()))
												.jwtID(_payloadData.getJti())
											.build();
			} else {
				claimsSet = new JWTClaimsSet.Builder()
												.subject(_payloadData.getSubject())
												.audience(_payloadData.getAudience())
												.expirationTime(DateTimeUtils.dateFromInstant(_payloadData.getExpirationTime()))
												.issuer(_payloadData.getIssuer())
												.issueTime(DateTimeUtils.dateFromInstant(_payloadData.getIssuedAt()))
												.jwtID(_payloadData.getJti())
											.build();
			}
			return claimsSet;
		}


		public String payloadAsClaimSetAsString() {
			return payloadAsClaimSet().toString();
		}

		@SuppressWarnings("static-method")
		private JWTClaimsSet _getCustomClaims(final Collection<CustomClaim> customClaims) {
			JWTClaimsSet.Builder builder = new JWTClaimsSet.Builder();
			for (CustomClaim cc : customClaims) {
		     	builder = builder.claim(cc.getName(), cc.getValue());
			}
			return builder.build();
		}
	}
	public static class JWTBuilderImplException
			    extends RuntimeException {
		private static final long serialVersionUID = -8313498571229772866L;
		public JWTBuilderImplException(final String msg) {
			super(msg);
		}
	}
}
