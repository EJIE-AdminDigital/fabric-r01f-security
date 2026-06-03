package r01f.model.security.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.FactoryFrom;
import r01f.patterns.IsBuilder;
import r01f.patterns.IsBuilderStep;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.securitycontext.SecurityOIDs.UserOID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class LoginBuilders 
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	START
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public static class LoginBuilderStart<L extends Login,
								   		  NEXT_STEP> 
			 implements IsBuilderStep {
		////////// Fields
		private final L _login;
		private final FactoryFrom<L,NEXT_STEP> _nextStepFactory;
		
		////////// Start
		public LoginBuilderUserStep forLoginId(final LoginID login) {
			_login.setLoginId(login);
			return new LoginBuilderUserStep(_login);
		}
		public LoginBuilderLoginStep forUserOid(final UserOID userOid) {
			_login.setUserOid(userOid);
			return new LoginBuilderLoginStep(_login);
		}
		////////// User
		@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
		public class LoginBuilderUserStep 
		  implements IsBuilderStep {
			
			private final L _login;
	
			public NEXT_STEP forUserOid(final UserOID userOid) {
				_login.setUserOid(userOid);
				return _nextStepFactory.from(_login);
			}
		}
		////////// Login
		@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
		public class LoginBuilderLoginStep 
		  implements IsBuilderStep {
			
			private final L _login;
	
			public NEXT_STEP withLoginId(final LoginID login) {
				_login.setLoginId(login);
				return _nextStepFactory.from(_login);
			}
			public NEXT_STEP noLoginId() {
				return _nextStepFactory.from(_login);
			}
		}
	}
//////////////////////////////////////////////////////////////////////////////////////
//	BUILD
//////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public static class LoginBuilderBuildStep<L extends Login> {
		private final L _login;

		public L build() {
			return _login;
		}
	}
}
