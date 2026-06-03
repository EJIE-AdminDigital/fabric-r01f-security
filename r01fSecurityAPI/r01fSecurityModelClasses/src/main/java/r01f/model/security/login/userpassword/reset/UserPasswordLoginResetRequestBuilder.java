package r01f.model.security.login.userpassword.reset;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;
import r01f.securitycontext.SecurityIDS.SecurityToken;

@NoArgsConstructor
public abstract class UserPasswordLoginResetRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginResetRequestBuilderLoginIDStep requestedAt(final Instant date) {
		return new UserPasswordLoginResetRequestBuilder() { /* nothing */ }
						.new UserPasswordLoginResetRequestBuilderLoginIDStep(date);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderLoginIDStep {
		private final Instant _requestedAt;

		public UserPasswordLoginResetRequestBuilderPasswordStep using(final LoginID login) {
			UserPasswordLoginResetRequest req = new UserPasswordLoginResetRequest();
			req.setRequestedAt(_requestedAt);
			req.setLoginId(login);
			return new UserPasswordLoginResetRequestBuilderPasswordStep(req);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderPasswordStep {
		private final UserPasswordLoginResetRequest _passwordResetRequest;

		public UserPasswordLoginResetRequestBuilderTokenStep withPassword(final Password password) {
			_passwordResetRequest.setPassword(password);
			return new UserPasswordLoginResetRequestBuilderTokenStep(_passwordResetRequest);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderTokenStep {
		private final UserPasswordLoginResetRequest _passwordResetRequest;

		public UserPasswordLoginResetRequestBuilderBuildStep andToken(final SecurityToken token) {
			_passwordResetRequest.setToken(token);
			return new UserPasswordLoginResetRequestBuilderBuildStep(_passwordResetRequest);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderBuildStep {
		private final UserPasswordLoginResetRequest _passwordResetRequest;

		public UserPasswordLoginResetRequest build() {
			return _passwordResetRequest;
		}
	}
}
