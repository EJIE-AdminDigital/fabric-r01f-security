package r01f.api.interfaces.security.webauthn;

import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForAuthenticatorCredential<C extends AuthenticatorCredential>
		 extends FindServicesForModelObject<CredentialOID,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	FindResult<C> findBy(final SecurityContext securityContext,
			             final SecurityPrincipalOID userOid );
}

