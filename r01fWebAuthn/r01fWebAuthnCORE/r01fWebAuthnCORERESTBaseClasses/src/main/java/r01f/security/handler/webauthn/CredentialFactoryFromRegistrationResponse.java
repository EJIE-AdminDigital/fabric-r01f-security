package r01f.security.handler.webauthn;


import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.types.StringBase64;

public interface CredentialFactoryFromRegistrationResponse<U extends IsSecurityPrincipal,
                                                           C extends AuthenticatorCredential> {

	C  from(final SecurityPrincipalOID securityPrincipalOid, 
			final StringBase64 credentialId,
			final StringBase64 pk,
			final Long signatureCount,
			final IdentityProvider backMappedSecurityProviderOnRegistration);
}
