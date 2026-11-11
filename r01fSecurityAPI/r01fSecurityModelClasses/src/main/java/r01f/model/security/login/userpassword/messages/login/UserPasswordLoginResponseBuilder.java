package r01f.model.security.login.userpassword.messages.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class UserPasswordLoginResponseBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginResponseBuilderLoginIdStep loggedIn() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.LOGGED_IN);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep awaitingAuthSecondFactorVerification() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.AWAITING_AUTHN_SECOND_FACTOR_VERIFICATION);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep invalidLoginId() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.INVALID_LOGIN_ID);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep passwordError() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.PASSWORD_ERROR);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep passwordExpired() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.PASSWORD_EXPIRED);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep loginNotActive() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.LOGIN_NOT_ACTIVE);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep authSecondFactorMeanNotRegistered() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.AUTHN_SECOND_FACTOR_MEAN_NOT_REGISTRED);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep authSecondFactorSecretError() {
		return UserPasswordLoginResponseBuilder.withResult(UserPasswordLoginResult.AUTHN_SECOND_FACTOR_SECRET_ERROR);
	}
	public static UserPasswordLoginResponseBuilderLoginIdStep withResult(final UserPasswordLoginResult result) {
		UserPasswordLoginResponse resp = new UserPasswordLoginResponse(result);
		return new UserPasswordLoginResponseBuilder() { /* nothing */ }
						.new UserPasswordLoginResponseBuilderLoginIdStep(resp);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class UserPasswordLoginResponseBuilderLoginIdStep {
		private final UserPasswordLoginResponse _response;
		public BuilderBuildStep<UserPasswordLoginResponse> forLogin(final LoginID loginId) {
			return this.forLogin(null,loginId);
		}
		public BuilderBuildStep<UserPasswordLoginResponse> forLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginOid(loginOid);
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}

	}
}
