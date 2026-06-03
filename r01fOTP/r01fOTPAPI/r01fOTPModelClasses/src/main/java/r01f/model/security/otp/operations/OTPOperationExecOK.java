package r01f.model.security.otp.operations;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecOK;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="otpOperationExecResult",typeId="ok")
@Accessors(prefix="_")
public class OTPOperationExecOK<T>
	 extends COREServiceMethodExecOK<T>
  implements OTPOperationExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public OTPOperationExecOK() {
		// default no-args constructor
	}
	public OTPOperationExecOK(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public OTPOperationExecOK(final COREServiceMethod reqOp,
							  final T instance) {
		super(reqOp,
			  instance);
	}
	public OTPOperationExecOK(final OTPOperation reqOp) {
		super(reqOp.getCOREServiceMethod());
	}
	public OTPOperationExecOK(final OTPOperation reqOp,
							  final T instance) {
		super(reqOp.getCOREServiceMethod(),
			  instance);
	}
}
