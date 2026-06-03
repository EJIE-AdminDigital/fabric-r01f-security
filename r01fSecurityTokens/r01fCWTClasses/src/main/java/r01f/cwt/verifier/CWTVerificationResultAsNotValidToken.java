package r01f.cwt.verifier;

import java.util.Collection;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import r01f.debug.Debuggable;

@NoArgsConstructor
@AllArgsConstructor
@Accessors(prefix="_")
public class CWTVerificationResultAsNotValidToken
  implements CWTVerificationResult,Debuggable {
/////////////////////////////////////////////////////////////////////////////////////////
//  FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	@Setter @Getter Collection<CWTVerificationResultInvalidCause> _cause;
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public boolean isValidCWT() {
		return false;
	}
	@Override
	public boolean isNotValidCWT() {
		return !isValidCWT();
	}
	@Override
	public CharSequence debugInfo() {
		StringBuffer sb = new StringBuffer();
		for (final CWTVerificationResultInvalidCause c:_cause) {
			sb.append(c.name()); 
		};
		return sb.toString();
	}
	@Override @SuppressWarnings("unchecked")
	public <R extends CWTVerificationResult> R as(final Class<R> toCast) {
		return (R)this;
	}
}
