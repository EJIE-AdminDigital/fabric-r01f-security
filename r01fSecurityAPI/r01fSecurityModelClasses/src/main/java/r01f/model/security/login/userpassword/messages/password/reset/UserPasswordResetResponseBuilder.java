package r01f.model.security.login.userpassword.messages.password.reset;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.patterns.IsBuilder;

public abstract class UserPasswordResetResponseBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordResetResponseBuilderLoginStep changed() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.CHANGED);
	}
	public static UserPasswordResetResponseBuilderLoginStep invalidLoginId() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.INVALID_LOGIN_ID);
	}
	public static UserPasswordResetResponseBuilderLoginStep unregisteredResetRequest() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.UNREGISTERED_RESET_REQUEST);
	}
	public static UserPasswordResetResponseBuilderLoginStep tokenWithInvalidSignature() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.TOKEN_WITH_INVALID_SIGNATURE);
	}
	public static UserPasswordResetResponseBuilderLoginStep tokenWithInvalidIssuer() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.TOKEN_WITH_INVALID_ISSUER);
	}
	public static UserPasswordResetResponseBuilderLoginStep tokenWithInvalidAudience() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.TOKEN_WITH_INVALID_AUDIENCE);
	}
	public static UserPasswordResetResponseBuilderLoginStep tokenExpired() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.TOKEN_EXPIRED);
	}
	public static UserPasswordResetResponseBuilderLoginStep tokenWithOtherError() {
		return UserPasswordResetResponseBuilder.withResult(UserPasswordLoginPasswordResetResult.TOKEN_WITH_OTHER_ERROR);
	}
	public static UserPasswordResetResponseBuilderLoginStep withResult(final UserPasswordLoginPasswordResetResult result) {
		UserPasswordResetResponse response = new UserPasswordResetResponse();
		response.setResult(result);
		return new UserPasswordResetResponseBuilder() { /* nothing */ } 
						.new UserPasswordResetResponseBuilderLoginStep(response);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordResetResponseBuilderLoginStep {
		private final UserPasswordResetResponse _response;

		public BuilderBuildStep<UserPasswordResetResponse> forLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginOid(loginOid);
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}
		public BuilderBuildStep<UserPasswordResetResponse> forLogin(final LoginID loginId) {
			return this.forLogin(null,loginId);
		}
	}
}
