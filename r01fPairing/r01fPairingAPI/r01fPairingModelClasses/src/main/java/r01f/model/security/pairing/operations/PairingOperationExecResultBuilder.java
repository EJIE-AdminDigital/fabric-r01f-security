package r01f.model.security.pairing.operations;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.otp.operations.OTPOperationExecResult;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.patterns.IsBuilder;
import r01f.util.types.Strings;

/**
 * Builder type for {@link OTPOperationExecResult}-implementing types:
 * <ul>
 * 		<li>A successful on operation execution result: {@link PairingOperationExecOK}</li>
 * 		<li>An error  on  operation execution:          {@link PairingOperationExecError}</li>
 * </ul>
 * If the operation execution was successful:
 * <pre class='brush:java'>
 * 		TK00PairingOperationExecOK<MyReturnedObjType> opOK = TK00PairingOperationExecResultBuilder.executed("an operation")
 * 															 					                      .returning(myReturnedObjTypeInstance);
 * </pre> 
 * If an error is raised while executing the OTP operation:
 * <pre class='brush:java'>
 * 		TK00PairingOperationExecOError<MyReturnedObjType> opError = TK00PairingOperationExecResultBuilder.notExecuted("an operation")
 *			  																							  .because(error);
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public class PairingOperationExecResultBuilder
  implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static TK00PairingOperationExecResultBuilderReturnedObjStep executed(final COREServiceMethod requestedOp) {
		return new PairingOperationExecResultBuilder() {/* nothing */}
		           .new TK00PairingOperationExecResultBuilderReturnedObjStep(requestedOp);
	}
	public static TK00PairingOperationExecResultBuilderErrorStep notExecuted(final COREServiceMethod requestedOp) {
		return  new PairingOperationExecResultBuilder() {/* nothing */}
		          .new TK00PairingOperationExecResultBuilderErrorStep( requestedOp);
	}
	public static TK00PairingOperationExecResultBuilderReturnedObjStep executed(final PairingOperation requestedOp) {
		return new PairingOperationExecResultBuilder() {/* nothing */}
		           .new TK00PairingOperationExecResultBuilderReturnedObjStep(requestedOp.getCOREServiceMethod());
	}
	public static TK00PairingOperationExecResultBuilderErrorStep notExecuted(final PairingOperation requestedOp) {
		return  new PairingOperationExecResultBuilder() {/* nothing */}
		          .new TK00PairingOperationExecResultBuilderErrorStep( requestedOp.getCOREServiceMethod());
	}

/////////////////////////////////////////////////////////////////////////////////////////
//  Operation
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class TK00PairingOperationExecResultBuilderReturnedObjStep {
		protected final COREServiceMethod _requestedOp;

		public <T> PairingOperationExecOK<T> returning(final T instance) {
			PairingOperationExecOK<T> outOpOK = new PairingOperationExecOK<T>(_requestedOp,
																                       instance);
			return outOpOK;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ERROR
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public final class TK00PairingOperationExecResultBuilderErrorStep {
		protected final COREServiceMethod _requestedOp;

		public <T> PairingOperationExecError<T> because(final Throwable th) {
			PairingOperationExecError<T> outError = new PairingOperationExecError<T>(_requestedOp,
																			                  th);
			return outError;
		}
		public <T> PairingOperationExecError<T> because(final COREServiceErrorType errType,
													        final Throwable th) {
			PairingOperationExecError<T> outError = new PairingOperationExecError<T>(_requestedOp,
																			                 errType,
																		                 	 th);
			return outError;
		}
		public <T> PairingOperationExecError<T> because(final COREServiceErrorType errType,
													        final String error) {
			PairingOperationExecError<T> outError = new PairingOperationExecError<T>(_requestedOp,
																			                  errType,
																			                  error);
			return outError;
		}
		public <T> PairingOperationExecError<T> becauseClientBadRequest(final String msg,final Object... vars) {
			@SuppressWarnings("static-access")
			PairingOperationExecError<T> outError = new PairingOperationExecError<T>(_requestedOp,
				                                                                             PairingErrorTypes.BAD_CLIENT_REQUEST,
																			                 Strings.customized(msg,vars));			// the error message
			return outError;
		}

	}
}
