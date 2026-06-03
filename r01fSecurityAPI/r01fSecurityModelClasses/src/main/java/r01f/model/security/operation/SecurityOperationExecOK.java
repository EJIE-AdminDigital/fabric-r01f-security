package r01f.model.security.operation;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceException;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecOK;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="securityOperationResult",typeId="ok")
@Accessors(prefix="_")
public class SecurityOperationExecOK<T>
	 extends COREServiceMethodExecOK<T> 
  implements SecurityOperationResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationExecOK() {
		// default no-args constructor
	}
	public SecurityOperationExecOK(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public SecurityOperationExecOK(final SecurityOperation reqOp) {
		super(reqOp.getCOREServiceMethod());
	}
	public SecurityOperationExecOK(final COREServiceMethod reqOp,
								   final T instance) {
		super(reqOp,
			  instance);
	}
	public SecurityOperationExecOK(final SecurityOperation reqOp,
								   final T instance) {
		super(reqOp.getCOREServiceMethod(),
			  instance);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperation getRequestedOperation() {
		return SecurityOperation.from(_calledMethod);
	}
	public void setRequestedOperation(final SecurityOperation calledOp) {
		_calledMethod = calledOp != null ? calledOp.getCOREServiceMethod() : null;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public T getOrThrow() throws COREServiceException {
		return _methodExecResult;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationExecOK<T> asSecurityOperationOK() {
		return this;
	}
	@Override
	public SecurityOperationExecError<T> asSecurityOperationError() {
		throw new ClassCastException();
	}
}
