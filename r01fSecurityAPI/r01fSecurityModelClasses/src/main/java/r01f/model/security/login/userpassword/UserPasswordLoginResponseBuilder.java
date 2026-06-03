package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.user.User;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;

@NoArgsConstructor(access=AccessLevel.PROTECTED)
public abstract class UserPasswordLoginResponseBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static UserPasswordLoginResponseBuilderResultStep forLoginId(final LoginID login) {		
		return new UserPasswordLoginResponseBuilder() { /* nothing */ } 
					.new UserPasswordLoginResponseBuilderResultStep(login);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	@RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class UserPasswordLoginResponseBuilderResultStep {
		private final LoginID _login;

		public <U extends User> LoginResponseBuilderBuilderBuildStep loggedOn(final U user) {
			UserPasswordLoginResponseOK<U> resp = new UserPasswordLoginResponseOK<U>();
			resp.setLoginId(_login);
			resp.setUser(user);
			return new LoginResponseBuilderBuilderBuildStep(resp);
		}
		public LoginResponseBuilderBuilderBuildStep withError(final UserPasswordLoginErrorType errorType) {
			UserPasswordLoginResponseNOK resp = new UserPasswordLoginResponseNOK();
			resp.setLoginId(_login);
			resp.setErrorType(errorType);
			return new LoginResponseBuilderBuilderBuildStep(resp);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class LoginResponseBuilderBuilderBuildStep {
		private final UserPasswordLoginResponse _resp;

		public UserPasswordLoginResponse build() {
			return _resp;
		}
		public UserPasswordLoginResponse buildAt(final Instant date) {
			_resp.setResponsedAt(date);
			return _resp;
		}
	}

}
