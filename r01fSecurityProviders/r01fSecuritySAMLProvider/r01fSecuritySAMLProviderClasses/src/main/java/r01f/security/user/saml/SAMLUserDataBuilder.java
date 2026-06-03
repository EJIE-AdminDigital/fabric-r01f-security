package r01f.security.user.saml;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.patterns.IsBuilder;
import r01f.securitycontext.SecurityIDS.LoginID;
import r01f.types.contact.EMail;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLUserDataBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static SAMLUserDataBuilderEMailStep createForLoginId(final LoginID loginId) {
		SAMLUserData samlUser = new SAMLUserData();
		samlUser.setLoginId(loginId);
		return new SAMLUserDataBuilder() { /* nothing */ }
					.new SAMLUserDataBuilderEMailStep(samlUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLUserDataBuilderEMailStep {
		private final SAMLUserData _user;

		public SAMLUserDataBuilderNameStep withEMail(final EMail samlMail) {
			_user.setMail(samlMail);
			return new SAMLUserDataBuilderNameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLUserDataBuilderNameStep {
		private final SAMLUserData _user;

		public SAMLUserDataBuilderSurnameStep name(final String name) {
			_user.setName(name);
			return new SAMLUserDataBuilderSurnameStep(_user);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLUserDataBuilderSurnameStep {
		private final SAMLUserData _user;

		public SAMLUserDataBuilderBuildStep surname(final String surname) {
			_user.setSurname(surname);
			return new SAMLUserDataBuilderBuildStep(_user);
		}
	}	
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLUserDataBuilderBuildStep {
		private final SAMLUserData  _user;

		public SAMLUserData build() {
			return _user;
		}
	}
}
