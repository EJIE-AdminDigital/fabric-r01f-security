package r01f.model.security.login.userpassword;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.securitycontext.SecurityIDS.Password;

/**
 * Usage: add a static constructor method in the {@link UserPasswordLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MyUserPasswordLogin,UserPasswordLoginBuilderPasswordStep<MyUserPasswordLogin>> create() {
 *			MyUserPasswordLogin login = new FN01UserPasswordLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   UserPasswordLoginBuilderPasswordStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class UserPasswordLoginBuilders
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class UserPasswordLoginBuilderPasswordStep<L extends UserPasswordLogin> 
			 implements IsBuilderStep {
		////////// Fields
		private final L _login;

		public UserPasswordLoginBuilderPasswordExpirationStep<L> withPassword(final Password password) {
			_login.setPassword(password);
			return new UserPasswordLoginBuilderPasswordExpirationStep<>(_login);
		}
	}
	@RequiredArgsConstructor
	public static class UserPasswordLoginBuilderPasswordExpirationStep<L extends UserPasswordLogin> 
			 implements IsBuilderStep {
		////////// Fields		
		private final L _login;

		public LoginBuilderBuildStep<L> expiratesAt(final Instant expiration) {
			_login.setPasswordExpirationDate(expiration);
			return new LoginBuilderBuildStep<L>(_login);
		}
		public LoginBuilderBuildStep<L> withoutExpiration() {
			return new LoginBuilderBuildStep<L>(_login);
		}
	}
}
