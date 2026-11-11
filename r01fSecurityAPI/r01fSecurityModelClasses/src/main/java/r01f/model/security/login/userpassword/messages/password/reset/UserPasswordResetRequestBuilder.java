package r01f.model.security.login.userpassword.messages.password.reset;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserPasswordResetRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordResetRequestBuilderPasswordStep forLogin(final LoginID loginId) {
		UserPasswordResetRequest req = new UserPasswordResetRequest();
		req.setLoginId(loginId);
		return new UserPasswordResetRequestBuilder() { /* nothing */ }
						.new UserPasswordResetRequestBuilderPasswordStep(req);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordResetRequestBuilderPasswordStep {
		private final UserPasswordResetRequest _request;

		public UserPasswordLoginResetRequestBuilderTokenStep withTempPassword(final Password password) {
			_request.setTempPassword(password);
			return new UserPasswordLoginResetRequestBuilderTokenStep(_request);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderTokenStep {
		private final UserPasswordResetRequest _request;

		public BuilderBuildStep<UserPasswordResetRequest> usingToken(final SecurityToken token) {
			_request.setToken(token);
			return new BuilderBuildStep<>(_request);
		}
	}
}