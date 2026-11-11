package r01f.model.security.login.entry;

import java.time.Instant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginEntryEvent;
import r01f.oids.SecurityIDs.LoginID;
import r01f.oids.SecurityOIDs.LoginEntryOID;
import r01f.oids.SecurityOIDs.LoginOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.patterns.IsBuilder;

@Slf4j
@NoArgsConstructor
public abstract class LoginEntryBuilder
		   implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//	
/////////////////////////////////////////////////////////////////////////////////////////
	public static LoginEntryBuilderIdStep create() {
		LoginEntry entry = new LoginEntry();
		entry.setOid(LoginEntryOID.supply());
		return new LoginEntryBuilder() { /* nothing */ }
					.new LoginEntryBuilderIdStep(new LoginEntry());
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	Entry Id.
/////////////////////////////////////////////////////////////////////////////////////////
    @RequiredArgsConstructor(access=AccessLevel.PUBLIC)
	public class LoginEntryBuilderIdStep {
		private final LoginEntry _loginEntry;

		public LoginEntryBuilderAtStep forEntryEvent(final LoginEntryEvent id) {			
			_loginEntry.setEvent(id);
			return new LoginEntryBuilderAtStep(_loginEntry);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	Date
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class LoginEntryBuilderAtStep {
		private final LoginEntry _loginEntry;

		public LoginEntryBuilderPrincipalStep at(final Instant date) {			
			_loginEntry.setAt(date);
			return new LoginEntryBuilderPrincipalStep(_loginEntry);
		}
		public LoginEntryBuilderPrincipalStep now() {			
			return this.at(Instant.now());
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	Security principal
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class LoginEntryBuilderPrincipalStep {
		private final LoginEntry _loginEntry;

		public LoginEntryBuilderLoginStep about(final SecurityPrincipalOID principalOid) {
			_loginEntry.setSecurityPrincipalOid(principalOid);
			return new LoginEntryBuilderLoginStep(_loginEntry);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
// 	Login .
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class LoginEntryBuilderLoginStep {
		private final LoginEntry _loginEntry;

		public BuilderBuildStep<LoginEntry> withLogin(final LoginOID oid,final LoginID id) {
			_loginEntry.setLoginOid(oid);
			_loginEntry.setLoginId(id);
			return new BuilderBuildStep<>(_loginEntry);
		}
	}
}
