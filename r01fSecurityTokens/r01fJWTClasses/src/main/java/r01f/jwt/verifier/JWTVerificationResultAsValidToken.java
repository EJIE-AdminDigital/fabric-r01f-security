package r01f.jwt.verifier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.jwt.JWTDecoded.Payload;

@NoArgsConstructor
@Accessors(prefix="_")
public class JWTVerificationResultAsValidToken
  implements JWTVerificationResult {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Setter @Getter Payload _payload = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override
	public boolean isValidJWT() {
		return true;
	}
	@Override
	public boolean isNotValidJWT() {
		return !this.isValidJWT();
	}
	@Override @SuppressWarnings("unchecked")
	public <R extends JWTVerificationResult> R as(final Class<R> toCast) {
		return (R)this;
	}
}
