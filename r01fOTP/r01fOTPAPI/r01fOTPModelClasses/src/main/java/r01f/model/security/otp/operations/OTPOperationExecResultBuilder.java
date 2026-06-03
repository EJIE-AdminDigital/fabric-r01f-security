package r01f.model.security.otp.operations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceErrorTypes;
import r01f.model.services.COREServiceMethod;
import r01f.patterns.IsBuilder;
import r01f.util.types.Strings;

/**
 * Builder type for {@link OTPOperationExecResult}-implementing types:
 * <ul>
 * 		<li>A successful operation execution result: {@link OTPOperationExecOK}</li>
 * 		<li>An error on a FIND operation execution:  {@link OTPOperationExecError}</li>
 * </ul>
 * If the operation execution was successful:
 * <pre class='brush:java'>
 * 		OTPOperationExecOK<MyReturnedObjType> opOK = OTPOperationExecResultBuilder.executed("an operation")
 * 																				     .returning(myReturnedObjTypeInstance);
 * </pre>
 * If an error is raised while executing the OTP operation:
 * <pre class='brush:java'>
 * 		OTPOperationExecError<MyReturnedObjType> opError = OTPOperationExecResultBuilder.notExecuted("an operation")
 *			  																							.because(error);
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class OTPOperationExecResultBuilder
  implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static OTPOperationExecResultBuilderReturnedObjStep executed(final COREServiceMethod requestedOp) {
		return new OTPOperationExecResultBuilder() {/* nothing */}
		           .new OTPOperationExecResultBuilderReturnedObjStep(requestedOp);
	}
	public static OTPOperationExecResultBuilderErrorStep notExecuted(final COREServiceMethod requestedOp) {
		return  new OTPOperationExecResultBuilder() {/* nothing */}
		          .new OTPOperationExecResultBuilderErrorStep( requestedOp);
	}
	public static OTPOperationExecResultBuilderReturnedObjStep executed(final OTPOperation requestedOp) {
		return new OTPOperationExecResultBuilder() {/* nothing */}
		           .new OTPOperationExecResultBuilderReturnedObjStep(requestedOp.getCOREServiceMethod());
	}
	public static OTPOperationExecResultBuilderErrorStep notExecuted(final OTPOperation requestedOp) {
		return  new OTPOperationExecResultBuilder() {/* nothing */}
		          .new OTPOperationExecResultBuilderErrorStep( requestedOp.getCOREServiceMethod());
	}

/////////////////////////////////////////////////////////////////////////////////////////
//  Operation
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPOperationExecResultBuilderReturnedObjStep {
		protected final COREServiceMethod _requestedOp;

		public <T> OTPOperationExecOK<T> returning(final T instance) {
			OTPOperationExecOK<T> outOpOK = new OTPOperationExecOK<T>(_requestedOp,
																	  instance);
			return outOpOK;
		}

	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ERROR
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class OTPOperationExecResultBuilderErrorStep {
		protected final COREServiceMethod _requestedOp;

		public <T> OTPOperationExecError<T> because(final Throwable th) {
			OTPOperationExecError<T> outError = new OTPOperationExecError<T>(_requestedOp,
																			 th);
			return outError;
		}
		public <T> OTPOperationExecError<T> because(final COREServiceErrorType errType,
													final Throwable th) {
			OTPOperationExecError<T> outError = new OTPOperationExecError<T>(_requestedOp,
																			 errType,
																			 th);
			return outError;
		}
		public <T> OTPOperationExecError<T> because(final COREServiceErrorType errType,
													final String error) {
			OTPOperationExecError<T> outError = new OTPOperationExecError<T>(_requestedOp,
																			 errType,
																			 error);
			return outError;
		}
		public <T> OTPOperationExecError<T> becauseClientBadRequest(final String msg,final Object... vars) {
			OTPOperationExecError<T> outError = new OTPOperationExecError<T>(_requestedOp,
																			 COREServiceErrorTypes.BAD_CLIENT_REQUEST,
																			 Strings.customized(msg,vars));			// the error message
			return outError;
		}

	}
}
