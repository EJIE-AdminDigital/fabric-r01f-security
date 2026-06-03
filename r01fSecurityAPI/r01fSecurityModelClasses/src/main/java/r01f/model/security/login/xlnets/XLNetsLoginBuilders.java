package r01f.model.security.login.xlnets;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.guids.CommonOIDs.WorkPlaceCode;
import r01f.model.security.login.LoginBuilders.LoginBuilderBuildStep;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**
 * Usage: add a static constructor method in the {@link XLNetsLogin}-extending type
 * <pre class='brush:java'>
 *		public static LoginBuilderStart<MyXLNetsLogin,XLNetsLoginBuilderWorkPlaceStep<MyXLNetsLogin>> create() {
 *			MyXLNetsLogin login = new FN01XLNetsLogin(LoginOID.supply());
 *			return new LoginBuilderStart<>(login,
 *										   XLNetsLoginBuilderWorkPlaceStep::new);	// next step factory
 *		}
 * </pre>
 */
@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class XLNetsLoginBuilders 
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor
	public static class XLNetsLoginBuilderWorkPlaceStep<L extends XLNetsLogin> 
			 implements IsBuilderStep {
		////////// Fields
		private final L _login;

		public LoginBuilderBuildStep<L> withWorkPlaceCode(final WorkPlaceCode wp) {
			_login.setWorkPlaceCode(wp);
			return new LoginBuilderBuildStep<L>(_login);
		}
		public LoginBuilderBuildStep<L> forUserOid(final UserOID usertOid) {
			_login.setUserOid(usertOid);
			return new LoginBuilderBuildStep<L>(_login);
		}
	}
}
