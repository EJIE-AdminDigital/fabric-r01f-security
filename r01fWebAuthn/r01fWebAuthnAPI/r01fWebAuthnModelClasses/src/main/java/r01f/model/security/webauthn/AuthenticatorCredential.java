package r01f.model.security.webauthn;

import r01f.model.security.PersistableSecurityObject;
import r01f.model.security.keystore.key.PublicKey;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;

/**

 */
public interface AuthenticatorCredential
		 extends PersistableSecurityObject<CredentialOID> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	public UserOID getUserOid();
	public void setUserOid(final UserOID userDataOid);

	public CredentialID getCredentialId();
	public void setCredentialId(final CredentialID credentialId);

	public  PublicKey  getPublicKey();

	public Long getSignatureCount();

	public SecurityProviderID getBackMappedSecurityProviderOnRegistration();

}
