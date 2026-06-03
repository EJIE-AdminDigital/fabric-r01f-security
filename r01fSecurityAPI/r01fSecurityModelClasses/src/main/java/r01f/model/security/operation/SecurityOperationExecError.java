package r01f.model.security.operation;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;
import r01f.model.services.COREServiceException;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.objectstreamer.annotations.MarshallType;
import r01f.util.types.Strings;


@MarshallType(as="securityOperationResult",typeId="error")
@Accessors(prefix="_")
public class SecurityOperationExecError<T>
	 extends COREServiceMethodExecError<T> 
  implements SecurityOperationResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR & BUILDER
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationExecError() {
		// default no-args constructor
	}
	public SecurityOperationExecError(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public SecurityOperationExecError(final SecurityOperation reqOp) {
		this(reqOp.getCOREServiceMethod());
	}
	public SecurityOperationExecError(final COREServiceMethod reqOp,
								  	  final Throwable th) {
		super(reqOp,
			  th);
	}
	public SecurityOperationExecError(final SecurityOperation reqOp,
								  	  final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  th);
	}
	public SecurityOperationExecError(final COREServiceMethod reqOp,
								  	  final COREServiceErrorType errType,
								  	  final Throwable th) {
		super(reqOp,
			  errType,
			  th);
	}
	public SecurityOperationExecError(final SecurityOperation reqOp,
								  	  final COREServiceErrorType errType,
								  	  final Throwable th) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  th);
	}
	public SecurityOperationExecError(final COREServiceMethod reqOp,
								  	  final String msg) {
		super(reqOp,
			  msg);
	}
	public SecurityOperationExecError(final SecurityOperation reqOp,
								  	  final String msg) {
		super(reqOp.getCOREServiceMethod(),
			  msg);
	}
	public SecurityOperationExecError(final COREServiceMethod reqOp,
								  	  final COREServiceErrorType errType,
								  	  final String errMsg) {
		super(reqOp,
			  errType,
			  errMsg);
	}
	public SecurityOperationExecError(final SecurityOperation reqOp,
								  	  final COREServiceErrorType errType,
								  	  final String errMsg) {
		super(reqOp.getCOREServiceMethod(),
			  errType,
			  errMsg);
	}
	public <E extends SecurityOperationExecError<?>> SecurityOperationExecError(final SecurityOperation reqOp,
																	 			final E otherError) {
		this(reqOp);
		this.setCalledMethod(otherError.getCalledMethod());
		this.setError(otherError.getError());
		this.setErrorDebug(otherError.getErrorDebug());
		this.setErrorMessage(otherError.getErrorMessage());
		this.setErrorType(otherError.getErrorType());
		this.setRequestedOperation(otherError.getRequestedOperation());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public T getOrThrow() throws COREServiceException {
		// BEWARE!! override getOrThrow method! otherwise a COREServiceExeception
		//			instance will be thrown
		throw this.getSecurityException();
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperation getRequestedOperation() {
		return SecurityOperation.from(_calledMethod) != null ? SecurityOperation.from(_calledMethod)
				                                             : SecurityOperation.OTHER;
	}
	public void setRequestedOperation(final SecurityOperation calledOp) {
		_calledMethod = calledOp != null ? calledOp.getCOREServiceMethod() : null;
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@Override
	public SecurityOperationExecOK<T> asSecurityOperationOK() {
		throw new ClassCastException();
	}
	@Override
	public SecurityOperationExecError<T> asSecurityOperationError() {
		return this;
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  
/////////////////////////////////////////////////////////////////////////////////////////
	public void throwAsSecurityException() throws SecurityOperationException {
		throw this.getSecurityException();
	}
	public SecurityOperationException getSecurityException() {
		String errorMsg = Strings.isNOTNullOrEmpty(_errorMessage) ? _errorMessage
																  : this.getDetailedMessage();
		COREServiceErrorType errorType = _errorType != null ? _errorType : COREServiceErrorTypes.UNKNOWN;
		return new SecurityOperationException(this.getRequestedOperation(),
											  errorType,
											  errorMsg);
	} 
}
