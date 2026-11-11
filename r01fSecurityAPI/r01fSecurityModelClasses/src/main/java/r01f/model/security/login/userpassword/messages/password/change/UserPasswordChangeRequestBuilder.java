package r01f.model.security.login.userpassword.messages.password.change;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserPasswordChangeRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordChangeRequestBuilderOldPasswordStep forLogin(final LoginID loginId) {
		UserPasswordChangeRequest req = new UserPasswordChangeRequest();
		req.setLoginId(loginId);
		return new UserPasswordChangeRequestBuilder() { /* nothing */ }
						.new UserPasswordChangeRequestBuilderOldPasswordStep(req);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordChangeRequestBuilderOldPasswordStep {
		private final UserPasswordChangeRequest _request;

		public UserPasswordChangeRequestBuilderNewPasswordStep withdOldPassword(final Password password) {
			_request.setOldPassword(password);
			return new UserPasswordChangeRequestBuilderNewPasswordStep(_request);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordChangeRequestBuilderNewPasswordStep {
		private final UserPasswordChangeRequest _request;

		public UserPasswordLoginResetRequestBuilderTokenStep withdNewPassword(final Password password) {
			_request.setOldPassword(password);
			return new UserPasswordLoginResetRequestBuilderTokenStep(_request);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderTokenStep {
		private final UserPasswordChangeRequest _request;

		public BuilderBuildStep<UserPasswordChangeRequest> usingToken(final SecurityToken token) {
			_request.setToken(token);
			return new BuilderBuildStep<>(_request);
		}
	}
}