package r01f.api.interfaces.security.principal.impersonation;

import java.time.Instant;

import r01f.api.interfaces.security.IsSecurityServiceInterface;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.interfaces.FindServicesForModelObject;
import r01f.types.Range;

public interface FindServicesForPrincipalImpersonation
		 extends FindServicesForModelObject<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation>,
				 IsSecurityServiceInterface {
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds [impersonation] within a date range for the given [principal] being impersonated
	 * @param securityContext
	 * @param impersonatedPrincipalOid
	 * @param dateRange
	 * @return
	 */
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatedPrincipal(final SecurityContext securityContext,
															 	   				   final SecurityPrincipalOID impersonatedPrincipalOid,
															 	   				   final Range<Instant> dateRange);
	/**
	 * Finds [impersonation] within a date range for the given [principal] impersonating another [principal] 
	 * @param securityContext
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public FindResult<SecurityPrincipalImpersonation> findForImpersonatingPrincipal(final SecurityContext securityContext,
												 	   								final SecurityPrincipalOID impersonatingPrincipalOid,
												 	   								final Range<Instant> dateRange);
}

