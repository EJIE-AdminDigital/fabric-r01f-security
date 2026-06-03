package r01f.model.security.otp.operations;

import r01f.model.persistence.PersistenceOperationExecError;
import r01f.model.services.COREServiceMethodExecResult;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo;
import r01f.objectstreamer.annotations.MarshallPolymorphicTypeInfo.MarshalTypeInfoIncludeCase;

@MarshallPolymorphicTypeInfo(includeTypeInfo=MarshalTypeInfoIncludeCase.ALWAYS)
public interface OTPOperationExecResult<T>
		 extends COREServiceMethodExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  REQUESTED OPERATION
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return the requested operation
	 */
	public default OTPOperation getOperation() {
		return OTPOperation.from(this.getCalledMethod());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * @return a {@link OTPOperationExecOK} instance
	 */
	public default OTPOperationExecOK<T> asOTPOperationOK() {
		if (this instanceof OTPOperationExecError) throw new ClassCastException(this.getClass() + " is not an instance of " + OTPOperationExecOK.class);
		return (OTPOperationExecOK<T>)this;
	}
	/**
	 * @return a {@link PersistenceOperationExecError} instance
	 */
	public default OTPOperationExecError<T> asOTPOperationError() {
		if (this instanceof OTPOperationExecOK) throw new ClassCastException(this.getClass() + " is not an instance of " + OTPOperationExecError.class);
		return (OTPOperationExecError<T>)this;
	}
}
