package r01f.security.principal.saml;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import r01f.model.builders.BuilderBuildStep;
import r01f.oids.SecurityIDs.LoginID;
import r01f.patterns.IsBuilder;
import r01f.types.contact.EMail;

@NoArgsConstructor(access=AccessLevel.PRIVATE)
public abstract class SAMLSecurityPrincipalDataBuilder
           implements IsBuilder {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public static SAMLSecurityPrincipalDataBuilderEMailStep createForLoginId(final LoginID loginId) {
		SecurityPrincipalDataFromSAMLIdentityProvider samlUser = new SecurityPrincipalDataFromSAMLIdentityProvider();
		samlUser.setLoginId(loginId);
		return new SAMLSecurityPrincipalDataBuilder() { /* nothing */ }
					.new SAMLSecurityPrincipalDataBuilderEMailStep(samlUser);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLSecurityPrincipalDataBuilderEMailStep {
		private final SecurityPrincipalDataFromSAMLIdentityProvider _principal;

		public SAMLSecurityPrincipalDataBuilderNameStep withEMail(final EMail samlMail) {
			_principal.setMail(samlMail);
			return new SAMLSecurityPrincipalDataBuilderNameStep(_principal);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLSecurityPrincipalDataBuilderNameStep {
		private final SecurityPrincipalDataFromSAMLIdentityProvider _principal;

		public SAMLSecurityPrincipalDataBuilderSurnameStep name(final String name) {
			_principal.setName(name);
			return new SAMLSecurityPrincipalDataBuilderSurnameStep(_principal);
		}
	}
	@RequiredArgsConstructor(access=AccessLevel.PRIVATE)
	public class SAMLSecurityPrincipalDataBuilderSurnameStep {
		private final SecurityPrincipalDataFromSAMLIdentityProvider _principal;

		public BuilderBuildStep<SecurityPrincipalDataFromSAMLIdentityProvider> surname(final String surname) {
			_principal.setSurname(surname);
			return new BuilderBuildStep<>(_principal);
		}
	}	
}
