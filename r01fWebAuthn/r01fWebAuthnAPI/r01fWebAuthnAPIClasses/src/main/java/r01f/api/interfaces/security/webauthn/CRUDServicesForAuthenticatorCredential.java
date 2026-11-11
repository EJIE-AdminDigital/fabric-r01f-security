package r01f.api.interfaces.security.webauthn;

import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForAuthenticatorCredential<C extends AuthenticatorCredential>
		 extends CRUDServicesForModelObject<CredentialOID,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a credential for the given principal
	 * @param securityContext the user auth data & context info
	 * @param securityPrincipalOid the user
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final SecurityPrincipalOID securityPrincipalOid);
	/**
	 * Returns a login for the given login id
	 * @param securityContext the user auth data & context info
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID credId);
	/**
	 * Returns a login for the given login id and principal
	 * @param securityContext
	 * @param credId
	 * @param securityPrincipalOid
	 * @return
	 */
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
			                    final CredentialID credId,final SecurityPrincipalOID securityPrincipalOid);



}

