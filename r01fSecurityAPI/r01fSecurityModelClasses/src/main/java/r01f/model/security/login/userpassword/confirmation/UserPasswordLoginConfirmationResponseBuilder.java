package r01f.model.security.login.userpassword.confirmation;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.security.user.UserStatus;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.LoginOID;
import r01f.securitycontext.SecurityOIDs.UserOID;

public class UserPasswordLoginConfirmationResponseBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserConfirmationResponseBuilderResultStep forUserOid(final UserOID userOid) {
		return new UserPasswordLoginConfirmationResponseBuilder() { /* nothing */ }
						.new UserConfirmationResponseBuilderResultStep(userOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationResponseBuilderResultStep {
		private final UserOID _userOid;

		public UserConfirmationResponseBuilderLoginStep ok() {
			return new UserConfirmationResponseBuilderLoginStep(_userOid);
		}
		public UserConfirmationResponseBuilderBuildStep withError(final UserPasswordLoginConfirmationErrorType errorType) {
			UserPasswordLoginConfirmationResponseNOK resp = new UserPasswordLoginConfirmationResponseNOK();
			resp.setUserOid(_userOid);
			resp.setErrorType(errorType);
			return new UserConfirmationResponseBuilderBuildStep(resp);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationResponseBuilderLoginStep {
		private final UserOID _userOid;

		public UserConfirmationResponseBuilderUserStatusAfterConfirmationStep withLogin(final LoginOID loginOid,final LoginID loginId) {
			return new UserConfirmationResponseBuilderUserStatusAfterConfirmationStep(_userOid,
																					  loginOid,loginId);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationResponseBuilderUserStatusAfterConfirmationStep {
		private final UserOID _userOid;
		private final LoginOID _loginOid;
		private final LoginID _loginId;

		public UserConfirmationResponseBuilderBuildStep withUserStatusAfterConfirmation(final UserStatus userStatus) {
			UserPasswordLoginConfirmationResponseOK resp = new UserPasswordLoginConfirmationResponseOK();
			resp.setUserOid(_userOid);
			resp.setLoginOid(_loginOid);
			resp.setLoginId(_loginId);
			resp.setUserStatusAfterLoginConfirmation(userStatus);
			return new UserConfirmationResponseBuilderBuildStep(resp);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationResponseBuilderBuildStep {
		private final UserPasswordLoginConfirmationResponse _resp;

		public UserPasswordLoginConfirmationResponse build() {
			return _resp;
		}
		public UserPasswordLoginConfirmationResponse buildAt(final Instant date) {
			_resp.setResponsedAt(date);
			return _resp;
		}
	}
}
