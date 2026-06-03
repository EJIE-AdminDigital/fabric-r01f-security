package r01f.security.user.giltza;

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
		GiltzaUserData googleUser = new GiltzaUserData();
		googleUser.setDni(dniNif);

		return new GiltzaUserDataBuilder() { /* nothing */ }
					.new GiltzaUserDataBuilderNameStep(googleUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderNameStep {
		private final GiltzaUserData _user;

		public GiltzaUserDataBuilderFamiliyNameStep givenName(final String name) {
			_user.setGivenName(name);
			return new GiltzaUserDataBuilderFamiliyNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderFamiliyNameStep {
		private final GiltzaUserData _user;

		public GiltzaUserDataBuilderDisplayNameStep familiyName(final String familiyName) {
			_user.setFamilyName(familiyName);
			return new GiltzaUserDataBuilderDisplayNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderDisplayNameStep {
		private final GiltzaUserData _user;

		public GiltzaUserDataBuilderBuildStep andDisplayName(final String displayName) {
			_user.setDisplayName(displayName);
			return new GiltzaUserDataBuilderBuildStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GiltzaUserDataBuilderBuildStep {
		private final GiltzaUserData _user;

		public BuilderBuildStep withMail(final EMail mail) {
			_user.setMail(mail);
			return new BuilderBuildStep(_user);
		}
		
		public BuilderBuildStep noDni() {
			return new BuilderBuildStep(_user);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class BuilderBuildStep {
		private final GiltzaUserData  _user;

		public GiltzaUserData build() {
			return _user;
		}
	}
}
