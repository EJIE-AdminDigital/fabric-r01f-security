package r01f.model.security.login.userpassword.messages.registration.acknowledge;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.model.security.SecurityPrincipalStatus;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.IsBuilder;

public class UserPasswordLoginRegistrationAcknowledgeRequestBuilder
  implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginConfirmationRequestBuilderPrincipalStep forPrincipal(final SecurityPrincipalOID securityPrincipalOid) {
		UserPasswordLoginRegistrationAcknowledgeRequest req = new UserPasswordLoginRegistrationAcknowledgeRequest();
		req.setSecurityPrincipalOid(securityPrincipalOid);
		return new UserPasswordLoginRegistrationAcknowledgeRequestBuilder() { /* nothing */ } 
						.new UserPasswordLoginConfirmationRequestBuilderPrincipalStep(req);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginConfirmationRequestBuilderPrincipalStep {
		private final UserPasswordLoginRegistrationAcknowledgeRequest _req;

		public BuilderBuildStep<UserPasswordLoginRegistrationAcknowledgeRequest> withLogin(final LoginOID loginOid) {
			_req.setLoginOid(loginOid);
			return new BuilderBuildStep<>(_req);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginConfirmationRequestBuilderPrincipalStatusStep {
		private final UserPasswordLoginRegistrationAcknowledgeRequest _req;

		public BuilderBuildStep<UserPasswordLoginRegistrationAcknowledgeRequest> setPrincipalStatusAfterConfirmation(final SecurityPrincipalStatus status) {
			_req.setPrincipalStatusAfterAcknowledgement(status);
			return new BuilderBuildStep<>(_req);
		}
	}
}
