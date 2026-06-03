package r01f.model.security.login.saml;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.types.contact.EMail;

/**
 * Usage: add a static constructor method in the {@link SAMLLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MySAMLLogin,SAMLLoginBuilderEMailStep<MySAMLLogin>> create() {
 *			MySAMLLogin login = new FN01SAMLLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   SAMLLoginBuilderEMailStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLLoginBuilders
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class SAMLLoginBuilderEMailStep<L extends SAMLLogin> 
			 implements IsBuilderStep {
		
		////////// Fields
		private final L _login;

		public LoginBuilderBuildStep<L> withSAMLMail(final EMail email) {
			if (email == null) throw new IllegalArgumentException(email + " is NOT a valid Mail address!");
			_login.setMail(email);
			return new LoginBuilderBuildStep<L>(_login);
		}
	}
}
