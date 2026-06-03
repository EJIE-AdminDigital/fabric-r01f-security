package r01f.cwt.verifier;

import r01f.cwt.CWT;

public interface CWTVerifierForCOSE {
	public boolean  verify (final CWT inpuData );

	public <S extends CWTVerifierForCOSE > S as(final Class<S> type);
}

