package r01f.model.security.pairing.operations;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecOK;
import r01f.objectstreamer.annotations.MarshallType;

@MarshallType(as="pairingOperationExecResult",typeId="ok")
@Accessors(prefix="_")
public class PairingOperationExecOK<T>
	 extends COREServiceMethodExecOK<T>
  implements PairingOperationExecResult<T> {
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public PairingOperationExecOK() {
		// default no-args constructor	
	}
	public PairingOperationExecOK(final COREServiceMethod reqOp) {
		super(reqOp);
	}
	public PairingOperationExecOK(final COREServiceMethod reqOp,
							  	  final T instance) {
		super(reqOp,
			  instance);
	}
	public PairingOperationExecOK(final PairingOperation reqOp) {
		super(reqOp.getCOREServiceMethod());
	}
	public PairingOperationExecOK(final PairingOperation reqOp,
							      final T instance) {
		super(reqOp.getCOREServiceMethod(),
			  instance);
	}
}
