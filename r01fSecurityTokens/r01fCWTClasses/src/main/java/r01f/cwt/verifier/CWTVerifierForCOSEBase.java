package r01f.cwt.verifier;

import r01f.cwt.model.transformers.CWTPayloadTransformer;
import r01f.cwt.model.transformers.CWTProtectedHeaderTransformer;
import r01f.cwt.model.transformers.CWTUnprotectedHeaderTransformer;

public abstract class CWTVerifierForCOSEBase
	       implements CWTVerifierForCOSE {
/////////////////////////////////////////////////////////////////////////////////////////
//	FIELDS
/////////////////////////////////////////////////////////////////////////////////////////
	CWTPayloadTransformer  _payloadTransformer;
	CWTProtectedHeaderTransformer _protectedHeaderTransformer;
	CWTUnprotectedHeaderTransformer _unprotectedHeaderTransformer;
/////////////////////////////////////////////////////////////////////////////////////////
//	CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////	
	public CWTVerifierForCOSEBase( ) {
		_payloadTransformer  = new CWTPayloadTransformer();
		_protectedHeaderTransformer  = new CWTProtectedHeaderTransformer();
		_unprotectedHeaderTransformer  = new CWTUnprotectedHeaderTransformer();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	METHODS
/////////////////////////////////////////////////////////////////////////////////////////	
	@Override @SuppressWarnings("unchecked")
	public <S extends CWTVerifierForCOSE> S as(final Class<S> type) {
		return (S)this;
	}
}
