package r01f.model.security.operation;

import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;

@MarshallPolymorphicTypeInfo
public interface IsSecurityOperationResult<T>
		 extends COREServiceMethodExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  REQUESTEED OPERATION
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return the requested operation
	 */
	public SecurityOperation getRequestedOperation();
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return a {@link SecurityOperationExecOK} instance
	 */
	public SecurityOperationExecOK<T> asSecurityOperationOK();
	/**
	 * @return a {@link SecurityOperationExecError} instance
	 */
	public SecurityOperationExecError<T> asSecurityOperationError();
}
