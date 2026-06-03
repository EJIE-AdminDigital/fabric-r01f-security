package r01f.security.user.google;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;
import r01f.types.url.Url;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class GoogleUserDataBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static GoogleUserDataBuilderNameStep createForMail(final EMail googleMail) {
		GoogleUserData googleUser = new GoogleUserData();
		googleUser.setMail(googleMail);


		return new GoogleUserDataBuilder() { /* nothing */ }
					.new GoogleUserDataBuilderNameStep(googleUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GoogleUserDataBuilderNameStep {
		private final GoogleUserData _user;

		public GoogleUserDataBuilderFamiliyNameStep givenName(final String name) {
			_user.setGivenName(name);
			return new GoogleUserDataBuilderFamiliyNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GoogleUserDataBuilderFamiliyNameStep {
		private final GoogleUserData _user;

		public GoogleUserDataBuilderDisplayNameStep familiyName(final String familiyName) {
			_user.setFamilyName(familiyName);
			return new GoogleUserDataBuilderDisplayNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GoogleUserDataBuilderDisplayNameStep {
		private final GoogleUserData _user;

		public GoogleUserDataBuilderBuildStep andDisplayName(final String displayName) {
			_user.setDisplayName(displayName);
			return new GoogleUserDataBuilderBuildStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class GoogleUserDataBuilderBuildStep {
		private final GoogleUserData _user;

		public BuilderBuildStep andProfile(final Url uri) {
			_user.setProfilePhoto(uri);
			return new BuilderBuildStep(_user);
		}
		public BuilderBuildStep noPersonaField() {
			return new BuilderBuildStep(_user);
		}
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class BuilderBuildStep {
		private final GoogleUserData  _user;

		public GoogleUserData build() {
			return _user;
		}
	}
}
