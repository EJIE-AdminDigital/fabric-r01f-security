package r01f.model.security.login.userpassword.reset;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;


@NoArgsConstructor
public abstract class UserPasswordLoginResetResponseBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordResetResponseBuilderDateStep forLoginId(final LoginID login) {
		return new UserPasswordLoginResetResponseBuilder() { /* nothing */ }
					.new UserPasswordResetResponseBuilderDateStep(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////		
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE) 
	public class UserPasswordResetResponseBuilderDateStep {
		private final LoginID _login;

		public UserPasswordResetResponseBuilderDoneStep responsedAt(final Instant date) {
			return new UserPasswordResetResponseBuilderDoneStep(_login,
																date);
		}
		public UserPasswordResetResponseBuilderDoneStep responsedNow() {
			return this.responsedAt(Instant.now());
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordResetResponseBuilderDoneStep {
		private final LoginID _login;
		private final Instant _requestDate;

		public UserPasswordResetResponseBuilderBuildStep done() {
			UserPasswordLoginResetResponseOK response = new UserPasswordLoginResetResponseOK();
			response.setResponsedAt(_requestDate);
			response.setLoginId(_login);
			return new UserPasswordResetResponseBuilderBuildStep(response);
		}
		public UserPasswordResetResponseBuilderBuildStep withError(final UserPasswordLoginResetResponseErrorType errorType) {
			UserPasswordLoginResetResponseNOK response = new UserPasswordLoginResetResponseNOK();
			response.setResponsedAt(_requestDate);
			response.setLoginId(_login);
			response.setErrorType(errorType);
			return new UserPasswordResetResponseBuilderBuildStep(response);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordResetResponseBuilderBuildStep {
		private final UserPasswordLoginResetResponse _response;

		public UserPasswordLoginResetResponse build() {
			return _response;
		}
	}
}
