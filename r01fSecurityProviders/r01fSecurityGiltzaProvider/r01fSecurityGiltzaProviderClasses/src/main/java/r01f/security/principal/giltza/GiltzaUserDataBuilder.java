package r01f.security.principal.giltza;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;
import r01f.types.contact.PersonID;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class GiltzaUserDataBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static GiltzaUserDataBuilderNameStep forPersonId(final PersonID dniNif) {
		SecurityPrincipalDataFromGiltza gilzaUser = new SecurityPrincipalDataFromGiltza();
		gilzaUser.setDni(dniNif);

		return new GiltzaUserDataBuilder() { /* nothing */ }
					.new GiltzaUserDataBuilderNameStep(gilzaUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderNameStep {
		private final SecurityPrincipalDataFromGiltza _user;

		public GiltzaUserDataBuilderSurname1Step name(final String name) {
			_user.setName(name);
			return new GiltzaUserDataBuilderSurname1Step(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderSurname1Step {
		private final SecurityPrincipalDataFromGiltza _user;

		public GiltzaUserDataBuilderSurname2Step surname1(final String surname1) {
			_user.setSurname1(surname1);
			return new GiltzaUserDataBuilderSurname2Step(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderSurname2Step {
		private final SecurityPrincipalDataFromGiltza _user;

		public GiltzaUserDataBuilderDisplayNameStep surname2(final String surname2) {
			_user.setSurname2(surname2);
			return new GiltzaUserDataBuilderDisplayNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderDisplayNameStep {
		private final SecurityPrincipalDataFromGiltza _user;

		public GiltzaUserDataBuilderBuildStep andDisplayName(final String displayName) {
			_user.setDisplayName(displayName);
			return new GiltzaUserDataBuilderBuildStep(_user);
		}
		public GiltzaUserDataBuilderBuildStep withMail(final EMail mail) {
			_user.setMail(mail);
			return new GiltzaUserDataBuilderBuildStep(_user);
		}
		public GiltzaUserDataBuilderBuildStep noDni() {
			return new GiltzaUserDataBuilderBuildStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderBuildStep {
		private final SecurityPrincipalDataFromGiltza _user;

		public GiltzaUserDataBuilderBuildStep withMail(final EMail mail) {
			_user.setMail(mail);
			return new GiltzaUserDataBuilderBuildStep(_user);
		}

		public GiltzaUserDataBuilderBuildStep displayName(final String displayName) {
			_user.setDisplayName(displayName);
			return new GiltzaUserDataBuilderBuildStep(_user);
		}

		public GiltzaUserDataBuilderBuildStep noDni() {
			return new GiltzaUserDataBuilderBuildStep(_user);
		}

		public SecurityPrincipalDataFromGiltza build() {
			return _user;
		}
	}
}
