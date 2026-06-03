package r01f.model.security.login.userpassword.confirmation;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.security.user.UserStatus;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityOIDs.UserOID;

public class UserPasswordLoginConfirmationRequestBuilder
  implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserConfirmationRequestBuilderUserStatusStep forUser(final UserOID userOid) {
		return new UserPasswordLoginConfirmationRequestBuilder() { /* nothing */ } 
					.new UserConfirmationRequestBuilderUserStatusStep(userOid);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationRequestBuilderUserStatusStep {
		private final UserOID _userOid;

		public UserConfirmationRequestBuilderBuildStep setUserStatusAfterConfirmation(final UserStatus status) {
			UserPasswordLoginConfirmationRequest req = new UserPasswordLoginConfirmationRequest(_userOid,
																								status);
			return new UserConfirmationRequestBuilderBuildStep(req);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserConfirmationRequestBuilderBuildStep {
		private final UserPasswordLoginConfirmationRequest _req;

		public UserPasswordLoginConfirmationRequest build() {
			return _req;
		}
		public UserPasswordLoginConfirmationRequest buildAt(final Instant date) {
			_req.setRequestAt(date);
			return _req;
		}
	}
}
