package r01f.model.security.webauthn;

import r01f.model.security.IsSecurityPersistableObject;
import r01f.model.security.keystore.key.PublicKey;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;

/**

 */
public interface AuthenticatorCredential
		 extends IsSecurityPersistableObject<CredentialOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public SecurityPrincipalOID getSecurityPrincipalOid();
	public void setSecurityPrincipalOid(final SecurityPrincipalOID securityPrincipalOid);

	public CredentialID getCredentialId();
	public void setCredentialId(final CredentialID credentialId);

	public  PublicKey  getPublicKey();

	public Long getSignatureCount();

	public IdentityProvider getBackMappedSecurityProviderOnRegistration();

}
