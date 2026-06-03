package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.userpassword.factors.LoginAuthFactor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityIDS.Password;


@NoArgsConstructor
public abstract class UserPasswordLoginRequestBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@SuppressWarnings("synthetic-access")
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
			UserPasswordLoginRequest request = new UserPasswordLoginRequest();
			request.setLoginId(_loginId);
			request.setPassword(pwd);
			return new UserPasswordBuilderAuthenticationFactorsStep(request);
		}
	}
	
	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordBuilderAuthenticationFactorsStep {
		private final UserPasswordLoginRequest  _loginRequest;

		public UserPasswordBuilderBuildStep noAuthenticationFactors() {
			return new UserPasswordBuilderBuildStep(_loginRequest);
		}
		public UserPasswordBuilderBuildStep authenticationFactor(final LoginAuthFactor factor) {
			_loginRequest.setAuthFactor(factor);
			return new UserPasswordBuilderBuildStep(_loginRequest);
		}		
		// trick for builder when not is required authentication factors ( this way for avoiding refactors...)
		public UserPasswordLoginRequest build() {		
			return new UserPasswordBuilderBuildStep(_loginRequest).build();
		}
		public UserPasswordLoginRequest buildAt( final Instant date) {		
			return new UserPasswordBuilderBuildStep(_loginRequest).buildAt(date);
		}
	}
	
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class UserPasswordBuilderBuildStep {
		private final UserPasswordLoginRequest  _loginRequest;

		public UserPasswordLoginRequest build() {
			return _loginRequest;
		}
		public UserPasswordLoginRequest buildAt(final Instant date) {
			_loginRequest.setRequestedAt(date);
			return _loginRequest;
		}
	
	}
}
