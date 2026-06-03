package r01f.cwt.model.transformers;

import java.util.List;

import com.authlete.cbor.CBORInteger;
import com.authlete.cbor.CBORPair;
import com.authlete.cwt.CWTClaimsSet;
import com.authlete.cwt.CWTClaimsSetBuilder;

import r01f.cwt.model.CWTCustomClaim;
import r01f.cwt.model.CWTPayload;
import r01f.cwt.model.CWTRegistredClaims;
import r01f.util.types.collections.CollectionUtils;
import r01f.util.types.collections.Lists;
import r01f.util.types.datetime.DateTimeUtils;

public class CWTPayloadTransformer
  implements CWTModelObjectTransformer<CWTPayload,CWTClaimsSet> {
/////////////////////////////////////////////////////////////////////////////////////////
//	FROM MODEL OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  CWTClaimsSet fromModelObject(final CWTPayload payloadData) {
		CWTClaimsSetBuilder builder = new CWTClaimsSetBuilder();

		if (payloadData.getIssuer() != null) builder.iss(payloadData.getIssuer());
		if (payloadData.getIssuedAt() != null) builder.iat(DateTimeUtils.dateFromInstant(payloadData.getIssuedAt()));
		if (payloadData.getSubject() != null) builder.sub(payloadData.getSubject());
		if (payloadData.getAudience() != null) {
			if (payloadData.getAudience().size() > 1) throw  new IllegalArgumentException(" Not supported more than one audience");
			builder.aud(CollectionUtils.pickOneAndOnlyElement(payloadData.getAudience()));
		}
		if (payloadData.getExpirationTime() != null) builder.exp(DateTimeUtils.dateFromInstant(payloadData.getExpirationTime()));
		if (payloadData.getNotBefore()!= null) builder.nbf(DateTimeUtils.dateFromInstant(payloadData.getNotBefore()));
		if (payloadData.getCti()!= null) builder.cti(payloadData.getCti());
		if (CollectionUtils.hasData(payloadData.getCustomClaims())) {
			payloadData.getCustomClaims()
						.forEach(customClaim -> {
									builder.put(customClaim.getKey().asRawValue(),
												customClaim.getValue().asRawValue());
								   /* if ( customClaim.getClaimKey() instanceof Integer ) {
									   int key = (Integer) customClaim.getClaimKey();
									   builder.put(key,(customClaim.getValue()));
								   } else if (  customClaim.getClaimKey() instanceof String) {
									   String key = (String) customClaim.getClaimKey();
									   builder.put(key,(customClaim.getValue()));
								   } else throw  new IllegalArgumentException("Unknown type of key :" + customClaim.getClaimKey().getClass());*/

								});
		}
		return builder.build();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PAYLOAD MODEL OBJECT
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public  CWTPayload toModelObject(final CWTClaimsSet claimSet) {
		CWTPayload payload = new CWTPayload();
		if (claimSet.getAud() != null) payload.setAudience(Lists.newArrayList(claimSet.getAud()));
		if (claimSet.getExp() != null) payload.setExpirationTime(claimSet.getExp().toInstant());
		if (claimSet.getIat() != null) payload.setIssuedAt(claimSet.getIat().toInstant());
		if (claimSet.getIss() != null) payload.setIssuer(claimSet.getIss());
		if (claimSet.getCti() != null) payload.setCti(new String (claimSet.getCti()));
		if (claimSet.getNbf() != null) payload.setNotBefore(claimSet.getNbf().toInstant());
		if (claimSet.getSub() != null) payload.setSubject(claimSet.getSub());

		//... the custom claims
		List<? extends CBORPair> customClaims = _filterRegistredCaims(claimSet);
		if (CollectionUtils.hasData(customClaims)) {
			customClaims.forEach(pair -> {
									CWTCustomClaim claim = CWTCustomClaimTransformer.parseClaim(pair);
									payload.setCustomClaim(claim);
								 });
		}
		return payload;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	private static  List<? extends CBORPair> _filterRegistredCaims(final CWTClaimsSet claimSet) {
		return claimSet.getPairs()
				 	   .stream()
				 	   .filter(p -> !(p.getKey() instanceof CBORInteger)
				 				  ||(! CWTRegistredClaims.exists(((CBORInteger)p.getKey()).getValue().intValue())))
				 	   .toList();
	 }
}
