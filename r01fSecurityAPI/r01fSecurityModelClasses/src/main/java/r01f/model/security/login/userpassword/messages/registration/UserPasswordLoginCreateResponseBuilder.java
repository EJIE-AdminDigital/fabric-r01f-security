package r01f.model.security.login.userpassword.messages.registration;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.IsBuilder;

public abstract class UserPasswordLoginCreateResponseBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep created() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.CREATED);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep principalAlreadyExists() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.PRINCIPAL_ALREADY_EXISTS);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep loginIdAlreadyExists() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.LOGIN_ID_ALREADY_EXISTS);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep invalidNotificationData() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.INVALID_NOTIFICATION_DATA);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep invalidNotificationType() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.INVALID_NOTIFICATION_TYPE);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep notificationError() {
		return UserPasswordLoginCreateResponseBuilder.withResult(UserPasswordLoginCreateResult.NOTIFICATION_ERROR);
	}
	public static UserPasswordLoginCreateResponseBuilderPrincipalStep withResult(final UserPasswordLoginCreateResult result) {
		UserPasswordLoginCreateResponse response = new UserPasswordLoginCreateResponse();
		response.setResult(result);
		return new UserPasswordLoginCreateResponseBuilder() { /* nothing */ }
					.new UserPasswordLoginCreateResponseBuilderPrincipalStep(response);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginCreateResponseBuilderPrincipalStep {
		private final UserPasswordLoginCreateResponse _response;

		public UserPasswordLoginCreateResponseBuilderLoginStep forPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
			_response.setSecurityPrincipalOid(securityPrincipalOid);
			return new UserPasswordLoginCreateResponseBuilderLoginStep(_response);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginCreateResponseBuilderLoginStep {
		private final UserPasswordLoginCreateResponse _response;

		public BuilderBuildStep<UserPasswordLoginCreateResponse> withLogin(final LoginID loginId) {
			return this.withLogin(null,loginId);
		}
		public BuilderBuildStep<UserPasswordLoginCreateResponse> withLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}
	}
}
