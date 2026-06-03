package r01f.model.security.login.pairedphone;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.types.contact.Phone;

/**
 * Usage: add a static constructor method in the {@link PairedPhoneLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MyPairedPhoneLogin,PairedPhoneLoginBuilderEMailStep<MyPairedPhoneLogin>> create() {
 *			MyPairedPhoneLogin login = new FN01PairedPhoneLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   PairedPhoneLoginBuilderPhoneStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class PairedPhoneLoginBuilders 
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class PairedPhoneLoginBuilderPhoneStep<L extends PairedPhoneLogin> 
			 implements IsBuilderStep {
		
		////////// Fields
		private final L _login;

		public LoginBuilderBuildStep<L> withPairedPhone(final Phone phone) {
			if (phone == null) throw new IllegalArgumentException(phone + " is NOT a valid phone!");
			_login.setPairedPhone(phone);
			return new LoginBuilderBuildStep<L>(_login);
		}
	}
}
