package r01f.cwt.model;

import r01f.cwt.model.transformers.CWTPayloadTransformer;
import r01f.cwt.model.transformers.CWTProtectedHeaderTransformer;
import r01f.cwt.model.transformers.CWTUnprotectedHeaderTransformer;

public abstract class CWTCOSESecurityStrategyBase
  		   implements CWTCOSESecurityStrategy {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	CWTPayloadTransformer  _payloadTransformer;
	CWTProtectedHeaderTransformer _protectedHeaderTransformer;
	CWTUnprotectedHeaderTransformer  _unprotectedHeaderTransformer;
//////////////////////////////////////////////////////////////////////////////////////
// 	CONSTRUCTOR
//////////////////////////////////////////////////////////////////////////////////////
	public CWTCOSESecurityStrategyBase( ) {
		_payloadTransformer  = new CWTPayloadTransformer();
		_protectedHeaderTransformer  = new CWTProtectedHeaderTransformer();
		_unprotectedHeaderTransformer  = new CWTUnprotectedHeaderTransformer();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <S extends CWTCOSESecurityStrategy> S as(final Class<S> type) {
		return (S)this;
	}
}
