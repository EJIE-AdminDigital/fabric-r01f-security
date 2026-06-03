package r01f.model.security.pairing.operations;

import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface PairingOperationExecResult<T>
		 extends COREServiceMethodExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  THE REQUESTED PAIRING OPERATION
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return the requested pairing-operation
	 */
	public default PairingOperation getOperation() {
		return PairingOperation.from(this.getCalledMethod());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return as a {@link PairingOperationExecOK} instance
	 */
	public default PairingOperationExecOK<T> asPairingOperationOK() {
		if (this instanceof PairingOperationExecError) throw new ClassCastException(this.getClass() + " cannot be casted to " + PairingOperationExecOK.class);
		return (PairingOperationExecOK<T>)this;
	}
	/**
	 * @return as a {@link PairingOperationExecError} instance
	 */
	public default PairingOperationExecError<T> asPairingOperationError() {
		if (this instanceof PairingOperationExecOK) throw new ClassCastException(this.getClass() + " cannot be casted to " + PairingOperationExecError.class);
		return (PairingOperationExecError<T>)this;
	}
}
