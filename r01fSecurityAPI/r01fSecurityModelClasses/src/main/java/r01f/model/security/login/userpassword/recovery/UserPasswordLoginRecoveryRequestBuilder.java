package r01f.model.security.login.userpassword.recovery;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;
import r01f.types.contact.Phone;


@NoArgsConstructor
public abstract class UserPasswordLoginRecoveryRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static PasswordRecoveryRequestBuilderNotificationTypeStep forLoginId(final LoginID login) {
		return new UserPasswordLoginRecoveryRequestBuilder() { /* nothing */ } 
					.new PasswordRecoveryRequestBuilderNotificationTypeStep(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class PasswordRecoveryRequestBuilderNotificationTypeStep {
		private final LoginID _loginId;

		public PasswordRecoveryRequestBuilderBuildStep sendNotificationTo(final EMail email) {
			UserPasswordLoginRecoveryRequest req = new UserPasswordLoginRecoveryRequest();
			req.setLoginId(_loginId);
			req.setEmail(email);
			return new PasswordRecoveryRequestBuilderBuildStep(req);
		}
		public PasswordRecoveryRequestBuilderBuildStep sendNotificationTo(final Phone phone) {
			UserPasswordLoginRecoveryRequest req = new UserPasswordLoginRecoveryRequest();
			req.setLoginId(_loginId);
			req.setPhone(phone);
			return new PasswordRecoveryRequestBuilderBuildStep(req);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class PasswordRecoveryRequestBuilderBuildStep {
		private final UserPasswordLoginRecoveryRequest _req;

		public UserPasswordLoginRecoveryRequest build() {
			return _req;
		}
		public UserPasswordLoginRecoveryRequest buildAt(final Instant date) {
			_req.setRequestAt(date);
			return _req;
		}
	}
}
