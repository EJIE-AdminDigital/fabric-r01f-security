package r01f.security.handler.webauthn;


import r01f.model.security.user.User;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.securitycontext.SecurityIDS.SecurityProviderID;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.types.StringBase64;

public interface CredentialFactoryFromRegistrationResponse<U extends User,
                                                           C extends AuthenticatorCredential> {

	C  from (final UserOID user, final StringBase64 credentialId , final StringBase64 pk , final Long signatureCount , final SecurityProviderID backMappedSecurityProviderOnRegistration);

}
