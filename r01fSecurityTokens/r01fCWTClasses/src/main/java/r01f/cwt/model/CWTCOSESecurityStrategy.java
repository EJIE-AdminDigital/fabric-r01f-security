package r01f.cwt.model;

public interface CWTCOSESecurityStrategy {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public CWTCOSE  apply (final CWTCOSESecurityStrategyInputData inpuData );

	public <S extends CWTCOSESecurityStrategy > S as(final Class<S> type);
	 
	public static interface CWTCOSESecurityStrategyInputData {
		 // a marker interface
	}
}

