package r01f.model.security.otp.operations;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="otpOperationExecResult",typeId="error")
@Accessors(prefix="_")
public class OTPOperationExecError<T>
	 extends COREServiceMethodExecError<T>
  implements OTPOperationExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPOperationExecError() {
		// default no-args constructor
	}
	public OTPOperationExecError(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public OTPOperationExecError(final OTPOperation reqOp) {
		this(reqOp.getCOREServiceMethod());
	}
	public OTPOperationExecError(final COREServiceMethod reqOp,
								 final Throwable th) {
		super(reqOp,
			  th);
	}
	public OTPOperationExecError(final OTPOperation reqOp,
								 final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  th);
	}
	public OTPOperationExecError(final COREServiceMethod reqOp,
								 final COREServiceErrorType errType,
								 final Throwable th) {
		super(reqOp,
			  errType,
			  th);
	}
	public OTPOperationExecError(final OTPOperation reqOp,
								 final COREServiceErrorType errType,
								 final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  th);
	}
	public OTPOperationExecError(final COREServiceMethod reqOp,
								 final String msg) {
		super(reqOp,
			  msg);
	}
	public OTPOperationExecError(final OTPOperation reqOp,
								 final String msg) {
		super(reqOp.getCOREServiceMethod(),
			  msg);
	}
	public OTPOperationExecError(final COREServiceMethod reqOp,
								 final COREServiceErrorType errType,
								 final String errMsg) {
		super(reqOp,
			  errType,
			  errMsg);
	}
	public OTPOperationExecError(final OTPOperation reqOp,
						  		 final COREServiceErrorType errType,
								 final String errMsg) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  errMsg);
	}
	public <E extends OTPOperationExecError<?>> OTPOperationExecError(final OTPOperation reqOp,
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
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public OTPOperation getOperation() {
		return OTPOperation.from(_calledMethod);
	}
	public void setOperation(final OTPOperation calledOp) {
		_calledMethod = calledOp != null ? calledOp.getCOREServiceMethod() : null;
	}	
}
