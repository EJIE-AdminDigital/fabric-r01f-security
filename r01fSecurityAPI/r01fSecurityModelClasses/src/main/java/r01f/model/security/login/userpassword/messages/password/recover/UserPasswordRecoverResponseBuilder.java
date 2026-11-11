package r01f.model.security.login.userpassword.messages.password.recover;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityToken;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.patterns.IsBuilder;

public abstract class UserPasswordRecoverResponseBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordRecoverResponseBuilderLoginStep registered(final SecurityToken token) {
		UserPasswordRecoverResponse response = new UserPasswordRecoverResponse();
		response.setResult(UserPasswordLoginPasswordRecoverResult.RECOVER_REQ_REGISTERED);
		response.setToken(token);
		return new UserPasswordRecoverResponseBuilder() { /* nothing */ } 
						.new UserPasswordRecoverResponseBuilderLoginStep(response);
	}
	public static UserPasswordRecoverResponseBuilderLoginStep invalidLoginId() {
		return UserPasswordRecoverResponseBuilder.withResult(UserPasswordLoginPasswordRecoverResult.INVALID_LOGIN_ID);
	}
	public static UserPasswordRecoverResponseBuilderLoginStep invalidNotificationData() {
		return UserPasswordRecoverResponseBuilder.withResult(UserPasswordLoginPasswordRecoverResult.INVALID_NOTIFICATION_DATA);
	}
	public static UserPasswordRecoverResponseBuilderLoginStep invalidNotificationType() {
		return UserPasswordRecoverResponseBuilder.withResult(UserPasswordLoginPasswordRecoverResult.INVALID_NOTIFICATION_TYPE);
	}
	public static UserPasswordRecoverResponseBuilderLoginStep withResult(final UserPasswordLoginPasswordRecoverResult result) {
		UserPasswordRecoverResponse response = new UserPasswordRecoverResponse();
		response.setResult(result);
		return new UserPasswordRecoverResponseBuilder() { /* nothing */ } 
						.new UserPasswordRecoverResponseBuilderLoginStep(response);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordRecoverResponseBuilderLoginStep {
		private final UserPasswordRecoverResponse _response;

		public BuilderBuildStep<UserPasswordRecoverResponse> forLogin(final LoginID loginId) {
			return this.forLogin(null,loginId);
		}
		public BuilderBuildStep<UserPasswordRecoverResponse> forLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginOid(loginOid);
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}
	}
}
