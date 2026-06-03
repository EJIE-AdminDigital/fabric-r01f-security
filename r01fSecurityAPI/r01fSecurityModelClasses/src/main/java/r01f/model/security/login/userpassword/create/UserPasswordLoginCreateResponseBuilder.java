package r01f.model.security.login.userpassword.create;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.SecurityToken;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

public class UserPasswordLoginCreateResponseBuilder
  implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserRegistrationResponseBuilderLoginIDStep of(final UserOID userOid) {
		return new UserPasswordLoginCreateResponseBuilder() { /* nothing */ }
						.new UserRegistrationResponseBuilderLoginIDStep(userOid);
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserRegistrationResponseBuilderLoginIDStep {
		private final UserOID _userOid;

		public UserRegistrationResponseBuilderResultStep withLoginId(final LoginID loginId) {
			return new UserRegistrationResponseBuilderResultStep(_userOid,
																 loginId);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserRegistrationResponseBuilderResultStep {
		private final UserOID _userOid;
		private final LoginID _loginId;

		public UserRegistrationResponseBuilderTokenStep withLoginOid(final LoginOID loginOid) {
			return new UserRegistrationResponseBuilderTokenStep(_userOid,
																_loginId,
																loginOid);
		}
		public UserRegistrationResponseBuilderBuildStep withError(final UserPasswordLoginCreateErrorType errorType) {
			UserPasswordLoginCreateResponseNOK resp = new UserPasswordLoginCreateResponseNOK();
			resp.setLoginId(_loginId);
			resp.setUserOid(_userOid);
			resp.setErrorType(errorType);
			return new UserRegistrationResponseBuilderBuildStep(resp);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserRegistrationResponseBuilderTokenStep {
		private final UserOID _userOid;
		private final LoginID _loginId;
		private final LoginOID _loginOid;

		public UserRegistrationResponseBuilderBuildStep withToken(final SecurityToken token) {
			UserPasswordLoginCreateResponseOK resp = new UserPasswordLoginCreateResponseOK();
			resp.setUserOid(_userOid);
			resp.setLoginId(_loginId);
			resp.setLoginOid(_loginOid);
			resp.setToken(token);
			return new UserRegistrationResponseBuilderBuildStep(resp);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserRegistrationResponseBuilderBuildStep {
		private final UserPasswordLoginCreateResponse _resp;

		public UserPasswordLoginCreateResponse build() {
			return _resp;
		}
		public UserPasswordLoginCreateResponse buildAt(final Instant date) {
			_resp.setRegisteredAt(date);
			return _resp;
		}
	}
}
