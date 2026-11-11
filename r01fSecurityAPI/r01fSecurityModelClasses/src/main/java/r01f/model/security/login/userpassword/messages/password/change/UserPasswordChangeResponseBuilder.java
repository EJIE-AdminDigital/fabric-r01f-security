package r01f.model.security.login.userpassword.messages.password.change;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.patterns.IsBuilder;

public abstract class UserPasswordChangeResponseBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordChangeResponseBuilderLoginIdStep changed() {
		return UserPasswordChangeResponseBuilder.withResult(UserPasswordChangeResult.CHANGED);
	}
	public static UserPasswordChangeResponseBuilderLoginIdStep invalidLoginId() {
		return UserPasswordChangeResponseBuilder.withResult(UserPasswordChangeResult.INVALID_LOGIN_ID);
	}
	public static UserPasswordChangeResponseBuilderLoginIdStep oldPasswordNotMatches() {
		return UserPasswordChangeResponseBuilder.withResult(UserPasswordChangeResult.OLD_PASSWORD_NOT_MATCHES);
	}
	public static UserPasswordChangeResponseBuilderLoginIdStep withResult(final UserPasswordChangeResult result) {
		UserPasswordChangeResponse response = new UserPasswordChangeResponse();
		response.setResult(result);
		return new UserPasswordChangeResponseBuilder() { /* nothing */ }
					.new UserPasswordChangeResponseBuilderLoginIdStep(response);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordChangeResponseBuilderLoginIdStep {
		private final UserPasswordChangeResponse _response;

		public BuilderBuildStep<UserPasswordChangeResponse> forLogin(final LoginID loginId) {
			return this.forLogin(null,loginId);
		}
		public BuilderBuildStep<UserPasswordChangeResponse> forLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}
	}
}
