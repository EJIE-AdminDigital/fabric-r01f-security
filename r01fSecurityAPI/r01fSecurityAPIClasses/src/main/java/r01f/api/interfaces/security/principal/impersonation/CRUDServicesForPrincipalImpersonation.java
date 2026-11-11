package r01f.api.interfaces.security.principal.impersonation;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.CRUDServicesForModelObject;

public interface CRUDServicesForPrincipalImpersonation
		 extends CRUDServicesForModelObject<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the ACTIVE [impersonation] for the given [principal] being impersonated
	 * BEWARE that a [principal] can only be impersonated ONCE at a time 
	 * @param securityContext
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatedPrincipal(final SecurityContext securityContext,
												 	   						 			 final SecurityPrincipalOID impersonatedPrincipalOid);
	/**
	 * Loads the ACTIVE [impersonation] for the given [principal] impersonating another [principal]
	 * BEWARE that a [principal] can only impersonate ONE [principal] at a time 
	 * @param securityContext
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public CRUDResult<SecurityPrincipalImpersonation> loadActiveForImpersonatingPrincipal(final SecurityContext securityContext,
												 	   						  			  final SecurityPrincipalOID impersonatingPrincipalOid);
}

