package r01f.model.security.pairing.operations;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="pairingOperationExecResult",typeId="error")
@Accessors(prefix="_")
public class PairingOperationExecError<T>
	 extends COREServiceMethodExecError<T>
  implements IsPairingOperationExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingOperationExecError() {
		// default no-args constructor
	}
	public PairingOperationExecError(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public PairingOperationExecError(final PairingOperation reqOp) {
		this(reqOp.getCOREServiceMethod());
	}
	public PairingOperationExecError(final COREServiceMethod reqOp,
								     final Throwable th) {
		super(reqOp,
			  th);
	}
	public PairingOperationExecError(final PairingOperation reqOp,
									 final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  th);
	}
	public PairingOperationExecError(final COREServiceMethod reqOp,
									 final COREServiceErrorType errType,
								     final Throwable th) {
		super(reqOp,
			  errType,
			  th);
	}
	public PairingOperationExecError(final PairingOperation reqOp,
								         final COREServiceErrorType errType,
								         final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  th);
	}
	public PairingOperationExecError(final COREServiceMethod reqOp,
								     final String msg) {
		super(reqOp,
			  msg);
	}
	public PairingOperationExecError(final PairingOperation reqOp,
								     final String msg) {
		super(reqOp.getCOREServiceMethod(),
			  msg);
	}
	public PairingOperationExecError(final COREServiceMethod reqOp,
								     final COREServiceErrorType errType,
								     final String errMsg) {
		super(reqOp,
			  errType,
			  errMsg);
	}
	public PairingOperationExecError(final PairingOperation reqOp,
						  		     final COREServiceErrorType errType,
								     final String errMsg) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  errMsg);
	}
	public <E extends PairingOperationExecError<?>> PairingOperationExecError(final PairingOperation reqOp,
																	          final E otherError) {
		this(reqOp);
		this.setCalledMethod(otherError.getCalledMethod());
		this.setError(otherError.getError());
		this.setErrorDebug(otherError.getErrorDebug());
		this.setErrorMessage(otherError.getErrorMessage());
		this.setErrorType(otherError.getErrorType());
		this.setOperation(otherError.getOperation());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	  SETTER / GETTER                                                                       
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public PairingOperation getOperation() {
		return PairingOperation.from(_calledMethod);
	}
	public void setOperation(final PairingOperation pairingOp) {
		_calledMethod = pairingOp != null ? pairingOp.getCOREServiceMethod() : null;
	}	
}
