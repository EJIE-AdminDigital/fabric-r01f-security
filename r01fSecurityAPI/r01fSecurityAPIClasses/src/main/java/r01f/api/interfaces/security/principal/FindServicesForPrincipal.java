package r01f.api.interfaces.security.principal;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.FindResult;
import r01f.model.security.IsSecurityPrincipal;
import r01f.model.security.context.SecurityContext;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.FindServicesForModelObject;

public interface FindServicesForPrincipal<P extends IsSecurityPrincipal>
		 extends FindServicesForModelObject<SecurityPrincipalOID,P>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//	                                                                          
/////////////////////////////////////////////////////////////////////////////////////////	
	/**
	 * Fina all registered principals
	 * @param securityContext
	 * @return
	 */
	public FindResult<P> findAllPrincipals(final SecurityContext securityContext);
	/**
	 * Finds all principals that have any system-wide role
	 * @param securityContext
	 * @return
	 */
	public FindResult<P> findHavingAnySystemWideRole(final SecurityContext securityContext);
	/**
	 * Find principals by text
	 * @param ontext
	 * @return
	 */
	public FindResult<P> findByText(final SecurityContext securityContext,
									final String text);
}