package r01f.model.security.login;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityIDs.LoginIDFormat;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.IsBuilder;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class LoginBuilderBase
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
// 	Id & type.
/////////////////////////////////////////////////////////////////////////////////////////
    @RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class LoginBuilderIdStep {
		private final Login _login;

		public LoginBuilderIdFormatStep withId(final LoginID id) {			
			_login.setId(id);
			return new LoginBuilderIdFormatStep(_login);
		}
		public LoginBuilderSecurityPrincipalStep withId(final LoginID id,final LoginIDFormat format) {			
			_login.setId(id);
			_login.setIdFormat(format);
			return new LoginBuilderSecurityPrincipalStep(_login);
		}
	}
    @RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class LoginBuilderIdFormatStep {
		private final Login _login;

		public LoginBuilderSecurityPrincipalStep withFormat(final LoginIDFormat format) {			
			_login.setIdFormat(format);
			return new LoginBuilderSecurityPrincipalStep(_login);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//	SECURITY PRINCIPAL
/////////////////////////////////////////////////////////////////////////////////////////    
    @RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class LoginBuilderSecurityPrincipalStep {
		private final Login _login;

		public BuilderBuildStep<Login> ownedBy(final SecurityPrincipalOID principalOid,final SecurityPrincipalID principalId) {			
			_login.setSecurityPrincipalOid(principalOid);
			_login.setSecurityPrincipalId(principalId);
			return new BuilderBuildStep<>(_login);
		}
	}
}
