package r01f.jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;
import com.nimbusds.jose.Algorithm;
import com.nimbusds.jose.CompressionAlgorithm;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@NoArgsConstructor
@Accessors(prefix="_")
public class JWTDecoded {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	private @Setter @Getter Header _header;
	private @Setter @Getter Payload _payload;
	private @Setter @Getter Signature _signature;
/////////////////////////////////////////////////////////////////////////////////////////
//	PAYLOAD Structure of a JWT Decoded
/////////////////////////////////////////////////////////////////////////////////////////
	@NoArgsConstructor
	@Accessors(prefix="_")
	public static class Payload {
		// Payload defined registered claims in standard

		@Getter @Setter private String _issuer;
		@Getter @Setter private String _subject;
		@Getter @Setter private List<String> _audience;
		@Getter @Setter private Instant _expirationTime;
		@Getter @Setter private Instant _notBefore;
		@Getter @Setter private Instant _issuedAt;
		@Getter @Setter private String _jti;
		@Getter @Setter private Collection<CustomClaim> _customClaims;

		public CustomClaim getCustomClaim(final String name) {
			if (_customClaims != null)  {
				return _customClaims.stream()
									.filter(myObj -> myObj.getName().equals(name))
									.findFirst().orElse(null);
			}
			return null;
		}
		public void setCustomClaim(final String name, final String customClaimValue) {
			if (_customClaims == null) {
				_customClaims = Lists.newArrayList();
			}
			CustomClaim cc = new CustomClaim();
			cc.setName(name);
			cc.setValue(customClaimValue);
			_customClaims.add(cc);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	HEADER Structure of a JWT Decoded
/////////////////////////////////////////////////////////////////////////////////////////
	@NoArgsConstructor
	@Accessors(prefix="_")
	public static class Header {
		@Getter @Setter private String  _algorithm;
		@Getter @Setter private String  _type;
		@Getter @Setter private String  _zip;

		public  boolean isCompressed() {
			return _zip != null  && _zip.equalsIgnoreCase(CompressionAlgorithm.DEF.getName());
		}
		public  boolean isSecurized() {
			return _algorithm != null  &&  !_algorithm.equalsIgnoreCase(Algorithm.NONE.getName());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	CLAIM
/////////////////////////////////////////////////////////////////////////////////////////
	@NoArgsConstructor
	@Accessors(prefix="_")
	public static class CustomClaim {
		@Getter @Setter private String _name;
		@Getter @Setter private String _value;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SIGNATURE
/////////////////////////////////////////////////////////////////////////////////////////
	@NoArgsConstructor
	@Accessors(prefix="_")
	public static class Signature {
		@Getter @Setter private String _value;
	}
}
