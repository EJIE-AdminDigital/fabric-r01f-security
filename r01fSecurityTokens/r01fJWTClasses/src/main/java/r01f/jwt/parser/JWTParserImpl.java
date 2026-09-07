package r01f.jwt.parser;

import java.text.ParseException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.HeaderParameterNames;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jose.PlainObject;
import com.nimbusds.jose.util.Base64URL;
import com.nimbusds.jose.util.JSONObjectUtils;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import r01f.jwt.JWT;
import r01f.jwt.JWTDecoded;
import r01f.jwt.JWTDecoded.CustomClaim;
import r01f.jwt.JWTDecoded.Header;
import r01f.jwt.JWTDecoded.Payload;
import r01f.jwt.JWTDecoded.Signature;
import r01f.types.StringBase64;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.datetime.DateTimeUtils;

public class JWTParserImpl
  implements JWTParser {
/////////////////////////////////////////////////////////////////////////////////////////
// PUBLIC METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public JWTDecoded parse(final JWT jwt) {
		return _doParse(jwt);
	}

	public JWTDecoded parse(final JWT jwt, final boolean isSigned) {
		return _doParse(jwt, isSigned);
	}
/////////////////////////////////////////////////////////////////////////////////////////
// DO PARSE
/////////////////////////////////////////////////////////////////////////////////////////
	protected JWTDecoded _doParse(final JWT jwt) {
		Header header = _parseHeader(jwt);
		if ( header.isCompressed())
			return _doParseCompressed(jwt, header, header.isSecurized());
		return _doParse(jwt,header.isSecurized());
	}

	public JWTDecoded _doParse(final JWT jwt, final boolean isSecurized) {
		JWTDecoded jwtDecoded = new JWTDecoded();
		Payload payload = new Payload();
		Header header = new Header();
		Signature signature = new Signature();
		try {
			final com.nimbusds.jwt.JWT sjwt;
			if (isSecurized) {
				sjwt = SignedJWT.parse(jwt.asString());
			} else {
				sjwt = com.nimbusds.jwt.JWTParser.parse(jwt.asString());
			}
			//.....the payload
			payload = parsePayload(sjwt.getJWTClaimsSet());

			//.....the header
			if (sjwt.getHeader().getAlgorithm().getName() != null) {
				header.setAlgorithm(sjwt.getHeader().getAlgorithm().getName());
			}
			if (sjwt.getHeader().getType() != null) {
				header.setType(sjwt.getHeader().getType().toString());
			}
			//.....the signature
			if (isSecurized) {
				signature.setValue(((SignedJWT)sjwt).getSignature().decodeToString());
			}
			//...set fields
			jwtDecoded.setHeader(header);
			jwtDecoded.setPayload(payload);
			jwtDecoded.setSignature(signature);
		} catch (final ParseException th) {
			throw new JWTParserException(th.getLocalizedMessage());
		}
		return jwtDecoded;
	}

	public JWTDecoded _doParseCompressed(final JWT jwt, final Header header ,final boolean isSecurized) {
		JWTDecoded jwtDecoded = new JWTDecoded();
		Payload payload = new Payload();
		Signature signature = new Signature();
		try {
		    if ( isSecurized ) {
				JWSObject parsed = JWSObject.parse(jwt.toString());
				StringBase64 base64New =  StringBase64.fromBase64EncodedString(parsed.getPayload().toBase64URL().toString());
				byte[] inflated = base64New.decodeAsURLSafeAsBytesAndInflate();

				com.nimbusds.jose.Payload npayload = new com.nimbusds.jose.Payload(Base64URL.encode(inflated));
				//.....the payload
				payload = parsePayload(JWTClaimsSet.parse(npayload.toJSONObject()));

				//.....the signature
				signature.setValue(parsed.getSignature().decodeToString());
		    } else {

				PlainObject parsed = PlainObject.parse(jwt.toString());
				StringBase64 base64New =  StringBase64.fromBase64EncodedString(parsed.getPayload().toBase64URL().toString());
				byte[] inflated = base64New.decodeAsURLSafeAsBytesAndInflate();
				com.nimbusds.jose.Payload npayload = new com.nimbusds.jose.Payload(Base64URL.encode(inflated));
				//.....the payload
				payload = parsePayload(JWTClaimsSet.parse(npayload.toJSONObject()));
			}
			//...set fields
			jwtDecoded.setHeader(header);
			jwtDecoded.setPayload(payload);
			jwtDecoded.setSignature(signature);

		} catch (ParseException th) {
			th.printStackTrace();
			throw new JWTParserException(th.getLocalizedMessage());
		}
		return jwtDecoded;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  PAYLOAD AND HEDAER
/////////////////////////////////////////////////////////////////////////////////////////
	public  Payload parsePayload(final JWTClaimsSet claimSet) {
		Payload payload = new Payload();
		payload.setAudience(claimSet.getAudience());
		payload.setExpirationTime(DateTimeUtils.asInstant(claimSet.getExpirationTime()));
		payload.setIssuedAt(DateTimeUtils.asInstant(claimSet.getIssueTime()));
		payload.setIssuer(claimSet.getIssuer());
		payload.setJti(claimSet.getJWTID());
		payload.setNotBefore(DateTimeUtils.asInstant(claimSet.getNotBeforeTime()));
		payload.setSubject(claimSet.getSubject());

		//... the custom  claims
		Collection<String>  customClaimKeySet = _getCustomClaimKeySet(claimSet);
		if (CollectionUtils.hasData(customClaimKeySet)) {
			Collection<CustomClaim> ccc= customClaimKeySet.stream()
														  .map(key -> {
																		CustomClaim cc = new CustomClaim();
																		cc.setName(key);
																		Object claimValue = claimSet.getClaim(key);
																		cc.setValue(claimValue instanceof String
																						? (String) claimValue
																						: (claimValue != null ? claimValue.toString() : null));
																		return cc;
																})
														  .collect(Collectors.toList());
			payload.setCustomClaims(ccc);
		}
		return payload;
	}


	protected Header _parseHeader( final JWT jwt) {
		final String jwtAsString = jwt.asString();
		final int firstDotPos = jwt.asString().indexOf(".");
		if (firstDotPos == -1)
			throw new JWTParserException("Invalid JWT serialization: Missing dot delimiter(s)");
		Base64URL header = new Base64URL(jwtAsString.substring(0, firstDotPos));
		Map<String, Object> jsonObject;
		try {
			jsonObject = JSONObjectUtils.parse(header.decodeToString());
			Algorithm alg = com.nimbusds.jose.Header.parseAlgorithm(jsonObject);
			String zip = JSONObjectUtils.getString(jsonObject, HeaderParameterNames.COMPRESSION_ALGORITHM);
			String type  = JSONObjectUtils.getString(jsonObject, HeaderParameterNames.TYPE);
			Header jwtHeader = new Header();
			jwtHeader.setAlgorithm(alg.getName());
			jwtHeader.setType(type);
			jwtHeader.setZip(zip);
			return jwtHeader;
		} catch (final ParseException e) {
			throw new JWTParserException("Invalid unsecured/JWS/JWE header: " + e.getMessage());
		}
	 }
/////////////////////////////////////////////////////////////////////////////////////////
//  PRIVATE METHODS
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("static-method")
	private  List<String> _getCustomClaimKeySet(final JWTClaimsSet claimSet) {
		 return claimSet.getClaims()
				 		.keySet()
				 		.stream()
				 		.filter(key -> ! JWTClaimsSet.getRegisteredNames().contains(key))
				 		.collect(Collectors.toList());
	}

/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
		class JWTParserException
	  extends RuntimeException {

		private static final long serialVersionUID = -3343752139233656177L;

		public JWTParserException(final String msg) {
			super(msg);
		}
	}
}
