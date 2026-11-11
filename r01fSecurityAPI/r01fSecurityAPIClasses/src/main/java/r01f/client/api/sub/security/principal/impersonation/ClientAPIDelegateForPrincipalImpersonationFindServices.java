package r01f.client.api.sub.security.principal.impersonation;

import java.time.Instant;
import java.util.Collection;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.principal.impersonation.FindServicesForPrincipalImpersonation;
import r01f.model.persistence.FindResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectFindServices;
import r01f.services.api.delegates.ClientAPIModelObjectChangesTrack;
import r01f.types.Range;

public class ClientAPIDelegateForPrincipalImpersonationFindServices
     extends ClientAPIDelegateForModelObjectFindServices<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPrincipalImpersonationFindServices(final Provider<SecurityContext>  securityContextProvider,
															 	  final Marshaller marshaller,
															      final FindServicesForPrincipalImpersonation findServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  findServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Finds [impersonation] within a date range for the given [principal] being impersonated
	 * @param impersonatedPrincipalOid
	 * @param dateRange
	 * @return
	 */
	public Collection<SecurityPrincipalImpersonation> findForImpersonatedPrincipal(final SecurityPrincipalOID impersonatedPrincipalOid,
												 	   				   			   final Range<Instant> dateRange) {
		FindResult<SecurityPrincipalImpersonation> outFindResult = this.getServiceProxyAs(FindServicesForPrincipalImpersonation.class)
														   .findForImpersonatedPrincipal(this.getSecurityContext(),
																					   	 impersonatedPrincipalOid,
																					   	 dateRange);
		Collection<SecurityPrincipalImpersonation> outImp = outFindResult.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outImp);
		return outImp;
	}
	/**
	 * Finds [impersonation] within a date range for the given [principal] impersonating another [principal] 
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public Collection<SecurityPrincipalImpersonation> findForImpersonatingPrincipal(final SecurityPrincipalOID impersonatingPrincipalOid,
												 	   								final Range<Instant> dateRange) {
		FindResult<SecurityPrincipalImpersonation> outFindResult = this.getServiceProxyAs(FindServicesForPrincipalImpersonation.class)
														   .findForImpersonatingPrincipal(this.getSecurityContext(),
																					   	  impersonatingPrincipalOid,
																					   	  dateRange);
		Collection<SecurityPrincipalImpersonation> outImp = outFindResult.getOrThrow();
		ClientAPIModelObjectChangesTrack.startTrackingChangesOnLoaded(outImp);
		return outImp;
	}
}
