package r01f.cwt.verifier;

public interface CWTVerificationResult {

	boolean isValidCWT();

	boolean isNotValidCWT();

	<R extends CWTVerificationResult> R as (final Class<R> toCast);
}
