package r01f.api.interfaces.security.webauthn;

import r01f.model.persistence.CRUDResult;
import r01f.model.security.webauthn.AuthenticatorCredential;
import r01f.model.security.webauthn.WebAuthnIDS.CredentialID;
import r01f.model.security.webauthn.WebAuthnOIDs.CredentialOID;
import r01f.securitycontext.SecurityContext;
import r01f.securitycontext.SecurityOIDs.UserOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForAuthenticatorCredential<C extends AuthenticatorCredential>
		 extends CRUDServicesForModelObject<CredentialOID,C> {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Returns a credential for the given user
	 * @param securityContext the user auth data & context info
	 * @param userOid the user
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final UserOID userOid);
	/**
	 * Returns a login for the given login id
	 * @param securityContext the user auth data & context info
	 * @param login the login
	 * @return a {@link CRUDResult} that encapsulates the entity if it was loaded successfully
	 */
	public CRUDResult<C> loadBy(final SecurityContext securityContext,
								final CredentialID credId);


	public CRUDResult<C> loadBy(final SecurityContext securityContext,
			                    final CredentialID credId, final UserOID userOid);



}

