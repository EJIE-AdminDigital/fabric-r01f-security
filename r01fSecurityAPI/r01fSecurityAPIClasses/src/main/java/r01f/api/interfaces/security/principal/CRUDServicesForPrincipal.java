package r01f.api.interfaces.security.principal;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.oids.SecurityIDs.IdentityProvider;
import r01f.oids.SecurityIDs.SecurityPrincipalID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.CRUDServicesForModelObject;
import r01f.types.contact.EMail;

public interface CRUDServicesForPrincipal<P extends IsSecurityPrincipal>
		 extends CRUDServicesForModelObject<SecurityPrincipalOID,P>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the [security principal] given the [identity provider] and [security principal] id
	 * @param securityContext
	 * @param identityProvider
	 * @param principalId
	 * @return
	 */
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final IdentityProvider identityProvider,final SecurityPrincipalID principalId);
	
	/**
	 * Loads the [security principal] given the [principal email]
	 * @param securityContext
	 * @param principalEmail
	 * @return
	 */
	public CRUDResult<P> loadBy(final SecurityContext securityContext,
								final EMail principalEmail);
}

