package r01f.model.security.login.userpassword.messages.password.recover;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;
import r01f.types.contact.IsContactMean;
import r01f.types.contact.Phone;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserPasswordRecoverRequestBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginResetRequestBuilderMediumStep forLogin(final LoginID loginId) {
		UserPasswordRecoverRequest req = new UserPasswordRecoverRequest();
		req.setLoginId(loginId);
		return new UserPasswordRecoverRequestBuilder() { /* nothing */ }
						.new UserPasswordLoginResetRequestBuilderMediumStep(req);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginResetRequestBuilderMediumStep {
		private final UserPasswordRecoverRequest _request;

		public BuilderBuildStep<UserPasswordRecoverRequest> sendNotificationTo(final EMail email) {
			return this.sendNotificationTo(email);
		}
		public BuilderBuildStep<UserPasswordRecoverRequest> sendNotificationTo(final Phone phone) {
			return this.sendNotificationTo(phone);
		}
		public BuilderBuildStep<UserPasswordRecoverRequest> sendNotificationTo(final IsContactMean contactMean) {
			_request.setContactMean(contactMean);
			return new BuilderBuildStep<>(_request);
		}
	}
}