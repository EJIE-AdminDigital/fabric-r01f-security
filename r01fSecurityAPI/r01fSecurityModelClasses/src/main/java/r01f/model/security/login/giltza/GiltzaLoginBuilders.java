package r01f.model.security.login.giltza;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.types.contact.EMail;

/**
 * Usage: add a static constructor method in the {@link GiltzaLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MyGiltzaLogin,GiltzaLoginBuilderEMailStep<MyGiltzaLogin>> create() {
 *			MyGiltzaLogin login = new FN01GiltzaLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   GiltzaLoginBuilderEMailStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class GiltzaLoginBuilders
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	EMAIL
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class GiltzaLoginBuilderEMailStep<L extends GiltzaLogin>
			 implements IsBuilderStep {

		////////// Fields
		private final L _login;

		////////// Methods
		public LoginBuilderBuildStep<L> withhUnknowMail() {	// The mail could be unknown
			return new LoginBuilderBuildStep<>(_login);
		}
		public LoginBuilderBuildStep<L> withGiltzaMail(final EMail email) {
			if (email != null ) {
				_login.setEmail(email);
			}
			return new LoginBuilderBuildStep<>(_login);
		}
	}
}
