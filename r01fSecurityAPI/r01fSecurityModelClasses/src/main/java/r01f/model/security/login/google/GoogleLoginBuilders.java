package r01f.model.security.login.google;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.types.contact.EMail;

/**
 * Usage: add a static constructor method in the {@link GoogleLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MyGoogleLogin,GoogleLoginBuilderEMailStep<MyGoogleLogin>> create() {
 *			MyGoogleLogin login = new FN01GoogleLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   GoogleLoginBuilderEMailStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class GoogleLoginBuilders 
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class GoogleLoginBuilderEMailStep<L extends GoogleLogin> 
			 implements IsBuilderStep {
		
		////////// Fields
		private final L _login;

		public LoginBuilderBuildStep<L> withGoogleMail(final EMail email) {
			if (email == null || !email.isGoogleEMail()) throw new IllegalArgumentException(email + " is NOT a valid Google Mail address!");
			_login.setGoogleMail(email);
			return new LoginBuilderBuildStep<L>(_login);
		}
	}
}
