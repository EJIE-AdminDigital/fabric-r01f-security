package r01f.model.security.operation;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.services.COREServiceErrorType;
import r01f.model.services.COREServiceMethod;
import r01f.model.services.COREServiceMethodExecError;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SecurityOperationExecResultBuilder
  		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////		
	public static SecurityOperationExecResultBuilderReturnedObjStep executed(final COREServiceMethod requestedOpName) {
		return new SecurityOperationExecResultBuilder() { /* nothing */ }
					.new SecurityOperationExecResultBuilderReturnedObjStep(requestedOpName);
	}
	public static SecurityOperationExecResultBuilderErrorStep notExecuted(final COREServiceMethod requestedOpName) {
		return new SecurityOperationExecResultBuilder() { /* nothing */ }
					.new SecurityOperationExecResultBuilderErrorStep(requestedOpName);
	}
	public static SecurityOperationExecResultBuilderReturnedObjStep executed(final SecurityOperation requestedOpName) {
		return new SecurityOperationExecResultBuilder() { /* nothing */ }
					.new SecurityOperationExecResultBuilderReturnedObjStep(requestedOpName.getCOREServiceMethod());
	}
	public static SecurityOperationExecResultBuilderErrorStep notExecuted(final SecurityOperation requestedOpName) {
		return new SecurityOperationExecResultBuilder() { /* nothing */ }
					.new SecurityOperationExecResultBuilderErrorStep(requestedOpName.getCOREServiceMethod());
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  Operation
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SecurityOperationExecResultBuilderReturnedObjStep {
		protected final COREServiceMethod _requestedOp;

		public <T> SecurityOperationExecOK<T> returning(final T instance) {
			SecurityOperationExecOK<T> outOpOK = new SecurityOperationExecOK<T>(_requestedOp,
																				instance);
			return outOpOK;
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//  ERROR
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SecurityOperationExecResultBuilderErrorStep {
		protected final COREServiceMethod _requestedOp;

		public <T> SecurityOperationExecError<T> because(final COREServiceMethodExecError<?> otherError) {
			SecurityOperationExecError<T> outError = new SecurityOperationExecError<T>(otherError);
			return outError;
		}
		public <T> SecurityOperationExecError<T> because(final COREServiceErrorType errType,
														 final String error) {
			SecurityOperationExecError<T> outError = new SecurityOperationExecError<T>(_requestedOp,
																					   errType,
																					   error);
			return outError;
		}
		public <T> SecurityOperationExecError<T> because(final COREServiceErrorType errType,
														 final Throwable error) {
			SecurityOperationExecError<T> outError = new SecurityOperationExecError<T>(_requestedOp,
																					   errType,
																					   error);
			return outError;
		}
	}
}
