package r01f.model.security.login.userpassword.messages.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.model.security.login.authn.secondfactor.IsLoginAuthnSecondFactor;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.Password;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public abstract class UserPasswordLoginRequestBuilder
            implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginRequestBuilderPasswordStep using(final LoginID loginId) {
		return new UserPasswordLoginRequestBuilder() { /* nothing */ }
					.new UserPasswordLoginRequestBuilderPasswordStep(loginId);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordLoginRequestBuilderPasswordStep {
		private final LoginID _loginId;

		public UserPasswordBuilderAuthenticationFactorsStep with(final Password pwd) {
			UserPasswordLoginRequest request = new UserPasswordLoginRequest(_loginId,pwd);
			return new UserPasswordBuilderAuthenticationFactorsStep(request);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordBuilderAuthenticationFactorsStep {
		private final UserPasswordLoginRequest  _loginRequest;

		public BuilderBuildStep<UserPasswordLoginRequest> doNOTRequireAuthenticationSecondFactor() {
			return new BuilderBuildStep<>(_loginRequest);
		}
		public BuilderBuildStep<UserPasswordLoginRequest> requireAuthenticationSecondFactor(final IsLoginAuthnSecondFactor secondFactor) {
			_loginRequest.setAuthnSecondFactor(secondFactor);
			return new BuilderBuildStep<>(_loginRequest);
		}		
		// trick for builder when not is required authentication factors ( this way for avoiding refactors...)
		public UserPasswordLoginRequest build() {		
			return _loginRequest;
		}
	}
}
