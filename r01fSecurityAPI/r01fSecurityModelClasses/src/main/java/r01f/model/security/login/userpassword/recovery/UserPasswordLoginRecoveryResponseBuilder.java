package r01f.model.security.login.userpassword.recovery;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;


@NoArgsConstructor
public abstract class UserPasswordLoginRecoveryResponseBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginRecoveryResponseBuilderResultStep forLoginId(final LoginID login) {
		return new UserPasswordLoginRecoveryResponseBuilder() { /* nothing */ }
						.new UserPasswordLoginRecoveryResponseBuilderResultStep(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginRecoveryResponseBuilderResultStep {
		private final LoginID _login;

		public UserPasswordLoginRecoveryResponseBuilderBuildStep okWithToken(final SecurityToken token) {
			UserPasswordLoginRecoveryResponseOK resp = new UserPasswordLoginRecoveryResponseOK();
			resp.setLoginId(_login);
			resp.setToken(token);
			return new UserPasswordLoginRecoveryResponseBuilderBuildStep(resp);
		}
		public UserPasswordLoginRecoveryResponseBuilderBuildStep withError(final UserPasswordLoginRecoveryErrorType errorType) {
			UserPasswordLoginRecoveryResponseNOK resp = new UserPasswordLoginRecoveryResponseNOK();
			resp.setLoginId(_login);
			resp.setErrorType(errorType);
			return new UserPasswordLoginRecoveryResponseBuilderBuildStep(resp);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginRecoveryResponseBuilderBuildStep {
		private final UserPasswordLoginRecoveryResponse _resp;

		public UserPasswordLoginRecoveryResponse build() {
			return _resp;
		}
		public UserPasswordLoginRecoveryResponse buildAt(final Instant date) {
			_resp.setResponsedAt(date);
			return _resp;
		}
	}
}
