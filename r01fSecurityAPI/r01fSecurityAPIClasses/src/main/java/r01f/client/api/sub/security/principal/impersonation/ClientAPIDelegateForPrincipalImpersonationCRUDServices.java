package r01f.client.api.sub.security.principal.impersonation;

import jakarta.inject.Provider;
import r01f.api.interfaces.security.principal.impersonation.CRUDServicesForPrincipalImpersonation;
import r01f.model.persistence.CRUDResult;
import r01f.model.security.context.SecurityContext;
import r01f.model.security.login.impersonation.SecurityPrincipalImpersonation;
import r01f.objectstreamer.Marshaller;
import r01f.oids.SecurityOIDs.SecurityPrincipalImpersonationOID;
import r01f.oids.SecurityOIDs.SecurityPrincipalOID;
import r01f.services.api.delegates.ClientAPIDelegateForModelObjectCRUDServices;

public class ClientAPIDelegateForPrincipalImpersonationCRUDServices
     extends ClientAPIDelegateForModelObjectCRUDServices<SecurityPrincipalImpersonationOID,SecurityPrincipalImpersonation> {

/////////////////////////////////////////////////////////////////////////////////////////
//  CONSTRUCTOR
/////////////////////////////////////////////////////////////////////////////////////////
	public ClientAPIDelegateForPrincipalImpersonationCRUDServices(final Provider<SecurityContext>  securityContextProvider,
															      final Marshaller marshaller,
															 	  final CRUDServicesForPrincipalImpersonation crudServicesProxy) {
		super(securityContextProvider,
			  marshaller,
			  crudServicesProxy);
	}
/////////////////////////////////////////////////////////////////////////////////////////
//
/////////////////////////////////////////////////////////////////////////////////////////
	/**
	 * Loads the ACTIVE [impersonation] for the given [principal] being impersonated
	 * BEWARE that a [principal] can only be impersonated ONCE at a time 
	 * @param impersonatedPrincipalOid
	 * @return
	 */
	public SecurityPrincipalImpersonation loadActiveForImpersonatedPrincipal(final SecurityPrincipalOID impersonatedPrincipalOid) {
		CRUDResult<SecurityPrincipalImpersonation> load = this.getServiceProxyAs(CRUDServicesForPrincipalImpersonation.class)
												  .loadActiveForImpersonatedPrincipal(this.getSecurityContext(),
														   							  impersonatedPrincipalOid);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(load);
	}
	/**
	 * Loads the ACTIVE [impersonation] for the given [principal] impersonating another [principal]
	 * BEWARE that a [principal] can only impersonate ONE [principal] at a time 
	 * @param impersonatingPrincipalOid
	 * @return
	 */
	public SecurityPrincipalImpersonation loadActiveForImpersonatingPrincipal(final SecurityPrincipalOID impersonatingPrincipalOid) {
		CRUDResult<SecurityPrincipalImpersonation> load = this.getServiceProxyAs(CRUDServicesForPrincipalImpersonation.class)
												  .loadActiveForImpersonatingPrincipal(this.getSecurityContext(),
														   							   impersonatingPrincipalOid);
		return ClientAPIDelegateForModelObjectCRUDServices.objFormLoadResult(load);
	}
}
