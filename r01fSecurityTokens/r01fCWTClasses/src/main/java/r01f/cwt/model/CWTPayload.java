package r01f.cwt.model;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(prefix="_")
public  class CWTPayload
   implements CWTModelObject {
/////////////////////////////////////////////////////////////////////////////////////////
//	Members ( Payload defined registered claims in standard)
/////////////////////////////////////////////////////////////////////////////////////////	
	@Getter @Setter private String _issuer;
	@Getter @Setter private String _subject;
	@Getter @Setter private List<String> _audience;
	@Getter @Setter private Instant _expirationTime;
	@Getter @Setter private Instant _notBefore;
	@Getter @Setter private Instant _issuedAt;
	@Getter @Setter private String _cti;
	@Getter @Setter private String _dgcV1;

	@Getter @Setter private Collection<CWTCustomClaim> _customClaims;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public CWTCustomClaim getCustomClaim(final CWTCustomClaimKey claimKey) {
		if (_customClaims != null)  {
			return _customClaims.stream()
								.filter(myObj -> myObj.getKey().equals(claimKey))
								.findFirst().orElse(null);
		}
		return null;
	}
	public  void setCustomClaim( final CWTCustomClaim claim) {
		if (_customClaims == null) _customClaims = Lists.newArrayList();
		_customClaims.add(claim);
	}
	public <V> void setCustomClaim(final String claimKey, final V customClaimValue) {
		if (_customClaims == null) _customClaims = Lists.newArrayList();
		_customClaims.add(CWTCustomClaim.create(claimKey, customClaimValue));
	}
	public <V> void setCustomClaim(final Integer claimKey, final V customClaimValue) {
		if (_customClaims == null) _customClaims = Lists.newArrayList();
		_customClaims.add(CWTCustomClaim.create(claimKey, customClaimValue));
	}
}