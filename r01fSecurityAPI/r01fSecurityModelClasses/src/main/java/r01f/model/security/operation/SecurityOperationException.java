package r01f.model.security.operation;

import lombok.experimental.Accessors;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;
import r01f.model.services.COREServiceException;
import r01f.util.types.Strings;


@Accessors(prefix="_")
public class SecurityOperationException
	 extends COREServiceException {

	private static final long serialVersionUID = -1161648233290893856L;
/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityOperationException(final SecurityOperation requestedOp,
									  final COREServiceErrorType errorType,
									  final Throwable th) {
		super(requestedOp.getCOREServiceMethod(),
			  errorType,
			  th);
	}
	public SecurityOperationException(final SecurityOperation requestedOp,
						 			  final Throwable th) {
		super(requestedOp.getCOREServiceMethod(),
			  th);
	}
	public SecurityOperationException(final SecurityOperation requestedOp,
									  final COREServiceErrorType errorType,
									  final String msg,final Throwable th) {
		super(requestedOp.getCOREServiceMethod(),
			  errorType,
			  th);
	}
	public SecurityOperationException(final SecurityOperation requestedOp,
						 			  final String msg,final Throwable th) {
		super(requestedOp.getCOREServiceMethod(),
			  th);
	}
	public SecurityOperationException(final SecurityOperation requestedOp,
									  final COREServiceErrorType errorType,
									  final String msg) {
		super(requestedOp.getCOREServiceMethod(),
			  errorType,
			  msg);
	}
	public SecurityOperationException(final SecurityOperation requestedOp,
						 			  final String msg) {
		super(requestedOp.getCOREServiceMethod(),
			  msg);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  BUILDERS
/////////////////////////////////////////////////////////////////////////////////////////
	public static SecurityOperationException serverError(final SecurityOperation requestedOp) {
		return new SecurityOperationException(requestedOp,
											 COREServiceErrorTypes.SERVER_ERROR,
											 (Throwable)null);
	}
	public static SecurityOperationException serverError(final SecurityOperation requestedOp,
														 final String msg,final Object... vars) {
		return new SecurityOperationException(requestedOp,
											  COREServiceErrorTypes.SERVER_ERROR,
											  Strings.customized(msg,vars));
	}
	public static SecurityOperationException serverError(final SecurityOperation requestedOp,
														 final Throwable th) {
		return new SecurityOperationException(requestedOp,
											  COREServiceErrorTypes.SERVER_ERROR,
											  th);
	}
	public static SecurityOperationException badClientRequest(final SecurityOperation requestedOp) {
		return new SecurityOperationException(requestedOp,
											  COREServiceErrorTypes.BAD_CLIENT_REQUEST,
											  (Throwable)null);
	}
	public static SecurityOperationException badClientRequest(final SecurityOperation requestedOp,
															  final Throwable th) {
		return new SecurityOperationException(requestedOp,
											  COREServiceErrorTypes.BAD_CLIENT_REQUEST,
											  th);
	}
	public static SecurityOperationException badClientRequest(final SecurityOperation requestedOp,
												 			  final String msg,final Object... vars) {
		return new SecurityOperationException(requestedOp,
											  COREServiceErrorTypes.BAD_CLIENT_REQUEST,
											  Strings.customized(msg,vars));
	}
}
