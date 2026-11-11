package r01f.model.security.login.userpassword.messages.registration.acknowledge;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.IsBuilder;

public abstract class UserPasswordLoginRegistrationAcknowledgeResponseBuilder
           implements IsBuilder {	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginConfirmationResponseBuilderPrincipalStep confirmed() {
		UserPasswordLoginRegistrationAcknowledgeResponse resp = new UserPasswordLoginRegistrationAcknowledgeResponse();
		resp.setResult(UserPasswordLoginRegistrationAcknowledgeResult.CONFIRMED);
		return new UserPasswordLoginRegistrationAcknowledgeResponseBuilder() { /* nothing */ }
						.new UserPasswordLoginConfirmationResponseBuilderPrincipalStep(resp);
	}
	public static UserPasswordLoginConfirmationResponseBuilderResultStep invalidLoginId() {
		UserPasswordLoginRegistrationAcknowledgeResponse resp = new UserPasswordLoginRegistrationAcknowledgeResponse();
		resp.setResult(UserPasswordLoginRegistrationAcknowledgeResult.INVALID_LOGIN_ID);
		return new UserPasswordLoginRegistrationAcknowledgeResponseBuilder() { /* nothing */ }
						.new UserPasswordLoginConfirmationResponseBuilderResultStep(resp);
	}
	public static UserPasswordLoginConfirmationResponseBuilderResultStep loginCouldNOTBeActivated() {
		UserPasswordLoginRegistrationAcknowledgeResponse resp = new UserPasswordLoginRegistrationAcknowledgeResponse();
		resp.setResult(UserPasswordLoginRegistrationAcknowledgeResult.lOGIN_COULD_NOT_BE_ACTIVATED);
		return new UserPasswordLoginRegistrationAcknowledgeResponseBuilder() { /* nothing */ }
						.new UserPasswordLoginConfirmationResponseBuilderResultStep(resp);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	PRINCIPAL                                                                          
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginConfirmationResponseBuilderPrincipalStep {
		private final UserPasswordLoginRegistrationAcknowledgeResponse _response;

		public UserPasswordLoginConfirmationResponseBuilderPrincipalStatusAfterConfirmationStep forSecurityPrincipal(final SecurityPrincipalOID principalOid,final SecurityPrincipalID principalId) {
			_response.setPrincipalOid(principalOid);
			_response.setPrincipalId(principalId);
			return new UserPasswordLoginConfirmationResponseBuilderPrincipalStatusAfterConfirmationStep(_response);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginConfirmationResponseBuilderPrincipalStatusAfterConfirmationStep {
		private final UserPasswordLoginRegistrationAcknowledgeResponse _response;

		public UserPasswordLoginConfirmationResponseBuilderResultStep withPrincipalStatusAfterConfirmation(final SecurityPrincipalStatus principalStatus) {
			_response.setPrincipalStatusAfterConfirmation(principalStatus);
			return new UserPasswordLoginConfirmationResponseBuilderResultStep(_response);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	LOGIN
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginConfirmationResponseBuilderResultStep {
		private final UserPasswordLoginRegistrationAcknowledgeResponse _response;

		public BuilderBuildStep<UserPasswordLoginRegistrationAcknowledgeResponse> forLogin(final LoginOID loginOid,final LoginID loginId) {
			_response.setLoginOid(loginOid);
			_response.setLoginId(loginId);
			return new BuilderBuildStep<>(_response);
		}
	}
}
