package r01f.cwt.verifier;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.cwt.model.CWTPayload;

@NoArgsConstructor
@Accessors(prefix="_")
public class CWTVerificationResultAsValidToken
  implements CWTVerificationResult {

/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Setter @Getter CWTPayload _payload = null;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isValidCWT() {
		return true;
	}
	@Override
	public boolean isNotValidCWT() {
		return !this.isValidCWT();
	}
	@Override @SuppressWarnings("unchecked")
	public <R extends CWTVerificationResult> R as(final Class<R> toCast) {
		return (R)this;
	}
}
